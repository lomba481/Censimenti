package com.example.censimenti;

import static com.example.censimenti.CensimentiInterni.refLocale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AggiungiLocale extends AppCompatActivity {
    TextInputEditText nomeLocale, note;
    RelativeLayout scegliImmagine;
    Button salvaBtn, indietroBtn;
    String keyLocale;
    float x, y;

    Uri uri;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_locale);

        nomeLocale = findViewById(R.id.locale);
        note = findViewById(R.id.noteLocale);
        salvaBtn = findViewById(R.id.saveBtnLocale);
        indietroBtn = findViewById(R.id.backBtnLocale);
        scegliImmagine = findViewById(R.id.ScegliImmagineLocale);
        imageView = findViewById(R.id.ImageViewLocale);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int larghezzaSchermo = size.x;
        int altezzaSchermo = size.y;

        keyLocale = getIntent().getStringExtra("keyLocale");
        x = getIntent().getFloatExtra("x", 0);
        y = getIntent().getFloatExtra("y", 0);

        float Kx = larghezzaSchermo / x;
        float Ky = altezzaSchermo / y;

        scegliImmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AggiungiLocale.this)
                        .cameraOnly()
                        .start();
            }
        });

        salvaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = new Locale(nomeLocale.getText().toString(),
                        note.getText().toString(),
                        null, Kx, Ky);
                refLocale.child(keyLocale).setValue(locale);
                refLocale.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Toast.makeText(AggiungiLocale.this, "Locale aggiunto con successo", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AggiungiLocale.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }
        });

        indietroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        imageView.setImageURI(uri);
    }
}
