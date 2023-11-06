package com.example.censimenti.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.censimenti.ComuniActivity;
import com.example.censimenti.R;
import com.example.censimenti.Utenti.AggiungiUtente;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText utenteMail, utentePassword;
    Button login, registrati;

    LinearLayout layout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        layout = findViewById(R.id.addingLinearLayout);
        utenteMail = findViewById(R.id.utenteEmail);
        utentePassword = findViewById(R.id.utentePass);
        login = findViewById(R.id.loginBtn);
        registrati = findViewById(R.id.addUtenteBtn);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference refUtente = FirebaseDatabase.getInstance().getReference("utenti");
                refUtente.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            if (dataSnapshot.child("mail").getValue().equals(utenteMail.getText().toString()) &&
                                    dataSnapshot.child("password").getValue().equals(utentePassword.getText().toString())) {
                                //passo all'activiti comuni
                                Intent intent = new Intent(LoginActivity.this, ComuniActivity.class);
                                startActivity(intent);


                            } else {
                                //messaggio nome utente o password non corretti
                                Toast.makeText(getApplicationContext(), "Nome utente o Password non corretti", Toast.LENGTH_SHORT).show();
                                utenteMail.setText("");
                                utentePassword.setText("");
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Log.d("sdasd", " fdsfs");

            }
        });


        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AggiungiUtente.class);
                startActivity(intent);
            }
        });
    }


}
