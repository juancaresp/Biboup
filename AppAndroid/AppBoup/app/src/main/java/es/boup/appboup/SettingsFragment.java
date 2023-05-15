package es.boup.appboup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import es.boup.appboup.Model.User;
import es.boup.appboup.Model.AppViewModel;

public class SettingsFragment extends Fragment {

    private LinearLayout btCerrarSesion,btPoliticas,btTerminos;
    private ImageButton btEditar;
    private FirebaseAuth mAuth;
    private TextView tvCorreo,tvUsername;
    private AppViewModel appViewModel;
    private boolean cerrar;


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

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        tvCorreo = view.findViewById(R.id.tvEmailS);
        tvUsername = view.findViewById(R.id.tvNombreS);
        Log.d("llamadaApi","antes de recoger el usuario desde el fragmento");
        User user = appViewModel.getUser();
        if (user !=null){
            tvUsername.setText(user.getUsername() + user.getNameU() + user.getTelephone());
            tvCorreo.setText(user.getEmail());
        }


        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);
        btEditar = view.findViewById(R.id.btEditar);
        btPoliticas = view.findViewById(R.id.btPoliticas);
        btTerminos = view.findViewById(R.id.btTerminos);

        getParentFragmentManager().setFragmentResultListener("cerrarApp",this,((requestKey, result) -> {
            cerrar = true;
        }));

        btCerrarSesion.setOnClickListener(view1 -> {
            logOut();
        });

    }



    public void logOut(){
        //obtener la sesion del usuario
        mAuth = FirebaseAuth.getInstance();
        //cerrar sesion
        mAuth.signOut();
        //evitar que vuelvan aqui
        appViewModel.setCerrar(true);
        //cambiar de fragmento al de inicio de sesion
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new LoginFragment())
                .addToBackStack(null)
                .commit();

    }
}