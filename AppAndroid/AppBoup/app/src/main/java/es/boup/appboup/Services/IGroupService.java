package es.boup.appboup.Services;

import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IGroupService {
    @POST("api/groups/create/{groupname}/user/{username}")
    Call<Group> insertarUsuario(@Path("groupname") String groupname,@Path("username") String username);

    @GET("api/groups/{id}")
    Call<Group> getGroupById(@Path("id") String id);

    @POST("api/groups/{idgroup}/addUser/{username}")
    Call<Group> insertarUsuarioEnGrupo(@Path("idgroup") int idgroup,@Path("username") String username);
}
