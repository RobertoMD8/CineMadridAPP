package com.example.cinemadridapp;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrincipalPeliculaDetalles extends AppCompatActivity {

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
    private ImageButton btnDibujoReseña;

    private ListView layoutReseñas;
    private ArrayAdapter arrayAdapter;

    private ProgressBar barra1;
    private ProgressBar barra2;
    private ProgressBar barra3;
    private ProgressBar barra4;
    private ProgressBar barra5;
    private ProgressBar barra6;
    private ProgressBar barra7;
    private ProgressBar barra8;
    private ProgressBar barra9;
    private ProgressBar barra10;

    private int numTotalNotas;
    private HashMap<Integer, Integer> mapaNotas;
    private FirebaseFirestore db;

    private ArrayList<ExpedienteReseñaNota> expedienteReseñaNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal_pelicula_detalles);

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
        btnDibujoReseña = findViewById(R.id.dibujoReseña);

        barra1 = findViewById(R.id.barra1);
        barra2 = findViewById(R.id.barra2);
        barra3 = findViewById(R.id.barra3);
        barra4 = findViewById(R.id.barra4);
        barra5 = findViewById(R.id.barra5);
        barra6 = findViewById(R.id.barra6);
        barra7 = findViewById(R.id.barra7);
        barra8 = findViewById(R.id.barra8);
        barra9 = findViewById(R.id.barra9);
        barra10 = findViewById(R.id.barra10);


        setVistaInicial();

        mapaNotas = new HashMap<>();
        // PRIMERO Nota Global
        // Para calcular barras de progreso
        getNotaGlobalBBDD();
        getReseñasBBDD();



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
            Intent intent = new Intent(PrincipalPeliculaDetalles.this, Resenya.class);
            intent.putExtra("Principal", true);
            startActivity(intent);
        });

        btnDibujoReseña.setOnClickListener( v -> {
            VistaGeneralResenyas.pelicula = pelicula;
            Intent intent = new Intent(PrincipalPeliculaDetalles.this, VistaGeneralResenyas.class);
            startActivity(intent);
        });

        // ATRAS
        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipalPeliculaDetalles.this, PantallaNavegacion.class);
            intent.putExtra("Eleccion", R.id.navegacion_pantalla_principal);
            startActivity(intent);
        });
    }

    public ArrayAdapter setArrayAdapterLayoutReseñas(ArrayList<ExpedienteReseñaNota> listaExp) {


        arrayAdapter = new ArrayAdapter<>(this, R.layout.layout_resenyas, listaExp) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_resenyas, parent, false);
                }

                View layout = convertView.findViewById(R.id.layoutReseñas);

                ExpedienteReseñaNota exp = listaExp.get(position);
                Log.d("HOALHOASLGOAGOG", String.valueOf(listaExp.size()));
                TextView tvNombreUsuario = layout.findViewById(R.id.tvNombreUsuario);
                TextView tvFechaReseña = layout.findViewById(R.id.tvFechaReseña);
                EditText reseña = layout.findViewById(R.id.reseña);
                RatingBar nota = layout.findViewById(R.id.nota);

                tvNombreUsuario.setText(exp.getUsuario());
                tvFechaReseña.setText(exp.getFecha());
                reseña.setText(exp.getReseña());
                nota.setRating((float) exp.getNota());
                return convertView;
            }
        };

        return arrayAdapter;
    }


    private void getNotaGlobalBBDD() {

        db.collection("NotasGlobales").whereEqualTo("pelicula", pelicula.getNombre()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty())    {

                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);

                        double sumaTotalNotas = document.getDouble("sumaTotalNotas");
                        int cantidadNotas = document.getLong("cantidadNotas").intValue();

                        double nota = sumaTotalNotas / cantidadNotas;
                        tvNotaGlobal.setText(String.format("%.1f", nota));
                        tvNumeroDeNotas.setText(cantidadNotas + " notas");
                        numTotalNotas = cantidadNotas;

                    }

                });
    }

    // TODO ESTO esta to raro con muchos casts
    public void setBarrasProgreso() {

        for (Map.Entry<Integer, Integer> entry : mapaNotas.entrySet()) {
            int num = entry.getKey();
            double cal = entry.getValue();
            double percent = cal / numTotalNotas * 1000;

            Log.d("NUM ", String.valueOf(num));
            Log.d("CALCAL ", String.valueOf(cal));
            switch (num) {
                case (1) :
                    barra1.setProgress((int) percent);
                    break;
                case (2) :
                    barra2.setProgress((int) percent);
                    break;
                case (3) :
                    barra3.setProgress((int) percent);
                    break;
                case (4) :
                    barra4.setProgress((int) percent);
                    break;
                case (5) :
                    barra5.setProgress((int) percent);
                    break;
                case (6) :
                    barra6.setProgress((int) percent);
                    break;
                case (7) :
                    barra7.setProgress((int) percent);
                    break;
                case (8) :
                    barra8.setProgress((int) percent);
                    break;
                case (9) :
                    barra9.setProgress((int) percent);
                    break;
                case (10) :
                    barra10.setProgress((int) percent);
                    break;
            }
        }


    }

    private void getReseñasBBDD() {

        expedienteReseñaNotas = new ArrayList<>();
        db.collection("ExpedienteReseñaNota").whereEqualTo("pelicula", pelicula.getNombre()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot expediente : queryDocumentSnapshots) {
                        ExpedienteReseñaNota reseña = new ExpedienteReseñaNota();
                        reseña.setUsuario(expediente.getString("usuario"));
                        Pelicula peliculaTemp = Pelicula.getPeliculaPorNombre(expediente.getString("pelicula"));
                        reseña.setPelicula(peliculaTemp);
                        reseña.setReseña(expediente.getString("reseña"));
                        reseña.setNota(expediente.getDouble("nota"));
                        reseña.setFecha(expediente.getString("fechaCreacion"));

                        expedienteReseñaNotas.add(reseña);

                        int llave = (int) Math.ceil(reseña.getNota());
                        Log.d("NOTANOTANOPTANTOA", String.valueOf(llave));
                        if (mapaNotas.containsKey(llave)) {
                            int x = mapaNotas.get(llave);
                            x++;
                            mapaNotas.put(llave, x);
                        } else {
                            mapaNotas.put(llave, 1);
                        }
                    }
                    setBarrasProgreso();
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
}