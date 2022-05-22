package team.space.pojo;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ChatMessagePojo {
    @Id
    long id;
    String fromUserId , toUserId ,sentByUserId ,message , messageType ,companyClientId  ,myDateTime;
    boolean isActive ;
    String id_;

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getSentByUserId() {
        return sentByUserId;
    }

    public void setSentByUserId(String sentByUserId) {
        this.sentByUserId = sentByUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCompanyClientId() {
        return companyClientId;
    }

    public void setCompanyClientId(String companyClientId) {
        this.companyClientId = companyClientId;
    }

    public String getMyDateTime() {
        return myDateTime;
    }

    public void setMyDateTime(String myDateTime) {
        this.myDateTime = myDateTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
