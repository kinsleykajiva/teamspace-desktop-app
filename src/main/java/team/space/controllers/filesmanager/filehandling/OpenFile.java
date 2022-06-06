package team.space.controllers.filesmanager.filehandling;



import team.space.controllers.filesmanager.FileManagerViewController;
import team.space.controllers.filesmanager.constants.CommonData;
import team.space.controllers.filesmanager.model.quickaccess.RecentFiles;
import team.space.requests.files.getfiles.Child;
import team.space.requests.files.getfiles.FileObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenFile {
    /*
        if Desktop not work here use OS module to open file using system call
        not yet implemented...
     */

    public static void open(FileObject fileDetail) {
        RecentFiles.addRecentQueue(fileDetail.getDetail().getUrl());
        // Desktop.getDesktop().open(fileDetail.getFile());
    }

    public static void doubleClick(ArrayList<Child> files ) throws IOException {
        if (CommonData.instance != null) {
            FileManagerViewController. BUTTON_PRESSED = "NEXT";
System.out.println("XXfiles:: " +files);
           // FileObject fileObject = new FileObject();
            List<FileObject> filesList = new ArrayList<>();
            for (Child file : files) {
                FileObject fileObject1 = new FileObject();
                fileObject1.setDetail(file.getDetail());
                fileObject1.setName(file.getName());
                fileObject1.setChildren(file.getChildren());
                filesList.add(fileObject1);
            }
            if (!CommonData.instance.updateView( filesList )) {
                FileManagerViewController.BUTTON_PRESSED = "NONE";
            }
        }
    }
}
