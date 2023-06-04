package es.boup.appboup.Services;

import java.util.List;

import es.boup.appboup.Model.Spent;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ISpentService {

    @GET("api/spents/group/{groupId}")
    Call<List<Spent>> getSpentsGroup(@Path("groupId") int groupId);

    @GET("api/spents/users/{username}")
    Call<List<Spent>> getSpentsUser(@Path("username") String username);

    @PUT("api/spents/")
    Call<Spent> updateSpent(@Body Spent spent);

    @DELETE("api/spents/{idSpent}")
    Call<Spent> deleteSpent(@Path("idSpent") String idSpent);

    @POST("api/spents")
    Call<Spent> addSpent(@Body Spent spent);



}
