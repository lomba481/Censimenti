package com.example.censimenti;

import static com.example.censimenti.AdapterPlanimetrie.refPlanimetrie;
import static com.example.censimenti.AggiungiComune.storageC;
import static com.example.censimenti.CensimentiInterni.refLampade;
import static com.example.censimenti.CensimentiInterni.refLocale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AggiungiLampade extends AppCompatActivity {

    TextInputEditText potenzaLampada, sorgenteLampada, attaccoLampada;
    AutoCompleteTextView nomeLampada, tipoLampada, installazioneLampada, localeLampada;
    Button salva, indietro;
    String keyLampada, keyPlanimetria;
    String url = null;
    RelativeLayout scegliImmagine;
    ImageView imageView;
    TextView textView;
    CircularProgressIndicator progressIndicator;
    float x, y;
    Long cont;
    String locale, tipo, nome, potenza, sorgente, attacco, installazione, modifica;
    Long id;


    ImageView refresh, fotoLamp;
    Uri uri = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_lampada);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int larghezzaSchermo = size.x;
        int altezzaSchermo = size.y;


        localeLampada = findViewById(R.id.localeLampada);
        nomeLampada = findViewById(R.id.nomeLampada);
        tipoLampada = findViewById(R.id.tipoLampada);
        potenzaLampada = findViewById(R.id.potenzaLampada);
        sorgenteLampada = findViewById(R.id.sorgenteLampada);
        attaccoLampada = findViewById(R.id.attaccoLampada);
        installazioneLampada = findViewById(R.id.installazioneLampada);
        refresh = findViewById(R.id.refreshLampade);
        fotoLamp = findViewById(R.id.ImageView);
        textView = findViewById(R.id.textLampada);
        progressIndicator = findViewById(R.id.progressIndicatorLamp);


        keyLampada = getIntent().getStringExtra("keyLampada");
        keyPlanimetria = getIntent().getStringExtra("keyPlanimetria");
        locale = getIntent().getStringExtra("locale");
        nome = getIntent().getStringExtra("nome");
        sorgente = getIntent().getStringExtra("sorgente");
        installazione = getIntent().getStringExtra("installazione");
        attacco = getIntent().getStringExtra("attacco");
        tipo = getIntent().getStringExtra("tipo");
        potenza = getIntent().getStringExtra("potenza");
        modifica = getIntent().getStringExtra("modifica");
        if (modifica == null) {
            textView.setText("Aggiungi Lampada");
        }
        else {
            textView.setText("Modifica Lampada");
        }


        localeLampada.setText(locale);
        tipoLampada.setText(tipo);
        nomeLampada.setText(nome);
        potenzaLampada.setText(potenza);
        sorgenteLampada.setText(sorgente);
        attaccoLampada.setText(attacco);
        installazioneLampada.setText(installazione);

        fotoLamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {
                    Intent intent = new Intent(AggiungiLampade.this, VisualizzaFoto.class);
                    intent.setData(uri);
                    startActivity(intent);
                }
                else if (url != null) {
                    Intent intent = new Intent(AggiungiLampade.this, VisualizzaFoto.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localeLampada.setText("");
                tipoLampada.setText("");
                nomeLampada.setText("");
                potenzaLampada.setText("");
                sorgenteLampada.setText("");
                attaccoLampada.setText("");
                installazioneLampada.setText("");
            }
        });





        x = getIntent().getFloatExtra("x", 0);
        y = getIntent().getFloatExtra("y", 0);

        float Kx = larghezzaSchermo / x;
        float Ky = altezzaSchermo / y;

        salva = findViewById(R.id.saveBtn);
        indietro = findViewById(R.id.backBtn);
        scegliImmagine = findViewById(R.id.ScegliImmagine);
        imageView = findViewById(R.id.ImageView);

        String[] opzioniTipo = {"PLAFONIERE STAGNE", "PLAFONIERE", "FARETTI", "PROIETTORI", "APPLIQUE", "RIFLETTORI", "APPARECCHI ILLUMINANTI LED"};

        String[] opzioniPlafStagna = {"SDF02.a - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 1x18W", "SDF02.b - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 1x36W",
                "SDF02.c - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 1x58W", "SDF02.d - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 2x18W",
                "SDF02.e - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 2x36W", "SDF02.f - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 2x58W",
                "SDF02.g - PLAFONIERA STAGNA CON TUBI FLUORESCENTI 4x55W"};

        String[] opzioniPlafoniere = {"SDF04.a - PLAFONIERA CON TUBI FLUORESCENTI T8 1x18W", "SDF04.b - PLAFONIERA CON TUBI FLUORESCENTI T8 1x36W",
                "SDF04.c - PLAFONIERA CON TUBI FLUORESCENTI T8 1x58W", "SDF04.d - PLAFONIERA CON TUBI FLUORESCENTI T8 2x18W",
                "SDF04.e - PLAFONIERA CON TUBI FLUORESCENTI T8 2x36W", "SDF04.f - PLAFONIERA CON TUBI FLUORESCENTI T8 2x58W",
                "SDF04.h - PLAFONIERA CON TUBI FLUORESCENTI T8 4x18W"};

        String[] opzioniFaretti = {"SDF05.a - FARETTO CON LAMPADE FLC 2x18W", "SDF05.bb - FARETTO CON LAMPADE FLC 2x26W",
                "SDF06.a - FARETTO CON LAMPADA DICROICA 35W", "SDF06.b - FARETTO CON LAMPADA DICROICA 50W"};

        String[] opzioniProiettori = {"SDF07.a - PROIETTORE CON LAMPADA A SCARICA 70W", "SDF07.b - PROIETTORE CON LAMPADA A SCARICA 150W",
                "SDF07.c - PROIETTORE CON LAMPADA A SCARICA 250W", "SDF07.d - PROIETTORE CON LAMPADA A SCARICA 400W",
                "SDF08.c - PROIETTORE CON LAMPADA AD INCANDESCENZA 200W", "SDF08.d - PROIETTORE CON LAMPADA AD INCANDESCENZA 300W",
                "SDF08.e - PROIETTORE CON LAMPADA AD INCANDESCENZA 500W"};

        String[] opzioniApplique = {"SDF09.a - APPARECCHIO ILLUMINANTE CON LAMPADA FLC 18W", "SDF09.b - APPARECCHIO ILLUMINANTE CON LAMPADE FLC 2x18W",
                "SDF09.c - APPARECCHIO ILLUMINANTE CON LAMPADA FLC 26W", "SDF09.d - APPARECCHIO ILLUMINANTE CON LAMPADA FLC 32W",
                "SDF09.l - APPARECCHIO ILLUMINANTE CON LAMPADE FLC 2x26W", "SDF10.a - APPARECCHIO ILLUMINANTE CON LAMPADE INCANDESCENZA 60W",
                "SDF10.b - APPARECCHIO ILLUMINANTE CON LAMPADE INCANDESCENZA 100W", "SDF10.c - APPARECCHIO ILLUMINANTE CON LAMPADE INCANDESCENZA 40W"};

        String[] opzioniRiflettori = {"SDF14.b - RIFLETTORE CON LAMPADA A SCARICA 250W", "SDF14.c - RIFLETTORE CON LAMPADA A SCARICA 400W"};

        String[] opzioniApparecchiLed = {"SDF.L01.b - PANNELLO LED RETTANGOLARE 30W", "SDF.L01.d - PANNELLO LED QUADRATO 30W",
                "SDF.L02.a - PROIETTORE LED 30W", "SDF.L02.b - PROIETTORE LED 50W", "SDF.L02.c - PROIETTORE LED 100W", "SDF.L02.h - PROIETTORE LED 150W",
                "SDF.L02.d - PROIETTORE LED 300W", "SDF.L03.a - APPARECCHIO ILLUMINANTE A LED 20W", "SDF.L04.a - FARETTO LED INCASSATO 18W",
                "SDF.L06.a - PLAFONIERA LED 40W", "SDF.L06.b - PLAFONIERA LED 30W", "SDF.L07.a - PLAFONIERA STAGNA LED 40W"};

        String[] opzioniInstallazione = {"CONTROSOFFITTO", "ESTERNA", "INCASSO PAVIM_PARETE", "LED", "PARETE", "SOFFITTO", "SOSPENSIONE"};

        riempiLocali();

        autoCompleteMenu(opzioniTipo, tipoLampada);
        autoCompleteMenu(opzioniInstallazione, installazioneLampada);
        tipoLampada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                switch(selected) {
                    case "PLAFONIERE STAGNE":
                        autoCompleteMenu(opzioniPlafStagna, nomeLampada);
                        sorgenteLampada.setText("TUBI FLUORESCENTI");
                        break;

                    case "PLAFONIERE":
                        autoCompleteMenu(opzioniPlafoniere, nomeLampada);
                        sorgenteLampada.setText("TUBI FLUORESCENTI");
                        break;

                    case "FARETTI":
                        autoCompleteMenu(opzioniFaretti, nomeLampada);
                        break;

                    case "PROIETTORI":
                        autoCompleteMenu(opzioniProiettori, nomeLampada);
                        break;

                    case "APPLIQUE":
                        autoCompleteMenu(opzioniApplique, nomeLampada);
                        break;

                    case "RIFLETTORI":
                        autoCompleteMenu(opzioniRiflettori, nomeLampada);
                        sorgenteLampada.setText("SCARICA");
                        break;

                    case "APPARECCHI ILLUMINANTI LED":
                        autoCompleteMenu(opzioniApparecchiLed, nomeLampada);
                        sorgenteLampada.setText("LED");
                        break;

                    default:
                        break;

                }
            }
        });

        controlloAttributi();

        refLampade.child(keyLampada).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String locale = snapshot.child("locale").getValue(String.class);
                    String tipo = snapshot.child("tipo").getValue(String.class);
                    String sorgente = snapshot.child("sorgente").getValue(String.class);
                    String attacco = snapshot.child("attacco").getValue(String.class);
                    String potenza = snapshot.child("potenza").getValue(String.class);
                    String nome = snapshot.child("nome").getValue(String.class);
                    id = snapshot.child("id").getValue(Long.class);
                    String installazione = snapshot.child ("installazione").getValue(String.class);
                    url = snapshot.child("foto").getValue(String.class);

                    localeLampada.setText(locale);
                    tipoLampada.setText(tipo);
                    nomeLampada.setText(nome);
                    potenzaLampada.setText(potenza);
                    sorgenteLampada.setText(sorgente);
                    attaccoLampada.setText(attacco);
                    installazioneLampada.setText(installazione);

                    if(url != null){
                        loadUrlAsDrawable(url, getApplicationContext(), new Icon.OnDrawableLoadedListener() {
                            @Override
                            public void onDrawableLoaded(Drawable d) {
                                imageView.setImageDrawable(d);
                            }
                        });

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        scegliImmagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AggiungiLampade.this)
                        .cameraOnly().maxResultSize(750,750)
                        .start();
            }
        });


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (installazioneLampada.getText().toString().equals("")) {
                    Toast.makeText(AggiungiLampade.this, "Devi compilare il campo \"Installazione\"", Toast.LENGTH_SHORT).show();

                } else {
                    progressIndicator.setVisibility(View.VISIBLE);
                    // recupero i dati per poi eseguire salvataggio in firebase
                    refPlanimetrie.child(keyPlanimetria).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (modifica == null){

                                cont = snapshot.child("conteggio").getValue(Long.class);
                                cont = cont + 1;
                                refPlanimetrie.child(keyPlanimetria).child("conteggio").setValue(cont);

                            } else {
                                cont = id;
                            }



                            Lampada lampada = new Lampada(localeLampada.getText().toString(),
                                    tipoLampada.getText().toString(),
                                    nomeLampada.getText().toString(),
                                    potenzaLampada.getText().toString(),
                                    sorgenteLampada.getText().toString(),
                                    attaccoLampada.getText().toString(),
                                    installazioneLampada.getText().toString(),
                                    url, x, y, Kx, Ky, cont);

                            try {
                                if (uri == null) {

                                    refLampade.child(keyLampada).setValue(lampada);
                                    refLampade.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Toast.makeText(AggiungiLampade.this, "Lampada aggiunta con successo", Toast.LENGTH_SHORT).show();

                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("sorgente", lampada.getSorgente());
                                            resultIntent.putExtra("tipo", lampada.getTipo());
                                            resultIntent.putExtra("attacco", lampada.getAttacco());
                                            resultIntent.putExtra("installazione", lampada.getInstallazione());
                                            resultIntent.putExtra("locale", lampada.getLocale());
                                            resultIntent.putExtra("nome", lampada.getNome());
                                            resultIntent.putExtra("potenza", lampada.getPotenza());
                                            setResult(RESULT_OK, resultIntent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(AggiungiLampade.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    addDataFirebase(lampada);
                                }
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });





        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void riempiLocali() {
        ArrayList<String> listaLocali = new ArrayList<String>();
        refLocale.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String nome = dataSnapshot.child("nome").getValue(String.class);

                    listaLocali.add(nome);
                }
                autoCompleteMenu(listaLocali.toArray(new String[0]), localeLampada);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        imageView.setImageURI(uri);
    }

    private void addDataFirebase(Lampada lampada) throws FileNotFoundException {

        UploadTask uploadTask = storageC.child(keyLampada).putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String imageUrl = downloadUri.toString();
                    lampada.setFoto(imageUrl);
                    refLampade.child(keyLampada).setValue(lampada);
                    refLampade.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Toast.makeText(AggiungiLampade.this, "Lampada aggiunta con successo", Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("sorgente", lampada.getSorgente());
                            resultIntent.putExtra("tipo", lampada.getTipo());
                            resultIntent.putExtra("attacco", lampada.getAttacco());
                            resultIntent.putExtra("installazione", lampada.getInstallazione());
                            resultIntent.putExtra("locale", lampada.getLocale());
                            resultIntent.putExtra("nome", lampada.getNome());
                            resultIntent.putExtra("potenza", lampada.getPotenza());
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AggiungiLampade.this, "Non riesco ad inserire i dati" + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }

        });


    }

    public void autoCompleteMenu(String[] elenco, AutoCompleteTextView autoCompleteTextView) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.drop_down_layout, elenco);
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void controlloAttributi () {
        nomeLampada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                switch (selected) {
                    case "SDF02.a - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 1x18W":
                    case "SDF04.a - PLAFONIERA CON TUBI FLUORESCENTI T8 1x18W":
                    case "SDF.L04.a - FARETTO LED INCASSATO 18W":
                        potenzaLampada.setText("18");
                        break;

                    case "SDF09.a - APPARECCHIO ILLUMINANTE CON LAMPADA FLC 18W":
                        potenzaLampada.setText("18");
                        sorgenteLampada.setText("FLUORESCENTI COMPATTE");
                        break;

                    case "SDF.L03.a - APPARECCHIO ILLUMINANTE A LED 20W":
                        potenzaLampada.setText("20");
                        break;

                    case "SDF09.c - APPARECCHIO ILLUMINANTE CON LAMPADA FLC 26W":
                        potenzaLampada.setText("26");
                        sorgenteLampada.setText("FLUORESCENTI COMPATTE");
                        break;

                    case "SDF.L01.b - PANNELLO LED RETTANGOLARE 30W":
                    case "SDF.L01.d - PANNELLO LED QUADRATO 30W":
                    case "SDF.L02.a - PROIETTORE LED 30W":
                    case "SDF.L06.b - PLAFONIERA LED 30W":
                        potenzaLampada.setText("30");
                        break;

                    case "SDF09.d - APPARECCHIO ILLUMINANTE CON LAMPADA FLC 32W":
                        potenzaLampada.setText("32");
                        sorgenteLampada.setText("FLUORESCENTI COMPATTE");
                        break;

                    case "SDF06.a - FARETTO CON LAMPADA DICROICA 35W":
                        potenzaLampada.setText("35");
                        sorgenteLampada.setText("INCANDESCENZA");
                        break;

                    case "SDF02.b - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 1x36W":
                    case "SDF02.d - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 2x18W":
                    case "SDF04.b - PLAFONIERA CON TUBI FLUORESCENTI T8 1x36W":
                    case "SDF04.d - PLAFONIERA CON TUBI FLUORESCENTI T8 2x18W":
                        potenzaLampada.setText("36");
                        break;

                    case "SDF05.a - FARETTO CON LAMPADE FLC 2x18W":
                    case "SDF09.b - APPARECCHIO ILLUMINANTE CON LAMPADE FLC 2x18W":
                        potenzaLampada.setText("36");
                        sorgenteLampada.setText("FLUORESCENTI COMPATTE");
                        break;

                    case "SDF10.c - APPARECCHIO ILLUMINANTE CON LAMPADE INCANDESCENZA 40W":
                        potenzaLampada.setText("40");
                        sorgenteLampada.setText("INCANDESCENZA");
                        attaccoLampada.setText("E14");
                        break;

                    case "SDF.L06.a - PLAFONIERA LED 40W":
                    case "SDF.L07.a - PLAFONIERA STAGNA LED 40W":
                        potenzaLampada.setText("40");
                        break;

                    case "SDF06.b - FARETTO CON LAMPADA DICROICA 50W":
                        potenzaLampada.setText("50");
                        sorgenteLampada.setText("INCANDESCENZA");
                        break;

                    case "SDF.L02.b - PROIETTORE LED 50W":
                        potenzaLampada.setText("50");
                        break;

                    case "SDF05.bb - FARETTO CON LAMPADE FLC 2x26W":
                    case "SDF09.l - APPARECCHIO ILLUMINANTE CON LAMPADE FLC 2x26W":
                        potenzaLampada.setText("52");
                        sorgenteLampada.setText("FLUORESCENTI COMPATTE");
                        break;

                    case "SDF02.c - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 1x58W":
                    case "SDF04.c - PLAFONIERA CON TUBI FLUORESCENTI T8 1x58W":
                        potenzaLampada.setText("58");
                        break;

                    case "SDF10.a - APPARECCHIO ILLUMINANTE CON LAMPADE INCANDESCENZA 60W":
                        potenzaLampada.setText("60");
                        sorgenteLampada.setText("INCANDESCENZA");
                        attaccoLampada.setText("E27");
                        break;

                    case "SDF07.a - PROIETTORE CON LAMPADA A SCARICA 70W":
                        potenzaLampada.setText("70");
                        sorgenteLampada.setText("SCARICA");
                        break;

                    case "SDF02.e - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 2x36W":
                    case "SDF04.e - PLAFONIERA CON TUBI FLUORESCENTI T8 2x36W":
                    case "SDF04.h - PLAFONIERA CON TUBI FLUORESCENTI T8 4x18W":
                        potenzaLampada.setText("72");
                        break;

                    case "SDF10.b - APPARECCHIO ILLUMINANTE CON LAMPADE INCANDESCENZA 100W":
                        sorgenteLampada.setText("INCANDESCENZA");
                        potenzaLampada.setText("100");
                        attaccoLampada.setText("E27");
                        break;

                    case "SDF.L02.c - PROIETTORE LED 100W":
                        potenzaLampada.setText("100");
                        break;

                    case "SDF02.f - PLAFONIERA STAGNA CON TUBI FLUORESCENTI T8 2x58W":
                    case "SDF04.f - PLAFONIERA CON TUBI FLUORESCENTI T8 2x58W":
                        potenzaLampada.setText("116");
                        break;

                    case "SDF07.b - PROIETTORE CON LAMPADA A SCARICA 150W":
                        sorgenteLampada.setText("SCARICA");
                        potenzaLampada.setText("150");
                        break;

                    case "SDF.L02.h - PROIETTORE LED 150W":
                        potenzaLampada.setText("150");
                        break;

                    case "SDF08.c - PROIETTORE CON LAMPADA AD INCANDESCENZA 200W":
                        potenzaLampada.setText("200");
                        sorgenteLampada.setText("INCANDESCENZA");
                        attaccoLampada.setText("R7s");
                        break;

                    case "SDF02.g - PLAFONIERA STAGNA CON TUBI FLUORESCENTI 4x55W":
                        potenzaLampada.setText("220");
                        break;

                    case "SDF07.c - PROIETTORE CON LAMPADA A SCARICA 250W":
                        potenzaLampada.setText("250");
                        sorgenteLampada.setText("SCARICA");
                        break;

                    case "SDF14.b - RIFLETTORE CON LAMPADA A SCARICA 250W":
                        potenzaLampada.setText("250");
                        break;

                    case "SDF08.d - PROIETTORE CON LAMPADA AD INCANDESCENZA 300W":
                        potenzaLampada.setText("300");
                        sorgenteLampada.setText("INCANDESCENZA");
                        attaccoLampada.setText("R7s");
                        break;

                    case "SDF.L02.d - PROIETTORE LED 300W":
                        potenzaLampada.setText("300");
                        break;

                    case "SDF07.d - PROIETTORE CON LAMPADA A SCARICA 400W":
                        potenzaLampada.setText("400");
                        sorgenteLampada.setText("SCARICA");
                        break;

                    case "SDF14.c - RIFLETTORE CON LAMPADA A SCARICA 400W":
                        potenzaLampada.setText("400");
                        break;

                    case "SDF08.e - PROIETTORE CON LAMPADA AD INCANDESCENZA 500W":
                        potenzaLampada.setText("500");
                        sorgenteLampada.setText("INCANDESCENZA");
                        attaccoLampada.setText("R7s");
                        break;

                    default:
                        break;
                }
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


}
