package es.boup.appboup.Services;

import java.util.List;

import es.boup.appboup.Model.AddWallet;
import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.Debt;
import es.boup.appboup.Model.EditUserDTO;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUserService {

    @PUT("api/users/{email}")
    Call<User> modificarUsuario(@Path("email")String email,@Body EditUserDTO user);

    //llamada para obtener un usuario por su correo
    @GET("api/users/email/{mail}")
    Call<User> obtenerUsuarioEmail(@Path("mail") String mail);

    //llamada para obtener un usuario por su correo
    @GET("api/users/username/{username}")
    Call<User> obtenerUsuarioUsername(@Path("username") String username);

    @GET("api/users/{username}/groups")
    Call<List<Group>> obtenerGruposDelUsuario(@Path("username") String username);

    @GET("api/users/{username}/debts")
    Call<List<Debt>> getUserDebts(@Path("username")String username);

    @GET("api/users/{username}/debts/win")
    Call<List<Debt>> getGruposQueTeDeben(@Path("username") String username);

    @GET("api/users/{username}/debts/lose")
    Call<List<Debt>> getGruposQueDebes(@Path("username") String username);

    //llamada para insertar un usuario
    @POST("api/users")
    Call<User> insertarUsuario(@Body CreateUserDTO user);

    //llamada para obtener un usuario a traves del email y el token
    @PATCH("api/users/email/{mail}/token/{token}")
    Call<User> obtenerUsuarioToken(@Path("mail") String mail, @Path("token") String token);

    @PATCH("api/users/wallet/add")
    Call<User> addSaldo(@Body AddWallet wallet);


}
