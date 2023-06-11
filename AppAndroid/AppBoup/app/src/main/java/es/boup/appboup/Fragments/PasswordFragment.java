package es.boup.appboup.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import es.boup.appboup.R;

public class PasswordFragment extends Fragment {

    private EditText etCorreo;
    private TextView tvRegistrar;
    private Button recuperar;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvRegistrar = view.findViewById(R.id.tvRegistroR);
        etCorreo = view.findViewById(R.id.etCorreoRe);
        recuperar = view.findViewById(R.id.btnRecuperar);

        tvRegistrar.setOnClickListener(v->{
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, new RegistroFragment())
                    .addToBackStack(null)
                    .commit();
        });

        recuperar.setOnClickListener(v->{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (etCorreo.getText().toString() != null && etCorreo.getText().toString() != ""){
                String correo = etCorreo.getText().toString();
                auth.sendPasswordResetEmail(correo).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Correo de recuperacion enviado", Toast.LENGTH_SHORT).show();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame, new LoginFragment())
                                .addToBackStack(null)
                                .commit();
                    }else{
                        Toast.makeText(getActivity(), "Correo no existente", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }
}