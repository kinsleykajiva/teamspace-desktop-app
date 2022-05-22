package team.space.models;



import java.util.ArrayList;

public class ChatHistory {
    // ChatHistory


    private ArrayList<Message> messages = new ArrayList<>();

    public void setChatHistory(ArrayList<Message> messages) {
        this.messages .addAll(messages);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
