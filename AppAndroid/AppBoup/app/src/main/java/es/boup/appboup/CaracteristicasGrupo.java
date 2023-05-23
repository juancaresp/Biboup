package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    public Button btnVisibilizar,btnAniadirParticipante;
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
        btnVisibilizar=view.findViewById(R.id.btnAniadir);
        etAniadirParticipante=view.findViewById(R.id.etAniadir);
        tvNombreGrupo=view.findViewById(R.id.tvNombreGrupo);
        btnAniadirParticipante=view.findViewById(R.id.btnAniadirParticipante);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        user = appViewModel.getUser();
        userService = retrofit.create(IUserService.class);
        getParentFragmentManager().setFragmentResultListener("resultadoGroup", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

                int id= result.getInt("group");
                groupService = retrofit.create(IGroupService.class);
                Call<Group> peticionGrupo = groupService.getGroupById(""+id);
                peticionGrupo.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if(response.code()== HttpURLConnection.HTTP_OK){
                            group=response.body();
                            tvNombreGrupo.setText(group.getGroupName());
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {

                    }
                });
            }
        });

        btnVisibilizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAniadirParticipante.setVisibility(View.VISIBLE);
                btnAniadirParticipante.setVisibility(View.VISIBLE);
            }
        });

        btnAniadirParticipante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // llamar al endpoint de añadir participante
                groupService = retrofit.create(IGroupService.class);
                Call<Group> peticionInsertarUsuario = groupService.insertarUsuarioEnGrupo(group.getId(), etAniadirParticipante.getText().toString() );
                peticionInsertarUsuario.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if(response.code()==HttpURLConnection.HTTP_OK ){
                            etAniadirParticipante.setVisibility(View.GONE);
                            btnAniadirParticipante.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {

                    }
                });
            }
        });
    }
}