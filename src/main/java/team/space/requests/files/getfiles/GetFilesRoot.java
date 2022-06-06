package team.space.requests.files.getfiles;


import com.fasterxml.jackson.annotation.JsonProperty;

public class GetFilesRoot {

    @JsonProperty("success")
    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    boolean success;

    @JsonProperty("message")
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;

    @JsonProperty("data")
    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    Data data;


}
