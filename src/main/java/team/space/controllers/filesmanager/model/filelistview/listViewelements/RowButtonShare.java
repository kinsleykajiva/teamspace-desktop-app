package team.space.controllers.filesmanager.model.filelistview.listViewelements;

import javafx.scene.control.Button;
import team.space.requests.files.getfiles.FileObject;
//import team.space.requests.files.fetchfiles.FileObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RowButtonShare extends Button implements ActionListener {
    private final FileObject fileDetail;
    private final String name;
    private final String path;

    public RowButtonShare(FileObject fileDetail, String text) {
        super(text);
        this.fileDetail = fileDetail;
        this.name = fileDetail.getDetail().getOriginalFilename();
        this.path = fileDetail.getDetail().getUrl();
    }

    public FileObject getFileDetail() {
        return fileDetail;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "{{" + name + "}}" + '\n' +
                "{{" + path + "}}" + '\n';
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
            Start Sharing file...
            not implemented yet...
         */
    }
}
