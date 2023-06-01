package es.boup.appboup;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.Spent;
import es.boup.appboup.Model.User;
import es.boup.appboup.Services.IGroupService;
import es.boup.appboup.Services.ISpentService;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CaracteristicasGrupo extends Fragment {

    public Button btnAniadirParticipante,btnAniadirGasto;

    public TextView tvNombreGrupo,tvGastosTotales,tvSaldo;
    private Group group;
    private IGroupService groupService;
    private AppViewModel appViewModel;
    private User user;
    private IUserService userService;
    private DecimalFormat formato ;
    private ISpentService spentService;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;
    public List<Spent> gastos;


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
        formato= new DecimalFormat("#.##");
        tvSaldo=view.findViewById(R.id.tvSaldoCG);
        tvNombreGrupo=view.findViewById(R.id.tvNombreGrupo);
        tvGastosTotales=view.findViewById(R.id.tvGastostotales);
        btnAniadirParticipante=view.findViewById(R.id.btAddP);
        btnAniadirGasto=view.findViewById(R.id.btnAniadirGasto);
        recyclerView = view.findViewById(R.id.rvGastos);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        user = appViewModel.getUser();
        tvSaldo.setText("Saldo: "+formato.format(user.getWallet())+"€");
        userService = retrofit.create(IUserService.class);
        gastos = new ArrayList<>();
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
            view2.findViewById(R.id.btEliminar).setOnClickListener(view3 -> {
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
                                Toast.makeText(getActivity(), "Usuario no existente", Toast.LENGTH_SHORT).show();
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
                fragmentTransaction.replace(R.id.frame,new AniadirGasto());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new GastoAdapter());

        spentService = retrofit.create(ISpentService.class);
        Call<List<Spent>> peticionObtenerSpentsGrupo = spentService.getSpentsGroup(appViewModel.getGroup().getId());
        peticionObtenerSpentsGrupo.enqueue(new Callback<List<Spent>>() {
            @Override
            public void onResponse(Call<List<Spent>> call, Response<List<Spent>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    Log.d("llamadaApi","gastos obtenidos");
                    gastos = response.body();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvGastosTotales.setText("Gastos totales :" + gastos.stream().mapToDouble(Spent::getQuantity).sum());
                    }
                    recyclerView.setAdapter(new GastoAdapter());
                }else{
                    Log.d("llamadaApi","error obteniendo gastos");
                }
            }

            @Override
            public void onFailure(Call<List<Spent>> call, Throwable t) {
                Log.d("llamadaApi",t.getLocalizedMessage());
            }
        });
    }

    class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoHolder>{


        @NonNull
        @Override
        public GastoAdapter.GastoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new GastoAdapter.GastoHolder(getLayoutInflater().inflate(R.layout.item_gasto,parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull GastoAdapter.GastoHolder holder, int position) {
            holder.imprimir(gastos.get(position));
        }

        @Override
        public int getItemCount() {
            return gastos.size();
        }

        public class GastoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvNombre, tvTitulo, tvDinero;
            private ImageView tipoGasto;

            public GastoHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvpagador);
                tvTitulo = itemView.findViewById(R.id.tvTitulo);
                tvDinero = itemView.findViewById(R.id.tvGasto);
                tipoGasto = itemView.findViewById(R.id.imgGasto);
                itemView.setOnClickListener(this);
            }

            public void imprimir (Spent spent){
                tvDinero.setText(""+spent.getQuantity());
                tvNombre.setText(spent.getPayer().getUsername());
                tvTitulo.setText(spent.getSpentName());

                //Fix me
                //cuando se añada el enumerado aqui poner un if o un switch
                tipoGasto.setImageResource(R.drawable.ic_baseline_airplanemode_active_24);
            }

            @Override
            public void onClick(View view) {
                appViewModel.setSpent(gastos.get(getAdapterPosition()));
                fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame,new GastoFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    }

}