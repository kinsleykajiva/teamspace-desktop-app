package team.space.requests.getallusers;

public class User {

    public String profilePictureUrl;
    public CreatedAt createdAt;
    public String id_;
    public String companyClientId;
    public String fullName;
    public boolean isActive;
    public String email;
    public boolean isIsVerified;

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(CreatedAt createdAt) {
        this.createdAt = createdAt;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getCompanyClientId() {
        return companyClientId;
    }

    public void setCompanyClientId(String companyClientId) {
        this.companyClientId = companyClientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsVerified() {
        return isIsVerified;
    }

    public void setIsVerified(boolean isVerified) {
        isIsVerified = isVerified;
    }
}
