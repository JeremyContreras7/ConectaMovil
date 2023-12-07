package com.Conectamovil;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class ContactoActivity extends AppCompatActivity {

    private EditText newContactEditText, searchEditText;
    private Button addContactButton;
    private ListView contactListView;

    private DatabaseReference contactsRef;
    private ArrayList<String> contactList;
    private ArrayAdapter<String> contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        newContactEditText = findViewById(R.id.newContactEditText);
        addContactButton = findViewById(R.id.addContactButton);
        contactListView = findViewById(R.id.contactListView);
        searchEditText = findViewById(R.id.searchEditText);

        contactsRef = FirebaseDatabase.getInstance().getReference("contacts");
        contactList = new ArrayList<>();
        contactAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactList);
        contactListView.setAdapter(contactAdapter);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No se necesita implementar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterContacts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No se necesita implementar
            }
        });

        loadContacts();
    }

    private void addContact() {
        // (Método addContact)

    }

    private void loadContacts() {
        contactsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contactList.clear();

                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    String contact = contactSnapshot.getValue(String.class);
                    contactList.add(contact);
                }

                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ContactoActivity.this, "Error al cargar contactos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterContacts(String query) {
        if (query.isEmpty()) {
            // Si la consulta está vacía, carga todos los contactos
            loadContacts();
        } else {
            ArrayList<String> filteredContacts = new ArrayList<>();

            for (String contact : contactList) {
                if (contact.toLowerCase().contains(query.toLowerCase())) {
                    filteredContacts.add(contact);
                }
            }

            // Borra la lista actual y agrega los contactos filtrados
            contactAdapter.clear();
            contactAdapter.addAll(filteredContacts);
            contactAdapter.notifyDataSetChanged();
        }
    }
}
