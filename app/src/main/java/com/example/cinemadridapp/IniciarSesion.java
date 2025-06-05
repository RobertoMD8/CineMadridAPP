package com.example.cinemadridapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemadridapp.Objetos.ExpedienteReseñaNota;
import com.example.cinemadridapp.Objetos.Pelicula;
import com.example.cinemadridapp.Objetos.Preferencia;
import com.example.cinemadridapp.Objetos.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class IniciarSesion extends AppCompatActivity {

    private EditText tNombreUsuario, tContraseña;
    private ImageButton btnAtras;
    private Button btnIniciarSesion;
    private TextView lMensaje;

    private String email;
    private String password;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        // FIND BY VIEW
        tNombreUsuario = findViewById(R.id.tNombreUsuario);
        tContraseña = findViewById(R.id.tContraseña);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnAtras = findViewById(R.id.btnAtras);
        lMensaje = findViewById(R.id.lMensaje);

        // Inicializar FireBase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        // INICIAR SESION
        btnIniciarSesion.setOnClickListener(v -> {

            email = tNombreUsuario.getText().toString().trim();
            password = tContraseña.getText().toString().trim();

            // email = "admin@gmail.com";
            // password = "administrador";
            // email = "rmd.innventio@gmail.com";
            // password = "Robertillo88";


            if (email.isEmpty() || password.isEmpty()) {
                // Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                // return;
                email = "prueba@gmail.com";
                password = "password";
            }

            // CHECK CREDENCIALES
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show();

                            // CADENA -> USUARIO -> RESEÑA/NOTA
                            setUsuario(email, password);
                            // Pasamos a Pantalla Principal
                            startActivity(new Intent(IniciarSesion.this, PantallaNavegacion.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // ATRAS
        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(IniciarSesion.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void setUsuario(String correo, String contraseña) {
        // Consultamos TB Usuario con Parametros -> Correo y Contraseña
        db.collection("Usuarios").whereEqualTo("correo", correo).whereEqualTo("contraseña", contraseña).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String nombreUsuario = document.getString("usuario");
                        String telefono = document.getString("telefono");
                        String fechaCreacion = document.getString("fechaCreacion");

                        Usuario.setNombreUsuario(nombreUsuario);
                        Usuario.setContraseña(contraseña);
                        Usuario.setCorreo(correo);
                        Usuario.setTelefono(telefono);
                        Usuario.setFechaCreacion(fechaCreacion);
                    }
                    // Llamamos a siguiente cadena una vez haya terminado consulta
                    // (Para que este inicializado USUARIO correctamete)
                    setExpedienteReseñaNota();
                })
                .addOnFailureListener(e -> Log.e("ERROR ERROR FIREBASE ERROR ERROR", "Error consultando TB Usuarios", e));


    }

    public void setExpedienteReseñaNota() {
        // Consultamos TB expedienteReseñaNota con Parametro -> Usuario
        db.collection("ExpedienteReseñaNota").whereEqualTo("usuario", Usuario.getNombreUsuario()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String usuario = document.getString("usuario");
                        String pelicula = document.getString("pelicula");
                        String reseña = document.getString("reseña");
                        double nota = document.getDouble("nota");
                        String fechaCreacion = document.getString("fechaCreacion");

                        // Buscamos pelicula por nombre e Iniciamos DTO ExpReseñaNota
                        Pelicula p = Pelicula.getPeliculaPorNombre(pelicula);
                        ExpedienteReseñaNota ex = new ExpedienteReseñaNota(usuario, p, reseña, nota, fechaCreacion);
                    }

                  setPreferencias();
                })
                .addOnFailureListener(e -> Log.e("ERROR ERROR FIREBASE ERROR ERROR", "Error consultando TB expedienteReseñaNota", e));
    }

    public void setPreferencias() {

        // Consultamos TB Preferencias con Parametro -> Usuario
        db.collection("Preferencias").document(Usuario.getNombreUsuario()).get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        for (String p : Preferencia.getListaPreferencias()) {
                            Preferencia.getPreferencias().put(p, documentSnapshot.getBoolean(p));
                        }

                    }

                })
                .addOnFailureListener(e -> Log.e("ERROR ERROR FIREBASE ERROR ERROR", "Error consultando TB expedienteReseñaNota", e));
    }
}