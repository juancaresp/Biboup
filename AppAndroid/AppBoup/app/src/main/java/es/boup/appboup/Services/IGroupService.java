package es.boup.appboup.Services;

import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IGroupService {
    @POST("api/groups/add/{groupname}/user/{username}")
    Call<Group> insertarUsuario(@Path("groupname") String groupname,@Path("username") String username);
}
