package com.example.cinemadridapp.Objetos;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Estadisticas {
    private static FirebaseFirestore db;

    private static String rangoCuenta;
    private static int nivelCuenta;
    private static int numPeliculasVistas;
    private static int numPeliculasReseñadas;
    private static double notaMedia;
    private static String directorFavorito;
    private static String generoFavorito;
    private static int totalMinutosVistos;
    private static double duracionMediaPeliculas;

    private static ArrayList<String> distribucionGeneros;

    public static void crearEstadisticas(String usuario) {
        db = FirebaseFirestore.getInstance();

        HashMap<String, Object> mapaEstadisticas = new HashMap<>();
        mapaEstadisticas.put("rangoCuenta", "Bronce");
        mapaEstadisticas.put("nivelCuenta", 0);
        mapaEstadisticas.put("numPeliculasVistas", 0);
        mapaEstadisticas.put("numPeliculasReseñadas", 0);
        mapaEstadisticas.put("notaMedia", 0.0);
        mapaEstadisticas.put("directorFavorito", "");
        mapaEstadisticas.put("generoFavorito", "");
        mapaEstadisticas.put("totalMinutosVistos", 0);
        mapaEstadisticas.put("duracionMediaPeliculas", 0.0);


        db.collection("Estadisticas").document(usuario).set(mapaEstadisticas);

    }


    public static void setRangoCuenta(String rangoCuenta) {
        Estadisticas.rangoCuenta = rangoCuenta;
    }

    public static void setNivelCuenta(int nivelCuenta) {
        Estadisticas.nivelCuenta = nivelCuenta;
    }

    public static void setNumPeliculasVistas(int numPeliculasVistas) {
        Estadisticas.numPeliculasVistas = numPeliculasVistas;
    }

    public static void setNumPeliculasReseñadas(int numPeliculasReseñadas) {
        Estadisticas.numPeliculasReseñadas = numPeliculasReseñadas;
    }

    public static void setNotaMedia(double notaMedia) {
        Estadisticas.notaMedia = notaMedia;
    }

    public static void setDirectorFavorito(String directorFavorito) {
        Estadisticas.directorFavorito = directorFavorito;
    }

    public static void setGeneroFavorito(String generoFavorito) {
        Estadisticas.generoFavorito = generoFavorito;
    }

    public static void setTotalMinutosVistos(int totalMinutosVistos) {
        Estadisticas.totalMinutosVistos = totalMinutosVistos;
    }

    public static void setDuracionMediaPeliculas(double duracionMediaPeliculas) {
        Estadisticas.duracionMediaPeliculas = duracionMediaPeliculas;
    }

    public static void setDistribucionGeneros(ArrayList<String> distribucionGeneros) {
        Estadisticas.distribucionGeneros = distribucionGeneros;
    }

    public static String getRangoCuenta() {
        return rangoCuenta;
    }

    public static int getNivelCuenta() {
        return nivelCuenta;
    }

    public static int getNumPeliculasVistas() {
        return numPeliculasVistas;
    }

    public static int getNumPeliculasReseñadas() {
        return numPeliculasReseñadas;
    }

    public static double getNotaMedia() {
        return notaMedia;
    }

    public static String getGeneroFavorito() {
        return generoFavorito;
    }

    public static String getDirectorFavorito() {
        return directorFavorito;
    }

    public static int getTotalMinutosVistos() {
        return totalMinutosVistos;
    }

    public static double getDuracionMediaPeliculas() {
        return duracionMediaPeliculas;
    }

    public static ArrayList<String> getDistribucionGeneros() {
        return distribucionGeneros;
    }
}
