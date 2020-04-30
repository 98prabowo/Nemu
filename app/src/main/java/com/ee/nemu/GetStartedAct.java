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

public class GetStartedAct extends AppCompatActivity {

    Button btn_sign_in, btn_new_account;
    Animation top_to_btm, btm_to_top;
    ImageView emblem_white;
    TextView tagline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // load animation
        top_to_btm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);
        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);

        // load element
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);
        emblem_white = findViewById(R.id.emblem_white);
        tagline = findViewById(R.id.tagline);

        // run animation
        tagline.startAnimation(top_to_btm);
        emblem_white.startAnimation(top_to_btm);
        btn_sign_in.startAnimation(btm_to_top);
        btn_new_account.startAnimation(btm_to_top);

        // go to activity
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignin = new Intent(GetStartedAct.this,SignInAct.class);
                startActivity(gotosignin);
            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedAct.this,RegisterOneAct.class);
                startActivity(gotoregisterone);
            }
        });
    }
}
