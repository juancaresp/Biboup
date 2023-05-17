package es.boup.appboup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.net.HttpURLConnection;

import es.boup.appboup.Model.User;
import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    //conexion api
    public static String CONEXION_API = "http://192.168.0.12:8080/";

    private FrameLayout frameLayout;
    //variable sesion del usuario
    private FirebaseAuth mAuth;
    //variable estadisticas
    private FirebaseAnalytics mFirebaseAnalytics;

    //conexion api
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private IUserService userService;

    //Valor del usuario que se pasara entre los fragmentos
    private AppViewModel appViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //quitar la barra de accion de la parte superior
        getSupportActionBar().hide();

        //crear el viewModel que se pasara por la actividad
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        appViewModel.setCerrar(false);

        //localizar layout para poner los fragmentos
        frameLayout = findViewById(R.id.frame);

        //Obtener la instancia de google
        mAuth = FirebaseAuth.getInstance();

        //mandar estadisticas
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);

        //navegador entre fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame,new LoginFragment());
        fragmentTransaction.commit();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mAuth.getCurrentUser() != null){
            userService = retrofit.create(IUserService.class);
            Log.d("llamadaApi","llamando a la api para obtener el usuario");
            Call<User> peticionObtenerUsuario = userService.obtenerUsuarioEmail(mAuth.getCurrentUser().getEmail());
            peticionObtenerUsuario.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK){
                        appViewModel.setUser(response.body());
                        Log.e("llamadaApi","Usuario obtenido");
                        fragmentTransaction.add(R.id.frame,new PerfilFragment());
                    }else{
                        Log.e("llamadaApi","Usuario no obtenido");
                        fragmentTransaction.add(R.id.frame,new LoginFragment());
                    }
                    fragmentTransaction.commit();
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("llamadaApi","Error de conexion obteniendo"  + t.getMessage());
                }
            });
        }else{
            fragmentTransaction.add(R.id.frame,new LoginFragment());
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        if (appViewModel.getCerrar()){
            this.finish();
        }
        super.onBackPressed();
    }



}