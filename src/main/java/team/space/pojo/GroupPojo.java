package team.space.pojo;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class GroupPojo {
    @Id
    long id;
    String title ,companyClientId  ,myDateTime;
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
