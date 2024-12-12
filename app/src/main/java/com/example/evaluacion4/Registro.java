package com.example.evaluacion4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Registro extends AppCompatActivity {

    private Spinner spPregunta;
    private EditText etCorreoRegistro, etContrasenaRegistro, etConfirmarContrasena, etRespuesta;
    private Button btnRegistrar, btnVolver;
    private AdministradorBaseDatos db;
    private ArrayList<Integer> preguntaIds; // Lista para almacenar los IDs de las preguntas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        db = new AdministradorBaseDatos(this);
        REFERENCIAS();
        Select_Preguntas();
        CONFIGURAR_BOTONES();
    }

    private void REFERENCIAS() {
        spPregunta = findViewById(R.id.spPregunta);
        etCorreoRegistro = findViewById(R.id.etCorreoRegistro);
        etContrasenaRegistro = findViewById(R.id.etContrasenaRegistro);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        etRespuesta = findViewById(R.id.etRespuesta);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVolver = findViewById(R.id.btnVolver);
    }

    private void Select_Preguntas() {
        preguntaIds = new ArrayList<>();
        ArrayList<String> preguntasTextos = new ArrayList<>();
        ArrayList<Object[]> preguntasData = db.getPreguntas(); // Obtener IDs y textos

        for (Object[] pregunta : preguntasData) {
            preguntaIds.add((Integer) pregunta[0]); // Almacenar el ID
            preguntasTextos.add((String) pregunta[1]); // Almacenar el texto
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, preguntasTextos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPregunta.setAdapter(adapter);
    }

    private void CONFIGURAR_BOTONES() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = etCorreoRegistro.getText().toString();
                String contrasena = etContrasenaRegistro.getText().toString();
                String confirmarContrasena = etConfirmarContrasena.getText().toString();
                String respuesta = etRespuesta.getText().toString();

                if (!contrasena.equals(confirmarContrasena)) {
                    etConfirmarContrasena.setError("Las contraseñas no coinciden");
                    return;
                }

                int preguntaId = preguntaIds.get(spPregunta.getSelectedItemPosition());

                Usuarios nuevoUsuario = new Usuarios(correo, contrasena, preguntaId, respuesta);

                if (db.insertarUsuario(nuevoUsuario)) {
                    Toast.makeText(Registro.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registro.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Registro.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registro.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
