package es.boup.appboup;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frLista,frCorreo;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private EditText etCorreo, etContra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT,bundle);
        etCorreo = findViewById(R.id.etCorreo);
        etContra = findViewById(R.id.etContra);
        mAuth = FirebaseAuth.getInstance();
        setup();
    }

    private void setup() {
        setTitle("Autentication");
        updateUI(mAuth.getCurrentUser());
    }

    public void LogIn (View view){
        String contra, correo;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        mAuth.signInWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {

                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Error en el login", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    public void SignIn (View view){
        String contra, correo;
        contra = etContra.getText().toString();
        correo = etCorreo.getText().toString();
        if (!correo.isEmpty() && !contra.isEmpty()){
            mAuth.createUserWithEmailAndPassword(correo,contra).
                    addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Error registrando al usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateUI (FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("usuario", user);
            startActivity(intent);
        }

    }
}