package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;

import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.User;
import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButtonGoogle;

    //variable autenticacion de usuario
    private FirebaseAuth mAuth;

    //variable para saber si es la primera vez con el login de google
    private boolean registrarse = false;

    //variable para cerrar la app
    private boolean cerrar = true;

    //variable para ver si me inician sesion con correo o con google
    private boolean loginGC = true;

    //edit texts
    private EditText etCorreo, etContra;
    private Button btnSign, btnLog;

    //conexion api
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //servicio insertar
    private IUserService userService;

    //View model aplicacion (datos que se pasan entre los fragmentos)
    private AppViewModel appViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //localizar variables
        etContra = view.findViewById(R.id.fEtContra);
        etCorreo = view.findViewById(R.id.fEtCorreo);
        btnLog = view.findViewById(R.id.fBtnLogIn);
        btnSign = view.findViewById(R.id.fBtnSign);
        signInButtonGoogle = view.findViewById(R.id.fBtnGoggle);

        //establecer el viewModel
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        //Obtener la instancia de google
        mAuth = FirebaseAuth.getInstance();

        //opciones de inicio de sesion google
        //le pasamos el token de identificacion que obtenemos desde la consola de firebase en el apartado de autenticacion
        //sign-in method google configuracion del SDK web
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("425064785005-pau1qfdhn78mc1ihrmc0cvkgrkb8erbj.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //creacion del objeto que crea el cliente de inicio de sesion de google
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        //boton de inicio de sesion de google
        signInButtonGoogle.setOnClickListener(view1 -> signInGoogle());

        //boton sign In correo
        btnSign.setOnClickListener(view1 -> signInCorreo());

        //boton log In correo
        btnLog.setOnClickListener(view1 -> logInCorreo());

    }

    private void cambiarFragmento() {

        // Reemplazar el fragmento actual con el nuevo fragmento
        FragmentManager fragmentManager = getParentFragmentManager();
        if (registrarse) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, new CredencialesFragment())
                    .addToBackStack(null)
                    .commit();

        }else{
            //aqui guardo el usuario que se va a pasar por los fragmento
            userService = retrofit.create(IUserService.class);
            //obtener Token
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("tokenNotis", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                // Get new FCM registration token
                String token = task.getResult();
                String correo  = loginGC == true?mAuth.getCurrentUser().getEmail():etCorreo.getText().toString();
                Log.d("llamadaApi","llamando a la api para obtener el usuario");
                Log.d("llamadaApi","token "+token);
                Log.d("llamadaApi","correo: "+correo);
                Call<User> peticionObtenerUsuario = userService.obtenerUsuarioToken(correo,token);
                peticionObtenerUsuario.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            appViewModel.setUser(response.body());
                            Log.e("llamadaApi","Usuario obtenido");
                            appViewModel.setCerrar(true);
                            Toast.makeText(getActivity(), "Sesion iniciada correctamente ", Toast.LENGTH_SHORT).show();
                            getActivity().findViewById(R.id.bottomNavM).setVisibility(View.VISIBLE);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame, new listaInicio())
                                    .addToBackStack(null)
                                    .commit();
                        }else{
                            Log.e("llamadaApi","Usuario no obtenido");

                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("llamadaApi","Error de conexion obteniendo"  + t.getMessage());
                    }
                });
            });
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            //Colocar username default para gmail (usuario de gmail sin @gmail.com)
                            //hacer llamada a la api para registrar al usuario
                            introducirUsuario();
                        } else {
                            registrarse =false;
                            loginGC = true;
                            cambiarFragmento();
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }
                });
    }

    //función de registro con correo electronico
    private void signInCorreo() {
        registrarse = true;
        cambiarFragmento();
    }


    //función de login con correo electronico
    private void logInCorreo() {
        String contra, correo;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        if (!contra.isEmpty() && !correo.isEmpty()) {
            mAuth.signInWithEmailAndPassword(correo, contra)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginGC= false;
                            cambiarFragmento();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Error en el login", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Por favor rellene los campos vacios", Toast.LENGTH_SHORT).show();
            mostrarCamposMal();
        }

    }

    private void mostrarCamposMal(){
        if (etCorreo.getText().toString().isEmpty()) {
            etCorreo.setHintTextColor(getResources().getColor(R.color.error));
        } else {
            etCorreo.setHintTextColor(getResources().getColor(R.color.hints));
        }
        if (etContra.getText().toString().isEmpty()) {
            etContra.setHintTextColor(getResources().getColor(R.color.error));
        } else {
            etContra.setHintTextColor(getResources().getColor(R.color.hints));
        }
    }

    private void introducirUsuario() {
        String username,email,nombre,telefono;
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        username = firebaseUser.getEmail();
        int index = username.indexOf("@");
        username = username.substring(0,index);

        nombre = firebaseUser.getDisplayName();
        telefono = firebaseUser.getPhoneNumber();
        email = mAuth.getCurrentUser().getEmail();

        CreateUserDTO createUserDTO = new CreateUserDTO(username,email,nombre,telefono);

        //obtener el token para las notificaciones
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("tokenNotis", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    createUserDTO.setToken(token);

                    //llamada a la api
                    userService = retrofit.create(IUserService.class);
                    Log.d("llamadaApi","Haciendo llamada a la api para insertar");
                    Call<User> peticionInsertarUsuario = userService.insertarUsuario(createUserDTO);
                    peticionInsertarUsuario.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK){
                                Log.d("llamadaApi","Usuario insertado");
                                Toast.makeText(getActivity(), "Usuario registrado", Toast.LENGTH_SHORT).show();
                                //establecer el usuario en la app
                                appViewModel.setUser(response.body());
                                //hacer que se cierre la app cuando pulsan atras
                                appViewModel.setCerrar(true);
                                getActivity().findViewById(R.id.bottomNavM).setVisibility(View.VISIBLE);
                                //cambiar de fragmento
                                FragmentManager fragmentManager = getParentFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.frame, new listaInicio())
                                        .addToBackStack(null)
                                        .commit();

                            }else{
                                Toast.makeText(getActivity(), "Error registrando", Toast.LENGTH_SHORT).show();
                                Log.d("llamadaApi","error registrando"+response.code());
                                mAuth.getCurrentUser().delete();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("llamadaApi","Error de conexion " + t.getMessage());
                            mAuth.getCurrentUser().delete();
                        }

                    });

                });

    }

}