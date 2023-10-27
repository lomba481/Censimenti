package com.example.censimenti;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class CensimentiInterni extends AppCompatActivity {

    RelativeLayout relativeLayout;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch switchBtn;

    static DatabaseReference lRef;

    String keyPlanimetria, keyLampada ;
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



        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            addNewCircle(event);
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
                                    handleImageLongClick((CircleImageView) child);
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

    private void addNewCircle(MotionEvent event) {

        CircleImageView circleImageView = new CircleImageView(getApplicationContext());
        circleImageView.setImageResource(R.drawable.verde);

        x = event.getX();
        y = event.getY();

        circleImageView.setX(x);
        circleImageView.setY(y);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        circleImageView.setLayoutParams(params);
        relativeLayout.addView(circleImageView);

        Lampada lampada = new Lampada("", "", "","", 0,0);
        keyLampada= lRef.push().getKey();
        if (keyLampada != null) lRef.child(keyPlanimetria).child(keyLampada).setValue(lampada);
    }

    private void handleImageClick(CircleImageView v) {
        Intent intent = new Intent(CensimentiInterni.this, AggiungiLampade.class);
//        activityResultLauncher.launch(intent);
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        intent.putExtra("keyLampada", keyLampada);
        intent.putExtra("keyPlanimetria", keyPlanimetria);
        startActivity(intent);
    }

    private void handleImageLongClick(CircleImageView v) {
        relativeLayout.removeView(v);
        lRef.child(keyPlanimetria).child(keyLampada).removeValue();
    }

    public void loadUrlAsDrawable (String url, Context context, final Icon.OnDrawableLoadedListener listener) {
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
//    interface OnDrawableLoadedListener {
//        void onDrawableLoaded(Drawable drawable);
//    }
}
