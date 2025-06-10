package com.example.cinemadridapp.ui.estadisticas;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cinemadridapp.Objetos.Estadisticas;
import com.example.cinemadridapp.Objetos.ExpedienteReseñaNota;
import com.example.cinemadridapp.Objetos.Pelicula;
import com.example.cinemadridapp.Objetos.Usuario;
import com.example.cinemadridapp.R;
import com.example.cinemadridapp.databinding.FragmentEstadisticasBinding;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class PantallaEstadisticas extends Fragment {

    private FragmentEstadisticasBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEstadisticasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    private ConstraintLayout layoutEstadisticas;
    private ProgressBar nivelCuenta;
    private ImageButton btnInterrogacion;
    private ImageButton btnRangoCuenta;
    private ImageView rangoActual;
    private ImageView rangoSiguiente;


    private TextView tvNombreUsuario;
    private TextView tvNivelCuenta;
    private TextView tvNumeroPeliculasVistas;
    private TextView tvNumPeliculasReseñadas;
    private TextView tvNotaMedia;
    private TextView tvDirectorFavorito;
    private TextView tvGeneroFavorito;
    private TextView tvTotalMinutosVistos;
    private TextView tvDuracionMedia;

    private ConstraintLayout mensajeInformacionNivel;
    private ConstraintLayout mensajeInformacionRango;

    private ArrayList<ExpedienteReseñaNota> expedientes;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutEstadisticas = view.findViewById(R.id.layoutEstadisticas);
        nivelCuenta = view.findViewById(R.id.nivelCuenta);
        btnInterrogacion = view.findViewById(R.id.btnInterrogacion);
        btnRangoCuenta = view.findViewById(R.id.btnRangoCuenta);
        rangoActual = view.findViewById(R.id.rangoActual);
        rangoSiguiente = view.findViewById(R.id.rangoSiguiente);

        // INTERFAZ ESTADISTICAS
        tvNombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tvNivelCuenta = view.findViewById(R.id.tvNivelCuenta);
        tvNumeroPeliculasVistas = view.findViewById(R.id.tvNumeroPeliculasVistas);
        tvNumPeliculasReseñadas = view.findViewById(R.id.tvNumPeliculasReseñadas);
        tvNotaMedia = view.findViewById(R.id.tvNotaMedia);
        tvDirectorFavorito = view.findViewById(R.id.tvDirectorFavorito);
        tvGeneroFavorito = view.findViewById(R.id.tvGeneroFavorito);
        tvTotalMinutosVistos = view.findViewById(R.id.tvTotalMinutosVistos);
        tvDuracionMedia = view.findViewById(R.id.tvDuracionMedia);

        // Constraint Layouts INFORMACION ADICIONAL
        mensajeInformacionRango = view.findViewById(R.id.mensajeInformacionRango);
        mensajeInformacionNivel = view.findViewById(R.id.mensajeInformacionNivel);

        calcularEstadisticas();
        setEstadisticasInterfaz();

        layoutEstadisticas.setOnTouchListener( (v, event) -> {
            mensajeInformacionRango.setVisibility(View.GONE);
            mensajeInformacionNivel.setVisibility(View.GONE);
            return false;
        });

        btnRangoCuenta.setOnClickListener( v -> {
            if (mensajeInformacionRango.getVisibility() == View.GONE) {
                mensajeInformacionRango.setVisibility(View.VISIBLE);

                mensajeInformacionNivel.setVisibility(View.GONE);
            } else {
                mensajeInformacionRango.setVisibility(View.GONE);

                mensajeInformacionNivel.setVisibility(View.GONE);
            }
        });

        btnInterrogacion.setOnClickListener( v -> {
            if (mensajeInformacionNivel.getVisibility() == View.GONE) {
                mensajeInformacionNivel.setVisibility(View.VISIBLE);

                mensajeInformacionRango.setVisibility(View.GONE);
            } else {
                mensajeInformacionNivel.setVisibility(View.GONE);

                mensajeInformacionRango.setVisibility(View.GONE);
            }
        });

    }


    public void calcularEstadisticas() {

        expedientes = ExpedienteReseñaNota.expedienteReseñaNotas;

        int numPeliculasVistas = expedientes.size();




        int numPeliculasReseñadas = 0;

        double totalNotas = 0;
        int numNotas = 0;

        int totalMinutos = 0;

        HashMap<String, Integer> directores = new HashMap<>();
        ArrayList<String> arrayGeneros = new ArrayList<>();
        for (ExpedienteReseñaNota p : expedientes) {
            if (!p.getReseña().isEmpty()) {
                numPeliculasReseñadas++;
            }
            if (p.getNota() != 0.0) {
                totalNotas += p.getNota();
                numNotas++;
            }

            String directorTemp = p.getPelicula().getDirector();
            directores.put(directorTemp, (directores.getOrDefault(directorTemp, 0) + 1));

            arrayGeneros.add(p.getPelicula().getGeneros().strip());

            totalMinutos += p.getPelicula().getDuracion();

        }
        double duracionMedia =  (double) totalMinutos /numPeliculasVistas;
        if (Double.isNaN(duracionMedia)) {
            duracionMedia = 0;
        }

        double notaMedia = totalNotas / numNotas;
        if (Double.isNaN(notaMedia)) {
            notaMedia = 0;
        }

        Estadisticas.setRangoCuenta(setRangoCuenta(numPeliculasVistas));
        Estadisticas.setNivelCuenta(numPeliculasVistas);
        Estadisticas.setNumPeliculasVistas(numPeliculasVistas);
        Estadisticas.setNumPeliculasReseñadas(numPeliculasReseñadas);
        Estadisticas.setNotaMedia(notaMedia);

        String generoFavorito = this.setGeneroFavorito(arrayGeneros);
        Estadisticas.setGeneroFavorito(generoFavorito);

        Estadisticas.setTotalMinutosVistos(totalMinutos);
        Estadisticas.setDuracionMediaPeliculas(duracionMedia);

        String directorFavorito = this.setDirectorFavorito(directores);
        Estadisticas.setDirectorFavorito(directorFavorito);


    }

    public String setGeneroFavorito(ArrayList<String> arrayGenerosSucio) {
        // Pasamos array con Tres Generos con ',' en cada posicion
        // Cogemos cada posicion, dividimos entre ',' y guardamos en arrayGenerosLimpio
        ArrayList<String> generos = new ArrayList<>();
        for (String g : arrayGenerosSucio) {

            String []arrayGenerosLimpio = g.split(",");
            generos.addAll(Arrays.asList(arrayGenerosLimpio));
        }

        HashMap<String, Integer> mapaGeneros = new HashMap<>();
        for ( String g : generos) {
            mapaGeneros.put(g, (mapaGeneros.getOrDefault(g, 0) + 1));
        }
        String generoFavorito = "Ninguno";
        int maxValue = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : mapaGeneros.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                generoFavorito = entry.getKey();
            }
        }

        return generoFavorito;
    }

    private String setDirectorFavorito(HashMap<String, Integer> directores) {
        String director = "Ninguno";
        int maxValue = Integer.MIN_VALUE;
        for (Map.Entry<String, Integer> entry : directores.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                director = entry.getKey();
            }
        }
        return director;
    }

    private void setEstadisticasInterfaz() {

        tvNombreUsuario.setText(Usuario.getNombreUsuario());
        tvNivelCuenta.setText(String.valueOf(Estadisticas.getNivelCuenta()));
        tvNumeroPeliculasVistas.setText("Peliculas Vistas -> " + String.valueOf(Estadisticas.getNumPeliculasVistas()));
        tvNumPeliculasReseñadas.setText("Reseñas -> " + String.valueOf(Estadisticas.getNumPeliculasReseñadas()));
        tvNotaMedia.setText("Nota media -> " + String.format("%.1f", Estadisticas.getNotaMedia()));
        tvDirectorFavorito.setText(Estadisticas.getDirectorFavorito());
        tvGeneroFavorito.setText(Estadisticas.getGeneroFavorito());
        tvTotalMinutosVistos.setText("Total -> " + String.valueOf(Estadisticas.getTotalMinutosVistos()) + " Minutos");
        tvDuracionMedia.setText("Media -> " + String.format("%.1f", Estadisticas.getDuracionMediaPeliculas()) + " Minutos");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String setRangoCuenta(int nivel ) {
        String rango;
        if (nivel <= 15) {
            rango = "Bronce";
            btnRangoCuenta.setImageResource(R.drawable.rango_bronce);
            rangoActual.setImageResource(R.drawable.rango_bronce);
            rangoSiguiente.setImageResource(R.drawable.rango_plata);
            nivelCuenta.setMax(16);
            nivelCuenta.setProgress(nivel);
        } else if (nivel <= 50) {
            rango = "Plata";
            btnRangoCuenta.setImageResource(R.drawable.rango_plata);
            rangoActual.setImageResource(R.drawable.rango_plata);
            rangoSiguiente.setImageResource(R.drawable.rango_oro);
            nivelCuenta.setMin(15);
            nivelCuenta.setMax(50);
            nivelCuenta.setProgress(nivel);
        } else if (nivel <= 150) {
            rango = "Oro";
            btnRangoCuenta.setImageResource(R.drawable.rango_oro);
            rangoActual.setImageResource(R.drawable.rango_oro);
            rangoSiguiente.setImageResource(R.drawable.rango_diamante);
            nivelCuenta.setMin(50);
            nivelCuenta.setMax(150);
            nivelCuenta.setProgress(nivel);
        } else if (nivel <= 500) {
            rango = "Diamante";
            btnRangoCuenta.setImageResource(R.drawable.rango_diamante);
            rangoActual.setImageResource(R.drawable.rango_diamante);
            rangoSiguiente.setImageResource(R.drawable.rango_cristal_nuevo);
            nivelCuenta.setMin(150);
            nivelCuenta.setMax(500);
            nivelCuenta.setProgress(nivel);
        } else if (nivel <= 1000) {
            rango = "Cristal";
            btnRangoCuenta.setImageResource(R.drawable.rango_cristal_nuevo);
            rangoActual.setImageResource(R.drawable.rango_cristal_nuevo);
            rangoSiguiente.setImageResource(R.drawable.rango_legendario);
            nivelCuenta.setMin(500);
            nivelCuenta.setMax(1000);
            nivelCuenta.setProgress(nivel);
        } else {
            rango = "Legendario";
            btnRangoCuenta.setImageResource(R.drawable.rango_legendario);
            rangoActual.setImageResource(R.drawable.rango_legendario);
            rangoSiguiente.setImageResource(R.drawable.rango_legendario);
            nivelCuenta.setMin(1000);
            nivelCuenta.setMax(50000);
            nivelCuenta.setProgress(nivel);
        }
        return rango;
    }

}