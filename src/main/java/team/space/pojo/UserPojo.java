package team.space.pojo;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class UserPojo {

    @Id
    public   long id ;
    public  String email;
    public  String password;
    public  String fullName;
    public  String profilePictureUrl;
    public  boolean isActive ;
    public  boolean isIsVerified;
    public  String companyClientId;
    public  String createdAt;

  public  String id_;

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isIsVerified() {
        return isIsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        isIsVerified = isVerified;
    }

    public String getCompanyClientId() {
        return companyClientId;
    }

    public void setCompanyClientId(String companyClientId) {
        this.companyClientId = companyClientId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}


