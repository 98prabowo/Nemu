package com.ee.nemu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashAct extends AppCompatActivity {

    Animation app_splash;
    ImageView app_logo;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getUsernameLocal();

        // load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        // load image
        app_logo = findViewById(R.id.app_logo);

        // run animation
        app_logo.startAnimation(app_splash);
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");

        if (username_key_new.isEmpty()) {
            //setting timer untuk 1 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //merubah activity ke activity lain
                    Intent gogetstarted = new Intent(SplashAct.this,GetStartedAct.class);
                    startActivity(gogetstarted);
                    finish();
                }
            },  1500);
        }
        else {
            //setting timer untuk 1 detik
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //merubah activity ke activity lain
                    Intent gogetstarted = new Intent(SplashAct.this,HomeScreenAct.class);
                    startActivity(gogetstarted);
                    finish();
                }
            },  1500);
        }
    }
}
