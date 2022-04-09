package com.example.gmauto;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_SCREEN = 5000;
    //animation variable
    Animation topAnimation,bottomAnimation;
    ImageView image;
    TextView logo,subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View dcoreView = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            dcoreView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

        //animations
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //reference hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.logo);
        subtitle = findViewById(R.id.subtitle);

        //set animation
        image.setAnimation(topAnimation);
        logo.setAnimation(bottomAnimation);
        subtitle.setAnimation(bottomAnimation);

        //change screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(),Dashbord.class));
                    finish();
                }else {
                    Intent intent = new Intent(MainActivity.this, WelomeScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);
    }
}