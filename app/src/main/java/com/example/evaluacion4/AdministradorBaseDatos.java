package com.example.evaluacion4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
