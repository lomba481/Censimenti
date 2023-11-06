package com.example.censimenti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CensimentiInterni extends AppCompatActivity {

    RelativeLayout relativeLayout;
    CircleImageView circleImageView;
    TextView textView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchBtn;

    static DatabaseReference lRef, lref1;

    String keyPlanimetria, keyLampada;
    String imageUrl;
    float x, y;
    int larghezzaSchermo, altezzaSchermo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.censimento);
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        larghezzaSchermo = size.x;
        altezzaSchermo = size.y;

        relativeLayout = findViewById(R.id.relativeLayout);
        switchBtn = findViewById(R.id.switchBtn);

        lRef = FirebaseDatabase.getInstance().getReference("lampade");

        keyPlanimetria = getIntent().getStringExtra("key");

        imageUrl = getIntent().getStringExtra("imageUrl");
//        Uri imageUri = Uri.parse(imageUrl);
//        Drawable drawable = uriToDrawable (imageUri, getContentResolver());
        loadUrlAsDrawable(imageUrl, getApplicationContext(), new Icon.OnDrawableLoadedListener() {
            @Override
            public void onDrawableLoaded(Drawable d) {
                relativeLayout.setBackground(d);

            }
        });


       lref1 = lRef.child(keyPlanimetria);
       recuperaDati();

        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            addNewCircle(event);
                            Intent intent = new Intent(CensimentiInterni.this, AggiungiLampade.class);
                            intent.putExtra("x", x);
                            intent.putExtra("y", y);
                            intent.putExtra("keyLampada", keyLampada);
                            intent.putExtra("keyPlanimetria", keyPlanimetria);
                            startActivity(intent);
                            return false;
                        }
                    });
                } else {
                    for (int i = 0; i < relativeLayout.getChildCount(); i++) {
                        View child = relativeLayout.getChildAt(i);
                        if (child instanceof CircleImageView) {

                            child.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    handleImageLongClick((CircleImageView) v);
                                    return true;
                                }
                            });

                            child.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    handleImageClick((CircleImageView) v);
                                }
                            });

                        }
                    }
                    relativeLayout.setOnTouchListener(null);
                }
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void addNewCircle(MotionEvent event) {

        circleImageView = new CircleImageView(getApplicationContext());

        float translate = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

        x = event.getX() - (larghezzaSchermo/80);
        y = event.getY() - (altezzaSchermo/128);
//        x = event.getX();
//        y = event.getY();

        circleImageView.setX(x);
        circleImageView.setY(y);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(larghezzaSchermo/40, altezzaSchermo/64);
        circleImageView.setLayoutParams(params);



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

        keyLampada = lRef.push().getKey();
    }

    private void handleImageClick(CircleImageView v) {

        Intent intent = new Intent(CensimentiInterni.this, AggiungiLampade.class);
        lref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    float x1 = dataSnapshot.child("x").getValue(Float.class);
//                    float y1 = dataSnapshot.child("y").getValue(Float.class);

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
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });

    }

    private void handleImageLongClick(CircleImageView v) {

        lref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    float x1 = dataSnapshot.child("x").getValue(Float.class);
//                    float y1 = dataSnapshot.child("y").getValue(Float.class);

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
                                lRef.child(keyPlanimetria).child(keyLampada).removeValue();
                                relativeLayout.removeView(v);
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


        lref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String sorgente = (String) (dataSnapshot.child("sorgente").getValue());
//                    float xRaw = dataSnapshot.child("x").getValue(Float.class);
//                    float yRaw = dataSnapshot.child("y").getValue(Float.class);
                    float Kx = dataSnapshot.child("kx").getValue(Float.class);
                    float Ky = dataSnapshot.child("ky").getValue(Float.class);


                    circleImageView = new CircleImageView(getApplicationContext());
                    switch (sorgente) {
                        case "Led":
                            circleImageView.setImageResource(R.drawable.viola);
                            break;
                        case "Fluorescente":
                            circleImageView.setImageResource(R.drawable.verde);
                            break;
                        case "Scarica":
                            circleImageView.setImageResource(R.drawable.rosso);
                            break;

                    }

                    float x = larghezzaSchermo / Kx;
                    float y = altezzaSchermo / Ky;


                    circleImageView.setX(x);
                    circleImageView.setY(y);

//                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
//                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(larghezzaSchermo/40, altezzaSchermo/64);
//                    float p = larghezzaSchermo / width;
//                    float pp = altezzaSchermo / height;
//                    Log.d("wawa", "" + p + " -- " + pp);
                    circleImageView.setLayoutParams(params);
                    relativeLayout.addView(circleImageView);

                    textView = new TextView(getApplicationContext());
                    textView.setBackgroundColor(Color.BLUE);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(7);
                    textView.setGravity(Gravity.CENTER);

                    textView.setX(x-5);
                    textView.setY(y-50);
                    textView.setText(dataSnapshot.child("tipo").getValue(String.class));


                    int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
                    int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    textView.setWidth(width1);
                    textView.setHeight(height1);
//                    relativeLayout.addView(textView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
