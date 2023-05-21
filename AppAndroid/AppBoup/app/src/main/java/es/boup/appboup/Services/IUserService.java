package es.boup.appboup.Services;

import java.util.List;

import es.boup.appboup.Model.AddWallet;
import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.EditUserDTO;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IUserService {

    //llamada para insertar un usuario
    @POST("api/users/add")
    Call<User> insertarUsuario(@Body CreateUserDTO user);

    //llamada para obtener un usuario a traves del email y el token
    @GET("api/users/email/{mail}/token/{token}")
    Call<User> obtenerUsuarioToken(@Path("mail") String mail, @Path("token") String token);

    //llamada para obtener un usuario por su correo
    @GET("api/users/email/{mail}")
    Call<User> obtenerUsuarioEmail(@Path("mail") String mail);

    //llamada para obtener un usuario por su correo
    @GET("api/users/username/{username}")
    Call<User> obtenerUsuarioUsername(@Path("username") String username);

    @GET("api/users/{username}/groups")
    Call<List<Group>> obtenerGruposDelUsuario(@Path("username") String username);

    @POST("api/users/{email}/update")
    Call<User> modificarUsuario(@Path("email")String email,@Body EditUserDTO user);

    @POST("api/users/wallet/add")
    Call<User> addSaldo(@Body AddWallet wallet);
}
