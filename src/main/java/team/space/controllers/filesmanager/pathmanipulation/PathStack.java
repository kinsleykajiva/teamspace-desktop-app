package team.space.controllers.filesmanager.pathmanipulation;


import team.space.controllers.filesmanager.constants.CommonData;
import team.space.requests.files.getfiles.FileObject;

import java.util.ArrayList;

public class PathStack {
    private static final ArrayList<FileObject> pathsPrevious = new ArrayList<>();
    private static final ArrayList<FileObject> pathsNext = new ArrayList<>();

    public static FileObject getNextDirectory() {
        if (pathsNext.isEmpty()) return null;
        FileObject res = pathsNext.get(pathsNext.size() - 1);
        pathsNext.remove(pathsNext.size() - 1);
        return res;
    }

    public static FileObject getPreviousDirectory() {
        if (pathsPrevious.isEmpty()) return null;
        FileObject res = pathsPrevious.get(pathsPrevious.size() - 1);
        pathsPrevious.remove(pathsPrevious.size() - 1);
        return res;
    }

    public static void setNextDirectory(FileObject fileDetail) {
        if (fileDetail != null) {
            pathsNext.add(fileDetail);
        }
    }

    public static void setPreviousDirectory(FileObject fileDetail) {
        if (fileDetail != null) {
            pathsPrevious.add(fileDetail);
        }
    }

    public static void printStack() {
        /*System.out.println();
        for (FileObject fileDetail : pathsPrevious) {
            System.out.print(fileDetail.getName() + " --> ");
        }
        System.out.print("{{ Current : " + CommonData.CURRENT_DIRECTORY.getName() + "}}" + " --> ");
        for (FileObject fileDetail : pathsNext) {
            System.out.print(fileDetail.getName() + " --> ");
        }
        System.out.println();*/
    }
}
