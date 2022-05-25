package team.space.dto;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "participants", daoClass = ParticipantDaoImpl.class)
public class ParticipantDto {
    @DatabaseField()
    private String id;
    @DatabaseField(canBeNull = false, foreign = true)
    private MeetDto meet;

    @DatabaseField(canBeNull = false)
    private String name;

    public String getId() {
        return id;
    }

    public MeetDto getMeet() {
        return meet;
    }

    public void setMeet(MeetDto meet) {
        this.meet = meet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParticipantDto(String name, MeetDto meetDto) {
        this();
        this.name = name;
        this.meet = meetDto;
    }

    public ParticipantDto() {

    }
}

