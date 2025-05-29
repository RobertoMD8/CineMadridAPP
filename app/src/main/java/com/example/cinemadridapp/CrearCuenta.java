package com.example.cinemadridapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemadridapp.Objetos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CrearCuenta extends AppCompatActivity {

    ImageButton btnAtras;
    EditText tNombreUsuario, tContraseña, tContraseña2, tTelefono, tCorreo, tAlias;
    TextView lNombreUsuario, lContraseña, lContraseña2, lTelefono, lCorreo, lAlias;
    Button btnCreateAccountSubmit;
    TextView lMensaje;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String nombreUsuario, contraseña, repetida,  correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        // FIND BY VIEW
        tNombreUsuario = findViewById(R.id.tNombreUsuario);
        tContraseña = findViewById(R.id.tContraseña);
        tContraseña2 = findViewById(R.id.tContraseña2);

        lNombreUsuario = findViewById(R.id.lNombreUsuario);
        lContraseña = findViewById(R.id.lContraseña);
        lContraseña2 = findViewById(R.id.lContraseña2);

        btnCreateAccountSubmit = findViewById(R.id.btnCrearCuenta);
        btnAtras = findViewById(R.id.btnAtras);

        // Vaciamos Usuario Por si Acaso
        Usuario.vaciarUsuario();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // CREAR CUENTA
        btnCreateAccountSubmit.setOnClickListener(v -> {
            correo = tNombreUsuario.getText().toString();
            contraseña = tContraseña.getText().toString();
            repetida = tContraseña2.getText().toString();
            nombreUsuario = "";

            if (!checkCorreo(correo)) {
                Toast.makeText(this, "Formato de correo inadecuado" , Toast.LENGTH_LONG).show();
            } else if  (contraseña.length() < 8) {
                Toast.makeText(this, "Contraseña demasiado corta", Toast.LENGTH_LONG).show();
            } else if (!contraseña.equals(repetida)) {
                Toast.makeText(this, "Contraseñas NO COINCIDEN", Toast.LENGTH_LONG).show();
            } else {
                // CREAMOS USUARIO Con CREDENCIALES
                mAuth.createUserWithEmailAndPassword(correo, contraseña)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Cogemos fecha actual
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                                Date fecha = Date.valueOf(timeStamp);
                                String fechaString = fecha.toString();

                                // Iniciamos Usuario
                                Usuario.setContraseña(contraseña);
                                Usuario.setCorreo(correo);
                                Usuario.setFechaCreacion(fechaString);

                                String[] temp = Usuario.getCorreo().split("@");
                                // Si el Nombre de Usuario Temporal NO DA ERROR
                                if (temp[0] != null) {
                                    nombreUsuario = temp[0];
                                    Usuario.setNombreUsuario(nombreUsuario);

                                    // Iniciamos Usuario en FireBase
                                    checkExisteUsuario();
                                    // TODO Iniciar intent desde aqui para if correcto para mas rapido
                                    Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }


        });

        // ATRAS
        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(CrearCuenta.this, MainActivity.class);
            startActivity(intent);
        });
    }

    public void checkExisteUsuario() {

        db.collection("Usuarios").document(nombreUsuario).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        crearCuentaBBDDD(false);
                    } else {
                        crearCuentaBBDDD(true);
                    }
                });

    }

    public void crearCuentaBBDDD(boolean cond) {

        if (cond) {
            // NO Existe es Usuario
            // Por lo que SI PODEMOS CREARLO
            HashMap<String, Object> mapaUsuario = new HashMap<>();
            mapaUsuario.put("usuario", nombreUsuario);
            mapaUsuario.put("correo", Usuario.getCorreo());
            mapaUsuario.put("contraseña", Usuario.getContraseña());
            mapaUsuario.put("fechaCreacion", Usuario.getFechaCreacion());

            db.collection("Usuarios").document(nombreUsuario).set(mapaUsuario);
            // Redirigir a la pantalla principal o a iniciar sesión
            startActivity(new Intent(CrearCuenta.this, CrearCuentaFinal.class));
            finish();
        } else {
            // Usuario YA Existe
            // TENEMOS Que Generar uno Automatico
            HashMap<String, Object> mapaUsuario = new HashMap<>();
            mapaUsuario.put("correo", Usuario.getCorreo());
            mapaUsuario.put("contraseña", Usuario.getContraseña());
            mapaUsuario.put("fechaCreacion", Usuario.getFechaCreacion());

            db.collection("Usuarios").add(mapaUsuario)
                    .addOnSuccessListener(documentReference -> {
                        nombreUsuario = documentReference.getId();
                        Usuario.setNombreUsuario(nombreUsuario);
                        mapaUsuario.put("usuario", nombreUsuario);
                        db.collection("Usuarios").document(nombreUsuario).set(mapaUsuario);

                        // Redirigir a la pantalla principal o a iniciar sesión
                        startActivity(new Intent(CrearCuenta.this, CrearCuentaFinal.class));
                        finish();
                    });

        }
    }

    public static boolean checkCorreo(String correo) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (!pat.matcher(correo).matches()) {
            return false;
        }
        return true;
    }
}