package es.boup.appboup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CredencialesFragment extends Fragment {

    private EditText etNombre,etUsername,etTelefono;
    private TextView tvTelefono,tvNombre;
    private Button boton;
    //recoger el bundle del fragmento de inicio
    private Bundle bundle;
    private boolean correo = true;
    public CredencialesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credenciales, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNombre = view.findViewById(R.id.etNombreC);
        etUsername = view.findViewById(R.id.etUsernameC);
        etTelefono = view.findViewById(R.id.etTelefonoC);
        tvTelefono = view.findViewById(R.id.tvTelefonoC);
        tvNombre = view.findViewById(R.id.tvNombreC);
        boton = view.findViewById(R.id.botonC);

        boton.setOnClickListener((view1)->{
            //hacer llamada a la api para introducir el usuario en nuestra base de datos propia
            //
            //
            //Reemplazar el fragmento actual con el nuevo fragmento
            if (correo){
                if (!etTelefono.getText().toString().isEmpty() && !etNombre.getText().toString().isEmpty() && !etUsername.getText().toString().isEmpty()) {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, new listaInicio())
                            .addToBackStack(null)
                            .commit();
                }else{
                    Toast.makeText(getActivity(), "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                }
            }else{
                if (!etUsername.getText().toString().isEmpty()) {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, new listaInicio())
                            .addToBackStack(null)
                            .commit();
                }else{
                    Toast.makeText(getActivity(), "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("resultadoRegistro", this, (requestKey, result) -> {
            if (!result.getBoolean("correo")){
                etTelefono.setVisibility(View.GONE);
                etNombre.setVisibility(View.GONE);
                tvNombre.setVisibility(View.GONE);
                tvTelefono.setVisibility(View.GONE);
                etTelefono.setVisibility(View.GONE);
                correo = false;
            }
        });
    }
}