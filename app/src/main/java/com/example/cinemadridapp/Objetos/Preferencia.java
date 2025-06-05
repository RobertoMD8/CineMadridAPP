package com.example.cinemadridapp.Objetos;

import android.content.Intent;

import com.example.cinemadridapp.CrearCuentaFinal;
import com.example.cinemadridapp.PantallaNavegacion;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Preferencia {
    private static FirebaseFirestore db;

    private static ArrayList<String> listaPreferencias = new ArrayList<>();

    private static HashMap<String, Boolean> preferencias = new HashMap<>();

    static {
        listaPreferencias.add("accion");
        listaPreferencias.add("aventura");
        listaPreferencias.add("drama");
        listaPreferencias.add("sci-Fi");
        listaPreferencias.add("fantasia");
        listaPreferencias.add("terror");
        listaPreferencias.add("comedia");
        listaPreferencias.add("animado");

        preferencias.put("accion", false);
        preferencias.put("aventura", false);
        preferencias.put("drama", false);
        preferencias.put("sci-Fi", false);
        preferencias.put("fantasia", false);
        preferencias.put("terror", false);
        preferencias.put("comedia", false);
        preferencias.put("animado", false);

    }

    public static void crearPreferencias (String usuario) {
        db = FirebaseFirestore.getInstance();
        db.collection("Preferencias").document(usuario).set(preferencias)
                .addOnSuccessListener(documentReference -> {

                });

    }

    public static void vaciarPreferencias() {
        listaPreferencias = new ArrayList<>();
        listaPreferencias.add("accion");
        listaPreferencias.add("aventura");
        listaPreferencias.add("drama");
        listaPreferencias.add("sci-Fi");
        listaPreferencias.add("fantasia");
        listaPreferencias.add("terror");
        listaPreferencias.add("comedia");
        listaPreferencias.add("animado");

        preferencias = new HashMap<>();
        preferencias.put("accion", false);
        preferencias.put("aventura", false);
        preferencias.put("drama", false);
        preferencias.put("sci-Fi", false);
        preferencias.put("fantasia", false);
        preferencias.put("terror", false);
        preferencias.put("comedia", false);
        preferencias.put("animado", false);

    }

    public static ArrayList<String> getListaPreferencias() {
        return listaPreferencias;
    }

    public static HashMap<String, Boolean> getPreferencias() {
        return preferencias;
    }
}

