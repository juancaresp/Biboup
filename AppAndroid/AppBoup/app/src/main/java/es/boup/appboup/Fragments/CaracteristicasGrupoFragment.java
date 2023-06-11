package es.boup.appboup.Fragments;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.app.AlertDialog;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.AddWallet;
import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Debt;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.Spent;
import es.boup.appboup.Model.User;
import es.boup.appboup.R;
import es.boup.appboup.Services.IDebtService;
import es.boup.appboup.Services.IGroupService;
import es.boup.appboup.Services.ISpentService;
import es.boup.appboup.Services.IUserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CaracteristicasGrupoFragment extends Fragment {

    public Button btnAniadirParticipante,btnAniadirGasto,btnLiquidarDeuda,btnVerPart;
    private List<User> users;
    private TextView tvNombreGrupo,tvGastosTotales,tvSaldo;
    private ImageView atrasVerPart;
    private IGroupService groupService;
    private AppViewModel appViewModel;
    private User user;
    private View layoutVerPart,layoutInicial;
    private IUserService userService;
    private DecimalFormat formato ;
    private ISpentService spentService;
    private IDebtService debtService;
    private LinearLayout addSaldo;
    private Debt debt;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView,rvUsuarios;
    public List<Spent> gastos;
    private TextView tvDeuda,tvDeudaText;


    public CaracteristicasGrupoFragment() {
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
        tvSaldo=view.findViewById(R.id.tvSaldoG);
        rvUsuarios=view.findViewById(R.id.rvVerUsus);
        btnLiquidarDeuda=view.findViewById(R.id.btnLiquidar);
        tvNombreGrupo=view.findViewById(R.id.tvNombreGrupo);
        layoutInicial=view.findViewById(R.id.layoutInicial);
        atrasVerPart=view.findViewById(R.id.atrasVerPart);
        btnVerPart=view.findViewById(R.id.btVerP);
        layoutVerPart=view.findViewById(R.id.layoutVerP);
        tvGastosTotales=view.findViewById(R.id.tvGastostotales);
        btnAniadirParticipante=view.findViewById(R.id.btAddP);
        btnAniadirGasto=view.findViewById(R.id.btnAniadirGasto);
        recyclerView = view.findViewById(R.id.rvGastos);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        user = appViewModel.getUser();
        tvDeuda=view.findViewById(R.id.tvDeuda);
        tvDeudaText=view.findViewById(R.id.tvDeudaText);
        tvSaldo.setText("Saldo: "+formato.format(user.getWallet())+"€");
        userService = retrofit.create(IUserService.class);
        gastos = new ArrayList<>();
        addSaldo=view.findViewById(R.id.llAddSaldoCG);
        tvNombreGrupo.setText(appViewModel.getGroup().getGroupName());
        debtService=retrofit.create(IDebtService.class);
        Call<Debt> peticionDeuda = debtService.getDebtByGroup(appViewModel.getUser().getUsername(),appViewModel.getGroup().getId().toString());
        peticionDeuda.enqueue(new Callback<Debt>() {
            @Override
            public void onResponse(Call<Debt> call, Response<Debt> response) {
                if(response.code()==HttpURLConnection.HTTP_OK ){
                    debt= response.body();
                    tvDeuda.setText(formato.format(debt.getAmount()));
                    if(debt.getAmount()<0){
                        tvDeuda.setTextColor(Color.RED);
                        tvDeudaText.setText("Debes:");
                        btnLiquidarDeuda.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Debt> call, Throwable t) {

            }
        });
        atrasVerPart.setOnClickListener(v -> {
            layoutVerPart.setVisibility(View.GONE);
            layoutInicial.setVisibility(View.VISIBLE);
        });
        btnVerPart.setOnClickListener(v -> {
            groupService = retrofit.create(IGroupService.class);
            Call<List<User>> peticionUsers = groupService.getGroupUsers(appViewModel.getGroup().getId().toString());
            peticionUsers.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if(response.code()==HttpURLConnection.HTTP_OK){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            users=response.body();
                        }
                        layoutVerPart.setVisibility(View.VISIBLE);
                        layoutInicial.setVisibility(View.GONE);
                        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                        rvUsuarios.setLayoutManager(linearLayoutManager);
                        rvUsuarios.setAdapter(new GrupoAdapter());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });
        });

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
                                Call<List<User>> peticionUsers = groupService.getGroupUsers(appViewModel.getGroup().getId().toString());
                                peticionUsers.enqueue(new Callback<List<User>>() {
                                    @Override
                                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                        if(response.code()==HttpURLConnection.HTTP_OK){
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                users=response.body();
                                            }
                                            layoutVerPart.setVisibility(View.VISIBLE);
                                            layoutInicial.setVisibility(View.GONE);
                                            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                                            rvUsuarios.setLayoutManager(linearLayoutManager);
                                            rvUsuarios.setAdapter(new GrupoAdapter());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<User>> call, Throwable t) {

                                    }
                                });
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

        btnAniadirGasto.setOnClickListener(v -> {
            fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame,new AniadirGastoFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        btnLiquidarDeuda.setOnClickListener(v -> {
            debtService=retrofit.create(IDebtService.class);
            Call<Debt> peticionLiquidar = debtService.closeDebt(appViewModel.getUser().getUsername(),appViewModel.getGroup().getId().toString());
            peticionLiquidar.enqueue(new Callback<Debt>() {
                @Override
                public void onResponse(Call<Debt> call, Response<Debt> response) {
                    if(response.code()==HttpURLConnection.HTTP_OK ){
                        fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame,new ListaInicioFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }

                @Override
                public void onFailure(Call<Debt> call, Throwable t) {

                }
            });
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

            addSaldo.setOnClickListener(v ->{
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity(),R.style.AlerDialogTheme);
                View view2 = LayoutInflater.from(getActivity()).inflate(
                        R.layout.layout_saldo,view.findViewById(R.id.layoutDialogContainer));
                builder.setView(view2);
                final androidx.appcompat.app.AlertDialog alertDialog = builder.create();

                //funcion add saldo del alert dialog
                view2.findViewById(R.id.btEliminar).setOnClickListener(view3 -> {

                    //recoger el saldo a añadir
                    EditText etSaldo = view2.findViewById(R.id.etNombreG);
                    if (!etSaldo.getText().toString().isEmpty()) {
                        double saldo = Double.parseDouble(etSaldo.getText().toString());
                        if (saldo > 0d) {
                            AddWallet addWallet = new AddWallet(user.getUsername(), saldo);
                            userService = retrofit.create(IUserService.class);
                            Call<User> addSaldo = userService.addSaldo(addWallet);
                            addSaldo.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    Log.d("alertDialog", "codigo de error: " + response.code());
                                    if (HttpURLConnection.HTTP_OK == response.code()) {
                                        alertDialog.dismiss();
                                        user.addSaldo(saldo);
                                        tvSaldo.setText("saldo: " + formato.format(user.getWallet()) + "€");
                                        Toast.makeText(getActivity(), "Saldo añadido", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Error añadiendo saldo", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "El saldo no puede ser negativo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if (alertDialog.getWindow() != null){
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
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
                tvNombre = itemView.findViewById(R.id.tvGrupo);
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
                fragmentTransaction.replace(R.id.frame,new GastoFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

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