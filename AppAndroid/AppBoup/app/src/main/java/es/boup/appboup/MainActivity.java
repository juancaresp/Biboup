package es.boup.appboup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import es.boup.appboup.Model.User;
import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Services.IUserService;
import es.boup.appboup.Services.RetrofitClient;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity{

    //conexion api
    private static String CONEXION_API = "http:/127.0.0.1:8080/";

    private FrameLayout frameLayout;
    //variable sesion del usuario
    private FirebaseAuth mAuth;
    //variable estadisticas
    private FirebaseAnalytics mFirebaseAnalytics;

    //Valor del usuario que se pasara entre los fragmentos
    private AppViewModel appViewModel;

    private IUserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //quitar la barra de accion de la parte superior
        getSupportActionBar().hide();

        //crear el viewModel que se pasara por la actividad
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        appViewModel.setCerrar(false);

        //conectar con retrofit
        Retrofit retrofit = RetrofitClient.getClient(CONEXION_API);
        userService = retrofit.create(IUserService.class);

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
            obtenerUsuario();
            fragmentTransaction.add(R.id.frame,new SettingsFragment());
        }else{
            fragmentTransaction.add(R.id.frame,new LoginFragment());
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (appViewModel.getCerrar()){
            this.finish();
        }
        super.onBackPressed();
    }


    private void obtenerUsuario() {
        //hasta que no probemos que la llamada funciona
        User user = new User(1,"token",mAuth.getCurrentUser().getDisplayName(),"Enrique",mAuth.getCurrentUser().getEmail(),"999999999",99d);
        //probar si se puede hacer esto o hay que hacer la misma llamada cada vez
        //user = userViewModel.getUserApi(mAuth);
        appViewModel.setUser(user);
    }

    /*
    private void obtenerUsuario() {
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
                                appViewModel.setUser(user);
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                            }
                        });
                    }
                });

    }
     */


}