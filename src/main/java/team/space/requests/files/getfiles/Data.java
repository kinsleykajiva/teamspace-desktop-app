package team.space.requests.files.getfiles;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Data {
    @JsonProperty("fileObjects")
    public ArrayList<FileObject> getFileObjects() {
        return this.fileObjects;
    }

    public void setFileObjects(ArrayList<FileObject> fileObjects) {
        this.fileObjects = fileObjects;
    }

    ArrayList<FileObject> fileObjects;
}
