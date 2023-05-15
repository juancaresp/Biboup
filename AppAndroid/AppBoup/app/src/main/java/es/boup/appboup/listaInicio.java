package es.boup.appboup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.boup.appboup.Model.Group;


public class listaInicio extends Fragment {

    private Button btnVisibilizar;
    private EditText etCrearGrupo;
    private Button btnCrearGrupo;
    private List<Group> groups;
    private RecyclerView rv;



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
            }
        });
        groups= new ArrayList<>();
        groups.add(new Group(1,"Valencia"));
        groups.add(new Group(2,"Barcelona"));
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
                getParentFragmentManager().setFragmentResult("resultadoLibro",bundle);

            }
        }
    }




}

