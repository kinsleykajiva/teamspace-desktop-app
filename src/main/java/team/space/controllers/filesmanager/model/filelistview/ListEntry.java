package team.space.controllers.filesmanager.model.filelistview;

import javafx.scene.image.Image;
import team.space.controllers.filesmanager.constants.Dimensions;
import team.space.controllers.filesmanager.constants.Icons;
import team.space.controllers.filesmanager.model.filegridview.GridEntry;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowButtonShare;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowImageView;
import team.space.controllers.filesmanager.model.filelistview.listViewelements.RowLabel;
import team.space.requests.files.getfiles.FileObject;

import java.io.File;

public class ListEntry {
    private String name, url;
    private GridEntry gridEntry;
    private RowImageView rowImageView;
    private RowLabel rowNameLabel, rowCount;
    private RowButtonShare delete, open, share;
    private FileObject fileDetail;


    public ListEntry(FileObject fileDetail) {
        this.fileDetail = fileDetail;
        this.name = fileDetail.getName();
        System.out.println("fileDetail.getMimeType() = " + fileDetail.getDetail().getMimeType());
        this.url = Icons.getFileIconPath(fileDetail.getDetail().getMimeType());

        rowNameLabel = new RowLabel(fileDetail);
        rowNameLabel.setMaxWidth(1000);
        rowNameLabel.setMinWidth(500);

        rowCount = new RowLabel(fileDetail);
        rowCount.setText("");
        rowCount.setMinWidth(30);

        Image image = new Image(new File(url).toURI().toString());
        this.rowImageView = new RowImageView(fileDetail);
        this.rowImageView.setImage(image);


        share = new RowButtonShare(fileDetail, "Share");


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

    public RowLabel getRowCount() {
        return rowCount;
    }

    public void setRowCount(RowLabel rowCount) {
        this.rowCount = rowCount;
    }

    public RowButtonShare getDelete() {
        return delete;
    }

    public void setDelete(RowButtonShare delete) {
        this.delete = delete;
    }

    public RowButtonShare getOpen() {
        return open;
    }

    public void setOpen(RowButtonShare open) {
        this.open = open;
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

    public void setFileDetail(FileObject fileDetail) {
        this.fileDetail = fileDetail;
    }

    public void refresh() {
        rowImageView.setFitWidth(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        rowImageView.setFitHeight(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        rowNameLabel.setMaxWidth(500);
        rowNameLabel.setMinWidth(500);
    }

    @Override
    public String toString() {
        return "ListEntry{" +
                "name='" + name + '\'' +
                ", imageView=" + "imageView" +
                ", delete=" + "delete" +
                ", open=" + "open" +
                '}';
    }


    public GridEntry getGridView() {
        return gridEntry;
    }

    public void setGridEntry(GridEntry gridEntry) {
        this.gridEntry = gridEntry;
    }
}
