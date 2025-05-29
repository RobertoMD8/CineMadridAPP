package com.example.cinemadridapp.Objetos;

import java.util.ArrayList;

public class ExpedienteReseñaNota {
    public static ArrayList<ExpedienteReseñaNota> expedienteReseñaNotas = new ArrayList<>();
    private String usuario;
    private Pelicula pelicula;
    private String reseña;
    private double nota;
    private String fecha;
    private boolean existeReseña;
    private boolean existeNota;

    public ExpedienteReseñaNota() {

    };

    public ExpedienteReseñaNota(String usuario, Pelicula pelicula, String reseña, double nota, String fecha) {
        this.usuario = usuario;
        this.pelicula = pelicula;
        this.reseña = reseña;
        this.nota = nota;
        this.fecha = fecha;

        expedienteReseñaNotas.add(this);
    }

    public static void vaciarExpedientes() {
        expedienteReseñaNotas = new ArrayList<>();
    }

    public void setExpedienteReseñaNotas(ArrayList<ExpedienteReseñaNota> expedienteReseñaNotas) {
        this.expedienteReseñaNotas = expedienteReseñaNotas;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public void setReseña(String reseña) {
        this.reseña = reseña;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setExisteReseña(boolean existeReseña) {
        this.existeReseña = existeReseña;
    }

    public void setExisteNota(boolean existeNota) {
        this.existeNota = existeNota;
    }

    public ArrayList<ExpedienteReseñaNota> getExpedienteReseñaNotas() {
        return expedienteReseñaNotas;
    }

    public String getUsuario() {
        return usuario;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public String getReseña() {
        return reseña;
    }

    public double getNota() {
        return nota;
    }

    public String getFecha() {
        return fecha;
    }

    public boolean isExisteReseña() {
        return existeReseña;
    }

    public boolean isExisteNota() {
        return existeNota;
    }
}
