
package com.axon.alex.axonsoft.api.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseObj {

    @SerializedName("results")
    @Expose
    private List<User> users = null;
    @SerializedName("info")
    @Expose
    private Info info;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}
