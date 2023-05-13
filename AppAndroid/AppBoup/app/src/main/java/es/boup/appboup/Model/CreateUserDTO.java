package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateUserDTO implements Serializable {

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

    public CreateUserDTO (String username, String email,String nombre,String telefono){
        this.email = email;
        this.username = username;
        this.nameU = nombre;
        this.telephone = telefono;
    }


    public String getNameU() {
        return nameU;
    }

    public void setNameU(String nameU) {
        this.nameU = nameU;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
