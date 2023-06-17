package es.boup.appboup;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;

import es.boup.appboup.Fragments.AniadirGastoFragment;
import es.boup.appboup.Fragments.HistoricoFragment;
import es.boup.appboup.Fragments.ListaInicioFragment;
import es.boup.appboup.Fragments.LoginFragment;
import es.boup.appboup.Fragments.PerfilFragment;
import es.boup.appboup.Model.User;
import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Services.IUserService;
import es.boup.appboup.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    //conexion api
    public static String CONEXION_API = "http://app.biboup.me/";


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

    //navegacion
    private BottomNavigationView navi;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        navi = findViewById(R.id.bottomNavM);
        if (mAuth.getCurrentUser() != null)
            navi.setVisibility(View.VISIBLE);
        else
            navi.setVisibility(View.GONE);
        binding.bottomNavM.setOnItemSelectedListener(item ->{

            switch (item.getItemId()){
                case R.id.navPerfil:
                    cambiarFragmento(new PerfilFragment());
                    break;
                case R.id.navLista:
                    cambiarFragmento(new ListaInicioFragment());
                    break;
                case R.id.navAdd:
                    cambiarFragmento(new HistoricoFragment());
                    break;
            }
            return true;
        });



    }

    private void cambiarFragmento(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Verificar si ya estás en el fragmento que deseas agregar
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.frame);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {
            // Estás en el mismo fragmento, no es necesario agregarlo a la pila de retroceso
            fragmentTransaction.replace(R.id.frame, fragment);

        } else {
            // No estás en el mismo fragmento, agrega el fragmento a la pila de retroceso
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mAuth.getCurrentUser() != null){

            //obtener token
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("tokenNotis", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                // Get new FCM registration token
                String token = task.getResult();
                //llamada a la api
                Log.d("llamadaApi","llamando a la api para obtener el usuario");
                userService = retrofit.create(IUserService.class);
                Call<User> peticionObtenerUsuario = userService.obtenerUsuarioToken(mAuth.getCurrentUser().getEmail(),token);
                peticionObtenerUsuario.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            appViewModel.setUser(response.body());
                            Log.e("llamadaApi","Usuario obtenido");
                            fragmentTransaction.replace(R.id.frame,new PerfilFragment());
                        }else{
                            Log.e("llamadaApi","Usuario no obtenido");
                            fragmentTransaction.replace(R.id.frame,new LoginFragment());
                        }
                        fragmentTransaction.commit();
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("llamadaApi","Error de conexion obteniendo"  + t.getMessage());
                    }
                });
            });
        }else{
            fragmentTransaction.add(R.id.frame,new LoginFragment());
            fragmentTransaction.commit();
            navi.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.frame);

        if (appViewModel.getCerrar()){
            this.finish();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila, retroceder al fragmento anterior
            if (fragment instanceof PerfilFragment)
                binding.bottomNavM.getMenu().getItem(2).setChecked(true);
            else if (fragment instanceof AniadirGastoFragment)
                binding.bottomNavM.getMenu().getItem(1).setChecked(true);
            else if (fragment instanceof ListaInicioFragment)
                binding.bottomNavM.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager().popBackStack();
        } else {
            // Si no hay fragmentos en la pila, permitir el comportamiento de retroceso predeterminado (cerrar la aplicación)
            super.onBackPressed();
        }
    }



}