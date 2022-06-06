package team.space.controllers.filesmanager.model.filelistview.listViewelements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import team.space.controllers.filesmanager.constants.Dimensions;
import team.space.controllers.filesmanager.constants.LogicConstants;
import team.space.requests.files.getfiles.FileObject;
//import team.space.requests.files.fetchfiles.FileObject;

import java.io.File;

public class RowImageView extends ImageView {
    private final FileObject fileDetail;
    private String imageURL;
    private Image image;

    public RowImageView(FileObject fileDetail) {
        this.fileDetail = fileDetail;
        this.setFitHeight(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        this.setFitWidth(Dimensions.LISTVIEW_ROWIMAGEVIEW);
    }

    public void refresh() {
        this.setFitHeight(Dimensions.LISTVIEW_ROWIMAGEVIEW);
        this.setFitWidth(Dimensions.LISTVIEW_ROWIMAGEVIEW);
    }

    public RowImageView(FileObject fileDetail, LogicConstants previewMode) {
        this.fileDetail = fileDetail;
        this.setFitHeight(Dimensions.PREVIEW_FILE_ICON);
        this.setFitWidth(Dimensions.PREVIEW_FILE_ICON);
    }

    public FileObject getFileDetail() {
        return fileDetail;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setRowImage(String url) {
        this.imageURL = url;
        image = new Image(new File(url).toURI().toString());
        super.setImage(image);
    }

    public void setRowImage(Image image) {
        super.setImage(image);
    }

    @Override
    public String toString() {
        return "{{" + fileDetail.getName() + "}}" + '\n' +
                "{{" + fileDetail.getDetail().getUrl() + "}}" + '\n';
    }

    public RowImageView getCopy() {
        RowImageView rowImageViewCopy = new RowImageView(fileDetail);
        rowImageViewCopy.setFitWidth(Dimensions.GRIDVIEW_ROWIMAGEVIEW);
        rowImageViewCopy.setFitHeight(Dimensions.GRIDVIEW_ROWIMAGEVIEW);
        return rowImageViewCopy;
    }
}
