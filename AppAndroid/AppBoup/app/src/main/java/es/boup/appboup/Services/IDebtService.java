package es.boup.appboup.Services;

import java.util.List;

import es.boup.appboup.Model.Debt;
import es.boup.appboup.Model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface IDebtService {

    @GET("api/debts/user/{username}/group/{groupid}")
    Call<Debt> getDebtByGroup(@Path("username") String username, @Path("groupid") String groupid);

    @PATCH ("api/debts/pay/{username}/group/{groupid}")
    Call<Debt> closeDebt(@Path("username") String username, @Path("groupid") String groupid);

}
