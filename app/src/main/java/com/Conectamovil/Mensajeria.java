package com.Conectamovil;

public class Mensajeria {

    private String nombreMensaje;
    private String tipoMensaje;
    private String contenido;
    private String timestamp;  // Agregué un campo para la marca de tiempo

    public Mensajeria(String nombreMensaje, String tipoMensaje, String contenido, String timestamp) {
        this.nombreMensaje = nombreMensaje;
        this.tipoMensaje = tipoMensaje;
        this.contenido = contenido;
        this.timestamp = timestamp;
    }

    public String getNombreMensaje() {
        return nombreMensaje;
    }

    public void setNombreMensaje(String nombreMensaje) {
        this.nombreMensaje = nombreMensaje;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Mensajeria{" +
                "nombreMensaje='" + nombreMensaje + '\'' +
                ", tipoMensaje='" + tipoMensaje + '\'' +
                ", contenido='" + contenido + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
