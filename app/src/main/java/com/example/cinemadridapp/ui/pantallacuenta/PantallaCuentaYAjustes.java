package com.example.cinemadridapp.ui.pantallacuenta;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinemadridapp.MainActivity;
import com.example.cinemadridapp.Objetos.ExpedienteReseñaNota;
import com.example.cinemadridapp.Objetos.Preferencia;
import com.example.cinemadridapp.Objetos.Usuario;
import com.example.cinemadridapp.R;
import com.example.cinemadridapp.databinding.FragmentPantallaCuentaAjustesBinding;

import java.util.ArrayList;

public class PantallaCuentaYAjustes extends Fragment {

    private FragmentPantallaCuentaAjustesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentPantallaCuentaAjustesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    private TextView lUsuario;
    private TextView lNombre;
    private TextView lContraseña;
    private TextView lTelefono;
    private TextView lCorreo;
    private Button btnCerrarSesion;
    private TextView lFecha;

    private ImageButton btnAccion;
    private ImageButton btnAventura;
    private ImageButton btnDrama;
    private ImageButton btnSciFi;
    private ImageButton btnFantasia;
    private ImageButton btnTerror;
    private ImageButton btnComedia;
    private ImageButton btnAnimado;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lUsuario = view.findViewById(R.id.lNombreUsuario);
        lContraseña = view.findViewById(R.id.lContraseña);
        lNombre = view.findViewById(R.id.lNombre);
        lTelefono = view.findViewById(R.id.lTelefono);
        lCorreo = view.findViewById(R.id.lCorreo);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        lFecha = view.findViewById(R.id.lFecha);

        btnAccion = view.findViewById(R.id.accion);
        btnAventura = view.findViewById(R.id.aventura);
        btnDrama = view.findViewById(R.id.drama);
        btnSciFi = view.findViewById(R.id.sciFi);
        btnFantasia = view.findViewById(R.id.fantasia);
        btnTerror = view.findViewById(R.id.terror);
        btnComedia = view.findViewById(R.id.comedia);
        btnAnimado = view.findViewById(R.id.animado);

        ArrayList<ImageButton> btnPreferencias = new ArrayList<>();
        btnPreferencias.add(btnAccion);
        btnPreferencias.add(btnAventura);
        btnPreferencias.add(btnDrama);
        btnPreferencias.add(btnSciFi);
        btnPreferencias.add(btnFantasia);
        btnPreferencias.add(btnTerror);
        btnPreferencias.add(btnComedia);
        btnPreferencias.add(btnAnimado);

        for (ImageButton v : btnPreferencias) {
            setBotonPreferencia(v);
        }




        lUsuario.setText(lUsuario.getText() + Usuario.getCorreo());
        lContraseña.setText(lContraseña.getText() + Usuario.getContraseña());
        lTelefono.setText(lTelefono.getText() + Usuario.getTelefono());
        lCorreo.setText(lCorreo.getText() + Usuario.getCorreo());
        lFecha.setText(lFecha.getText() + Usuario.getFechaCreacion());
        lNombre.setText(lNombre.getText() + Usuario.getNombreUsuario());

        btnCerrarSesion.setOnClickListener(v -> {
            Usuario.vaciarUsuario();
            Preferencia.vaciarPreferencias();
            ExpedienteReseñaNota.vaciarExpedientes();
            Intent intent = new Intent(PantallaCuentaYAjustes.this.getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void setBotonPreferencia(ImageButton v) {
        for (String p : Preferencia.getListaPreferencias()) {

            if (p.equals(v.getTag().toString())) {
                if (!Preferencia.getPreferencias().get(p)) {
                    v.setAlpha(0.5f);
                } else {
                    v.setAlpha(1f);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}