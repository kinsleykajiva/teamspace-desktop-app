package team.space.controllers.filesmanager.model;



import team.space.controllers.filesmanager.constants.FileType;
import team.space.utils.FileValidity;



public class FileDetail {

    private  String filePath, fileName;
    private String sizeInByte, sizeInKiloByte, sizeInGigaByte, sizeInMegaByte, optimizedSize;
    private String lastAccessTime, lastModifiedTime, creationTime;

    private FileType fileType;
    private String fileExtension = "unknown";


    private int numberOfFiles = 1;


    public String getLastAccessTime() {
        return lastAccessTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public FileType getFileType() {
        return fileType;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }

    public String getSizeInByte() {
        return sizeInByte;
    }

    public String getSizeInKiloByte() {
        return sizeInKiloByte;
    }

    public String getSizeInGigaByte() {
        return sizeInGigaByte;
    }



    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getOptimizedSize() {

        return optimizedSize;
    }

    @Override
    public String toString() {
        return "fileName       : " + fileName + "\n" +
                "filePath       : " + filePath + "\n" +
                "sizeInByte     : " + sizeInByte + "\n" +
                "sizeInKiloByte : " + sizeInKiloByte + "\n" +
                "sizeInGigaByte : " + sizeInGigaByte + "\n" +
                "optimizedSize  : " + optimizedSize + "\n" +
                "fileType       : " + fileType + "\n" +
                "fileExtension  : " + fileExtension + "\n" +
                "numberOfFiles  : " + numberOfFiles + "\n";

    }

}
