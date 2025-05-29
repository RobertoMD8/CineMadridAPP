package com.example.cinemadridapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.widget.Button;

import com.example.cinemadridapp.Objetos.Usuario;

public class MainActivity extends AppCompatActivity {


    private Button btnIniciarSesion;

    private Button btnCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // FIND BY VIEW
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        // Vaciamos Usuario Por si Acaso
        Usuario.vaciarUsuario();

        btnIniciarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, IniciarSesion.class);
            startActivity(intent);
        });

        btnCrearCuenta.setOnClickListener( v ->  {
            Intent intent = new Intent(MainActivity.this, CrearCuenta.class);
            startActivity(intent);
        });

    }


}