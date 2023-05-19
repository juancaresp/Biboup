package es.boup.appboup.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditUserDTO {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("nameU")
    @Expose
    private String nameU;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    public EditUserDTO (String username,String nombre,String telefono){
        this.username = username;
        this.nameU = nombre;
        this.telephone = telefono;
    }
}
