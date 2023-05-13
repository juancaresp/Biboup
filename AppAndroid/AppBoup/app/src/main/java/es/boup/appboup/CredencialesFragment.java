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

    private EditText etNombre,etTelefono;
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
        etTelefono = view.findViewById(R.id.etTelefonoC);
        tvTelefono = view.findViewById(R.id.tvTelefonoC);
        tvNombre = view.findViewById(R.id.tvNombreC);

        boton = view.findViewById(R.id.botonC);
        boton.setOnClickListener((view1)->confirmar());

    }

    private void confirmar(){
        Bundle bundle = new Bundle();
        String nombre,telefono;
        nombre = etNombre.getText().toString();
        telefono = etTelefono.getText().toString();
        //Reemplazar el fragmento actual con el nuevo fragmento
        if (!telefono.isEmpty() && !nombre.isEmpty()) {
            bundle.putString("telefono",telefono);
            bundle.putString("nobmre",nombre);
            FragmentManager fragmentManager = getParentFragmentManager();
            getParentFragmentManager().setFragmentResult("resultadoCredenciales",bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame, new RegistroFragment())
                    .addToBackStack(null)
                    .commit();
        }else{
            Toast.makeText(getActivity(), "Rellena los campos vacios", Toast.LENGTH_SHORT).show();
        }

    }
}