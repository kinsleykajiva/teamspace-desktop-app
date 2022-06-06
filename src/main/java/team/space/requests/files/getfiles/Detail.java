package team.space.requests.files.getfiles;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Detail {
    @JsonProperty("folder")
    public String getFolder() {
        return this.folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    String folder;

    @JsonProperty("createdAt")
    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    Date createdAt;

    @JsonProperty("folderLevels")
    public int getFolderLevels() {
        return this.folderLevels;
    }

    public void setFolderLevels(int folderLevels) {
        this.folderLevels = folderLevels;
    }

    int folderLevels;

    @JsonProperty("mimeType")
    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    String mimeType;

    @JsonProperty("createdByUserId")
    public String getCreatedByUserId() {
        return this.createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    String createdByUserId;

    @JsonProperty("etag")
    public String getEtag() {
        return this.etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    String etag;

    @JsonProperty("groupId")
    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    String groupId;

    @JsonProperty("id_")
    public String getId_() {
        return this.id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    String id_;

    @JsonProperty("key")
    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;

    @JsonProperty("lastModifiedUserId")
    public String getLastModifiedUserId() {
        return this.lastModifiedUserId;
    }

    public void setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }

    String lastModifiedUserId;

    @JsonProperty("originalFilename")
    public String getOriginalFilename() {
        return this.originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    String originalFilename;

    @JsonProperty("sizeInBytes")
    public int getSizeInBytes() {
        return this.sizeInBytes;
    }

    public void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    int sizeInBytes;

    @JsonProperty("url")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    @JsonProperty("usersIdsArrWithAccess")
    public Object getUsersIdsArrWithAccess() {
        return this.usersIdsArrWithAccess;
    }

    public void setUsersIdsArrWithAccess(Object usersIdsArrWithAccess) {
        this.usersIdsArrWithAccess = usersIdsArrWithAccess;
    }

    Object usersIdsArrWithAccess;
}