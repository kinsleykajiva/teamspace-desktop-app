package team.space.controllers.filesmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import team.space.controllers.filesmanager.constants.*;
import team.space.controllers.filesmanager.model.FileDetail;
import team.space.controllers.filesmanager.model.filegridview.GridEntry;
import team.space.controllers.filesmanager.model.filelistview.CellFactory;
import team.space.controllers.filesmanager.model.filelistview.ListEntry;
import team.space.controllers.filesmanager.model.filelistview.PathStack;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import static javafx.application.Application.setUserAgentStylesheet;
import static team.space.controllers.filesmanager.constants.CommonData.CURRENT_DIRECTORY;

public class FileManagerViewController implements Initializable {
    @FXML
    private ListView<ListEntry> listView;
    @FXML
    private ScrollPane gridViewScrollPane;
    @FXML
    private FlowPane gridView;



    @FXML
    private AnchorPane middle;
    @FXML
    private AnchorPane middleTop;
    public static String BUTTON_PRESSED = "NONE";
    ContextMenu menuPopup;
    final private ObservableList<ListEntry> observableList = FXCollections.observableArrayList();
    final private ArrayList<GridEntry> gridObjectList = new ArrayList<>();
    private LogicConstants.SortingType sortingType = LogicConstants.SortingType.BY_DEFAULT;
    Comparator<ListEntry> size_desc = (o2, o1) -> o1.getFileDetail().getSizeInByte().toLowerCase().compareTo(o2.getFileDetail().getSizeInByte().toLowerCase());

    Comparator<ListEntry> size_inc = (o2, o1) -> o2.getFileDetail().getSizeInByte().toLowerCase().compareTo(o1.getFileDetail().getSizeInByte().toLowerCase());

    Comparator<ListEntry> name_desc = (o2, o1) -> o1.getFileDetail().getFileName().toLowerCase().compareTo(o2.getFileDetail().getFileName().toLowerCase());

    Comparator<ListEntry> name_inc = (o2, o1) -> o2.getFileDetail().getFileName().toLowerCase().compareTo(o1.getFileDetail().getFileName().toLowerCase());

    Comparator<ListEntry> access_desc = (o2, o1) -> o1.getFileDetail().getLastAccessTime().toLowerCase().compareTo(o2.getFileDetail().getLastAccessTime().toLowerCase());

    Comparator<ListEntry> access_inc = (o2, o1) -> o2.getFileDetail().getLastAccessTime().toLowerCase().compareTo(o1.getFileDetail().getLastAccessTime().toLowerCase());

    void sortRows(LogicConstants.SortingType sortingType) {
        if (this.sortingType != sortingType) {
            if (sortingType == LogicConstants.SortingType.BY_ACCESS_DESC) {
                FXCollections.sort(observableList, access_desc);
            } else if (sortingType == LogicConstants.SortingType.BY_ACCESS_INC) {
                FXCollections.sort(observableList, access_inc);
            } else if (sortingType == LogicConstants.SortingType.BY_SIZE_DESC) {
                FXCollections.sort(observableList, size_desc);
            } else if (sortingType == LogicConstants.SortingType.BY_SIZE_INC) {
                FXCollections.sort(observableList, size_inc);
            } else if (sortingType == LogicConstants.SortingType.BY_NAME_DESC) {
                FXCollections.sort(observableList, name_desc);
            } else if (sortingType == LogicConstants.SortingType.BY_NAME_INC) {
                FXCollections.sort(observableList, name_inc);
            }
            this.sortingType = sortingType;
        }
    }

