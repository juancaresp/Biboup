package es.boup.appboup.Services;

import es.boup.appboup.Model.CreateUserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {
    @POST("users/insert")
    Call<Void> insertarUsuario(@Body CreateUserDTO user);

}
