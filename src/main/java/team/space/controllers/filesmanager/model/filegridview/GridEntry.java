package team.space.controllers.filesmanager.model.filegridview;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import team.space.controllers.filesmanager.constants.CommonData;
import team.space.controllers.filesmanager.constants.Dimensions;
import team.space.controllers.filesmanager.constants.Icons;
import team.space.controllers.filesmanager.model.filelistview.ListEntry;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowButtonShare;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowImageView;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowLabel;
import team.space.requests.files.getfiles.FileObject;

import java.io.File;

//import static win95.utilities.filehandling.OpenFile.doubleClick;

public class GridEntry {
    private String name, url;

    private RowImageView rowImageView;
    private RowLabel rowNameLabel;
    private RowButtonShare share;
    private FileObject fileDetail;
    private ListEntry listEntry;
    GridPane fileBlock;

    public GridEntry(FileObject fileDetail) {
        setData(fileDetail);
        listEntry = new ListEntry(fileDetail);
        listEntry.setGridEntry(this);
        makeFileBlock();
    }


    public GridEntry(ListEntry listEntry) {
        this.listEntry = listEntry;
        setData(listEntry.getFileDetail());
        makeFileBlock();
    }

    private void setData(FileObject fileDetail) {
        this.fileDetail = fileDetail;
        this.name = fileDetail.getDetail().getOriginalFilename();
        this.url = Icons.getFileIconPath(fileDetail.getDetail().getMimeType());

        rowNameLabel = new RowLabel(fileDetail);

        Image image = new Image(new File(url).toURI().toString());
        this.rowImageView = new RowImageView(fileDetail);
        this.rowImageView.setImage(image);
        rowImageView.setFitHeight(Dimensions.GRIDVIEW_ROWIMAGEVIEW);
        rowImageView.setFitWidth(Dimensions.GRIDVIEW_ROWIMAGEVIEW);
        share = new RowButtonShare(fileDetail, "Share");

        share.setOnAction(event -> {
            /*
                replace with share module..
                not implemented yet...
             */
            // LogsPrinter.printLogic("ListEntry", 42, "Share logic not implemented yet");
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RowImageView getRowImageView() {
        return rowImageView;
    }

    public void setRowImageView(RowImageView rowImageView) {
        this.rowImageView = rowImageView;
    }

    public RowLabel getRowNamelabel() {
        return rowNameLabel;
    }

    public void setRowNamelabel(RowLabel rowNamelabel) {
        this.rowNameLabel = rowNamelabel;
    }

    public RowButtonShare getShare() {
        return share;
    }

    public void setShare(RowButtonShare share) {
        this.share = share;
    }

    public FileObject getFileDetail() {
        return fileDetail;
    }

    public ListEntry getListEntry() {
        return listEntry;
    }

    public void setListEntry(ListEntry listEntry) {
        this.listEntry = listEntry;
    }

    public GridPane getFileBlock() {
        return fileBlock;
    }

    public void setFileBlock(GridPane fileBlock) {
        this.fileBlock = fileBlock;
    }

    public void setFileDetail(FileObject fileDetail) {
        this.fileDetail = fileDetail;
    }

    public void makeFileBlock() {
        ContextMenu contextMenu = new ContextMenu();
        fileBlock = new GridPane();
        fileBlock.getStyleClass().add("file-block");
        fileBlock.setHgap(5);
        fileBlock.setVgap(5);
//        fileBlock.setGridLinesVisible(true);
        GridPane.setHalignment(rowImageView, HPos.CENTER);
        rowNameLabel.setWrapText(true);

        rowNameLabel.setAlignment(Pos.CENTER);
        fileBlock.add(rowImageView, 0, 0, 2, 1);
        fileBlock.add(rowNameLabel, 0, 1, 2, 1);

        GridPane.setHalignment(rowNameLabel, HPos.CENTER);

        fileBlock.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.getClickCount() == 2) {
                    /*try {
                      //  doubleClick(listEntry.getFileDetail());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                } else {
                    if (CommonData.instance != null) {
                        CommonData.instance.showPreview(listEntry.getFileDetail());
                    } else {
                       // LogsPrinter.printLogic("UpdateCellFactory", 47, "controller instance is null");
                    }
                }
            }
        });
        fileBlock.setOnContextMenuRequested(event -> {
            contextMenu.show(fileBlock, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        contextMenu.getStyleClass().add(".context-menu");

        MenuItem menuItem1 = new MenuItem("Change tags");
        menuItem1.getStyleClass().add("menu-item");
        menuItem1.setOnAction(e -> {
            System.out.println("context click : " + listEntry.toString());
           // CommonData.instance.showAddTagToFileDialog(listEntry);

        });

        if (listEntry.getFileDetail().getDetail().getMimeType() .equals("folder")) {
            MenuItem menuItem2 = new MenuItem("Open in Terminal");
            menuItem2.getStyleClass().add("menu-item");
            menuItem2.setOnAction(e -> {
                System.out.println("context click : " + listEntry.toString());
               // SystemCommands.OpenDirInTerminal(listEntry.getFileDetail().getFilePath());

            });
            contextMenu.getItems().add(menuItem2);
        }
        if (!listEntry.getFileDetail().getDetail().getMimeType() .equals("folder")) {
            MenuItem share = new MenuItem("Share");
            share.getStyleClass().add("menu-item");
            share.setOnAction(e -> {
               // ListOfFileTransfer.add(fileDetail);

            });
            contextMenu.getItems().add(share);
        }

        MenuItem menuItem3 = new MenuItem("Delete");
        menuItem3.getStyleClass().add("menu-item");
        menuItem3.setOnAction(e -> {
            CommonData.instance.deleteView(this);
            if (listEntry.getFileDetail().getDetail().getMimeType().equals("folder")) {
               // SystemCommands.deleteFolder(listEntry.getFileDetail().getFilePath());
            } else {
               // SystemCommands.deleteFile(listEntry.getFileDetail().getFilePath());
            }
        });

        contextMenu.getItems().addAll(menuItem1, menuItem3);
        fileBlock.getStyleClass().clear();
        fileBlock.getStyleClass().add("file-block");
    }


    public GridPane getFileGridBlock() {

        return fileBlock;
    }

    public void refresh() {
        fileBlock.setMaxWidth(100);
        fileBlock.setMinWidth(100);
        rowNameLabel.setMaxWidth(100);
        rowNameLabel.setMinWidth(100);
    }

    @Override
    public String toString() {
        return "GridEntry{" +
                "name='" + name + '\'' +
                ", imageView=" + "imageView" +
                ", delete=" + "delete" +
                ", open=" + "open" +
                '}';
    }
}
