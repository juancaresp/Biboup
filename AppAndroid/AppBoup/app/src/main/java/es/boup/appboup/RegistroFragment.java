package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.CreateUserDTO;
import es.boup.appboup.Model.User;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegistroFragment extends Fragment {

    private static final String TAG = "GoogleActivity";

    private EditText etCorreo, etContra, etUsername;
    private Button btnSign;

    //variable autenticacion
    private FirebaseAuth mAuth;

    //bundle fragmento credenciales
    private String nombre,telefono;

    //View model aplicacion (datos que se pasan entre los fragmentos)
    private AppViewModel appViewModel;

    //conexion api
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //servicio insertar
    private IUserService userService;

    public RegistroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //localizar variables
        etContra = view.findViewById(R.id.etContraR);
        etCorreo = view.findViewById(R.id.etCorreoR);
        etUsername = view.findViewById(R.id.etUsernameR);
        btnSign = view.findViewById(R.id.btnRegistrar);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        //obtener instancia de google
        mAuth = FirebaseAuth.getInstance();

        //listener boton
        btnSign.setOnClickListener(view1 -> signInCorreo());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("resultadoCredenciales", this, (requestKey, result) -> {
            nombre = result.getString("nombre");
            telefono = result.getString("telefono");
        });

    }


    //función de registro con correo electronico
    private void signInCorreo() {
        String contra, correo, username;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        username = etUsername.getText().toString();
        //Cambiar al fragmento de registro

        if (!correo.isEmpty() && !contra.isEmpty() && !username.isEmpty()) {
            Log.d("prueba","hola");
            //hacer llamada para comprobar si el username existe antes de registrar

            userService = retrofit.create(IUserService.class);
            Log.d("llamadaApi","Haciendo llamada a la api para insertar");
            Call <User> peticionInsertarUsuario = userService.obtenerUsuarioUsername(username);
            peticionInsertarUsuario.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK){
                        Log.d("llamadaApi","Usuario encontrado");
                        Toast.makeText(getActivity(), "Username ya registrado", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("llamadaApi","username no registrado"+response.code());
                        mAuth.createUserWithEmailAndPassword(correo, contra).
                            addOnCompleteListener(getActivity(), task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //funcion para guardar el usuario en la base de datos
                                    introducirUsuario();
                                } else{
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    if (etContra.getText().toString().length() < 6) {
                                        Toast.makeText(getActivity(), "La contraseña debe ser de 6 caracteres o más", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Error registrando al usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("llamadaApi","Error de conexion " + t.getMessage());
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
        if (etUsername.getText().toString().isEmpty()) {
            etUsername.setHintTextColor(getResources().getColor(R.color.error));
        } else {
            etUsername.setHintTextColor(getResources().getColor(R.color.hints));
        }

    }

    private void introducirUsuario() {
        String username,email;
        username = etUsername.getText().toString();
        email = etCorreo.getText().toString();
        //añadir el username al usuario de firebase
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().
                setDisplayName(username).build();
        mAuth.getCurrentUser().updateProfile(profileUpdate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("prueba", "Email sent.");
            }
        });

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
                    Call <User> peticionInsertarUsuario = userService.insertarUsuario(createUserDTO);
                    peticionInsertarUsuario.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == HttpURLConnection.HTTP_OK){
                                Log.d("llamadaApi","Usuario insertado");
                                Toast.makeText(getActivity(), "Registro completo", Toast.LENGTH_SHORT).show();
                                appViewModel.setUser(response.body());
                                FragmentManager fragmentManager = getParentFragmentManager();
                                //mandar el usuario
                                //hacer que se cierre la app cuando pulsan atras
                                appViewModel.setCerrar(true);
                                getActivity().findViewById(R.id.bottomNavM).setVisibility(View.VISIBLE);
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