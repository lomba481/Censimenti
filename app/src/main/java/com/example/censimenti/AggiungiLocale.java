package com.example.censimenti;

import static com.example.censimenti.AggiungiComune.storageC;
import static com.example.censimenti.CensimentiInterni.refLocale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AggiungiLocale extends AppCompatActivity {
    TextInputEditText nomeLocale, note;
    RelativeLayout scegliImmagine;
    Button salvaBtn, indietroBtn;
    String keyLocale, modifica, url;
    TextView textView;
    float x, y;
    ImageView precedente, successivo;
    CircularProgressIndicator progressIndicator;
    String input= null;
    Uri uri;
    List<Uri> uriList = new ArrayList<>();
   ViewFlipper viewFlipper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_locale);

        nomeLocale = findViewById(R.id.locale);
        note = findViewById(R.id.noteLocale);
        salvaBtn = findViewById(R.id.saveBtnLocale);
        indietroBtn = findViewById(R.id.backBtnLocale);
        scegliImmagine = findViewById(R.id.ScegliImmagineLocale);
        viewFlipper = findViewById(R.id.ImageViewLocale);
        textView = findViewById(R.id.textLocale);
        precedente = findViewById(R.id.precedente);
        successivo = findViewById(R.id.successivo);
        progressIndicator = findViewById(R.id.progressIndicatorLoc);



        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int larghezzaSchermo = size.x;
        int altezzaSchermo = size.y;

        keyLocale = getIntent().getStringExtra("keyLocale");
        modifica = getIntent().getStringExtra("modifica");
        if (modifica == null) {
            textView.setText("Aggiungi Locale");
        } else {
            textView.setText("Modifica Locale");

        }
        x = getIntent().getFloatExtra("x", 0);
        y = getIntent().getFloatExtra("y", 0);

        float Kx = larghezzaSchermo / x;
        float Ky = altezzaSchermo / y;

        refLocale.child(keyLocale).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("nome").getValue(String.class);
                    String note1 = snapshot.child("note").getValue(String.class);
                    url = snapshot.child("foto").getValue(String.class);

                    nomeLocale.setText(nome);
                    note.setText(note1);

                    if(url != null){

                        String[] stringheDivise = url.split(",");


                        // Stampa le stringhe risultanti
                        for (String s : stringheDivise) {

                            loadUrlAsDrawable(s, getApplicationContext(), new Icon.OnDrawableLoadedListener() {
                                @Override
                                public void onDrawableLoaded(Drawable d) {
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT));
                                    imageView.setImageDrawable(d);
                                    viewFlipper.addView(imageView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                             if (s != null) {
                                                Intent intent = new Intent(AggiungiLocale.this, VisualizzaFoto.class);
                                                intent.putExtra("url", s);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            });

                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        precedente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showPrevious();
            }
        });

        successivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
        scegliImmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AggiungiLocale.this)
                        .cameraOnly()
                        .maxResultSize(750,750)
                        .start();
            }
        });

        nomeLocale.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input = s.toString();
            }
        });

        salvaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!input.matches("P\\w-[0-9]{3}")) {
                    Toast.makeText(AggiungiLocale.this, "Scrivi il locale secondo la sintassi suggerita", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressIndicator.setVisibility(View.VISIBLE);
                    Locale locale = new Locale(nomeLocale.getText().toString(),
                            note.getText().toString(),
                            url, Kx, Ky);

                    try {
                        if (uri == null) {

                            refLocale.child(keyLocale).setValue(locale);
                            refLocale.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Toast.makeText(AggiungiLocale.this, "Locale aggiunto con successo", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AggiungiLocale.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            addDataFirebase(locale, uriList);
                        }

                    }catch (FileNotFoundException e) {
                        throw new RuntimeException(e);

                    }
                }
            }
        });

        indietroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void loadUrlAsDrawable(String url, Context context, final Icon.OnDrawableLoadedListener listener) {
        Glide.with(context)
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        listener.onDrawableLoaded(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();

        uriList.add(uri);
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageURI(uri);
        viewFlipper.addView(imageView);


    }

    private void addDataFirebase(Locale locale, List<Uri> uriList) throws FileNotFoundException {
        List<String> imageUrl = new ArrayList<>();
        AtomicInteger uploadCount = new AtomicInteger(0);

        for(Uri uri : uriList) {

            String timestamp = Long.toString(System.currentTimeMillis());
            UploadTask uploadTask = storageC.child(timestamp).putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(downloadUri -> {


                        imageUrl.add(downloadUri.toString());
                        int count = uploadCount.incrementAndGet();

                        // Se tutte le immagini sono caricate, esegui l'azione successiva
                        if (count == uriList.size()) {
                            // Fai qualcosa dopo che il ciclo è finito e tutti i caricamenti sono completati
                            String url1 = "";
                            for (String url : imageUrl) {
                                url1 = url1  + url +",";
                            }


                            locale.setFoto(url1);
                            refLocale.child(keyLocale).setValue(locale);
                            refLocale.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Toast.makeText(AggiungiLocale.this, "Locale aggiunto con successo", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(AggiungiLocale.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                    });
                }

            });

        }

    }
}
