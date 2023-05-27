package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.User;
import es.boup.appboup.Services.IGroupService;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CaracteristicasGrupo extends Fragment {

    public Button btnAniadirParticipante,btnAniadirGasto;
    public EditText etAniadirParticipante;
    public TextView tvNombreGrupo;
    private Group group;
    private IGroupService groupService;
    private AppViewModel appViewModel;
    private User user;
    private IUserService userService;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private FragmentManager fragmentManager;


    public CaracteristicasGrupo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caracteristicas_grupo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNombreGrupo=view.findViewById(R.id.tvNombreGrupo);
        btnAniadirParticipante=view.findViewById(R.id.btAddP);
        btnAniadirGasto=view.findViewById(R.id.btnAniadirGasto);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        user = appViewModel.getUser();
        userService = retrofit.create(IUserService.class);
        tvNombreGrupo.setText(appViewModel.getGroup().getGroupName());

        btnAniadirParticipante.setOnClickListener(v -> {
            // llamar al endpoint de a単adir participante
            //crear el alertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlerDialogTheme);
            View view2 = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_add_participante,view.findViewById(R.id.layoutDialogContainer));
            builder.setView(view2);
            final AlertDialog alertDialog = builder.create();

            //funcion add saldo del alert dialog
            view2.findViewById(R.id.btAddGrupoG).setOnClickListener(view3 -> {
                //recoger el saldo a a単adir
                EditText etNombre = view2.findViewById(R.id.etNombreG);
                if (!etNombre.getText().toString().isEmpty()) {
                    String nombre = etNombre.getText().toString();
                    groupService = retrofit.create(IGroupService.class);
                    Call<Group> peticionInsertarUsuario = groupService.insertarUsuarioEnGrupo(appViewModel.getGroup().getId(),nombre);
                    peticionInsertarUsuario.enqueue(new Callback<Group>() {
                        @Override
                        public void onResponse(Call<Group> call, Response<Group> response) {
                            if(response.code()==HttpURLConnection.HTTP_OK ){
                                Toast.makeText(getActivity(), "Participante añadido", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(getActivity(), "Error añadiendo", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Group> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Introduce el nombre", Toast.LENGTH_SHORT).show();
                }
            });
            if (alertDialog.getWindow() != null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        });

        btnAniadirGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame,new AniadirGasto());
                fragmentTransaction.commit();
            }
        });
    }
}