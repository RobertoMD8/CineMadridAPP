package com.example.cinemadridapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cinemadridapp.Objetos.ExpedienteReseñaNota;
import com.example.cinemadridapp.Objetos.Pelicula;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalPeliculaDetalles extends AppCompatActivity {

    private ImageButton btnAtras;

    public static Pelicula pelicula;

    private ImageView posterPelicula;
    private TextView tvNotaGlobal;
    private TextView tvNumeroDeNotas;
    private TextView tvNombrePelicula;
    private TextView tvDirector;
    private TextView tvDuracion;
    private TextView tvGeneros;
    private TextView tvDescripcion;
    private FloatingActionButton btnSugerenciaMas;
    private ScrollView scrollDetalles;
    private ConstraintLayout layoutDetalles;

    private TextView tvFechaReseña;
    private EditText reseña;
    private RatingBar nota;
    private TextView tituloTuReseña;
    private ImageView dibujoReseña;

    private FirebaseFirestore db;
    private ExpedienteReseñaNota expedienteReseñaNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_pelicula_detalles);

        db = FirebaseFirestore.getInstance();

        btnAtras = findViewById(R.id.btnAtras);
        posterPelicula = findViewById(R.id.posterPelicula);
        tvNotaGlobal = findViewById(R.id.tvNotaGlobal);
        tvNumeroDeNotas = findViewById(R.id.tvNumeroDeNotas);
        tvNombrePelicula = findViewById(R.id.tvNombrePelicula);
        tvDirector = findViewById(R.id.tvDirector);
        tvDuracion = findViewById(R.id.tvDuracion);
        tvGeneros = findViewById(R.id.tvGeneros);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        btnSugerenciaMas = findViewById(R.id.btnSugerenciaMas);
        scrollDetalles = findViewById(R.id.scrollDetalles);
        layoutDetalles = findViewById(R.id.layoutDetalles);

        reseña = findViewById(R.id.reseña);
        nota = findViewById(R.id.nota);
        tvFechaReseña = findViewById(R.id.tvFechaReseña);
        tituloTuReseña = findViewById(R.id.tituloTuReseña);
        dibujoReseña = findViewById(R.id.dibujoReseña);

        setVistaInicial();

        getTuReseña();
        setReseña();

        btnSugerenciaMas.hide();
        scrollDetalles.post(() -> {
            if (scrollDetalles.getHeight() >= layoutDetalles.getHeight()) {
                btnSugerenciaMas.hide();
            } else {
                btnSugerenciaMas.show();
            }
        });

        scrollDetalles.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (scrollDetalles.getScrollY() > 25 || scrollDetalles.getScrollY() == layoutDetalles.getHeight()) {
                btnSugerenciaMas.hide();
            } else {
                btnSugerenciaMas.show();
            }

        });

        btnSugerenciaMas.setOnClickListener(v -> {
            scrollDetalles.setScrollY(layoutDetalles.getHeight());
        });

        // Pulsar Poster -> Escribir Reseña
        posterPelicula.setOnClickListener( v -> {
            Resenya.setPelicula(pelicula);
            Intent intent = new Intent(PersonalPeliculaDetalles.this, Resenya.class);
            startActivity(intent);
        });

        // ATRAS
        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalPeliculaDetalles.this, PantallaNavegacion.class);
            startActivity(intent);
        });
    }

    private void setVistaInicial() {
        int id = getResources().getIdentifier(pelicula.getPoster(), "drawable", getPackageName());
        posterPelicula.setImageResource(id);
        tvNombrePelicula.setText(pelicula.getNombre());
        tvDirector.setText(pelicula.getDirector());
        tvDuracion.setText(pelicula.getDuracion() + " Min");
        tvGeneros.setText(pelicula.getGeneros());
        tvDescripcion.setText(pelicula.getDescripcion());
    }

    private void getTuReseña() {

        for (ExpedienteReseñaNota exp : ExpedienteReseñaNota.expedienteReseñaNotas) {
            if (exp.getPelicula() == pelicula) {
                expedienteReseñaNota = exp;
                tvNotaGlobal.setText(String.valueOf(expedienteReseñaNota.getNota()));
            }
        }
    }

    private void setReseña() {

        if (expedienteReseñaNota != null) {
            reseña.setVisibility(View.VISIBLE);
            nota.setVisibility(View.VISIBLE);
            tvFechaReseña.setVisibility(View.VISIBLE);

            tituloTuReseña.setVisibility(View.VISIBLE);
            dibujoReseña.setVisibility(View.VISIBLE);

            reseña.setText(expedienteReseñaNota.getReseña());
            nota.setRating((float) expedienteReseñaNota.getNota());
            tvFechaReseña.setText(expedienteReseñaNota.getFecha());
        }

    }


}