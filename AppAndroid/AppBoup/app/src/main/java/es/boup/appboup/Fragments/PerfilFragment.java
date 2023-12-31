package es.boup.appboup.Fragments;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.net.HttpURLConnection;
import java.text.DecimalFormat;

import es.boup.appboup.Model.AddWallet;
import es.boup.appboup.Model.EditUserDTO;
import es.boup.appboup.Model.User;
import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.R;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilFragment extends Fragment {

    private LinearLayout btCerrarSesion,btPoliticas,btTerminos,btNotis;
    private ImageButton btEditar;
    private FirebaseAuth mAuth;
    private TextView tvUsername,tvSaldo,tvCorreo;
    private DecimalFormat formato ;
    private EditText etNombre,etUsername,etTelefono;
    private Button btConfirmar,btRetirar;
    private AppViewModel appViewModel;
    private User user;
    private boolean cerrar,edit;

    //conexion api
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //servicio insertar
    private IUserService userService;


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

        formato= new DecimalFormat("#.##");
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        tvCorreo = view.findViewById(R.id.tvCorreoP);
        etUsername = view.findViewById(R.id.etUsernameP);
        tvUsername = view.findViewById(R.id.tvCorreoP);
        etNombre = view.findViewById(R.id.etNombreP);
        etTelefono = view.findViewById(R.id.etTelefonoP);
        tvSaldo = view.findViewById(R.id.tvSaldoP);
        btRetirar = view.findViewById(R.id.btRetirarDinero);
        btConfirmar = view.findViewById(R.id.btConfirmar);
        btConfirmar.setVisibility(View.GONE);
        edit = false;
        user = appViewModel.getUser();
        if (user !=null){
            tvCorreo.setText(user.getEmail());
            etUsername.setText(user.getUsername());
            if (user.getWallet() == null)
                tvSaldo.setText("saldo: 0.00"+ "€");
            else
                tvSaldo.setText("saldo: "+formato.format(user.getWallet())+ "€");
            if (user.getTelephone() != null)
                etTelefono.setText(user.getTelephone());
            else
                etTelefono.setHint("Sin datos");
            etNombre.setText(user.getNameU());
        }
        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);
        btEditar = view.findViewById(R.id.btEditar);
        btPoliticas = view.findViewById(R.id.btPoliticas);
        btTerminos = view.findViewById(R.id.btTerminos);
        btNotis = view.findViewById(R.id.btNoti);

        getParentFragmentManager().setFragmentResultListener("cerrarApp",this,((requestKey, result) -> {
            cerrar = true;
        }));

        btEditar.setOnClickListener(view1 -> {
            edit = !edit;
            if (edit) {
                btConfirmar.setVisibility(View.VISIBLE);
                etTelefono.setHintTextColor(getResources().getColor(R.color.edit));
            }
            else{
                btConfirmar.setVisibility(View.GONE);
                etTelefono.setHintTextColor(getResources().getColor(R.color.hintedit ));
            }
            etUsername.setEnabled(edit);
            etTelefono.setEnabled(edit);
            etNombre.setEnabled(edit);
        });

        btConfirmar.setOnClickListener(view1 -> confirmarCambios());

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
        btNotis.setOnClickListener(view1 -> abrirNotificaciones());

        btRetirar.setOnClickListener(v->{
            user = appViewModel.getUser();
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity(),R.style.AlerDialogTheme);
            View view2 = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_retirar,view.findViewById(R.id.layoutDialogContainer));
            builder.setView(view2);
            final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

            //funcion add saldo del alert dialog
            view2.findViewById(R.id.btEliminar).setOnClickListener(view3 -> {

                //recoger el saldo a añadir
                EditText etSaldo = view2.findViewById(R.id.etNombreG);
                if (!etSaldo.getText().toString().isEmpty()) {
                    double saldo = Double.parseDouble(etSaldo.getText().toString());
                    if (saldo > 0d && user.getWallet() >= saldo) {
                        AddWallet addWallet = new AddWallet(user.getUsername(), -saldo);
                        userService = retrofit.create(IUserService.class);
                        Call<User> retirarSaldo = userService.addSaldo(addWallet);
                        retirarSaldo.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                Log.d("alertDialog", "codigo de error: " + response.code());
                                if (HttpURLConnection.HTTP_OK == response.code()) {
                                    alertDialog.dismiss();
                                    user.setWallet(user.getWallet()-saldo);
                                    tvSaldo.setText("saldo: " + formato.format(user.getWallet()) + "€");
                                    Toast.makeText(getActivity(), "Saldo retirado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Error retirando saldo", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "El saldo no puede quedar negativo", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (alertDialog.getWindow() != null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        });

    }

    private void abrirNotificaciones() {
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
    }


    public void logOut(){
        //obtener la sesion del usuario
        mAuth = FirebaseAuth.getInstance();
        //cerrar sesion
        mAuth.signOut();
        //evitar que vuelvan aqui

        //cambiar de fragmento al de inicio de sesion
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(R.id.frame, new LoginFragment())
                .commit();
        getActivity().findViewById(R.id.bottomNavM).setVisibility(View.GONE);

    }

    public void confirmarCambios(){
        String nombre,telefono,username;
        nombre = etNombre.getText().toString();
        telefono = etTelefono.getText().toString();
        username = etUsername.getText().toString();
        if (telefono.length() == 9 && !nombre.isEmpty() && !username.isEmpty()){
            userService = retrofit.create(IUserService.class);
            Log.d("llamadaApi","Haciendo llamada a la api para insertar");
            if (user.getUsername().equalsIgnoreCase(username)){
                EditUserDTO usu = new EditUserDTO(username,nombre,telefono);
                Call<User> editarUsuario = userService.modificarUsuario(user.getEmail(),usu);
                editarUsuario.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            Toast.makeText(getActivity(), "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
                            appViewModel.setUser(response.body());
                        }else{
                            Toast.makeText(getActivity(), "Error modificando el usuario "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }else{
                Call<User> comprobarUsername = userService.obtenerUsuarioUsername(username);
                comprobarUsername.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            Toast.makeText(getActivity(), "Username ya existente "+response.code(), Toast.LENGTH_SHORT).show();
                        }else{
                            EditUserDTO usu = new EditUserDTO(username,nombre,telefono);
                            Call<User> editarUsuario = userService.modificarUsuario(user.getEmail(),usu);
                            editarUsuario.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.code() == HttpURLConnection.HTTP_OK){
                                        Toast.makeText(getActivity(), "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
                                        appViewModel.setUser(response.body());
                                    }else{
                                        Toast.makeText(getActivity(), "Error modificando el usuario "+response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }

        }else{
            Toast.makeText(getActivity(), "Rellena los campos que quieras modificar", Toast.LENGTH_SHORT).show();
        }

    }
}