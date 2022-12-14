package team.space.controllers.filesmanager.model.filelistview;

import team.space.controllers.filesmanager.constants.CommonData;
import team.space.requests.files.getfiles.FileObject;

import java.util.ArrayList;
import java.util.List;

public class PathStack {
    private static final ArrayList<List<FileObject>> pathsPrevious = new ArrayList<>();
    private static final ArrayList<List<FileObject>> pathsNext = new ArrayList<>();

    public static List<FileObject> getNextDirectory() {
        if (pathsNext.isEmpty()) return null;
        List<FileObject> res = pathsNext.get(pathsNext.size() - 1);
        pathsNext.remove(pathsNext.size() - 1);
        return res;
    }

    public static List<FileObject> getPreviousDirectory() {
        if (pathsPrevious.isEmpty()) return null;
        List<FileObject> res = pathsPrevious.get(pathsPrevious.size() - 1);
        pathsPrevious.remove(pathsPrevious.size() - 1);
        return res;
    }

    public static void setNextDirectory(List<FileObject> fileDetail) {
        if (fileDetail != null) {
            pathsNext.add(fileDetail);
        }
    }

    public static void setPreviousDirectory(List<FileObject> fileDetail) {
        if (fileDetail != null) {
            pathsPrevious.add(fileDetail);

        }
    }

    public static void printStack() {
      /*  System.out.println();
        for (FileObject fileDetail : pathsPrevious) {
           // System.out.print(fileDetail.getFileName() + " --> ");
            System.out.print(fileDetail.getName() + " --> ");
        }
        System.out.print("{{ Current : " + CommonData.CURRENT_DIRECTORY.getName() + "}}" + " --> ");
        for (FileObject fileDetail : pathsNext) {
          //  System.out.print(fileDetail.getFileName() + " --> ");
            System.out.print(fileDetail.getDetail().getOriginalFilename() + " --> ");
        }
        System.out.println();*/
    }
}

