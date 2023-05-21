package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddWallet {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("amount")
    @Expose
    private double amount;

    public AddWallet (String username,double amount){
        this.username = username;
        this.amount = amount;
    }
}
