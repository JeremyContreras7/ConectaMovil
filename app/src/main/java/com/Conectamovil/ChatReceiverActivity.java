package com.Conectamovil;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.Conectamovil.R;

public class ChatReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_receiver);

        // Aquí puedes obtener el mensaje recibido desde MQTT y mostrarlo en un TextView
        TextView receivedMessageTextView = findViewById(R.id.receivedMessageTextView);

        // Obtén el mensaje del intent
        String receivedMessage = getIntent().getStringExtra("MESSAGE_KEY");

        // Muestra el mensaje
        receivedMessageTextView.setText(receivedMessage);
    }
}
