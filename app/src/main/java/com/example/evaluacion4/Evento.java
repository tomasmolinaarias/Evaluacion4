package com.example.evaluacion4;

public class Evento {
    private String titulo, fecha, observacion, lugar;
    private int importancia, tiempoAviso;

    public Evento(String titulo, String fecha, int importancia, String observacion, String lugar, int tiempoAviso) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.importancia = importancia;
        this.observacion = observacion;
        this.lugar = lugar;
        this.tiempoAviso = tiempoAviso;
    }

    public String getTitulo() { return titulo; }
    public String getFecha() { return fecha; }
    public int getImportancia() { return importancia; }
    public String getObservacion() { return observacion; }
    public String getLugar() { return lugar; }
    public int getTiempoAviso() { return tiempoAviso; }
}
