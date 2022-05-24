package team.space.controllers.includechat;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import team.space.emojis.Emoji;
import team.space.emojis.EmojiOne;
import team.space.emojis.ImageCache;
import team.space.events.ApplicationEvents;
import team.space.events.MessageEvent;
import team.space.models.ChatHistory;
import team.space.models.Contact;
import team.space.models.CreateMessages;
import team.space.models.Message;
import team.space.notifications.rabbitmq.AMQP;
import team.space.utils.XUtils;
import team.space.widgetlists.ContactchatItem;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;


import static team.space.network.ReqChats.getAllUsersCompany;
import static team.space.network.ReqChats.sendChatMessage;
import static team.space.utils.Constants.QUEUE_ON_USER_SAVED;
import static team.space.utils.Shared.*;

public class ChatPArentViewController implements Initializable, ApplicationEvents {
    private static final boolean SHOW_MISC = false;
    @FXML
    private ScrollPane searchScrollPane;
    @FXML
    private FlowPane searchFlowPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<Image> boxTone;
    @FXML
    public VBox MAIN_CONTACTS, MSGS_CONTAINER;
    @FXML
    public ImageView SEND_BTN;
    @FXML
    public HBox loadingMotion;
    @FXML
    public AnchorPane parentParentRootPane , chatPane;
    @FXML
    public TextArea txtMsg;
    @FXML
    public Label txtInChatTitleNamePerson, txtInChatTitleSubNamePerson;
    private Contact contactInCurrentView;
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;
    private AMQP amqp = new AMQP();
    private Stage stage;
    private ApplicationEvents applicationEvents;
    private ObservableList<ContactchatItem> ContactchatItemList = FXCollections.observableArrayList();

    public ChatPArentViewController(Stage stage, ApplicationEvents applicationEvents) {
        this.stage = stage;
        this.applicationEvents = applicationEvents;

        EventBus.getDefault().register(this);
    }


    void initDialogs() {
        this.dialogContent = MFXGenericDialogBuilder.build()

                .makeScrollable(true)
                .get();
        this.dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .initOwner(stage)
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                // .setTitle("Dialogs Preview")
                .setOwnerNode(parentParentRootPane)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();

        dialogContent.setMaxSize(100, 100);
    }

