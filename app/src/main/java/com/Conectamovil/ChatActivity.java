package com.Conectamovil;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.Conectamovil.Mensajeria;
import com.Conectamovil.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "android_chat";
    private MqttHandler mqttHandler;
    private EditText editTextPublish;
    private EditText editTextSubscribe;

    private EditText txtId, txtNombreMensaje, txttipoMensaje, txtContenido, txttimestamp;
    private Button btnGuardar, btnEliminar, btnModificar;
    private DatabaseReference databaseReference;

    private List<Mensajeria> chatsList;
    private ChatsAdapter chatsAdapter;
    private ListView listViewChats;

    Button btnConnect;
    Button btnPublish;
    Button btnSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnConnect = findViewById(R.id.btnConnect);
        btnPublish = findViewById(R.id.btnPublish);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        editTextPublish = findViewById(R.id.editTextPublish);
        editTextSubscribe = findViewById(R.id.editTextSubscribe);
        txtId = findViewById(R.id.txtid);
        txtNombreMensaje = findViewById(R.id.txtnombreMensaje);
        btnGuardar = findViewById(R.id.btnRegistrar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        txttipoMensaje = findViewById(R.id.txttipoMensaje);
        txtContenido = findViewById(R.id.txtContenido);
        txttimestamp = findViewById(R.id.txttimestamp);

        Button btnRegresarContacto = findViewById(R.id.btnRegresarContacto);

        btnRegresarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí inicia la actividad de perfil
                Intent intent = new Intent(getApplicationContext(), ContactoActivity.class);
                startActivity(intent);
            }
        });

        // Obtén la referencia a la instancia de la base de datos de Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Mensajeria");

        chatsList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(this, chatsList);
        // Asegúrate de tener un ListView en tu layout con el id correspondiente

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarDatos();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarDatos();
            }
        });

    }

    private void guardarDatos() {
        String id = txtId.getText().toString().trim();
        String nombreMensaje = txtNombreMensaje.getText().toString().trim();
        String tipoMensaje = txttipoMensaje.getText().toString().trim();
        String Contenido = txtContenido.getText().toString().trim();
        String timestamp = txttimestamp.getText().toString().trim();

        if (!id.isEmpty() && !nombreMensaje.isEmpty() && !tipoMensaje.isEmpty() && !Contenido.isEmpty() && !timestamp.isEmpty()) {
            // Crea un nuevo nodo en la base de datos con el ID como clave
            DatabaseReference chatsRef = databaseReference.child(id);

            chatsRef.child("nombreMensaje").setValue(nombreMensaje);
            chatsRef.child("tipoMensaje").setValue(tipoMensaje);
            chatsRef.child("Contenido").setValue(Contenido);
            chatsRef.child("timestamp").setValue(timestamp);

            Toast.makeText(this, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarDatos() {
        String id = txtId.getText().toString().trim();

        if (!id.isEmpty()) {
            // Elimina el nodo en la base de datos con el ID como clave
            databaseReference.child(id).removeValue();
            Toast.makeText(this, "Datos eliminados en Firebase", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Completa el campo ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void modificarDatos() {
        String id = txtId.getText().toString().trim();
        String nuevoNombre = txtNombreMensaje.getText().toString().trim();

        if (!id.isEmpty() && !nuevoNombre.isEmpty()) {
            // Elimina el nodo existente en la base de datos con el ID como clave
            databaseReference.child(id).removeValue();

            // Crea un nuevo nodo en la base de datos con el ID como clave y el nuevo nombre
            databaseReference.child(id).child("Nombre").setValue(nuevoNombre);

            Toast.makeText(this, "Datos modificados en Firebase", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombreMensaje.setText("");
        txttipoMensaje.setText("");
        txtContenido.setText("");
        txttimestamp.setText("");




        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Conectar al broker MQTT
                mqttHandler.connect(BROKER_URL, CLIENT_ID);
                showToast("Conectado al broker");
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Publicar un mensaje
                String message = editTextPublish.getText().toString();
                String publishTopic = "node/jeremy"; // Reemplaza con tu tema desead
                publishMessage(publishTopic, message);

                // Actualizar el EditText de suscripción con el mensaje publicado
                editTextSubscribe.setText(message);
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Suscribirse a un tema
                String topic = editTextSubscribe.getText().toString();
                subscribeToTopic(topic);
            }
        });

    }

    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();
    }

    private void publishMessage(String topic, String message) {
        Toast.makeText(this, "Publicando mensaje: " + message, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic, message);
    }

    private void subscribeToTopic(String topic) {
        Toast.makeText(this, "Suscrito al tema " + topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Manejar el mensaje recibido
        String receivedMessage = new String(message.getPayload());
        showToast("Mensaje recibido: " + receivedMessage);

        // Puedes abrir la nueva actividad aquí y pasar el mensaje como extra
        Intent intent = new Intent(ChatActivity.this, ChatReceiverActivity.class);
        intent.putExtra("MESSAGE_KEY", receivedMessage);
        startActivity(intent);
    }


}
