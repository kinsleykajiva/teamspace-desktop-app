package team.space.controllers.includechat;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import team.space.events.ApplicationEvents;
import team.space.events.MessageEvent;
import team.space.models.ChatHistory;
import team.space.models.Contact;
import team.space.models.CreateMessages;
import team.space.models.Message;
import team.space.notifications.rabbitmq.AMQP;
import team.space.utils.XUtils;
import team.space.widgetlists.ContactchatItem;

import java.net.URL;
import java.util.*;


import static team.space.network.ReqChats.getAllUsersCompany;
import static team.space.network.ReqChats.sendChatMessage;
import static team.space.utils.Shared.*;

public class ChatPArentViewController implements Initializable, ApplicationEvents {

@FXML public VBox MAIN_CONTACTS ,MSGS_CONTAINER;
@FXML public ImageView SEND_BTN;
@FXML public AnchorPane parentParentRootPane;
@FXML public TextArea txtMsg;
@FXML public Label txtInChatTitleNamePerson , txtInChatTitleSubNamePerson;
private Contact contactInCurrentView;
    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;

    private Stage stage;
    private ApplicationEvents applicationEvents;
    public ChatPArentViewController(Stage stage, ApplicationEvents applicationEvents) {
        this.stage = stage;
        this.applicationEvents = applicationEvents;

        EventBus.getDefault().register(this);
    }



    void initDialogs(){
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
       /* dialogContent.addActions(
                Map.entry(new MFXButton("Confirm"), event -> {
                }),
                Map.entry(new MFXButton("Cancel"), event -> dialog.close())
        );*/
        dialogContent.setMaxSize(100, 100);
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

                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MESSAGE_EVENT_MAKE_OUT_GOING_CALL_ALERT,"Hello everyone!"));
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
        System.out.println("Send message");

        ArrayList<Message> messages = new ArrayList<>();
        var meg = new Message(LOGGED_USER.getUserId() , contactInCurrentView.getId(), txtMsg.getText(), XUtils.currentTimeStamp());
        messages.add(meg);
        historyMutableMap.get(contactInCurrentView).getMessages().addAll(messages);
        txtMsg.setText("");

        SR.senderMessage(meg, MSGS_CONTAINER);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                sendChatMessage(meg.getMessage() ,"text" , contactInCurrentView.getId() );
                return null;
            }
        };

        new Thread(task).start();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()->{

        initDialogs();

        });
        var meg = new Message(LOGGED_USER.getUserId() , "r", "dsasvcx sdfsdfsdf sdf sdfdf sd fsd fa asdasd asdsa", XUtils.currentTimeStamp().split(" ")[0]);

        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.receiveMessage(meg, MSGS_CONTAINER);
        SR.senderMessage(meg, MSGS_CONTAINER);



        txtMsg.setText("");

        Task<ObservableList<Contact>>  getContacts = new Task<ObservableList<Contact>>() {
            @Override
            protected ObservableList<Contact> call() throws Exception {
                contactObservableArrayList.clear();
                return getAllUsersCompany();
            }
        };

        getContacts.setOnSucceeded(event -> {
            ObservableList<Contact> contacts = getContacts.getValue();
            System.out.println(contacts);
            contactObservableArrayList.addAll(contacts);

            contactObservableArrayList.forEach(contact -> {
              //   System.out.println(contact.getName());
                 ContactchatItem item = new ContactchatItem(contact);
                 item.getRootAncherPane_user_custome_cell().setOnMouseClicked(e->{
                     System.out.println("Clicked");
                     setContactInChatView(contact);
                 });
                MAIN_CONTACTS.getChildren().add(item.getRootAncherPane_user_custome_cell());
            });

        });

        getContacts.setOnFailed(event -> {
            System.err.println("Failed");
        });

        getContacts.setOnCancelled(event -> {
            System.err.println("Cancelled");
        });

        new Thread(getContacts).start();


        txtMsg.textProperty().addListener((obs,old,niu)->{
           System.out.println("Typing ...." + niu);

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
                    if(!txtMsg.getText().isEmpty()){
                        onSendMessageEvent();
                    }
                }
            }
        });



        amqp.setToListenToCapture("QUEUE_onUserSaved");

    }
   // Timer t = new Timer();

    AMQP amqp =  new AMQP();
    void setContactInChatView(Contact contact) {

        if(historyMutableMap.get(contact) == null) {
            var chats = new ChatHistory();
            chats.setChatHistory(new ArrayList<Message>());
            historyMutableMap.put(contact, chats);
        }


        this.contactInCurrentView = contact;
        txtInChatTitleNamePerson.setText(contact.getFullName());
        txtInChatTitleSubNamePerson.setText(contact.getEmail());
        amqp.setToListenToCapture("onMessageSaved_" + LOGGED_USER.getUserId() + "__to_" + contactInCurrentView.getId());
    }


}
