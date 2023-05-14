package es.boup.appboup.Model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import es.boup.appboup.Services.IUserService;
import es.boup.appboup.Services.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppViewModel extends ViewModel {
    //conexion api
    private static String CONEXION_API = "http:/127.0.0.1:8080/";
    private IUserService userService;
    private User user;
    private User userApi;

    //variable para cerrar tras login y registro
    private boolean cerrar;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public User getUserApi(FirebaseAuth mAuth){

        //conectar con retrofit
        Retrofit retrofit = RetrofitClient.getClient(CONEXION_API);
        userService = retrofit.create(IUserService.class);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TokenPush", "Error getting token", task.getException());
                            return;
                        }
                        // Obtener el token de notificación push
                        String token = task.getResult();
                        Call<User> peticionObtenerUsuario = userService.obtenerUsuario(mAuth.getCurrentUser().getEmail(),token);
                        peticionObtenerUsuario.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                User user = response.body();
                                userApi = user;
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                            }
                        });
                    }
                });
        return userApi;
    }

    public boolean getCerrar(){
        return cerrar;
    }
    public void setCerrar(boolean cerrar){
        this.cerrar = cerrar;
    }
}
