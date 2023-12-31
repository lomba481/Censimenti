package com.example.censimenti;

import static com.example.censimenti.AdapterPlanimetrie.refPlanimetrie;
import static com.example.censimenti.AggiungiComune.storageC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class CensimentiInterni extends AppCompatActivity {

    RelativeLayout relativeLayout;
    CircleImageView circleImageView;


    com.getbase.floatingactionbutton.FloatingActionButton  lampadaFAB, localeFAB, indietroBtn;
    TextView textView;
    ImageView localeImage;

    static DatabaseReference refLampade, refLocale, refScreenshot;

    String keyPlanimetria, keyLampada, keyLocale;
    String imageUrl;
    float x, y;
    int larghezzaSchermo, altezzaSchermo, orientation;
    boolean isFABonLocale = false;
    boolean isFABonLampada = false;
    String locale, tipo, nome, potenza, sorgente, attacco, installazione;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.censimento);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        orientation = this.getResources().getConfiguration().orientation;

        larghezzaSchermo = size.x;
        altezzaSchermo = size.y;

        relativeLayout = findViewById(R.id.relativeLayout);
        indietroBtn = findViewById(R.id.indietroBtn);
        lampadaFAB = findViewById(R.id.lampadaFAB);
        localeFAB = findViewById(R.id.localeFAB);

        keyPlanimetria = getIntent().getStringExtra("key");

        imageUrl = getIntent().getStringExtra("imageUrl");

        refLampade = refPlanimetrie.child(keyPlanimetria).child("Lampade");
        refLocale = refPlanimetrie.child(keyPlanimetria).child("Locali");
        refScreenshot = refPlanimetrie.child(keyPlanimetria).child("Screenshot");


        loadUrlAsDrawable(imageUrl, getApplicationContext(), new Icon.OnDrawableLoadedListener() {
            @Override
            public void onDrawableLoaded(Drawable d) {
                relativeLayout.setBackground(d);

            }
        });


