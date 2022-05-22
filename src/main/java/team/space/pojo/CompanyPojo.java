package team.space.pojo;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class CompanyPojo {
    @Id
    long id;
    String title , adminEmail  ,myDateTime;
    boolean isActive ;

    String id_;

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
