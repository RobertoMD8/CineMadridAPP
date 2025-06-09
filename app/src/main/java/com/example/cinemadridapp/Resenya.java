package com.example.cinemadridapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemadridapp.Objetos.ExpedienteReseñaNota;
import com.example.cinemadridapp.Objetos.Pelicula;
import com.example.cinemadridapp.Objetos.Usuario;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class Resenya extends AppCompatActivity {

    private Button btnGuardarReseña;
    private ImageButton btnAtras;
    private EditText etReseña;
    private RatingBar nota;
    private TextView tvNotaString;
    private static Pelicula pelicula;
    double notaVieja;

    private ExpedienteReseñaNota expedienteReseñaNota;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resenya);

        btnAtras = findViewById(R.id.btnAtras);
        btnGuardarReseña = findViewById(R.id.btnGuardarReseña);
        etReseña = findViewById(R.id.etReseña);
        tvNotaString = findViewById(R.id.tvNotaString);
        nota = findViewById(R.id.nota);

        db = FirebaseFirestore.getInstance();

        expedienteReseñaNota = null;
        checkReseña();
        notaVieja = nota.getRating();

        nota.setOnRatingBarChangeListener( (ratingBar, rating, fromUser) -> {
            tvNotaString.setText(String.valueOf(rating));
        });

        // ATRAS
        btnAtras.setOnClickListener(v -> {
            pelicula = null;
            Intent intent = new Intent();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                if (Objects.equals(extras.get("Principal"), true)) {
                    intent = new Intent(Resenya.this, PrincipalPeliculaDetalles.class);
                } else if (Objects.equals(extras.get("Personal"), true)) {
                    intent = new Intent(Resenya.this, PersonalPeliculaDetalles.class);
                } else {
                    intent = new Intent(Resenya.this, PantallaNavegacion.class);
                }
            }

            startActivity(intent);
        });

        // GUARDAR
        btnGuardarReseña.setOnClickListener(v -> {
            if (guardarReseña()) {
                Intent intent = new Intent(Resenya.this, PantallaNavegacion.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Debes poner Nota O Reseña!", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void checkReseña() {
        for (ExpedienteReseñaNota exp : ExpedienteReseñaNota.expedienteReseñaNotas) {
            if (exp.getPelicula() == pelicula) {
                etReseña.setText(exp.getReseña());
                nota.setRating((float) exp.getNota());
                expedienteReseñaNota = exp;

                btnGuardarReseña.setBackground(getDrawable(R.drawable.boton_crear_cuenta));
                btnGuardarReseña.setText("Modificar");
                btnGuardarReseña.setTextColor(Color.RED);
            }
        }
    }

    private boolean guardarReseña() {
        String textoReseña = etReseña.getText().toString();
        if ( !(textoReseña.isEmpty() && nota.getRating() == 0) ) {

            if (expedienteReseñaNota == null) {
                String fechaCreacion = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    fechaCreacion = currentDate.format(formatter);
                }
                expedienteReseñaNota = new ExpedienteReseñaNota(Usuario.getNombreUsuario(), pelicula, textoReseña, nota.getRating(), fechaCreacion);
                // INICIAR BBDD
                iniciarReseñaBBDD(expedienteReseñaNota);
            } else {
                // MODIFICAR RESEÑA
                String fechaCreacion = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    fechaCreacion = currentDate.format(formatter);
                }
                // No creamos nuevo objeto ya que se añadiria a lista
                expedienteReseñaNota.setUsuario(Usuario.getNombreUsuario());
                expedienteReseñaNota.setPelicula(pelicula);
                expedienteReseñaNota.setReseña(textoReseña);
                expedienteReseñaNota.setNota(nota.getRating());
                expedienteReseñaNota.setFecha(fechaCreacion);
                modificarReseñaBBDD(expedienteReseñaNota);
            }

            return true;
        } else {
            return false;
        }

    }

    private void iniciarReseñaBBDD(ExpedienteReseñaNota expedienteReseñaNota) {

        HashMap<String, Object> mapaReseña = new HashMap<>();

        mapaReseña.put("usuario", expedienteReseñaNota.getUsuario());
        mapaReseña.put("pelicula", expedienteReseñaNota.getPelicula().getNombre());
        mapaReseña.put("reseña", expedienteReseñaNota.getReseña());
        mapaReseña.put("nota", expedienteReseñaNota.getNota());
        mapaReseña.put("fechaCreacion", expedienteReseñaNota.getFecha());

        db.collection("ExpedienteReseñaNota").add(mapaReseña)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FIREBASE RESEÑA AÑADIDA : ", documentReference.getId());

                    añadirNotaGlobalBBDD(expedienteReseñaNota);
                })
                .addOnFailureListener(e -> Log.e("ERROR ERROR FIREBASE ERROR ERROR : ", "Error añadiendo Reseña ", e));

    }

    private void modificarReseñaBBDD(ExpedienteReseñaNota expedienteReseñaNota) {

        HashMap<String, Object> mapaReseña = new HashMap<>();

        mapaReseña.put("usuario", expedienteReseñaNota.getUsuario());
        mapaReseña.put("pelicula", expedienteReseñaNota.getPelicula().getNombre());
        mapaReseña.put("reseña", expedienteReseñaNota.getReseña());
        mapaReseña.put("nota", expedienteReseñaNota.getNota());
        mapaReseña.put("fechaCreacion", expedienteReseñaNota.getFecha());

        db.collection("ExpedienteReseñaNota").whereEqualTo("usuario", Usuario.getNombreUsuario()).whereEqualTo("pelicula", pelicula.getNombre()).get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {

                    DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                    DocumentReference documentoActualizar = db.collection("ExpedienteReseñaNota").document(document.getId());

                    documentoActualizar.set(mapaReseña);

                    Log.d("FIREBASE RESEÑA MODIFICADA : ", document.getId());
                    modificarNotaGlobarBBDD();
                }
            })
                .addOnFailureListener(e -> Log.e("ERROR ERROR FIREBASE ERROR ERROR : ", "Error modificando Reseña ", e));
    }

    private void añadirNotaGlobalBBDD(ExpedienteReseñaNota expedienteReseñaNota) {

        HashMap<String, Object> mapaNotasGlobales = new HashMap<>();
        mapaNotasGlobales.put("pelicula", pelicula);

        db.collection("NotasGlobales").whereEqualTo("pelicula", pelicula.getNombre()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference documentoActualizar = db.collection("NotasGlobales").document(document.getId());

                        String nombrePelicula = document.getString("pelicula");
                        double sumaTotalNotas = document.getDouble("sumaTotalNotas");
                        int cantidadNotas = document.getLong("cantidadNotas").intValue();

                        sumaTotalNotas += expedienteReseñaNota.getNota();
                        cantidadNotas++;

                        mapaNotasGlobales.put("pelicula", nombrePelicula);
                        mapaNotasGlobales.put("sumaTotalNotas", sumaTotalNotas);
                        mapaNotasGlobales.put("cantidadNotas", cantidadNotas);

                        documentoActualizar.set(mapaNotasGlobales);

                    } else {

                        mapaNotasGlobales.put("pelicula", pelicula.getNombre());
                        mapaNotasGlobales.put("sumaTotalNotas", expedienteReseñaNota.getNota());
                        mapaNotasGlobales.put("cantidadNotas", 1);
                        db.collection("NotasGlobales").add(mapaNotasGlobales).addOnSuccessListener(e -> {
                            Log.d("FIREBASE NOTA GLOBAL : ", "SE HA CREADO NUEVA TB NotasGlobales para esta pelicula ");
                        });

                    }

                })
                .addOnFailureListener(v -> {

                });
    }

    private void modificarNotaGlobarBBDD() {
        HashMap<String, Object> mapaNotasGlobales = new HashMap<>();

        db.collection("NotasGlobales").whereEqualTo("pelicula", pelicula.getNombre()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference doc = db.collection("NotasGlobales").document(document.getId());

                        String nombrePelicula = document.getString("pelicula");
                        double sumaTotalNotas = document.getDouble("sumaTotalNotas");
                        int cantidadNotas = document.getLong("cantidadNotas").intValue();


                        sumaTotalNotas -= notaVieja;
                        sumaTotalNotas += expedienteReseñaNota.getNota();

                        mapaNotasGlobales.put("pelicula", nombrePelicula);
                        mapaNotasGlobales.put("sumaTotalNotas", sumaTotalNotas);
                        mapaNotasGlobales.put("cantidadNotas", cantidadNotas);
                        doc.set(mapaNotasGlobales);
                    }

                });
    }

    public static Pelicula getPelicula() {
        return pelicula;
    }

    public static void setPelicula(Pelicula pelicula) {
        Resenya.pelicula = pelicula;
    }
}