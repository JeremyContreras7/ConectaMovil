package com.Conectamovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String USERNAME_KEY = "username";
    private static final String IMAGE_URL_KEY = "image_url";

    private ImageView profileImageView;
    private Button changePhotoButton;
    private EditText usernameEditText;
    private Button saveProfileButton;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        profileImageView = findViewById(R.id.profileImageView);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        saveProfileButton = findViewById(R.id.saveProfileButton);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No hay usuario autenticado, redirigir a la actividad de inicio de sesión
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Obtener la referencia del usuario actual en la base de datos
        userReference = FirebaseDatabase.getInstance().getReference("usuarios").child(currentUser.getUid());

        // Escuchar cambios en los datos del usuario
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Obtener datos del usuario
                String username = dataSnapshot.child("username").getValue(String.class);
                String imageUrl = dataSnapshot.child("image_url").getValue(String.class);

                // Mostrar datos del usuario en la interfaz
                usernameEditText.setText(username);

                // Mostrar la imagen usando Picasso
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de datos
                Toast.makeText(ProfileActivity.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });

        changePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = usernameEditText.getText().toString();

                if (newUsername.isEmpty()) {
                    return;
                }

                // Actualizar el nombre de usuario en la base de datos
                userReference.child("username").setValue(newUsername)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this, "Nombre de usuario actualizado con éxito", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Error al actualizar el nombre de usuario", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebaseStorage(imageUri);
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference imagesRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

        imagesRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Mostrar la imagen usando Picasso
                        Picasso.get().load(imageUrl).into(profileImageView);
                        // Guardar la URL de la imagen en SharedPreferences
                        userReference.child("image_url").setValue(imageUrl);  // Actualizar la URL de la imagen en la base de datos
                    });
                })
                .addOnFailureListener(e -> {
                    // Manejar errores durante la carga de la imagen.
                    Toast.makeText(ProfileActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                });
    }
}
