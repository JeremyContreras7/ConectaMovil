package com.Conectamovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, nameEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        mAuth = FirebaseAuth.getInstance();
    }

        public void goBack(View view) {
            // Este método se llama cuando se hace clic en el botón "Atrás"
            finish(); // Cerrar la actividad actual y volver a la actividad anterior
        }




    public void registerUser(View view) {
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        final String name = nameEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                            // Guarda el correo y el nombre en la base de datos de Firebase
                            saveDataToDatabase(email, name);

                        } else {
                            // Error en el registro
                            Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                            if (task.getException() != null) {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error en el registro: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void saveDataToDatabase(String email, String name) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(userId);

        UserData userData = new UserData(email, name);

        databaseReference.setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Datos guardados en la base de datos", Toast.LENGTH_SHORT).show();

                            // Iniciar la ProfileActivity y pasar el nombre como extra
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("USERNAME", name);
                            startActivity(intent);
                            finish(); // Esto evita que el usuario pueda volver atrás a esta actividad.
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error al guardar datos en la base de datos", Toast.LENGTH_SHORT).show();
                            if (task.getException() != null) {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
