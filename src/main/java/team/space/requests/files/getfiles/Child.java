package team.space.requests.files.getfiles;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Child {
    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    @JsonProperty("detail")
    public Detail getDetail() {
        return this.detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    Detail detail;

    @JsonProperty("children")
    public ArrayList<Child> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    ArrayList<Child> children;


}
