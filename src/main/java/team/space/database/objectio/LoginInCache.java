package team.space.database.objectio;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import team.space.dto.AccountDaoImpl;
import team.space.dto.MeetDto;

import java.util.Date;


/**This class will always have one row based on he logged in user*/

@DatabaseTable(tableName = "accounts", daoClass = AccountDaoImpl.class)
public class LoginInCache {
    @DatabaseField(generatedId = true)
    long id;
    @DatabaseField
    public String companyId;
    @DatabaseField
    public String profileImage;
    @DatabaseField
    public String fullName;

    @DatabaseField
    public String email;
    @DatabaseField
    public String userId;
    @DatabaseField
    public int role;
    @DatabaseField
    public String refreshToken;
    public String accessToken;
    @ForeignCollectionField()
    private ForeignCollection<MeetDto> meets;

    Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "LoginInCache{" +
                "id=" + id +
                ", companyId='" + companyId + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", role=" + role +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", date=" + date +
                '}';
    }
}
