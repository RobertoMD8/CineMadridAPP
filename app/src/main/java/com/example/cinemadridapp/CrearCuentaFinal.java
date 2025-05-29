package com.example.cinemadridapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemadridapp.Objetos.Preferencia;
import com.example.cinemadridapp.Objetos.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CrearCuentaFinal extends AppCompatActivity {


    private TextView lNombreUsuario;
    private TextView lTelefono;
    private TextView lPreferencias;
    private EditText tNombreUsuario;
    private EditText tTelefono;
    private Button btnCrearCuenta;

    private ImageButton btnAccion;
    private ImageButton btnAventura;
    private ImageButton btnDrama;
    private ImageButton btnSciFi;
    private ImageButton btnFantasia;
    private ImageButton btnTerror;
    private ImageButton btnComedia;
    private ImageButton btnAnimado;
    private String nombreUsuarioGenerado;
    private String telefono;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta_final);

        db = FirebaseFirestore.getInstance();

        lNombreUsuario = findViewById(R.id.lNombreUsuario);
        lTelefono = findViewById(R.id.lTelefono);
        lPreferencias = findViewById(R.id.lPreferencias);
        tNombreUsuario = findViewById(R.id.tNombreUsuario);
        tTelefono = findViewById(R.id.tTelefono);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnAccion = findViewById(R.id.accion);
        btnAventura = findViewById(R.id.aventura);
        btnDrama = findViewById(R.id.drama);
        btnSciFi = findViewById(R.id.sciFi);
        btnFantasia = findViewById(R.id.fantasia);
        btnTerror = findViewById(R.id.terror);
        btnComedia = findViewById(R.id.comedia);
        btnAnimado = findViewById(R.id.animado);

        btnAccion.setOnClickListener(pulsarPreferencia);
        btnAventura.setOnClickListener(pulsarPreferencia);
        btnDrama.setOnClickListener(pulsarPreferencia);
        btnSciFi.setOnClickListener(pulsarPreferencia);
        btnFantasia.setOnClickListener(pulsarPreferencia);
        btnTerror.setOnClickListener(pulsarPreferencia);
        btnComedia.setOnClickListener(pulsarPreferencia);
        btnAnimado.setOnClickListener(pulsarPreferencia);

        btnCrearCuenta.setOnClickListener( v -> {
            crearCuentaBBDD();
        });

        telefono = "";
        nombreUsuarioGenerado = Usuario.getNombreUsuario();
        tNombreUsuario.setText(nombreUsuarioGenerado);
    }

    View.OnClickListener pulsarPreferencia = v -> {

        ImageButton buttonPreferencia = (ImageButton) v;

        for (String p : Preferencia.getListaPreferencias()) {

            if (p.equals(v.getTag().toString())) {
                if (!Preferencia.getPreferencias().get(p)) {
                    // No Seleccionado --> Selecciona
                    Preferencia.getPreferencias().put(p, true);
                    buttonPreferencia.setAlpha(1f);
                } else {
                    // Seleccionado --> Deselecciona
                    Preferencia.getPreferencias().put(p, false);
                    buttonPreferencia.setAlpha(0.5f);
                }
            }
        }
     };

    private void crearCuentaBBDD() {
        String usuario = tNombreUsuario.getText().toString();
        telefono = tTelefono.getText().toString();

        if (usuario.equals(nombreUsuarioGenerado)) {
            db.collection("Usuarios").document(nombreUsuarioGenerado).update("telefono", telefono);
            Usuario.setTelefono(telefono);
            crearPreferenciasBBDD();
        }
        else if (usuario.isEmpty()) {
            mostrarError("Usuario Vacio");
        } else {
            db.collection("Usuarios").document(usuario).get()
                    .addOnSuccessListener(documentUsuarioNuevoCheck -> {
                       if (documentUsuarioNuevoCheck.exists()) {
                           lNombreUsuario.setTextColor(Color.parseColor("#610000"));
                           mostrarError("Nombre de Usuario ya Existe");
                       } else {
                           db.collection("Usuarios").document(Usuario.getNombreUsuario()).get()
                                   .addOnSuccessListener(documentUsuarioExistente -> {
                                       Map<String, Object> datosNuevos = documentUsuarioExistente.getData();
                                       datosNuevos.put("telefono", telefono);
                                       datosNuevos.put("usuario", usuario);
                                       db.collection("Usuarios").document(Usuario.getNombreUsuario()).delete();
                                       db.collection("Usuarios").document(usuario).set(datosNuevos);
                                       Usuario.setNombreUsuario(usuario);
                                       Usuario.setTelefono(telefono);
                                       crearPreferenciasBBDD();
                                   });
                       }
                    });
        }

    }

    private void crearPreferenciasBBDD() {

        HashMap<String, Boolean> mapaPreferencias = Preferencia.getPreferencias();

        db.collection("Preferencias").document(Usuario.getNombreUsuario()).set(mapaPreferencias)
                .addOnSuccessListener(documentReference -> {
                    Intent intent = new Intent(CrearCuentaFinal.this, PantallaNavegacion.class);
                    startActivity(intent);
                });
    }



    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

}

