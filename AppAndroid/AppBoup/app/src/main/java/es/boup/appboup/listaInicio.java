package es.boup.appboup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class listaInicio extends Fragment {

    private Button btnVisibilizar;
    private EditText etCrearGrupo;
    private Button btnCrearGrupo;


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
    }




}

