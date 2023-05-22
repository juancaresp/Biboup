package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

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


public class listaInicio extends Fragment {

    private Button btnVisibilizar;
    private EditText etCrearGrupo;
    private Button btnCrearGrupo;
    private List<Group> groups;
    private RecyclerView rv;
    private IGroupService groupService;
    private IUserService userService;
    //Valor del usuario que se pasara entre los fragmentos
    private AppViewModel appViewModel;
    private User user;

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();



    public listaInicio() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_inicio, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCrearGrupo=view.findViewById(R.id.etAniadir);
        btnCrearGrupo=view.findViewById(R.id.btnCrearGrupo);
        btnVisibilizar=view.findViewById(R.id.btnAniadir);
        rv=view.findViewById(R.id.listaDeGrupos);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        user = appViewModel.getUser();
        userService = retrofit.create(IUserService.class);
        Log.d("llamadaApi","fd");
        Call<List<Group>> peticionGrupos = userService.obtenerGruposDelUsuario(user.getUsername());
        peticionGrupos.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if(response.code()== HttpURLConnection.HTTP_OK){
                    groups=response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {

            }
        });
        btnVisibilizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCrearGrupo.setVisibility(View.VISIBLE);
                btnCrearGrupo.setVisibility(View.VISIBLE);
            }
        });

        btnCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamar a endpoint de crearGrupo con el et.gettext
                groupService = retrofit.create(IGroupService.class);
                Call<Group> peticionInsertarGrupo = groupService.insertarUsuario(etCrearGrupo.getText().toString(),user.getUsername());
                peticionInsertarGrupo.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if(response.code()== HttpURLConnection.HTTP_OK){
                            groups.add(response.body());
                            rv.setAdapter(new GrupoAdapter());
                            etCrearGrupo.setVisibility(View.GONE);
                            btnCrearGrupo.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        Toast.makeText(getContext(), "fallo", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        groups= new ArrayList<>();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new GrupoAdapter());
    }

    class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoHolder>{

        @NonNull
        @Override
        public GrupoAdapter.GrupoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GrupoAdapter.GrupoHolder(getLayoutInflater().inflate(R.layout.listgroup,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GrupoAdapter.GrupoHolder holder, int position) {
            holder.imprimir(groups.get(position));
        }



        @Override
        public int getItemCount() {return groups.size();
        }
        public class GrupoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvNombre,tvDni;


            public GrupoHolder(@NonNull View itemView) {
                super(itemView);
                tvDni=itemView.findViewById(R.id.tvId);
                tvNombre=itemView.findViewById(R.id.tvNombre);
                itemView.setOnClickListener(this);
            }

            public void imprimir(Group group){
                tvDni.setText(group.getId().toString());
                tvNombre.setText(group.getGroupName());

            }

            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putInt("group",groups.get(getAdapterPosition()).getId());
                getParentFragmentManager().setFragmentResult("resultadoGroup",bundle);

            }
        }
    }




}