//       recuperaDati();

       lampadaFAB.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("ClickableViewAccessibility")
           @Override
           public void onClick(View v) {

               isFABonLampada = !isFABonLampada;

               updateFABcolorLampada(lampadaFAB);

           }
       });

       localeFAB.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("ClickableViewAccessibility")
           @Override
           public void onClick(View v) {
               isFABonLocale = !isFABonLocale;

               updateFABcolorLocale(localeFAB);

           }
       });
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isFABonLampada && !isFABonLocale) {

                    addNewCircle(event);
                    Intent intent = new Intent(CensimentiInterni.this, AggiungiLampade.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    intent.putExtra("keyLampada", keyLampada);
                    intent.putExtra("keyPlanimetria", keyPlanimetria);
                    intent.putExtra("locale", locale);
                    intent.putExtra("installazione", installazione);
                    intent.putExtra("potenza", potenza);
                    intent.putExtra("attacco", attacco);
                    intent.putExtra("nome", nome);
                    intent.putExtra("sorgente", sorgente);
                    intent.putExtra("tipo", tipo);


                    startActivityForResult(intent, 1);
                }
                else if (isFABonLocale && !isFABonLampada) {

                    keyLocale = refLocale.push().getKey();
                    addNewLocal(event);
                    Intent intent = new Intent(CensimentiInterni.this, AggiungiLocale.class);
                    intent.putExtra("x", x);
                    intent.putExtra("y", y);
                    intent.putExtra("keyLocale", keyLocale);
                    startActivity(intent);

                }
                else {


                    for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                        View child = relativeLayout.getChildAt(i);
                        if (child instanceof CircleImageView) {

                            child.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    LampadaLongClick((CircleImageView) v);
                                    return true;
                                }
                            });

                            child.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LampadaClick(v);

                                }
                            });

                        } else if (child instanceof ImageView){
                            child.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LocaleClick(v);
                                }
                            });

                            child.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    LocaleLongClick(v);
                                    return true;
                                }
                            });

                        }
                    }
                }
                return false;
            }
        });

    }
    private void LocaleClick(View v){
        Intent intent = new Intent(CensimentiInterni.this, AggiungiLocale.class);

        refLocale.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);
                    float x1 = larghezzaSchermo / Kx;
                    float y1 = altezzaSchermo / Ky;

                    if (v.getX() == x1 && v.getY() == y1) {
                        keyLocale = dataSnapshot.getKey();

                        intent.putExtra("x", x1);
                        intent.putExtra("y", y1);
                        intent.putExtra("keyLocale", keyLocale);
                        intent.putExtra("modifica", "si");

                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LocaleLongClick(View v){
        refLocale.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);
                    float x1 = larghezzaSchermo / Kx;
                    float y1 = altezzaSchermo / Ky;

                    if (v.getX() == x1 && v.getY() == y1) {
                        keyLocale = dataSnapshot.getKey();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CensimentiInterni.this);
                        builder.setMessage("Sei sicuro di voler eliminare?");
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refLocale.child(keyLocale).removeValue();
                                relativeLayout.removeView(v);
                                storageC.child(keyLocale).delete();
                                Intent intent = new Intent(CensimentiInterni.this, CensimentiInterni.class);
                                intent.putExtra("key", keyPlanimetria);
                                intent.putExtra("imageUrl", imageUrl);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            locale = data.getStringExtra("locale");
            nome = data.getStringExtra("nome");
            sorgente = data.getStringExtra("sorgente");
            installazione = data.getStringExtra("installazione");
            attacco = data.getStringExtra("attacco");
            tipo = data.getStringExtra("tipo");
            potenza = data.getStringExtra("potenza");


        }
    }

    private void updateFABcolorLocale(FloatingActionButton fab) {
        if (isFABonLocale) {
            fab.setColorNormalResId(R.color.orange_restart);

        } else {
            fab.setColorNormalResId(R.color.gray);
        }
    }

    private void updateFABcolorLampada(FloatingActionButton fab) {
        if (isFABonLampada) {
            fab.setColorNormalResId(R.color.orange_restart);

        } else {
            fab.setColorNormalResId(R.color.gray);
        }
    }

    private void addNewLocal (MotionEvent event) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            x = event.getX() - (larghezzaSchermo/80);
            y = event.getY() - (altezzaSchermo/128);
        } else {
            x = event.getX() - (larghezzaSchermo/128);
            y = event.getY() - (altezzaSchermo/80);
        }



    }

    @SuppressLint("ResourceAsColor")
    private void addNewCircle(MotionEvent event) {

        circleImageView = new CircleImageView(getApplicationContext());

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            x = event.getX() - (larghezzaSchermo/80);
            y = event.getY() - (altezzaSchermo/128);
        } else {
            x = event.getX() - (larghezzaSchermo/128);
            y = event.getY() - (altezzaSchermo/80);
        }


        textView = new TextView(getApplicationContext());
        textView.setBackgroundColor(Color.BLUE);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(5);
        textView.setGravity(Gravity.CENTER);

        textView.setX(x-5);
        textView.setY(y-50);


        int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        textView.setWidth(width1);
        textView.setHeight(height1);

        keyLampada = refLampade.push().getKey();
    }

    private void LampadaClick(View v) {

        Intent intent = new Intent(CensimentiInterni.this, AggiungiLampade.class);

        refLampade.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);
                    float x1 = larghezzaSchermo / Kx;
                    float y1 = altezzaSchermo / Ky;

                    if (v.getX() == x1 && v.getY() == y1) {
                        keyLampada = dataSnapshot.getKey();

                        intent.putExtra("x", x1);
                        intent.putExtra("y", y1);
                        intent.putExtra("keyLampada", keyLampada);
                        intent.putExtra("keyPlanimetria", keyPlanimetria);
                        intent.putExtra("modifica", "si");

                        startActivity(intent);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void LampadaLongClick(CircleImageView v) {

        refLampade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);
                    float x1 = larghezzaSchermo / Kx;
                    float y1 = altezzaSchermo / Ky;

                    if (v.getX() == x1 && v.getY() == y1) {
                        keyLampada = dataSnapshot.getKey();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CensimentiInterni.this);
                        builder.setMessage("Sei sicuro di voler eliminare?");
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refLampade.child(keyLampada).removeValue();
                                relativeLayout.removeView(v);
                                storageC.child(keyLampada).delete();
                                Intent intent = new Intent(CensimentiInterni.this, CensimentiInterni.class);
                                intent.putExtra("key", keyPlanimetria);
                                intent.putExtra("imageUrl", imageUrl);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


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

    private void recuperaDati() {

        refLocale.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);
                    float x = larghezzaSchermo / Kx;
                    float y = altezzaSchermo / Ky;

                    localeImage = new ImageView(getApplicationContext());
                    localeImage.setImageResource(R.drawable.baseline_place_24_red);
                    localeImage.setX(x);
                    localeImage.setY(y);

                    RelativeLayout.LayoutParams params;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        params = new RelativeLayout.LayoutParams(larghezzaSchermo/40, altezzaSchermo/64);

                    }
                    else {
                        params = new RelativeLayout.LayoutParams(larghezzaSchermo/64, altezzaSchermo/40);

                    }

                    localeImage.setLayoutParams(params);
                    relativeLayout.addView(localeImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        refLampade.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String installazione = dataSnapshot.child("installazione").getValue(String.class);
                    Long id = dataSnapshot.child("id").getValue(Long.class);
                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);


                    circleImageView = new CircleImageView(getApplicationContext());
                    switch (installazione) {
                        case "CONTROSOFFITTO":
                            circleImageView.setImageResource(R.drawable.controsoffitto);
                            break;

                        case "ESTERNA":
                            circleImageView.setImageResource(R.drawable.esterna);
                            break;

                        case "INCASSO PAVIM_PARETE":
                            circleImageView.setImageResource(R.drawable.incasso);
                            break;

                        case "LED":
                            circleImageView.setImageResource(R.drawable.led);
                            break;

                        case "PARETE":
                            circleImageView.setImageResource(R.drawable.parete);
                            break;

                        case "SOFFITTO":
                            circleImageView.setImageResource(R.drawable.soffitto);
                            break;

                        case "SOSPENSIONE":
                            circleImageView.setImageResource(R.drawable.sospensione);
                            break;

                    }

                    float x = larghezzaSchermo / Kx;
                    float y = altezzaSchermo / Ky;


                    circleImageView.setX(x);
                    circleImageView.setY(y);

                    RelativeLayout.LayoutParams params;
                    int width1, height1;
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        width1 = larghezzaSchermo/40;
                        height1 = altezzaSchermo/64;
                        params = new RelativeLayout.LayoutParams(width1, height1);


                    }
                    else {
                        width1 = larghezzaSchermo/64;
                        height1 = altezzaSchermo/40;
                        params = new RelativeLayout.LayoutParams(width1, height1);

                    }

                    circleImageView.setLayoutParams(params);
                    relativeLayout.addView(circleImageView);

                    textView = new TextView(getApplicationContext());

                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(15);
                    textView.setGravity(Gravity.CENTER);


                    textView.setX(x);
                    textView.setY(y-30);
                    textView.setText(id.toString());

                    textView.setWidth(width1);
                    textView.setHeight(height1);
                    relativeLayout.addView(textView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaDati();
        indietroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ottieni la vista radice del layout dell'attività
                View rootView = getWindow().getDecorView().getRootView();

                // Crea uno screenshot della vista radice
                Bitmap screenshot = getScreenshot(rootView);

                // Salva lo screenshot su Firebase Storage
                uploadScreenshot(screenshot);


            }
        });
    }

    private Bitmap getScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap screenshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return screenshot;
    }

    private void uploadScreenshot(Bitmap screenshot) {
        // Crea uno stream di byte dalla bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        screenshot.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        // Ottieni un riferimento al percorso di archiviazione su Firebase
        StorageReference storageRef = storageC.child(keyPlanimetria);

        // Carica i dati su Firebase Storage
        storageRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        addScreenshotInfoToDatabase(storageRef);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CensimentiInterni.this, "Errore durante il caricamento dello screenshot su Firebase Storage", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }

    private void addScreenshotInfoToDatabase(StorageReference storageRef) {
        // Ottieni l'URL del file appena caricato su Storage
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(android.net.Uri uri) {
                // Aggiungi informazioni al Realtime Database
                DatabaseReference databaseRef = refScreenshot;
                databaseRef.setValue(uri.toString());

                Toast.makeText(CensimentiInterni.this, "Informazioni dello screenshot aggiunte al Realtime Database", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CensimentiInterni.this, "Errore nel recupero dell'URL dallo screenshot su Firebase Storage", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }
}
