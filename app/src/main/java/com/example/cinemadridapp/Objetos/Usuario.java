package com.example.cinemadridapp.Objetos;

public class Usuario {

    private static String nombreUsuario;

    private static String contraseña;

    private static String correo;

    private static String telefono;
    private static String fechaCreacion;

    public static void vaciarUsuario() {
        nombreUsuario = "";
        contraseña = "";
        correo = "";
        telefono = "";
        fechaCreacion = "";
    }



    public static void setNombreUsuario(String nombreUsuario) {
        Usuario.nombreUsuario = nombreUsuario;
    }

    public static void setContraseña(String contraseña) {
        Usuario.contraseña = contraseña;
    }

    public static void setCorreo(String correo) {
        Usuario.correo = correo;
    }

    public static void setTelefono(String telefono) {
        Usuario.telefono = telefono;
    }

    public static void setFechaCreacion(String fechaCreacion) {
        Usuario.fechaCreacion = fechaCreacion;
    }

    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static String getContraseña() {
        return contraseña;
    }

    public static String getCorreo() {
        return correo;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static String getFechaCreacion() {
        return fechaCreacion;
    }
}
