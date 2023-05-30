package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Group  implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("groupName")
    @Expose
    private String groupName;

    private Debt debt;

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public Group(Integer id, String groupName) {
        this.id = id;
        this.groupName = groupName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
