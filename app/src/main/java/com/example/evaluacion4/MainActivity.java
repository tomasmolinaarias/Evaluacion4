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

public class MainActivity extends AppCompatActivity {
    private TextView teRegistrarse, teRecuperarContrasena;
    private Button btnInicio;
    private EditText etCorreo, etContrasena;
    AdministradorBaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new AdministradorBaseDatos(this);

        REFERENCIAS();
        REDIRECCION();
        CONFIGURAR_BOTONES();
    }

    private void REFERENCIAS() {
        teRegistrarse = findViewById(R.id.teRegistrarse);
        teRecuperarContrasena = findViewById(R.id.teRecuperarContrasena);
        btnInicio = findViewById(R.id.btnInicio);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
    }

    private void REDIRECCION() {
        teRegistrarse.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });

        teRecuperarContrasena.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Recuperar.class);
            startActivity(intent);
        });
    }

    private void CONFIGURAR_BOTONES() {
        btnInicio.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString();
            String contrasena = etContrasena.getText().toString();

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase database = db.getReadableDatabase();
            Cursor cursor = database.rawQuery(
                    "SELECT id FROM usuarios WHERE correo = ? AND contrasena = ?",
                    new String[]{correo, contrasena});

            if (cursor.moveToFirst()) {
                int userId = cursor.getInt(0); // Obtenemos el ID del usuario
                Intent intent = new Intent(MainActivity.this, EventosActivity.class);
                intent.putExtra("userId", userId); // Enviamos el ID a la siguiente actividad
                startActivity(intent);
                finish(); // Cerramos la actividad actual
            } else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            database.close();
        });
    }
}
