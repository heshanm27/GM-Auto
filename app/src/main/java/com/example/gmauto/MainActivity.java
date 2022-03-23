package com.example.gmauto;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_SCREEN = 5000;
    //animation variable
    Animation topAnimation,bottomAnimation;
    ImageView image;
    TextView logo,subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //hide status bar
//       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

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
                Intent intent = new Intent(MainActivity.this,Dashbord.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}