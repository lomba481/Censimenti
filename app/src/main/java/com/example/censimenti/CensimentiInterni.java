package com.example.censimenti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.censimento);

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
        lref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

//                    String tipo = dataSnapshot.child("tipo").getValue(String.class);
                    String sorgente = (String) (dataSnapshot.child("sorgente").getValue());
//                    String attacco = (String) (dataSnapshot.child("attacco").getValue());
                    float x = dataSnapshot.child("x").getValue(Float.class);
                    float y = dataSnapshot.child("y").getValue(Float.class);


                    Log.d ("coords", x + " " + y );


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


                    circleImageView.setX(x);
                    circleImageView.setY(y);

                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
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
                    relativeLayout.addView(textView);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                            startActivityForResult(intent, RESULT_FIRST_USER);

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

        x = event.getX();
        y = event.getY();


        circleImageView.setX(x);
        circleImageView.setY(y);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
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

//        Log.d("alfa", CircleX + " " + CircleY);
        Intent intent = new Intent(CensimentiInterni.this, AggiungiLampade.class);
//        activityResultLauncher.launch(intent);
//        intent.putExtra("x", x);
//        intent.putExtra("y", y);


        lref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    float x1 = dataSnapshot.child("x").getValue(Float.class);
                    float y1 = dataSnapshot.child("y").getValue(Float.class);
                    if (v.getX() == x1 && v.getY() == y1) {
                        keyLampada = dataSnapshot.getKey();
                        intent.putExtra("keyLampada", keyLampada);
                        intent.putExtra("keyPlanimetria", keyPlanimetria);
                        startActivityForResult(intent, RESULT_FIRST_USER);
//                        String tipo = Tipologia.getInstance().getTipo();
//                        textView.setText(tipo);
//                        relativeLayout.addView(textView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });

    }

    private void handleImageLongClick(CircleImageView v) {
        relativeLayout.removeView(v);
        Log.d("asdf", keyLampada);
        //lRef.child(keyPlanimetria).child(keyLampada).removeValue();
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
        if (requestCode == RESULT_FIRST_USER && resultCode == RESULT_OK) {
            String tipo = data.getStringExtra("tipo");
            textView.setText(tipo);
            //relativeLayout.addView(textView);

            String sorgente = data.getStringExtra("sorgente");
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
//            relativeLayout.addView(circleImageView);

        }

    }
}