   // @FXML
    void showMenu(MouseEvent event) {
        System.out.println("Menu button pressed");
     //   menu = (ImageView) event.getSource();
     //   menuPopup.show(menu, event.getScreenX(), event.getScreenY());
    }
   // @FXML
    void nextDirectory(ActionEvent event) throws IOException {
        System.out.println("Next directory button pressed");
       /* BUTTON_PRESSED = "NEXT";
        FileDetail nextDirectory = PathStack.getNextDirectory();
        if (nextDirectory == null) {
            BUTTON_PRESSED = "NONE";
            System.out.println("PathStack.getNextDirectory() return null ");
            return;
        }
        if (updateView(nextDirectory)) {
            preview.getChildren().clear();
        } else {
            BUTTON_PRESSED = "NONE";
        }*/
    }
   // @FXML
    void previousDirectory(ActionEvent event) throws IOException {
        System.out.println("Previous directory button pressed");
        BUTTON_PRESSED = "PREVIOUS";
       /* FileDetail previousDirectory = PathStack.getPreviousDirectory();
        if (previousDirectory == null) {
            BUTTON_PRESSED = "NONE";
            System.out.println("PathStack.getPreviousDirectory() return null ");
            return;
        }
        if (updateView(previousDirectory)) {
            preview.getChildren().clear();
        } else {
            BUTTON_PRESSED = "NONE";
        }*/
    }

    public void showPreview(FileDetail fileDetail) {


    }

    void setHoverEffect(Node node) {
    }

    private void toggleViewMode() {
        if (CommonData.VIEW_MODE.equals("LISTVIEW")) {
            CommonData.VIEW_MODE = "GRIDVIEW";
            gridView.getChildren().clear();
            gridObjectList.clear();
            ArrayList<ListEntry> temp = new ArrayList<>(observableList);
            observableList.clear();
            for (ListEntry listEntry : temp) {
                if (listEntry != null) {
                    updateView(new GridEntry(listEntry));
                }
            }
            listView.setVisible(false);
            gridView.setVisible(true);
        } else {
            CommonData.VIEW_MODE = "LISTVIEW";
            for (ListEntry listEntry : observableList) {
                listEntry.refresh();
            }
            listView.setVisible(true);
            gridView.setVisible(false);
        }
        System.out.println(CommonData.VIEW_MODE);
        for (ListEntry listEntry : observableList) System.out.print(listEntry.toString() + " ");
        System.out.println();
        for (GridEntry listEntry : gridObjectList) System.out.print(listEntry.toString() + " ");
    }
    public void deleteView(GridEntry gridEntry) {
        gridView.getChildren().remove(gridEntry.getFileGridBlock());
        gridObjectList.remove(gridEntry);
        observableList.remove(gridEntry.getListEntry());
    }

    public void appendView(GridEntry gridEntry) {
        updateView(gridEntry);
    }


    void updateView(GridEntry gridEntry) {
        ListEntry listEntry = gridEntry.getListEntry();
        if (CommonData.VIEW_MODE.equals("GRIDVIEW")) {
            gridEntry.refresh();
        } else {
            listEntry.refresh();
        }
        gridObjectList.add(gridEntry);
        gridView.getChildren().add(gridEntry.getFileGridBlock());
        observableList.add(listEntry);
    }

