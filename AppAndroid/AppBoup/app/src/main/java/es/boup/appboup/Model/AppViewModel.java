package es.boup.appboup.Model;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.net.HttpURLConnection;

import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppViewModel extends ViewModel {

    private User user;

    //variable para cerrar tras login y registro
    private boolean cerrar;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean getCerrar(){
        return cerrar;
    }
    public void setCerrar(boolean cerrar){
        this.cerrar = cerrar;
    }
}
