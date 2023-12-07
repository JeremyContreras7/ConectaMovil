package com.Conectamovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.Conectamovil.ProfileActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btnChat = findViewById(R.id.btnChat);
        Button btnContacto = findViewById(R.id.btnContacto);
        Button btnPerfil = findViewById(R.id.btnPerfil);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad ChatActivity
                Intent intent = new Intent(MainActivity2.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        btnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar ContactoActivity al hacer clic en el botón
                Intent intent = new Intent(MainActivity2.this, ContactoActivity.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de perfil al hacer clic en el botón
                Intent intent = new Intent(MainActivity2.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
    }

