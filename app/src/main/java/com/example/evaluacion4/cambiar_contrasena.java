package com.example.evaluacion4;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class cambiar_contrasena extends AppCompatActivity {

    private EditText etNuevaContrasena, etConfirmarContrasena;
    private Button btnCambiarContrasena, btnVolver;
    private AdministradorBaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        db = new AdministradorBaseDatos(this);
        REFERENCIAS();
        configurarBotones();
    }
    private void REFERENCIAS(){
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);
        btnVolver = findViewById(R.id.btnVolver);

    };
    private void configurarBotones() {
        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaContrasena = etNuevaContrasena.getText().toString();
                String confirmarContrasena = etConfirmarContrasena.getText().toString();

                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    etConfirmarContrasena.setError("Las contraseñas no coinciden");
                    return;
                }

                String correo = getIntent().getStringExtra("correo");
                SQLiteDatabase database = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("contrasena", nuevaContrasena);

                int rows = database.update("usuarios", values, "correo = ?", new String[]{correo});
                database.close();

                if (rows > 0) {
                    Toast.makeText(cambiar_contrasena.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(cambiar_contrasena.this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    };
}
