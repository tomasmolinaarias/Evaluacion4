package com.example.evaluacion4;

public class Usuarios {
    private String correo;
    private String contrasena;
    private int preguntaId;
    private String respuesta;

    public Usuarios(String correo, String contrasena, int preguntaId, String respuesta) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.preguntaId = preguntaId;
        this.respuesta = respuesta;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public int getPreguntaId() {
        return preguntaId;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
