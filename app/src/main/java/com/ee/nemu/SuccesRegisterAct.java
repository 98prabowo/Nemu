package com.ee.nemu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccesRegisterAct extends AppCompatActivity {

    Animation app_splash, top_to_btm, btm_to_top;
    Button btn_explore;
    ImageView icon_success_signup;
    TextView header, subheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_register);

        // load animation
        top_to_btm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);
        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        // load element
        btn_explore = findViewById(R.id.btn_explore);
        icon_success_signup = findViewById(R.id.ic_success_signup);
        header = findViewById(R.id.header);
        subheader = findViewById(R.id.subheader);

        // run animation
        icon_success_signup.startAnimation(app_splash);
        header.startAnimation(top_to_btm);
        subheader.startAnimation(top_to_btm);
        btn_explore.startAnimation(btm_to_top);

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohomescreen = new Intent(SuccesRegisterAct.this,HomeScreenAct.class);
                startActivity(gotohomescreen);
                finish();
            }
        });
    }
}
