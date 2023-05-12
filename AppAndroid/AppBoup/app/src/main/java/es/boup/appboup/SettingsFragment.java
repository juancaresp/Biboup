package es.boup.appboup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    public LinearLayout btCerrarSesion,btPoliticas,btTerminos;
    public ImageButton btEditar;
    public FirebaseAuth mAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);
        btEditar = view.findViewById(R.id.btEditar);
        btPoliticas = view.findViewById(R.id.btPoliticas);
        btTerminos = view.findViewById(R.id.btTerminos);
        btCerrarSesion.setOnClickListener(view1 -> {
            logOut();
        });

    }

    public void logOut(){
        //obtener la sesion del usuario
        mAuth = FirebaseAuth.getInstance();
        //cerrar sesion
        mAuth.signOut();
        //cambiar de fragmento al de inicio de sesion
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new LoginFragment())
                .addToBackStack(null)
                .commit();

    }
}