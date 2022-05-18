package team.space;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

import java.util.Date;

@Entity
public class Note {

    @Id
    long id;

    String text;
    String comment;
    Date date;



    public Note(Long id) {
        this.id = id;
    }

    public Note(long id, String text, String comment, Date date) {
        this.id = id;
        this.text = text;
        this.comment = comment;
        this.date = date;
    }

    public Note() {
    }

    public Note(String text) {
        this.text = text;
        date = new Date();
    }

    public long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Note \"" + text + "\" on " + date;
    }
}
