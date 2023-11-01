package com.example.censimenti;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;

public class AggiungiUtente extends AppCompatActivity {

    TextInputEditText utenteNome, utenteCognome, utenteMail, utentePassword;
    Button salva, indietro;

    LinearLayout layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_utente);
        DatabaseReference refUtente = FirebaseDatabase.getInstance().getReference("utenti");

        layout = findViewById(R.id.addingLinearLayout);
        utenteNome = findViewById(R.id.utenteNome);
        utenteCognome = findViewById(R.id.utenteCognome);
        utenteMail = findViewById(R.id.utenteEmail);
        utentePassword = findViewById(R.id.utentePass);
        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utente utente = new Utente(utenteNome.getText().toString(),
                        utenteCognome.getText().toString(),
                        utenteMail.getText().toString(),
                        utentePassword.getText().toString());
                // salvo l'utente nel db firebase


                refUtente.push().setValue(utente);
                Toast.makeText(getApplicationContext(), "Nuovo utente aggiunto", Toast.LENGTH_SHORT).show();


            }
        });


    }

}
