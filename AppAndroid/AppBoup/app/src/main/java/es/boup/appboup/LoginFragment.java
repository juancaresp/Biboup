package es.boup.appboup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButtonGoogle;

    //variable autenticacion de usuario
    private FirebaseAuth mAuth;
    //variable para saber si es la primera vez con el login de google
    private boolean primera = false;

    //edit texts

    private EditText etCorreo, etContra;
    private Button btnSign,btnLog;
    private Bundle bundle;

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

        //iniciarlizar bundle registro
        bundle = new Bundle();

    }

    private void cambiarFragmento(){

        // Reemplazar el fragmento actual con el nuevo fragmento
        FragmentManager fragmentManager = getParentFragmentManager();
        //en este caso se habra pulsado el registro del correo o el de google no existiendo la cuenta en la base de datos
        if (primera){
            getParentFragmentManager().setFragmentResult("resultadoRegistro",bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, new CredencialesFragment())
                    .addToBackStack(null)
                    .commit();
        }else{
            //en este caso se habra pulsado el login del correo o el de google existiendo ya la cuenta en la base de datos
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, new listaInicio())
                    .addToBackStack(null)
                    .commit();
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
                            Toast.makeText(getActivity(), "El usuario no existia en la base de datos", Toast.LENGTH_SHORT).show();
                            //Aqui introduzco en el bundle si el registro es con google o con correo
                            bundle.putBoolean("correo",false);
                            primera = true;
                        } else {
                            Toast.makeText(getActivity(), "El usuario existia en la base de datos", Toast.LENGTH_SHORT).show();
                        }
                        cambiarFragmento();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }
                });
    }

    //función de registro con correo electronico
    @SuppressLint("SuspiciousIndentation")
    private void signInCorreo() {
        String contra, correo;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        if (!correo.isEmpty() && !contra.isEmpty()){
            mAuth.createUserWithEmailAndPassword(correo,contra).
                    addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Aqui introduzco en el bundle si el registro es con google o con correo
                            bundle.putBoolean("correo",true);
                            primera = true;
                            cambiarFragmento();
                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if(etContra.getText().toString().length() < 6){
                                Toast.makeText(getActivity(), "La contraseña debe ser de 6 caracteres o más", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Error registrando al usuario", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }

    //función de login con correo electronico
    private void logInCorreo() {
        String contra, correo;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        mAuth.signInWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getActivity(), "Sesion iniciada correctamente "+ user.getEmail(), Toast.LENGTH_SHORT).show();
                        cambiarFragmento();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getActivity(), "Error en el login", Toast.LENGTH_SHORT).show();

                    }
                });
    }


}