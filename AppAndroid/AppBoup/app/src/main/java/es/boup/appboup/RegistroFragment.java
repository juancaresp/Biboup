package es.boup.appboup;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        String contra, correo,username;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        username = etUsername.getText().toString();
        //Cambiar al fragmento de registro

        if (!correo.isEmpty() && !contra.isEmpty() && !username.isEmpty()) {
            Log.d("prueba","hola");
            //hacer llamada para comprobar si el username existe antes de registrar

            mAuth.createUserWithEmailAndPassword(correo, contra).
                    addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //funcion para guardar el usuario en la base de datos
                            introducirUsuario();
                            //funcion para cambiar de fragmento
                            cambiarFragmento();
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
        } else {
            Toast.makeText(getActivity(), "Por favor rellene los campos vacios", Toast.LENGTH_SHORT).show();
            mostrarCamposMal();
        }
    }

    private void introducirUsuario() {
        String username,email;
        username = etUsername.getText().toString();
        email = etCorreo.getText().toString();
        //añadir el username al usuario de firebase
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().
                setDisplayName(username).build();
        mAuth.getCurrentUser().updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("pruieba", "Email sent.");
                }
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
                });

        //llamada a la api
        /*
        Retrofit retrofit = new Retrofit.Builder().
        baseUrl(URLBASE).addConverterFactory(GsonConverterFactory.create()).build();
        IUserService userService = retrofit.create(IUserService.class);
        Call<Void> peticionInsertarUsuario = userService.insertarUsuario(createUserDTO);
        peticionInsertarUsuario.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    Toast.makeText(getActivity(),"usuario introducido con exito",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "error introdiendo el usuario", Toast.LENGTH_SHORT).show();
            }
        });
         */

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

    private void cambiarFragmento() {
        // Reemplazar el fragmento actual con el nuevo fragmento
        FragmentManager fragmentManager = getParentFragmentManager();
        //mandar el usuario
        mandarUsuario();
        //hacer que se cierre la app cuando pulsan atras
        appViewModel.setCerrar(true);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void mandarUsuario() {
        User user;
        //aqui habria que hacer la llamada a la api
        //user = userViewModel.getUserApi(mAuth);
        user = new User(1,"token",mAuth.getCurrentUser().getDisplayName(),"Enrique",mAuth.getCurrentUser().getEmail(),"999999999",99d);
        appViewModel.setUser(user);
    }

}