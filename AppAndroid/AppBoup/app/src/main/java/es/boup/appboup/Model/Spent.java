package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Spent implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("spentName")
    @Expose
    private String spentName;

    @SerializedName("spentDesc")
    @Expose
    private String spentDesc;

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @SerializedName("payer")
    @Expose
    private User payer;

    @SerializedName("users")
    @Expose
    private List<User> users;

    @SerializedName("quantity")
    @Expose
    private Double quantity;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("group")
    @Expose
    private Group group;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpentName() {
        return spentName;
    }

    public void setSpentName(String spentName) {
        this.spentName = spentName;
    }

    public String getSpentDesc() {
        return spentDesc;
    }

    public void setSpentDesc(String spentDesc) {
        this.spentDesc = spentDesc;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