    void initEmjis(){
        if(!SHOW_MISC) {
            tabPane.getTabs().remove(tabPane.getTabs().size()-2, tabPane.getTabs().size());
        }
        ObservableList<Image> tonesList = FXCollections.observableArrayList();

        for(int i = 1; i <= 5; i++) {
            Emoji emoji = EmojiOne.getInstance().getEmoji(":thumbsup_tone"+i+":");
            Image image = ImageCache.getInstance().getImage(getEmojiImagePath(emoji.getHex()));
            tonesList.add(image);
        }
        Emoji em = EmojiOne.getInstance().getEmoji(":thumbsup:"); //default tone
        Image image = ImageCache.getInstance().getImage(getEmojiImagePath(em.getHex()));
        tonesList.add(image);
        boxTone.setItems(tonesList);
        boxTone.setCellFactory(e->new ToneCell());
        boxTone.setButtonCell(new ToneCell());
        boxTone.getSelectionModel().selectedItemProperty().addListener(e->refreshTabs());


        searchScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        searchFlowPane.prefWidthProperty().bind(searchScrollPane.widthProperty().subtract(5));
        searchFlowPane.setHgap(5);
        searchFlowPane.setVgap(5);

        txtSearch.textProperty().addListener(x-> {
            String text = txtSearch.getText();
            if(text.isEmpty() || text.length() < 2) {
                searchFlowPane.getChildren().clear();
                searchScrollPane.setVisible(false);
            } else {
                searchScrollPane.setVisible(true);
                List<Emoji> results = EmojiOne.getInstance().search(text);
                searchFlowPane.getChildren().clear();
                results.forEach(emoji ->searchFlowPane.getChildren().add(createEmojiNode(emoji)));
            }
        });


        for(Tab tab : tabPane.getTabs()) {
            ScrollPane scrollPane = (ScrollPane) tab.getContent();
            FlowPane pane = (FlowPane) scrollPane.getContent();
            pane.setPadding(new Insets(5));
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            pane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(5));
            pane.setHgap(5);
            pane.setVgap(5);

            tab.setId(tab.getText());
            ImageView icon = new ImageView();
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            switch (tab.getText().toLowerCase()) {
                case "frequently used":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":heart:").getHex())));
                    break;
                case "people":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":smiley:").getHex())));
                    break;
                case "nature":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":dog:").getHex())));
                    break;
                case "food":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":apple:").getHex())));
                    break;
                case "activity":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":soccer:").getHex())));
                    break;
                case "travel":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":airplane:").getHex())));
                    break;
                case "objects":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":bulb:").getHex())));
                    break;
                case "symbols":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":atom:").getHex())));
                    break;
                case "flags":
                    icon.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(EmojiOne.getInstance().getEmoji(":flag_eg:").getHex())));
                    break;
            }

            if(icon.getImage() != null) {
                tab.setText("");
                tab.setGraphic(icon);
            }

            tab.setTooltip(new Tooltip(tab.getId()));
            tab.selectedProperty().addListener(ee-> {
                if(tab.getGraphic() == null) return;
                if(tab.isSelected()) {
                    tab.setText(tab.getId());
                } else {
                    tab.setText("");
                }
            });
        }



        boxTone.getSelectionModel().select(0);
        tabPane.getSelectionModel().select(1);
    }

    private void refreshTabs() {
        Map<String, List<Emoji>> map = EmojiOne.getInstance().getCategorizedEmojis(boxTone.getSelectionModel().getSelectedIndex()+1);
        for(Tab tab : tabPane.getTabs()) {
            ScrollPane scrollPane = (ScrollPane) tab.getContent();
            FlowPane pane = (FlowPane) scrollPane.getContent();
            pane.getChildren().clear();
            String category = tab.getId().toLowerCase();
            if(map.get(category) == null) continue;
            map.get(category).forEach(emoji -> pane.getChildren().add(createEmojiNode(emoji)));
        }
    }

    private Node createEmojiNode(Emoji emoji) {
        StackPane stackPane = new StackPane();
        stackPane.setMaxSize(32, 32);
        stackPane.setPrefSize(32, 32);
        stackPane.setMinSize(32, 32);
        stackPane.setPadding(new Insets(3));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(32);
        imageView.setFitHeight(32);
        imageView.setImage(ImageCache.getInstance().getImage(getEmojiImagePath(emoji.getHex())));
        stackPane.getChildren().add(imageView);

        Tooltip tooltip = new Tooltip(emoji.getShortname());
        Tooltip.install(stackPane, tooltip);
        stackPane.setCursor(Cursor.HAND);
        ScaleTransition st = new ScaleTransition(Duration.millis(90), imageView);

        stackPane.setOnMouseEntered(e-> {
            //stackPane.setStyle("-fx-background-color: #a6a6a6; -fx-background-radius: 3;");
            imageView.setEffect(new DropShadow());
            st.setToX(1.2);
            st.setToY(1.2);
            st.playFromStart();
            if(txtSearch.getText().isEmpty())
                txtSearch.setPromptText(emoji.getShortname());
        });
        stackPane.setOnMouseExited(e-> {
            //stackPane.setStyle("");
            imageView.setEffect(null);
            st.setToX(1.);
            st.setToY(1.);
            st.playFromStart();
        });
        return stackPane;
    }

    private String getEmojiImagePath(String hexStr) {
        return getClass().getResource("/emojis/png_40/" + hexStr + ".png").toExternalForm();
    }

    class ToneCell extends ListCell<Image> {
        private final ImageView imageView;
        public ToneCell() {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            imageView = new ImageView();
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
        }
        @Override
        protected void updateItem(Image item, boolean empty) {
            super.updateItem(item, empty);

            if(item == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                imageView.setImage(item);
                setGraphic(imageView);
            }
        }
    }
    @Override
    public void onFiredEvent(Event event) {

    }


    // UI updates must run on MainThread
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {


        System.out.println("Received event: " + event.getEventType());
        // textField.setText(event.message);
    }


    @FXML
    public void onChatPopMenue(Event event) {
        System.out.println("Send message");
    }

    @FXML
    public void onVideoCallEvent(Event event) {
        System.out.println("Send make a video call");
    }

    @FXML
    public void onMakeAudioCallEvent(Event event) {
        System.out.println("Send make a call");
        dialogContent.clearActions();
        dialogContent.addActions(
                Map.entry(new MFXButton("I understand Make the call"), event1 -> {
                    dialog.close();

                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MESSAGE_EVENT_MAKE_OUT_GOING_CALL_ALERT, contactInCurrentView));
                }),
                Map.entry(new MFXButton("Cancel"), event1 -> dialog.close())
        );
        dialogContent.setHeaderIcon(null);
        dialogContent.setContentText("Your about to make a call , please make sure you have a stable internet connection");
        dialogContent.setHeaderText("Calling Dialog");
        convertDialogTo(null);
        dialog.showDialog();
    }

    @FXML
    public void onAtReferenceEvent(Event event) {
        System.out.println("Send message");
    }


    @FXML
    public void onEmojiEvent(Event event) {
        System.out.println("Send message");
    }

    @FXML
    public void onAttachEvent(Event event) {
        System.out.println("Send message");
    }

    CreateMessages SR = new CreateMessages();

    @FXML
    public void onSendMessageEvent(Event event) {
        onSendMessageEvent();

    }

    private void convertDialogTo(String styleClass) {
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog")
        );

        if (styleClass != null)
            dialogContent.getStyleClass().add(styleClass);
    }

    public void onSendMessageEvent() {
        if(contactInCurrentView == null){
            return;
        }

        ArrayList<Message> messages = new ArrayList<>();
        var meg = new Message(LOGGED_USER.getUserId(), contactInCurrentView.getId(), txtMsg.getText(), XUtils.currentTimeStamp());
        messages.add(meg);
        historyMutableMap.get(contactInCurrentView).getMessages().addAll(messages);
        txtMsg.setText("");

        SR.senderMessage(meg, MSGS_CONTAINER);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                sendChatMessage(meg.getMessage(), "text", contactInCurrentView.getId());
                return null;
            }
        };

        new Thread(task).start();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::initDialogs);
        loadingMotion.setVisible(true);
        chatPane.setVisible(false);
        initEmjis();
       /* var meg = new Message(LOGGED_USER.getUserId(), "r", "dsasvcx sdfsdfsdf sdf sdfdf sd fsd fa asdasd asdsa", XUtils.currentTimeStamp().split(" ")[0]);

        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);*/


        txtMsg.setText("");

        Task<ObservableList<Contact>> getContacts = new Task<>() {
            @Override
            protected ObservableList<Contact> call() throws Exception {
                contactObservableArrayList.clear();
                return getAllUsersCompany();
            }
        };

        getContacts.setOnSucceeded(event -> {
            loadingMotion.setVisible(false);
            ObservableList<Contact> contacts = getContacts.getValue();
            System.out.println(contacts);
            contactObservableArrayList.addAll(contacts);
            ContactchatItemList.clear();
            contactObservableArrayList.forEach(contact -> {
                //   System.out.println(contact.getName());
                ContactchatItem item = new ContactchatItem(contact);
                ContactchatItemList.add(item);
                item.getRootAncherPane_user_custome_cell().setOnMouseClicked(e -> {
                  //  item.rectangleCurrentSelect.setVisible(false);
                    ContactchatItemList.forEach(contactchatItem -> {
                        if (contactchatItem != item) {
                            contactchatItem.rectangleCurrentSelect.setVisible(false);
                        }else{
                            contactchatItem.rectangleCurrentSelect.setVisible(true);
                        }
                    });

                    setContactInChatView(contact);
                });
                MAIN_CONTACTS.getChildren().add(item.getRootAncherPane_user_custome_cell());
            });

        });

        getContacts.setOnFailed(event -> {
            loadingMotion.setVisible(false);
            getContacts.getException().printStackTrace();
            System.err.println("Failed" + getContacts.getException());
        });

        getContacts.setOnCancelled(event -> {
            loadingMotion.setVisible(false);
            System.err.println("Cancelled");
        });

        new Thread(getContacts).start();


        txtMsg.textProperty().addListener((obs, old, niu) -> {
            //  System.out.println("Typing ...." + niu);

            // t.cancel()//;

           /* t.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Typing ....");
                }
            }, 0, 5000);*/
        });
        txtMsg.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                if (event.isShiftDown()) {
                    txtMsg.appendText(System.getProperty("line.separator"));
                } else {
                    if (!txtMsg.getText().isEmpty()) {
                        onSendMessageEvent();
                    }
                }
            }
        });


        amqp.setToListenToCapture("QUEUE_onUserSaved");


    }
    // Timer t = new Timer();


    void setContactInChatView(Contact contact) {

        if (historyMutableMap.get(contact) == null) {
            var chats = new ChatHistory();
            chats.setChatHistory(new ArrayList<Message>());
            historyMutableMap.put(contact, chats);
        }

        MSGS_CONTAINER.getChildren().clear();
        this.contactInCurrentView = contact;
        txtInChatTitleNamePerson.setText(contact.getFullName());
        txtInChatTitleSubNamePerson.setText(contact.getEmail());
        amqp.setToListenToCapture("onMessageSaved_" + LOGGED_USER.getUserId() + "_to_" + contactInCurrentView.getId());
        ObservableList<Message> chatsListOb = FXCollections.observableArrayList();
        for (Map.Entry<Contact, ChatHistory> entry : historyMutableMap.entrySet()) {
            var contact_ = entry.getKey();
            if (Objects.equals(contact_.getId(), contact.getId())) {
                var chats = entry.getValue().getMessages();
                chats.forEach(chat -> {
                    System.out.println("row: " + chat.getMessage());
                    chatsListOb.add(chat);

                    if (chat.getSender().equals(LOGGED_USER.getUserId())) {
                        // this me
                        var meg = new Message(LOGGED_USER.getFullName(), chat.getReceiver(), chat.getMessage(), XUtils.currentTimeStamp().split(" ")[0]);

                        SR.senderMessage(meg, MSGS_CONTAINER);
                    } else {
                        // other
                        var meg = new Message(contact.getFullName(), chat.getSender(), chat.getMessage(), XUtils.currentTimeStamp().split(" ")[0]);

                        SR.receiveMessage(meg, MSGS_CONTAINER);
                    }

                });
            }
        }
        chatPane.setVisible(true);
    }
}
