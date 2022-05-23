package team.space.requests.getallusers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatedAt {
    public String timezone;
    @JsonProperty("$reql_type$")
    public String reqlType;
    public double epoch_time;


    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getReqlType() {
        return reqlType;
    }

    public void setReqlType(String reqlType) {
        this.reqlType = reqlType;
    }

    public double getEpoch_time() {
        return epoch_time;
    }

    public void setEpoch_time(double epoch_time) {
        this.epoch_time = epoch_time;
    }
}
