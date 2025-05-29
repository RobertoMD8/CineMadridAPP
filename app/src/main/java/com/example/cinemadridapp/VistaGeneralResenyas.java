package com.example.cinemadridapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinemadridapp.Objetos.ExpedienteReseñaNota;
import com.example.cinemadridapp.Objetos.Pelicula;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class VistaGeneralResenyas extends AppCompatActivity {

    private ImageButton btnAtras;
    private ListView layoutReseñas;
    private HashMap<Integer, Integer> mapaNotas;
    private ArrayAdapter arrayAdapter;
    private FirebaseFirestore db;
    public static Pelicula pelicula;

    private ArrayList<ExpedienteReseñaNota> expedienteReseñaNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_general_resenyas);

        db = FirebaseFirestore.getInstance();

        btnAtras = findViewById(R.id.btnAtras);
        layoutReseñas = findViewById(R.id.listViewReseñas);

        mapaNotas = new HashMap<>();
        // PRIMERO Nota Global
        // Para calcular barras de progreso
        getReseñasBBDD();


        // ATRAS
        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(VistaGeneralResenyas.this, PrincipalPeliculaDetalles.class);
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
                TextView tvNotaTexto = layout.findViewById(R.id.tvNotaTexto);

                tvNombreUsuario.setText(exp.getUsuario());
                tvFechaReseña.setText(exp.getFecha());
                reseña.setText(exp.getReseña());
                nota.setRating((float) exp.getNota());
                tvNotaTexto.setText(String.valueOf(exp.getNota()));
                return convertView;
            }
        };

        return arrayAdapter;
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
                    layoutReseñas.setAdapter(setArrayAdapterLayoutReseñas(expedienteReseñaNotas));
                });

    }
}