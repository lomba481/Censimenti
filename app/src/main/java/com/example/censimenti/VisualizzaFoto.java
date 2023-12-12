package com.example.censimenti;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class VisualizzaFoto extends AppCompatActivity {
    ImageView relativeLayout;
    String url;
    com.google.android.material.floatingactionbutton.FloatingActionButton indietroBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualizzafoto);
        indietroBtn = findViewById(R.id.indietroBtnFoto);
        relativeLayout = findViewById(R.id.relativeLayoutFoto);

        url = getIntent().getStringExtra("url");
        if (url != null){
            loadUrlAsDrawable(url, getApplicationContext(), new Icon.OnDrawableLoadedListener() {
                @Override
                public void onDrawableLoaded(Drawable d) {
                    relativeLayout.setImageDrawable(d);
                }
            });
        }
        else {
            relativeLayout.setImageURI(getIntent().getData());
        }


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
}
