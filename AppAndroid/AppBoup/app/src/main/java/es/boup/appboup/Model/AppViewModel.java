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
    private Group group;
    //variable para cerrar tras login y registro
    private boolean cerrar;
    private Spent spent;

    public Spent getSpent() {
        return spent;
    }

    public void setSpent(Spent spent) {
        this.spent = spent;
    }

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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
