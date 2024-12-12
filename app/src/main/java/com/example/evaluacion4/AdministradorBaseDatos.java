package com.example.evaluacion4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class AdministradorBaseDatos extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MisEventos.db";
    private static final int DATABASE_VERSION = 1;

    public AdministradorBaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE preguntas (id INTEGER PRIMARY KEY AUTOINCREMENT, pregunta TEXT NOT NULL)");
        db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT UNIQUE NOT NULL, contrasena TEXT NOT NULL, pregunta_id INTEGER NOT NULL, respuesta TEXT NOT NULL, FOREIGN KEY(pregunta_id) REFERENCES preguntas(id))");
        db.execSQL("CREATE TABLE eventos (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT NOT NULL, fecha TEXT NOT NULL, importancia INTEGER NOT NULL, observacion TEXT, lugar TEXT, tiempo_aviso INTEGER NOT NULL, usuario_id INTEGER NOT NULL, FOREIGN KEY(usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE)");

        // Preguntas predefinidas
        db.execSQL("INSERT INTO preguntas (pregunta) VALUES " +
                "('¿Cuál es el nombre de tu primera escuela?')," +
                "('¿En qué ciudad naciste?')," +
                "('¿Cuál es tu comida favorita?')," +
                "('¿Cómo se llama tu mejor amigo de la infancia?')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS eventos");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS preguntas");
        onCreate(db);
    }

    // Método para obtener las preguntas de seguridad
    public ArrayList<Object[]> getPreguntas() {
        ArrayList<Object[]> preguntas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, pregunta FROM preguntas", null);

        if (cursor.moveToFirst()) {
            do {
                preguntas.add(new Object[]{cursor.getInt(0), cursor.getString(1)});
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return preguntas;
    }

    // Método para insertar un usuario en la base de datos
    public boolean insertarUsuario(Usuarios usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("correo", usuario.getCorreo());
        values.put("contrasena", usuario.getContrasena());
        values.put("pregunta_id", usuario.getPreguntaId());
        values.put("respuesta", usuario.getRespuesta());

        long result = db.insert("usuarios", null, values);
        db.close();

        return result != -1; // Devuelve true si la inserción fue exitosa
    }
    public ArrayList<String> getEventosTitles() {
        ArrayList<String> eventos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT titulo FROM eventos", null);

        if (cursor.moveToFirst()) {
            do {
                eventos.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return eventos;
    }
    public void eliminarEventoPorTitulo(String titulo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("eventos", "titulo = ?", new String[]{titulo});
        db.close();
    }
    public boolean insertarEvento(Evento evento, int usuarioId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Log para verificar si el usuario existe
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE id = ?", new String[]{String.valueOf(usuarioId)});
        if (!cursor.moveToFirst()) {
            Log.e("DB_ERROR", "Usuario no encontrado con ID: " + usuarioId);
            cursor.close();
            db.close();
            return false;
        }
        cursor.close();

        // Log para los datos del evento
        Log.d("DB_INSERT", "Insertando evento: " +
                "Título=" + evento.getTitulo() + ", Fecha=" + evento.getFecha() +
                ", Importancia=" + evento.getImportancia() + ", Observación=" + evento.getObservacion() +
                ", Lugar=" + evento.getLugar() + ", TiempoAviso=" + evento.getTiempoAviso() +
                ", UsuarioId=" + usuarioId);

        ContentValues values = new ContentValues();
        values.put("titulo", evento.getTitulo());
        values.put("fecha", evento.getFecha());
        values.put("importancia", evento.getImportancia());
        values.put("observacion", evento.getObservacion());
        values.put("lugar", evento.getLugar());
        values.put("tiempo_aviso", evento.getTiempoAviso());
        values.put("usuario_id", usuarioId);

        long result = db.insert("eventos", null, values);

        if (result == -1) {
            Log.e("DB_ERROR", "Error al insertar el evento en la base de datos");
        } else {
            Log.d("DB_INSERT", "Evento insertado con éxito, ID=" + result);
        }

        db.close();
        return result != -1;
    }

}
