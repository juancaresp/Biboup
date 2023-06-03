package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Group;
import es.boup.appboup.Model.Spent;
import es.boup.appboup.Services.IGroupService;
import es.boup.appboup.Services.ISpentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HistoricoFragment extends Fragment {

    private TextView tvSaldo, tvTotal;
    private AppViewModel appViewModel;
    private List<Spent> gastos;
    private RecyclerView rv;
    public FragmentManager fragmentManager;
    private ISpentService spentService;
    private IGroupService groupoService;
    private DecimalFormat formato ;
    private Double total;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CONEXION_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public HistoricoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSaldo = view.findViewById(R.id.tvSaldoH);
        tvTotal = view.findViewById(R.id.tvGastostotalesH);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        rv = view.findViewById(R.id.rvHistorico);
        spentService = retrofit.create(ISpentService.class);
        groupoService = retrofit.create(IGroupService.class);
        gastos = new ArrayList<>();

        formato= new DecimalFormat("#.##");
        tvSaldo.setText("Saldo: "+formato.format(appViewModel.getUser().getWallet())+"€");

        Call<List<Spent>> peticionGastos = spentService.getSpentsUser(appViewModel.getUser().getUsername());
        peticionGastos.enqueue(new Callback<List<Spent>>() {
            @Override
            public void onResponse(Call<List<Spent>> call, Response<List<Spent>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    gastos = response.body();
                    Log.d("llamadaApi","totald de gastos del usuario: " + gastos.size());
                    rv.setAdapter(new HistoricoAdapter());
                    total = gastos.stream().mapToDouble(r->r.getQuantity()).sum();
                    tvTotal.setText("Gastos totales: "+formato.format(total)+"€");
                }else{
                    Log.d("llamadaApi","fallo obteniendo el historico: " + gastos.size());

                }
            }

            @Override
            public void onFailure(Call<List<Spent>> call, Throwable t) {
                Log.d("llamadaApi","error de red en el historico" + gastos.size());

            }
        });

        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new HistoricoAdapter());

    }

    class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoHolder>{


        @NonNull
        @Override
        public HistoricoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HistoricoHolder(getLayoutInflater().inflate(R.layout.item_historico,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull HistoricoHolder holder, int position) {
            holder.imprimir(gastos.get(position));
        }

        @Override
        public int getItemCount() {return gastos.size();
        }
        public class HistoricoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvNombre, tvTitulo, tvDinero;
            private ImageView tipoGasto;


            public HistoricoHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvGrupo);
                tvTitulo = itemView.findViewById(R.id.tvTitulo);
                tvDinero = itemView.findViewById(R.id.tvGasto);
                tipoGasto = itemView.findViewById(R.id.imgGasto);
                itemView.setOnClickListener(this);
            }

            public void imprimir(Spent spent){
                tvDinero.setText(""+spent.getQuantity());
                tvNombre.setText(spent.getGroup().getGroupName());
                tvTitulo.setText(spent.getSpentName());

                //Fix me
                //cuando se añada el enumerado aqui poner un if o un switch
                tipoGasto.setImageResource(R.drawable.ic_baseline_airplanemode_active_24);
            }

            @Override
            public void onClick(View view) {
                String gid = gastos.get(getAdapterPosition()).getGroup().getId()+"";
                Call<Group> peticionGrupo = groupoService.getGroupById(gid);
                peticionGrupo.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK){
                            appViewModel.setGroup(response.body());
                            fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame,new CaracteristicasGrupo());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {

                    }
                });

            }
        }
    }

}