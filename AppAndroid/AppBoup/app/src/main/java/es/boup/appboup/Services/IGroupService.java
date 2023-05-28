package es.boup.appboup.Services;

import java.util.List;

import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.Debt;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IGroupService {

    @GET("api/groups/{groupid}/users")
    Call<List<User>> getGroupUsers(@Path("groupid") String groupid);

    @GET("api/groups/{id}")
    Call<Group> getGroupById(@Path("id") String id);

    @GET("api/groups/{groupid}/debts")
    Call<List<Debt>> getGroupDebt(@Path("groupid") String groupid);

    @POST("api/groups/{groupname}/user/{username}")
    Call<Group> insertarUsuario(@Path("groupname") String groupname,@Path("username") String username);

    @PUT("api/groups/{idgroup}/addUser/{username}")
    Call<Group> insertarUsuarioEnGrupo(@Path("idgroup") int idgroup,@Path("username") String username);


}