    public boolean updateView(FileDetail fileDetail) {
        if (fileDetail == null) return false;
        File parentPath = fileDetail.getFile();

        File[] files = parentPath.listFiles();
        if (files == null) {
           /* LogsPrinter.printLogic("Controller", 157,
                    "parent Path list files give null (invalid path/some other error");*/
            return false;
        }
        if (BUTTON_PRESSED.equals("NEXT")) {
            PathStack.setPreviousDirectory(CURRENT_DIRECTORY);
            BUTTON_PRESSED = "NONE";
            CURRENT_DIRECTORY = fileDetail;
        } else if (BUTTON_PRESSED.equals("PREVIOUS")) {
            PathStack.setNextDirectory(CURRENT_DIRECTORY);
            BUTTON_PRESSED = "NONE";
            CURRENT_DIRECTORY = fileDetail;
        }
        observableList.clear();
        gridView.getChildren().clear();
        gridObjectList.clear();
        for (File file : files) {
            try {
                FileDetail inFileDetail = new FileDetail(file);
                ListEntry listEntry = new ListEntry(inFileDetail);
                GridEntry gridEntry = new GridEntry(listEntry);
                updateView(gridEntry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.DIRECTORY;
        System.out.println(observableList.toString());
        System.out.println(gridObjectList.toString());
        return true;

    }
    public void updateHomeView() throws IOException {
        File USER_HOME = new File(System.getProperty("user.home"));
        CommonData.CURRENT_LIST_VIEW_ITEM = FileType.DIRECTORY;
        CURRENT_DIRECTORY = new FileDetail(USER_HOME);
        updateView(CURRENT_DIRECTORY);
    }

    void setFavouritePanel() {
        HBox top = new HBox();
        Label favourite = new Label("Favourite");
        favourite.setFont(Fonts.TOP_FONT);

        /*HBox recent = getLeftPaneHBox("Recent", Icons.RECENT_LIGHT);
        recent.getStyleClass().add("hbox");
        HBox desktop = getLeftPaneHBox("Desktop", Icons.DESKTOP_LIGHT);
        desktop.getStyleClass().add("hbox");
        HBox download = getLeftPaneHBox("Downloads", Icons.DOWNLOAD_LIGHT);
        download.getStyleClass().add("hbox");
        HBox document = getLeftPaneHBox("Documents", Icons.DOCUMENT_LIGHT);
        document.getStyleClass().add("hbox");
        HBox home = getLeftPaneHBox("Home", Icons.HOME_LIGHT);
        home.getStyleClass().add("hbox");
        favouritePanel.getChildren().addAll(favourite, recent, desktop, download, document, home);*/


    }


    Button right, left;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridView.setVgap(10);
        gridViewScrollPane.setFitToWidth(true);
        listView.setItems(observableList);
        left = new Button("<");
        right = new Button("<");
        if (CommonData.VIEW_MODE.equals("LISTVIEW")) {
            listView.setVisible(true);
            gridView.setVisible(false);
        } else {
            listView.setVisible(false);
            gridView.setVisible(true);
        }
        try {
            updateHomeView();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image leftImageLight = new Image(new File(Icons.LIGHT_LEFT_ARROW).toURI().toString());
        ImageView leftImageLightImageView = new ImageView(leftImageLight);
        leftImageLightImageView.setFitWidth(Dimensions.DIRECTORY_MOVEMENT_ICON);
        leftImageLightImageView.setFitHeight(Dimensions.DIRECTORY_MOVEMENT_ICON);
        left.setGraphic(leftImageLightImageView);

        Image TRANSFER = new Image(new File(Icons.TRANSFER_LIGHT).toURI().toString());
       // transfer.setImage(TRANSFER);

        Image rightImageLight = new Image(new File(Icons.LIGHT_RIGHT_ARROW).toURI().toString());
        ImageView rightImageLightImageView = new ImageView(rightImageLight);
        rightImageLightImageView.setFitWidth(Dimensions.DIRECTORY_MOVEMENT_ICON);
        rightImageLightImageView.setFitHeight(Dimensions.DIRECTORY_MOVEMENT_ICON);
        right.setGraphic(rightImageLightImageView);

        Image image = new Image(new File(Icons.LIGHT_MENU_DOT).toURI().toString());
        //menu.setFitHeight(Dimensions.MENU_ICON);
      //  menu.setFitWidth(Dimensions.MENU_ICON);
      //  menu.setImage(image);


        setFavouritePanel();


        listView.setCellFactory(new CellFactory());

        menuPopup = new ContextMenu();
        menuPopup.getStyleClass().add("context-menu");

        MenuItem sort_by_name = new MenuItem("sort by name");
        sort_by_name.getStyleClass().add("menu-item");
        MenuItem createNewFile = new MenuItem("New File");
        createNewFile.getStyleClass().add("menu-item");
        createNewFile.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CreateFile.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //CreateFile createFile = fxmlLoader.<CreateFile>getController();
           // createFile.setFileDetail(CURRENT_DIRECTORY);
           // createFile.setFileType(FileType.FILE);
            Scene scene1 = new Scene(parent, 419, 159);
            if (UserPreference.getTHEME() == Themes.LIGHT) {
                scene1.getStylesheets().add(getClass().getResource("../view/css/LightStyle.css").toExternalForm());
            } else if (UserPreference.getTHEME() == Themes.DARK) {
                scene1.getStylesheets().add(getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene1);
            stage.showAndWait();
        });

        MenuItem createNewFolder = new MenuItem("New Folder");
        createNewFolder.getStyleClass().add("menu-item");
        createNewFolder.setOnAction(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/CreateFile.fxml"));
            Parent parent = null;
            try {
                parent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
           // CreateFile createFile = fxmlLoader.<CreateFile>getController();
          //  createFile.setFileDetail(CURRENT_DIRECTORY);
//            Scene scene1 = new Scene(parent, 419, 159);
            if (UserPreference.getTHEME() == Themes.LIGHT) {
               // scene1.getStylesheets().add(getClass().getResource("../view/css/LightStyle.css").toExternalForm());
            } else if (UserPreference.getTHEME() == Themes.DARK) {
              //  scene1.getStylesheets().add(getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
         //   stage.setScene(scene1);
            stage.showAndWait();
        });

        sort_by_name.setOnAction(event -> {
            if (sortingType == LogicConstants.SortingType.BY_DEFAULT ||
                    sortingType == LogicConstants.SortingType.BY_NAME_DESC) {
                sortRows(LogicConstants.SortingType.BY_NAME_INC);
            } else {
                sortRows(LogicConstants.SortingType.BY_NAME_DESC);
            }
        });


        MenuItem sort_by_size = new MenuItem("sort by size");
        sort_by_size.getStyleClass().add("menu-item");
        sort_by_size.setOnAction(event -> {
            if (sortingType == LogicConstants.SortingType.BY_DEFAULT ||
                    sortingType == LogicConstants.SortingType.BY_SIZE_DESC) {
                sortRows(LogicConstants.SortingType.BY_SIZE_INC);
            } else {
                sortRows(LogicConstants.SortingType.BY_SIZE_DESC);
            }
        });


        MenuItem sort_by_access = new MenuItem("sort by access time");
        sort_by_access.getStyleClass().add("menu-item");
        sort_by_access.setOnAction(event -> {
            if (sortingType == LogicConstants.SortingType.BY_DEFAULT ||
                    sortingType == LogicConstants.SortingType.BY_ACCESS_DESC) {
                sortRows(LogicConstants.SortingType.BY_ACCESS_INC);
            } else {
                sortRows(LogicConstants.SortingType.BY_ACCESS_DESC);
            }
        });

        MenuItem theme = new MenuItem("Change Theme");
        theme.setOnAction(e -> {
            Scene scene = listView.getScene();
            scene.getStylesheets().clear();
            setUserAgentStylesheet(null);
            System.out.println(scene);
            if (UserPreference.getTHEME() == Themes.DARK) {
                scene.getStylesheets().add(this.getClass().getResource("../view/css/LightStyle.css").toExternalForm());
                UserPreference.setTHEME(Themes.LIGHT);
            } else if (UserPreference.getTHEME() == Themes.LIGHT) {
                scene.getStylesheets().add(this.getClass().getResource("../view/css/DarkStyle.css").toExternalForm());
                UserPreference.setTHEME(Themes.DARK);
            }
        });

        MenuItem toggleFileView = new MenuItem("Toggle view mode");
        toggleFileView.setOnAction(event -> {
            toggleViewMode();
        });
        menuPopup.getItems().addAll(sort_by_name, sort_by_size, sort_by_access, theme, toggleFileView);
        if (CommonData.CURRENT_LIST_VIEW_ITEM == FileType.DIRECTORY) {
            menuPopup.getItems().addAll(createNewFile, createNewFolder);
        }
        CommonData.instance = this;
    }




}
