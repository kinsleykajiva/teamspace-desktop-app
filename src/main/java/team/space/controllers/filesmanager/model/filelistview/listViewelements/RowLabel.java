package team.space.controllers.filesmanager.model.filelistview.listViewelements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import team.space.requests.files.getfiles.FileObject;

//import win95.model.FileDetail;

public class RowLabel extends Label {

    private final FileObject fileDetail;

    public RowLabel(FileObject fileDetail) {
        super(fileDetail.getDetail().getMimeType().equals("folder") ? fileDetail.getName(): fileDetail.getDetail().getOriginalFilename());
        this.fileDetail = fileDetail;
    }

    public FileObject getFileDetail() {
        return fileDetail;
    }

    public void refresh() {
        this.setMaxWidth(500);
        this.setMinWidth(500);
        this.setAlignment(Pos.BASELINE_CENTER);
    }


    @Override
    public String toString() {
        return "{{" + fileDetail.getName() + "}}" + '\n' +
                "{{" + fileDetail.getDetail() + "}}" + '\n';
    }
}
