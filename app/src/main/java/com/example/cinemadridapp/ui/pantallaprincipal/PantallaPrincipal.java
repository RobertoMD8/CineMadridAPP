package com.example.cinemadridapp.ui.pantallaprincipal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.cinemadridapp.Objetos.Pelicula;
import com.example.cinemadridapp.Objetos.Preferencia;
import com.example.cinemadridapp.PersonalPeliculaDetalles;
import com.example.cinemadridapp.PrincipalPeliculaDetalles;
import com.example.cinemadridapp.R;
import com.example.cinemadridapp.databinding.FragmentPantallaPrincipalBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PantallaPrincipal extends Fragment {

    private FragmentPantallaPrincipalBinding binding;
    private View vBoton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentPantallaPrincipalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    private ListView layoutPeliculas;
    private SearchView etBuscarPeliculas;
    private ArrayAdapter arrayAdapterLayoutPeliculas;
    private Spinner spinnerFiltro;
    private ArrayAdapter arrayAdapterFiltro;
    private FirebaseFirestore db;

    List<List<Pelicula>> peliculasDivididas;
    List<Pelicula> peliculas;
    HashMap<String, String> mapaNotasGlobales;

    public static int posicionFiltro = -1;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // FIND BY VIEW
        layoutPeliculas = view.findViewById(R.id.layoutPeliculas);
        etBuscarPeliculas = view.findViewById(R.id.etBuscarPeliculas);
        spinnerFiltro = view.findViewById(R.id.spinnerFiltro);

        peliculas = Pelicula.peliculas;
        peliculas.sort((p1, p2) -> Integer.compare(p2.getAño(), p1.getAño()));

        peliculasDivididas  = dividirPeliculasEn4(peliculas);
        layoutPeliculas.setAdapter(setArrayAdapterLayoutPeliculas(peliculasDivididas));

        db = FirebaseFirestore.getInstance();

        mapaNotasGlobales = new HashMap<>();
        getNotasGlobalesBBDD();

        etBuscarPeliculas.setOnQueryTextListener(busqueda());

        ArrayList<String> filtros = new ArrayList<>();
        filtros.add("Año");
        filtros.add("Nota");
        filtros.add("Nombre");
        filtros.add("Preferencias");
        arrayAdapterFiltro = new ArrayAdapter<>(getContext(), R.layout.spinner_texto_blanco, filtros);
        spinnerFiltro.setAdapter(arrayAdapterFiltro);

        spinnerFiltro.setOnItemSelectedListener(filtrarListener());
        if (posicionFiltro != -1) {
            spinnerFiltro.setSelection(posicionFiltro);
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            getNotasGlobalesBBDD();
        }, 1000);

    }

    // Accion Pulsar Poster Pelicula
    View.OnClickListener verPelicula = v -> {
        ImageButton ib = (ImageButton) v;
        String poster = ib.getTag().toString();
        Log.d("POSTER  --> ", poster);

        for (Pelicula pelicula : Pelicula.peliculas) {
            if (Objects.equals(pelicula.getPoster(), poster)) {
                PrincipalPeliculaDetalles.pelicula = pelicula;
                Intent intent = new Intent(PantallaPrincipal.this.getActivity(), PrincipalPeliculaDetalles.class);
                startActivity(intent);
            }
        }

        Intent intent = new Intent(PantallaPrincipal.this.getActivity(), PrincipalPeliculaDetalles.class);
        startActivity(intent);
    };

    public ArrayAdapter setArrayAdapterLayoutPeliculas(List<List<Pelicula>> peliculas) {
        // Coge la altura de la pantalla
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int alturaPantalla = displayMetrics.heightPixels;


        arrayAdapterLayoutPeliculas = new ArrayAdapter<>(getContext(), R.layout.fila_peliculas, peliculas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_peliculas, parent, false);
                }


                int alturaPorcentaje = (int) (alturaPantalla * 0.185);
                View layout = convertView.findViewById(R.id.filaPeliculas);

                if (layout != null) {
                    ViewGroup.LayoutParams params = layout.getLayoutParams();
                    params.height = alturaPorcentaje;
                    layout.setLayoutParams(params);
                }


                List<Pelicula> pelis = getItem(position);
                if (pelis == null) {
                    return convertView;
                }

                int [] posterIds = {R.id.imagenPelicula1, R.id.imagenPelicula2, R.id.imagenPelicula3, R.id.imagenPelicula4};
                int [] notaIds = {R.id.notaPelicula1, R.id.notaPelicula2, R.id.notaPelicula3, R.id.notaPelicula4};

                for (int x = 0; x < 4; x++) {
                    ImageButton imagenPelicula = convertView.findViewById(posterIds[x]);
                    TextView notaPelicula = convertView.findViewById(notaIds[x]);

                    if (x < pelis.size()) {

                        Pelicula pelicula = pelis.get(x);
                        // TODO Actualizar un poco
                        if (Pelicula.mapaIdPosters.containsKey(pelicula.getPoster())) {
                            Glide.with(getContext())
                                    .load(Pelicula.mapaIdPosters.get(pelicula.getPoster()))
                                    .into(imagenPelicula);

                        } else {
                            int id = getResources().getIdentifier(pelicula.getPoster(), "drawable", requireActivity().getPackageName());
                            Pelicula.mapaIdPosters.put(pelicula.getPoster(), id);
                            Glide.with(getContext())
                                    .load(getResources().getIdentifier(pelicula.getPoster(), "drawable", getContext().getPackageName()))
                                    .into(imagenPelicula);
                            Log.d("AQUI", "AQUI AQUI AQUI");
                        }
                        // Comprobamos para ahorrar memoria
                        if (imagenPelicula.getVisibility() != View.VISIBLE) {
                            imagenPelicula.setVisibility(View.VISIBLE);
                        }

                        imagenPelicula.setOnClickListener(verPelicula);
                        imagenPelicula.setTag(pelicula.getPoster());

                        notaPelicula.setVisibility(View.VISIBLE);
                        notaPelicula.setTextColor(Color.parseColor("#e6ae02"));
                        String notaGlobal = mapaNotasGlobales.get(pelicula.getNombre());
                        if (notaGlobal != null) {
                            notaPelicula.setText(notaGlobal);
                        } else {
                            notaPelicula.setText("0.0");
                        }

                    } else {
                        imagenPelicula.setVisibility(View.INVISIBLE);
                        notaPelicula.setVisibility(View.INVISIBLE);
                    }
                }


                return convertView;
            }
        };

        return arrayAdapterLayoutPeliculas;
    }

    private SearchView.OnQueryTextListener busqueda() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("MENSAJE TEXTO NUEVO ", "[" + newText + "]");
                List<Pelicula> peliculasEnBusqueda = new ArrayList<>();
                final List<Pelicula> listaPeliculasEntera = Pelicula.getPeliculas();
                if (!newText.isEmpty()) {
                    for (Pelicula p : listaPeliculasEntera) {
                        if (p.getNombre().toLowerCase().strip().contains(newText.toLowerCase().strip())) {
                            peliculasEnBusqueda.add(p);
                        }
                    }
                    // Modificamos aqui pelis para que a la
                    // hora de filtrar filtre con esta busqueda
                    peliculas = peliculasEnBusqueda;

                } else {
                    // Esta vacio la busqueda, por lo que son TODAS las pelis
                    peliculas = Pelicula.peliculas;
                    // Filtramos manualmente para ordenar por seleccion
                    filtrar(spinnerFiltro.getSelectedItem().toString());
                }

                // Filtramos por si acaso (NECESARIO , NO lo Quites)
                filtrar(spinnerFiltro.getSelectedItem().toString());
                peliculasDivididas = dividirPeliculasEn4(peliculas);
                layoutPeliculas.setAdapter(setArrayAdapterLayoutPeliculas(peliculasDivididas));
                return false;
            }
        };

    }



    public AdapterView.OnItemSelectedListener filtrarListener() {

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicionFiltro = position;
                String seleccion = parent.getItemAtPosition(position).toString();
                filtrar(seleccion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void filtrar(String seleccion) {

        switch (seleccion) {
            case "Año":
                peliculas.sort((p1, p2) -> Integer.compare(p2.getAño(), p1.getAño()));
                break;
            case "Nota":
                peliculas.sort((p1, p2) -> Double.compare(p2.getNotaGlobal(), p1.getNotaGlobal()));
                // Filtrar por nota
                break;
            case "Nombre":
                peliculas.sort((p1, p2) -> CharSequence.compare(p1.getNombre(), p2.getNombre()));
                // Filtrar por nombre
                break;
            case "Preferencias":
                // Filtrar por género
                filtrarPreferencias();
                break;
        }

        peliculasDivididas = dividirPeliculasEn4(peliculas);
        layoutPeliculas.setAdapter(setArrayAdapterLayoutPeliculas(peliculasDivididas));
    }

    private void getNotasGlobalesBBDD() {

        db.collection("NotasGlobales").get()
                .addOnSuccessListener( queryDocumentSnapshots -> {

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        int cantidadNotas = doc.getLong("cantidadNotas").intValue();
                        double sumaTotalNotas = doc.getDouble("sumaTotalNotas");
                        double notaGlobal = sumaTotalNotas / cantidadNotas;
                        String notaGlobalTexto = String.format("%.1f", notaGlobal);

                        mapaNotasGlobales.put(doc.getString("pelicula"), notaGlobalTexto);
                    }

                    for (Pelicula temp : peliculas) {
                        temp.setNotaGlobal(0.0);
                        for (Map.Entry<String, String> m : mapaNotasGlobales.entrySet()) {
                            if (temp.getNombre().equals(m.getKey())) {
                                temp.setNotaGlobal(Double.parseDouble(m.getValue()));
                            }
                        }
                    }


                    arrayAdapterLayoutPeliculas.notifyDataSetChanged();
                });

    }

    private void filtrarPreferencias() {
        ArrayList<Pelicula> grupo1 = new ArrayList<>();
        ArrayList<Pelicula> grupo2 = new ArrayList<>();
        ArrayList<Pelicula> grupo3 = new ArrayList<>();
        ArrayList<Pelicula> grupo4 = new ArrayList<>();



        ArrayList<String> preferencias = new ArrayList<>();
        for (Map.Entry<String, Boolean> mapa: Preferencia.getPreferencias().entrySet()) {
            if (mapa.getValue() != null) {
                if (mapa.getValue()) {
                    preferencias.add(mapa.getKey());
                    Log.d("PREFERENCIAS", mapa.getKey());
                }
            }

        }

        int count = 0;
        for (Pelicula p : peliculas) {
            count = 0;
            String temp = p.getGeneros().strip().toLowerCase();
            String[] split = temp.split(",");

            for (String s : preferencias) {
                for (int x = 0; x < split.length ; x++) {
                    if (split[x].trim().toLowerCase().equals(s.toLowerCase().trim())) {
                        Log.d("PERO BUENO", p.getNombre() + " : " + split[x]);
                        Log.d("PREFERENCIAS MATCH", p.getNombre() + " : " + s);
                        count++;
                    }
                }
            }

            if (count == 3) {
                grupo1.add(p);
            } else if (count == 2) {
                grupo2.add(p);
            } else if (count == 1) {
                grupo3.add(p);
            } else {
                grupo4.add(p);
            }

        }

        peliculas = new ArrayList<>();
        peliculas.addAll(grupo1);
        peliculas.addAll(grupo2);
        peliculas.addAll(grupo3);
        peliculas.addAll(grupo4);
    }


    private List<List<Pelicula>> dividirPeliculasEn4(List<Pelicula> peliculas) {
        List<List<Pelicula>> chunks = new ArrayList<>();
        for (int i = 0; i < peliculas.size(); i += 4) {
            chunks.add(peliculas.subList(i, Math.min(i + 4, peliculas.size())));
        }
        return chunks;
    }

    private List<Pelicula> comprimirPeliculas(List<List<Pelicula>> peliculas) {
        List<Pelicula> chunks = new ArrayList<>();
        for (int x = 0; x < peliculas.size(); x++) {
            for (int y = 0; y < peliculas.get(x).size(); y ++) {
                chunks.add(peliculas.get(x).get(y));
            }
        }
        return chunks;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}