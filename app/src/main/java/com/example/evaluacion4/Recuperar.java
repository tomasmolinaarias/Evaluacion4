package com.example.evaluacion4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Recuperar extends AppCompatActivity {

    private EditText etCorreoRecuperar, etRespuestaRecuperar;
    private TextView tvPreguntaRecuperar;
    private Button btnVerificarRespuesta, btnVolver,btnObtenerPregunta;
    private AdministradorBaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        db = new AdministradorBaseDatos(this);
        REFERENCIAS();
        configurarBotones();
    }
    private void REFERENCIAS(){
        etCorreoRecuperar = findViewById(R.id.etCorreoRecuperar);
        etRespuestaRecuperar = findViewById(R.id.etRespuestaRecuperar);
        tvPreguntaRecuperar = findViewById(R.id.tvPreguntaRecuperar);
        btnVerificarRespuesta = findViewById(R.id.btnVerificarRespuesta);
        btnObtenerPregunta = findViewById(R.id.btnObtenerPregunta);
        btnVolver = findViewById(R.id.btnVolver);

    };
    private void configurarBotones() {
        btnObtenerPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreoRecuperar.getText().toString();
                SQLiteDatabase database = db.getReadableDatabase();
                Cursor cursor = database.rawQuery(
                        "SELECT p.pregunta FROM usuarios u " +
                                "INNER JOIN preguntas p ON u.pregunta_id = p.id WHERE u.correo = ?",
                        new String[]{correo});

                if (cursor.moveToFirst()) {
                    String pregunta = cursor.getString(0);
                    tvPreguntaRecuperar.setText(pregunta);
                } else {
                    Toast.makeText(Recuperar.this, "Correo no encontrado", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
                database.close();
            }
        });

        btnVerificarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreoRecuperar.getText().toString();
                String respuesta = etRespuestaRecuperar.getText().toString();

                SQLiteDatabase database = db.getReadableDatabase();
                Cursor cursor = database.rawQuery(
                        "SELECT u.respuesta FROM usuarios u WHERE u.correo = ?",
                        new String[]{correo});

                if (cursor.moveToFirst()) {
                    String respuestaCorrecta = cursor.getString(0);

                    if (respuesta.equals(respuestaCorrecta)) {
                        Intent intent = new Intent(Recuperar.this, cambiar_contrasena.class);
                        intent.putExtra("correo", correo);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Recuperar.this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Recuperar.this, "Correo no encontrado", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
                database.close();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recuperar.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    };
}
