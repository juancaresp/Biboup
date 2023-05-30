package es.boup.appboup;

import static es.boup.appboup.MainActivity.CONEXION_API;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.AppViewModel;
import es.boup.appboup.Model.Debt;
import es.boup.appboup.Model.DebtDTO;
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

    private Button btnCrearGrupo;
    private List<Group> groups;
    private List<DebtDTO> debtDTOS;
    private RecyclerView rv;
    private IGroupService groupService;
    private IUserService userService;
    //Valor del usuario que se pasara entre los fragmentos
    private AppViewModel appViewModel;
    private User user;
    private Group group;
    public FragmentManager fragmentManager;
    private List<Debt> debts;
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

        btnCrearGrupo = view.findViewById(R.id.btAddP);
        rv=view.findViewById(R.id.listaDeGrupos);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        user = appViewModel.getUser();
        userService = retrofit.create(IUserService.class);
        Log.d("llamadaApi","fd");
        Call<List<Debt>> peticionDebts= userService.getUserDebts(user.getUsername());
        peticionDebts.enqueue(new Callback<List<Debt>>() {
            @Override
            public void onResponse(Call<List<Debt>> call, Response<List<Debt>> response) {
                if(response.code()== HttpURLConnection.HTTP_OK){
                    debts=response.body();
                    Call<List<Group>> peticionGrupos = userService.obtenerGruposDelUsuario(user.getUsername());
                    peticionGrupos.enqueue(new Callback<List<Group>>() {
                        @Override
                        public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                            if(response.code()== HttpURLConnection.HTTP_OK){
                                groups=response.body();
                                for (Debt d: debts) {
                                    for (Group g: groups) {
                                        if(d.getGroup().getId()==g.getId())
                                            debtDTOS.add(new DebtDTO(g,d));
                                    }
                                }
                                rv.setAdapter(new GrupoAdapter());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Group>> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Debt>> call, Throwable t) {

            }
        });

        btnCrearGrupo.setOnClickListener(view1 -> {
            //crear el alertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlerDialogTheme);
            View view2 = LayoutInflater.from(getActivity()).inflate(
                    R.layout.layout_crear_grupo,view.findViewById(R.id.layoutDialogContainer));
            builder.setView(view2);
            final AlertDialog alertDialog = builder.create();

            //funcion add saldo del alert dialog
            view2.findViewById(R.id.btEliminar).setOnClickListener(view3 -> {
                //recoger el saldo a añadir
                EditText etNombre = view2.findViewById(R.id.etNombreG);

                if (!etNombre.getText().toString().isEmpty()) {
                    String nombre = etNombre.getText().toString();
                    Log.d("llamadaApi","dentro del if del dialogo");
                    groupService = retrofit.create(IGroupService.class);
                    Call<Group> peticionInsertarGrupo = groupService.insertarUsuario(nombre,user.getUsername());
                    Log.d("llamadaApi",nombre + user.getUsername());
                    peticionInsertarGrupo.enqueue(new Callback<Group>() {
                        @Override
                        public void onResponse(Call<Group> call, Response<Group> response) {
                            if(response.code()== HttpURLConnection.HTTP_OK){
                                debtDTOS.add(new DebtDTO(response.body(),new Debt()));
                                groups.add(response.body());

                                rv.setAdapter(new GrupoAdapter());
                                alertDialog.dismiss();
                                Log.d("llamadaApi","llamada hecha");
                            }else{
                                Log.d("llamadaApi","error"+response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<Group> call, Throwable t) {
                            Toast.makeText(getContext(), "fallo", Toast.LENGTH_SHORT).show();
                            Log.d("llamadaApi","error de red");
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

            private TextView tvNombre,tvDni,tvResumen;


            public GrupoHolder(@NonNull View itemView) {
                super(itemView);
                tvDni=itemView.findViewById(R.id.tvId);
                tvNombre=itemView.findViewById(R.id.tvTitulo);
                tvResumen=itemView.findViewById(R.id.tvGasto);
                itemView.setOnClickListener(this);
            }

            public void imprimir(Group group){
                tvDni.setText(group.getId().toString());
                tvNombre.setText(group.getGroupName());
                //tvResumen.setText(debtDTOS.get(group).getDebt().getAmount()+ " €");
            }

            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putInt("group",groups.get(getAdapterPosition()).getId());
                groupService = retrofit.create(IGroupService.class);
                Call<Group> peticionGrupo = groupService.getGroupById(""+groups.get(getAdapterPosition()).getId());
                peticionGrupo.enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if(response.code()== HttpURLConnection.HTTP_OK){
                            group=response.body();
                            appViewModel.setGroup(group);
                            Toast.makeText(getActivity(), ""+group.getGroupName(), Toast.LENGTH_SHORT).show();
                            fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame,new CaracteristicasGrupo());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }else{
                            Toast.makeText(getActivity(), "fallo", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        Toast.makeText(getActivity(), "fallo", Toast.LENGTH_SHORT).show();
                    }
                });






            }
        }
    }




}

