package team.space.controllers.filesmanager.model.filelistview.listViewelements;

import javafx.scene.layout.GridPane;
import team.space.requests.files.getfiles.FileObject;
//import team.space.requests.files.fetchfiles.FileObject;
//import win95.model.FileDetail;

public class RowGridPane extends GridPane {
    private final FileObject fileDetail;

    public RowGridPane(FileObject fileDetail) {
        this.fileDetail = fileDetail;
    }

    public FileObject getFileDetail() {
        return fileDetail;
    }


    @Override
    public String toString() {
        return "{{" + fileDetail.getName() + "}}" + '\n' +
                "{{" + fileDetail.getDetail().getUrl() + "}}" + '\n';
    }
}
