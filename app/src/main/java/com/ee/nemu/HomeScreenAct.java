package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeScreenAct extends AppCompatActivity {

    ImageView  photo_home_user;
    CircleView btn_to_profile;
    LinearLayout btn_tiket_sarangan, btn_tiket_pulau_merah, btn_tiket_cheng_ho,
                 btn_tiket_penataran, btn_tiket_coban_rondo, btn_tiket_gili_ketapang;
    TextView nama_depan, user_ballance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        getUsernameLocal();

        btn_tiket_sarangan = findViewById(R.id.btn_tiket_sarangan);
        btn_tiket_pulau_merah = findViewById(R.id.btn_tiket_pulau_merah);
        btn_tiket_cheng_ho = findViewById(R.id.btn_tiket_cheng_ho);
        btn_tiket_penataran = findViewById(R.id.btn_tiket_penataran);
        btn_tiket_coban_rondo = findViewById(R.id.btn_tiket_coban_rondo);
        btn_tiket_gili_ketapang = findViewById(R.id.btn_tiket_gili_ketapang);

        btn_to_profile = findViewById(R.id.btn_to_profile);
        photo_home_user = findViewById(R.id.photo_home_user);
        nama_depan = findViewById(R.id.nama_depan);
        user_ballance = findViewById(R.id.user_ballance);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_depan.setText(dataSnapshot.child("nama_depan").getValue().toString());
                user_ballance.setText("US$ " + dataSnapshot.child("user_ballance").getValue().toString());
                Picasso.with(HomeScreenAct.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop()
                        .fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_tiket_sarangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosarangan = new Intent(HomeScreenAct.this,TicketDetailAct.class);
                // meletakan data kepada Intent
                gotosarangan.putExtra("jenis_tiket","Telaga Sarangan");
                startActivity(gotosarangan);
            }
        });

        btn_tiket_pulau_merah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosarangan = new Intent(HomeScreenAct.this,TicketDetailAct.class);
                // meletakan data kepada Intent
                gotosarangan.putExtra("jenis_tiket","Pantai Pulau Merah");
                startActivity(gotosarangan);
            }
        });

        btn_tiket_cheng_ho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosarangan = new Intent(HomeScreenAct.this,TicketDetailAct.class);
                // meletakan data kepada Intent
                gotosarangan.putExtra("jenis_tiket","Masjid Cheng Ho");
                startActivity(gotosarangan);
            }
        });

        btn_tiket_penataran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosarangan = new Intent(HomeScreenAct.this,TicketDetailAct.class);
                // meletakan data kepada Intent
                gotosarangan.putExtra("jenis_tiket","Candi Penataran");
                startActivity(gotosarangan);
            }
        });

        btn_tiket_coban_rondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosarangan = new Intent(HomeScreenAct.this,TicketDetailAct.class);
                // meletakan data kepada Intent
                gotosarangan.putExtra("jenis_tiket","Coban Rondo");
                startActivity(gotosarangan);
            }
        });

        btn_tiket_gili_ketapang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosarangan = new Intent(HomeScreenAct.this,TicketDetailAct.class);
                // meletakan data kepada Intent
                gotosarangan.putExtra("jenis_tiket","Gili Ketapang");
                startActivity(gotosarangan);
            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeScreenAct.this,MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });
    }
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
