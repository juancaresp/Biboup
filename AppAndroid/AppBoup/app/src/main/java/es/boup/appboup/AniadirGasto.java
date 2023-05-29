package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Spent;
import es.boup.appboup.Model.User;
import es.boup.appboup.Services.IGroupService;
import es.boup.appboup.Services.ISpentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AniadirGasto extends Fragment {
    private RecyclerView recyclerViewElegirPagador,recyclerViewElegirDeudores;
    private EditText etTitulo,etDescripcion, etCantidad;
    private Button btnElegirPagador,btnElegirDeudores,btnGuardarLista,btnGuardarGasto;
    private IGroupService groupService;
    private ISpentService spentService;
    private AppViewModel appViewModel;
    private List<User> users;
    private List<User> deudores;
    private User pagador;
    private Spent spent;
    private FragmentManager fragmentManager;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public AniadirGasto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aniadir_gasto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTitulo=view.findViewById(R.id.etTitulo);
        etDescripcion=view.findViewById(R.id.etDescripcion);
        etCantidad=view.findViewById(R.id.etCantidad);
        btnElegirDeudores=view.findViewById(R.id.btnElegirDeudores2);
        btnElegirPagador=view.findViewById(R.id.btnElegirPagador2);
        btnGuardarGasto=view.findViewById(R.id.btnGuardarGasto);
        btnGuardarLista=view.findViewById(R.id.btnGuardarLista);
        recyclerViewElegirPagador=view.findViewById(R.id.rvPagadores);
        recyclerViewElegirDeudores=view.findViewById(R.id.rvDeudores);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        btnElegirPagador.setText(appViewModel.getUser().getUsername());
        pagador=appViewModel.getUser();
        spent= new Spent();
        deudores=new ArrayList<>();


        spent.setGroup(appViewModel.getGroup());

        groupService = retrofit.create(IGroupService.class);
        Call<List<User>> peticionUsers = groupService.getGroupUsers(appViewModel.getGroup().getId().toString());
        peticionUsers.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.code()==HttpURLConnection.HTTP_OK){
                    users=response.body();
                    deudores.addAll(users);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
        btnGuardarGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCantidad.getText().toString().equals("")){
                spent.setQuantity(Integer.parseInt(etCantidad.getText().toString()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    spent.setDate(LocalDate.now().toString());
                }
                spent.setSpentDesc(etDescripcion.getText().toString());
                spent.setUsers(deudores);
                spent.setSpentName(etTitulo.getText().toString());
                spent.setPayer(pagador);
                spentService = retrofit.create(ISpentService.class);
                Call<Spent> peticionAddGasto = spentService.addSpent(spent);
                peticionAddGasto.enqueue(new Callback<Spent>() {
                    @Override
                    public void onResponse(Call<Spent> call, Response<Spent> response) {
                        if(response.code()==HttpURLConnection.HTTP_OK){
                            fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.frame,new CaracteristicasGrupo());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Spent> call, Throwable t) {

                    }
                });}
                else{
                    Toast.makeText(getContext(), "Tienes que añadir cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnElegirPagador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTitulo.setVisibility(View.GONE);
                etDescripcion.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                recyclerViewElegirPagador.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                recyclerViewElegirPagador.setLayoutManager(linearLayoutManager);
                recyclerViewElegirPagador.setAdapter(new GrupoAdapter());

            }
        });
        btnElegirDeudores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etTitulo.setVisibility(View.GONE);
                etDescripcion.setVisibility(View.GONE);
                etCantidad.setVisibility(View.GONE);
                recyclerViewElegirDeudores.setVisibility(View.VISIBLE);
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                recyclerViewElegirDeudores.setLayoutManager(linearLayoutManager);
                btnGuardarLista.setVisibility(View.VISIBLE);
                recyclerViewElegirDeudores.setAdapter(new GrupoAdapter2());
                deudores=new ArrayList<>();
                deudores.addAll(users);
                deudores.remove(pagador);
                spent.setUsers(deudores);
            }
        });
        btnGuardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( deudores.size()!= users.size()-1){
                    btnElegirDeudores.setText("personalizado");
                }else{
                    btnElegirDeudores.setText("entre todos");
                }
                btnGuardarLista.setVisibility(View.GONE);
                recyclerViewElegirDeudores.setVisibility(View.GONE);

                etTitulo.setVisibility(View.VISIBLE);
                etDescripcion.setVisibility(View.VISIBLE);
                etCantidad.setVisibility(View.VISIBLE);
                Log.d("llamadaApi",deudores.toString());
                spent.setPayer(pagador);
            }
        });
    }
    class GrupoAdapter extends RecyclerView.Adapter<AniadirGasto.GrupoAdapter.GrupoHolder>{

        @NonNull
        @Override
        public AniadirGasto.GrupoAdapter.GrupoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AniadirGasto.GrupoAdapter.GrupoHolder(getLayoutInflater().inflate(R.layout.itemusuario,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AniadirGasto.GrupoAdapter.GrupoHolder holder, int position) {
            holder.imprimir(users.get(position));
        }



        @Override
        public int getItemCount() {return users.size();
        }
        public class GrupoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvNombre;


            public GrupoHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre=itemView.findViewById(R.id.tvNombrePagador);
                itemView.setOnClickListener(this);
            }

            public void imprimir(User user){
                tvNombre.setText(user.getUsername());

            }

            @Override
            public void onClick(View view) {
                btnElegirPagador.setText(users.get(getAdapterPosition()).getUsername());
                pagador=users.get(getAdapterPosition());
                Toast.makeText(getContext(), ""+pagador.getUsername(), Toast.LENGTH_SHORT).show();
                recyclerViewElegirPagador.setVisibility(View.GONE);
                etTitulo.setVisibility(View.VISIBLE);
                etDescripcion.setVisibility(View.VISIBLE);
                etCantidad.setVisibility(View.VISIBLE);
            }
        }
    }
    class GrupoAdapter2 extends RecyclerView.Adapter<AniadirGasto.GrupoAdapter2.GrupoHolder2>{

        @NonNull
        @Override
        public AniadirGasto.GrupoAdapter2.GrupoHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AniadirGasto.GrupoAdapter2.GrupoHolder2(getLayoutInflater().inflate(R.layout.item_usuario_checkbox,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AniadirGasto.GrupoAdapter2.GrupoHolder2 holder, int position) {
            holder.imprimir(deudores.get(position));
        }



        @Override
        public int getItemCount() {return deudores.size();
        }
        public class GrupoHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvNombre;
            private CheckBox checkbox;


            public GrupoHolder2(@NonNull View itemView) {
                super(itemView);
                tvNombre=itemView.findViewById(R.id.tvNombrePagador);
                checkbox=itemView.findViewById(R.id.checkBox);
                itemView.setOnClickListener(this);
                checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkbox.isChecked()){
                            deudores.remove(users.get(getAdapterPosition()));
                        }else{
                            deudores.add(users.get(getAdapterPosition()));
                        }
                    }
                });
            }

            public void imprimir(User user){
                tvNombre.setText(user.getUsername());

            }

            @Override
            public void onClick(View view) {

            }
        }
    }

}