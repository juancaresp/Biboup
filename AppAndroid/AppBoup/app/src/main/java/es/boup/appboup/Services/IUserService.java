package es.boup.appboup.Services;

import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IUserService {

    //llamada para insertar un usuario
    @POST("users/insert")
    Call<Void> insertarUsuario(@Body CreateUserDTO user);

    //llamada para obtener un usuario a traves del email y el token
    @GET("users/{email}/token/{token}")
    Call<User> obtenerUsuario(@Path("email") String email, @Path("token") String token);
}
