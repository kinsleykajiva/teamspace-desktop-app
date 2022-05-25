package team.space.dto;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import team.space.database.objectio.LoginInCache;

import java.sql.Timestamp;

@DatabaseTable(tableName = "meets", daoClass = MeetDaoImpl.class)
public class MeetDto {

    @DatabaseField(generatedId = true)
    private int id;
  @DatabaseField()
    private String id_;

    @DatabaseField(canBeNull = false, foreign = true)
    private LoginInCache account;

    @DatabaseField(canBeNull = false)
    private String room;

    @DatabaseField(canBeNull = false)
    private Timestamp startDate;

    @DatabaseField(canBeNull = false)
    private Timestamp endDate;

    @ForeignCollectionField()
    private ForeignCollection<ParticipantDto> participants;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_() {
        return id_;
    }

    public LoginInCache getAccount() {
        return account;
    }

    public void setAccount(LoginInCache account) {
        this.account = account;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public ForeignCollection<ParticipantDto> getParticipants() {
        return participants;
    }

    public MeetDto(String room, Timestamp startDate, Timestamp endDate) {
        this();
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public MeetDto(String room, Timestamp startDate, Timestamp endDate, LoginInCache accountDto) {
        this(room, startDate, endDate);
        this.account = accountDto;
    }

    public MeetDto() {
    }
}

