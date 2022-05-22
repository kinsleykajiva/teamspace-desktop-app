package team.space.models;

import team.space.beans.IntegerProperty;
import team.space.beans.StringProperty;

public class Contact {

    private StringProperty id;
    private StringProperty fullName;
    private StringProperty email;
    private IntegerProperty unreadMessages;

    public Contact(String  id,  String fullName,  String email) {

        this.id = new StringProperty(id);
        this.fullName = new StringProperty(fullName);
        this.email = new StringProperty(email);
        this.unreadMessages = new IntegerProperty(0);

    }

    public int getUnreadMessages() {
        return unreadMessages.get();
    }

    public IntegerProperty unreadMessagesProperty() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages.set(unreadMessages);
    }


    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }



    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }





    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

}
