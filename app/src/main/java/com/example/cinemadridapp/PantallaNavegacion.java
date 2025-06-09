package com.example.cinemadridapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cinemadridapp.databinding.ActivityPantallaNavegacionBinding;

public class PantallaNavegacion extends AppCompatActivity {

    private ActivityPantallaNavegacionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPantallaNavegacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navegacion_pantalla_principal, R.id.navegacion_pantalla_personal,R.id.navegacion_pantalla_estadisticas,R.id.navegacion_pantalla_ajustes_cuenta)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_pantalla_principal);

        NavigationUI.setupWithNavController(binding.navView, navController);

        Bundle extra = getIntent().getExtras();
        int id;
        if (extra != null) {
            id = (int) extra.get("Eleccion");
            if (id != 0) {
                binding.navView.setSelectedItemId(id);
                setIntent(new Intent());
            }
        }



    }

}