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


public class CaracteristicasGrupo extends Fragment {

    public Button btnVisibilizar,btnAniadirParticipante;
    public EditText etAniadirParticipante;



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
        btnAniadirParticipante=view.findViewById(R.id.btnAniadirParticipante);

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
                
            }
        });
    }
}