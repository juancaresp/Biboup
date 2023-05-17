package es.boup.appboup;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
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

public class PerfilFragment extends Fragment {

    private LinearLayout btCerrarSesion,btPoliticas,btTerminos,btNotis;
    private ImageButton btEditar;
    private FirebaseAuth mAuth;
    private TextView tvCorreo,tvUsername,tvNombre,tvSaldo,tvTelefono;
    private AppViewModel appViewModel;
    private boolean cerrar;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        tvCorreo = view.findViewById(R.id.tvCorreoP);
        tvUsername = view.findViewById(R.id.tvUsernameP);
        tvNombre = view.findViewById(R.id.tvNombrePersonal);
        tvTelefono = view.findViewById(R.id.tvTelefonoP);
        tvSaldo = view.findViewById(R.id.tvSaldoP);
        Log.d("llamadaApi","antes de recoger el usuario desde el fragmento");
        User user = appViewModel.getUser();
        if (user !=null){
            tvUsername.setText(user.getUsername());
            tvCorreo.setText("Correo electronico: " +user.getEmail());
            if (user.getWallet() == null)
                tvSaldo.setText("saldo: "+0 + "€");
            else
                tvSaldo.setText("saldo: "+user.getWallet()+ "€");
            if (user.getTelephone() != null)
                tvTelefono.setText("Numero de telefono: "+ user.getTelephone());
            else
                tvTelefono.setText("Numero de telefono: Sin datos");
            tvNombre.setText("Nombre personal: " + user.getNameU());
        }
        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);
        btEditar = view.findViewById(R.id.btEditar);
        btPoliticas = view.findViewById(R.id.btPoliticas);
        btTerminos = view.findViewById(R.id.btTerminos);
        btNotis = view.findViewById(R.id.btNoti);

        getParentFragmentManager().setFragmentResultListener("cerrarApp",this,((requestKey, result) -> {
            cerrar = true;
        }));

        btCerrarSesion.setOnClickListener(view1 -> {
            logOut();
        });
        btPoliticas.setOnClickListener(view1 -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://policies.google.com/privacy?hl=es"));
            startActivity(i);
        });
        btTerminos.setOnClickListener(view1 -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://policies.google.com/terms?hl=es"));
            startActivity(i);
        });
        btNotis.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Para versiones de Android Oreo (API level 26) en adelante, debemos especificar el paquete de nuestra aplicación.
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getActivity().getPackageName());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Para versiones de Android Lollipop (API level 21) en adelante hasta Android Nougat (API level 25).
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getActivity().getPackageName());
                intent.putExtra("app_uid", getActivity().getApplicationInfo().uid);
            } else {
                // Para versiones de Android anteriores a Lollipop.
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
            }
            startActivity(intent);
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