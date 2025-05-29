package com.example.cinemadridapp.Objetos;

import java.util.ArrayList;
import java.util.HashMap;

public class Preferencia {

    private static ArrayList<String> listaPreferencias = new ArrayList<>();

    private static HashMap<String, Boolean> preferencias = new HashMap<>();

    static {
        listaPreferencias.add("accion");
        listaPreferencias.add("aventura");
        listaPreferencias.add("drama");
        listaPreferencias.add("sciFi");
        listaPreferencias.add("fantasia");
        listaPreferencias.add("terror");
        listaPreferencias.add("comedia");
        listaPreferencias.add("animado");

        preferencias.put("accion", false);
        preferencias.put("aventura", false);
        preferencias.put("drama", false);
        preferencias.put("sciFi", false);
        preferencias.put("fantasia", false);
        preferencias.put("terror", false);
        preferencias.put("comedia", false);
        preferencias.put("animado", false);

    }

    public static void vaciarPreferencias() {
        listaPreferencias = new ArrayList<>();
        listaPreferencias.add("accion");
        listaPreferencias.add("aventura");
        listaPreferencias.add("drama");
        listaPreferencias.add("sciFi");
        listaPreferencias.add("fantasia");
        listaPreferencias.add("terror");
        listaPreferencias.add("comedia");
        listaPreferencias.add("animado");

        preferencias = new HashMap<>();
        preferencias.put("accion", false);
        preferencias.put("aventura", false);
        preferencias.put("drama", false);
        preferencias.put("sciFi", false);
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

