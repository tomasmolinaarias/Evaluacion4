package com.example.evaluacion4;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarEvento extends AppCompatActivity {

    private EditText etTituloEvento, etFechaEvento, etImportanciaEvento, etObservacionEvento, etLugarEvento, etTiempoAvisoEvento;
    private Button btnGuardarEvento, btnVolver;
    private AdministradorBaseDatos db;
    private int usuarioId; // Declaramos usuarioId como una variable de instancia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_evento);
        db = new AdministradorBaseDatos(this);

        // Obtenemos usuarioId y verificamos que sea válido
        usuarioId = getIntent().getIntExtra("userId", -1);
        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            Log.e("USER_ERROR", "UsuarioId inválido: " + usuarioId);
            finish();
            return;
        }

        REFERENCIAS();
        CONFIGURAR_BOTONES(); // No necesitamos pasar usuarioId aquí, ya es una variable de instancia
    }

    private void REFERENCIAS() {
        etTituloEvento = findViewById(R.id.etTituloEvento);
        etFechaEvento = findViewById(R.id.etFechaEvento);
        etImportanciaEvento = findViewById(R.id.etImportanciaEvento);
        etObservacionEvento = findViewById(R.id.etObservacionEvento);
        etLugarEvento = findViewById(R.id.etLugarEvento);
        etTiempoAvisoEvento = findViewById(R.id.etTiempoAvisoEvento);
        btnGuardarEvento = findViewById(R.id.btnGuardarEvento);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void CONFIGURAR_BOTONES() {
        btnGuardarEvento.setOnClickListener(v -> {
            String titulo = etTituloEvento.getText().toString();
            String fecha = etFechaEvento.getText().toString();
            String importanciaStr = etImportanciaEvento.getText().toString();
            String observacion = etObservacionEvento.getText().toString();
            String lugar = etLugarEvento.getText().toString();
            String tiempoAvisoStr = etTiempoAvisoEvento.getText().toString();

            if (titulo.isEmpty() || fecha.isEmpty() || importanciaStr.isEmpty() || tiempoAvisoStr.isEmpty()) {
                Toast.makeText(AgregarEvento.this, "Por favor, complete los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int importancia = Integer.parseInt(importanciaStr);
            if (importancia < 1 || importancia > 10) {
                etImportanciaEvento.setError("La importancia debe estar entre 1 y 10");
                return;
            }

            int tiempoAviso = Integer.parseInt(tiempoAvisoStr);

            Evento evento = new Evento(titulo, fecha, importancia, observacion, lugar, tiempoAviso);

            Log.d("EVENTO_CREACION", "Datos del evento: Título=" + titulo + ", Fecha=" + fecha +
                    ", Importancia=" + importancia + ", Observación=" + observacion +
                    ", Lugar=" + lugar + ", TiempoAviso=" + tiempoAviso + ", UsuarioId=" + usuarioId);

            if (db.insertarEvento(evento, usuarioId)) {
                Log.d("EVENTO_CREACION", "Evento guardado correctamente");
                Toast.makeText(this, "Evento guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Log.e("EVENTO_CREACION", "Error al guardar el evento");
                Toast.makeText(this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
