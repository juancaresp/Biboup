package es.boup.appboup.Fragments;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Spent;
import es.boup.appboup.Model.User;
import es.boup.appboup.R;
import es.boup.appboup.Services.IGroupService;
import es.boup.appboup.Services.ISpentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GastoFragment extends Fragment {

    private RecyclerView recyclerViewElegirPagador,recyclerViewElegirDeudores;
    private EditText etTitulo,etDescripcion, etCantidad;
    private Button btnElegirPagador,btnElegirDeudores,btnGuardarLista,btGuardar;
    private ImageButton btBorrar;
    private IGroupService groupService;
    private ISpentService spentService;
    private AppViewModel appViewModel;
    private List<User> users;
    private List<User> deudores;
    private User pagador;
    private Spent gasto;
    private FragmentManager fragmentManager;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public GastoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gasto, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTitulo=view.findViewById(R.id.etTitulo2);
        etDescripcion=view.findViewById(R.id.etDescripcion2);
        etCantidad=view.findViewById(R.id.etCantidad2);
        btnElegirDeudores=view.findViewById(R.id.btnElegirDeudores3);
        btnElegirPagador=view.findViewById(R.id.btnElegirPagador3);
        btBorrar = view.findViewById(R.id.btBorrar);
        btGuardar = view.findViewById(R.id.btGuardar);
        btnGuardarLista=view.findViewById(R.id.btnGuardarLista2);
        recyclerViewElegirPagador=view.findViewById(R.id.rvPagadores);
        recyclerViewElegirDeudores=view.findViewById(R.id.rvDeudores);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        btnElegirPagador.setText(appViewModel.getUser().getUsername());
        gasto = appViewModel.getSpent();
        etTitulo.setText(gasto.getSpentName());
        etDescripcion.setText(gasto.getSpentDesc());
        etCantidad.setText(""+gasto.getQuantity());
        users = gasto.getUsers();
        pagador = gasto.getPayer();
        btnElegirPagador.setText(pagador.getUsername());
        btnElegirDeudores.setEnabled(false);
        deudores = appViewModel.getSpent().getUsers();

        groupService = retrofit.create(IGroupService.class);
        Call<List<User>> peticionUsers = groupService.getGroupUsers(appViewModel.getGroup().getId().toString());
        peticionUsers.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.code()== HttpURLConnection.HTTP_OK){
                    users=response.body();
                    if (deudores.size() == users.size()){
                        btnElegirDeudores.setText("Todos");
                    }else{
                        btnElegirDeudores.setText("Personalizado");
                    }
                    users.remove(pagador);
                    deudores.addAll(users);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
        pagador=appViewModel.getUser();
        btnElegirPagador.setOnClickListener(v -> {
            etTitulo.setVisibility(View.GONE);
            etDescripcion.setVisibility(View.GONE);
            etCantidad.setVisibility(View.GONE);
            recyclerViewElegirPagador.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
            recyclerViewElegirPagador.setLayoutManager(linearLayoutManager);
            recyclerViewElegirPagador.setAdapter(new GrupoAdapter());
            recyclerViewElegirDeudores.setVisibility(View.INVISIBLE);
            btnElegirDeudores.setEnabled(true);

        });
        btnElegirDeudores.setOnClickListener(v -> {
            etTitulo.setVisibility(View.GONE);
            etDescripcion.setVisibility(View.GONE);
            etCantidad.setVisibility(View.GONE);
            recyclerViewElegirDeudores.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
            recyclerViewElegirDeudores.setLayoutManager(linearLayoutManager);
            btnGuardarLista.setVisibility(View.VISIBLE);
            deudores=new ArrayList<>();
            deudores.addAll(users);
            deudores.remove(pagador);
            recyclerViewElegirDeudores.setAdapter(new GrupoAdapter2());
            gasto.setUsers(deudores);
            btGuardar.setEnabled(true);
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
                gasto.setPayer(pagador);
            }
        });

        btGuardar.setOnClickListener(view1 -> {
            if(!etCantidad.getText().toString().equals("")){
                gasto.setQuantity(Double.parseDouble(etCantidad.getText().toString()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    gasto.setDate(LocalDate.now().toString());
                }
                gasto.setSpentDesc(etDescripcion.getText().toString());
                deudores.remove(pagador);
                gasto.setUsers(deudores);
                gasto.setSpentName(etTitulo.getText().toString());
                gasto.setPayer(pagador);
                spentService = retrofit.create(ISpentService.class);
                Call<Spent> peticionEdit = spentService.updateSpent(gasto);
                peticionEdit.enqueue(new Callback<Spent>() {
                    @Override
                    public void onResponse(Call<Spent> call, Response<Spent> response) {
                        if(response.code()== HttpURLConnection.HTTP_OK){
                            Toast.makeText(getActivity(), "Gasto modificado", Toast.LENGTH_SHORT).show();
                            fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame,new CaracteristicasGrupoFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }else{
                            Toast.makeText(getActivity(), "Error modificando"+response.code(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Spent> call, Throwable t) {

                    }
                });
            }else{
                Toast.makeText(getContext(), "Tienes que añadir cantidad", Toast.LENGTH_SHORT).show();
            }

        });

        btBorrar.setOnClickListener(view1 -> {
            //crear el alertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlerDialogTheme);
            View view2 = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_borrar_gasto,view.findViewById(R.id.layoutDialogContainer));
            builder.setView(view2);
            final AlertDialog alertDialog = builder.create();
            view2.findViewById(R.id.btEliminar).setOnClickListener(view3 -> {
            spentService = retrofit.create(ISpentService.class);
            Call<Spent> peticionEdit = spentService.deleteSpent(""+gasto.getId());
            peticionEdit.enqueue(new Callback<Spent>() {
                @Override
                public void onResponse(Call<Spent> call, Response<Spent> response) {
                    if(response.code()== HttpURLConnection.HTTP_OK){
                        Toast.makeText(getActivity(), "Gasto Eliminado", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("llamadaApi","error eliminando"+response.code());
                    }
                    alertDialog.dismiss();
                    fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new CaracteristicasGrupoFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                @Override
                public void onFailure(Call<Spent> call, Throwable t) {
                    Log.d("llamadaApi","error eliminando de red un gasto"+t.getMessage());
                    alertDialog.dismiss();
                    fragmentManager = getParentFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,new CaracteristicasGrupoFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            });

        });
            if (alertDialog.getWindow() != null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();

        });

    }

    class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoHolder>{

        @NonNull
        @Override
        public GrupoAdapter.GrupoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GrupoAdapter.GrupoHolder(getLayoutInflater().inflate(R.layout.itemusuario,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GrupoAdapter.GrupoHolder holder, int position) {
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
    class GrupoAdapter2 extends RecyclerView.Adapter<GrupoAdapter2.GrupoHolder2>{

        @NonNull
        @Override
        public GrupoAdapter2.GrupoHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GrupoAdapter2.GrupoHolder2(getLayoutInflater().inflate(R.layout.item_usuario_checkbox,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GrupoAdapter2.GrupoHolder2 holder, int position) {
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
                deudores.remove(pagador);
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