package com.example.evaluacion4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EventosActivity extends AppCompatActivity {

    private Button btnAgregarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        btnAgregarEvento = findViewById(R.id.btnAgregarEvento);

    }
}
