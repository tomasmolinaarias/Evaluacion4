package com.example.evaluacion4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity {

    private Button btnAgregarEvento, btnCerrarSesion;
    private ListView lvEventos;
    private ArrayList<String> eventosList; // Usaremos títulos como ejemplo.
    private ArrayAdapter<String> adapter;
    private AdministradorBaseDatos db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        btnAgregarEvento = findViewById(R.id.btnAgregarEvento);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        lvEventos = findViewById(R.id.lvEventos);
        db = new AdministradorBaseDatos(this);

        int usuarioId = getIntent().getIntExtra("userId", -1);
        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        eventosList = db.getEventosTitles();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventosList);
        lvEventos.setAdapter(adapter);

        btnAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventosActivity.this, AgregarEvento.class);
                intent.putExtra("userId", usuarioId);
                startActivity(intent);
            }
        });
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion() {
        Intent intent = new Intent(EventosActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia la pila de actividades
        startActivity(intent);
        finish(); // Finaliza la actividad actual
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }

}
