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

public class SuccesBuyAct extends AppCompatActivity {

    Animation app_splash, top_to_btm, btm_to_top;
    Button btn_home, btn_view_ticket;
    ImageView ic_success_buy;
    TextView header_buy, subheader_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_buy);

        // load animation
        top_to_btm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);
        btm_to_top = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        // load component
        btn_home = findViewById(R.id.btn_home);
        btn_view_ticket = findViewById(R.id.btn_view_ticket);
        header_buy = findViewById(R.id.header_buy);
        subheader_buy = findViewById(R.id.subheader_buy);
        ic_success_buy = findViewById(R.id.ic_success_buy);

        // run animation
        ic_success_buy.startAnimation(app_splash);
        header_buy.startAnimation(top_to_btm);
        subheader_buy.startAnimation(top_to_btm);
        btn_home.startAnimation(btm_to_top);
        btn_view_ticket.startAnimation(btm_to_top);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(SuccesBuyAct.this, HomeScreenAct.class);
                startActivity(backtohome);
            }
        });

        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(SuccesBuyAct.this, MyProfileAct.class);
                startActivity(gotoprofile);
                finish();
            }
        });
    }
}
