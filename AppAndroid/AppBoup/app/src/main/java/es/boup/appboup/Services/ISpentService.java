package es.boup.appboup.Services;

import java.util.List;

import es.boup.appboup.Model.Spent;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ISpentService {

    @GET("api/spents/group/{groupId}")
    Call<List<Spent>> getSpentsGroup(@Path("groupId") int groupId);


}
