package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User  implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("nameU")
    @Expose
    private String nameU;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("wallet")
    @Expose
    private Double wallet;

    public User(Integer id, String token, String username, String nameU, String email, String telephone, Double wallet) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.nameU = nameU;
        this.email = email;
        this.telephone = telephone;
        this.wallet = wallet;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameU() {
        return nameU;
    }

    public void setNameU(String nameU) {
        this.nameU = nameU;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public void addSaldo(double saldo){
        if (saldo > 0)
            setWallet(wallet+saldo);
    }
}

