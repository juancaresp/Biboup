package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Debt implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("group")
    @Expose
    private Group group;

    @SerializedName("amount")
    @Expose
    private Double amount;

}
