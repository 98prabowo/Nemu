package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyProfileAct extends AppCompatActivity {

    Button btn_edit_profile, btn_sign_out;
    LinearLayout item_my_tiket;
    ImageView photo_profile, btn_back_home;
    TextView nama_depan, nama_belakang, passion;

    DatabaseReference reference, reference2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView my_tiket_place;
    ArrayList<MyTiket> list;
    TiketAdapter tiketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getUsernameLocal();

        my_tiket_place = findViewById(R.id.my_tiket_place);
        my_tiket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTiket>();

        item_my_tiket = findViewById(R.id.item_my_tiket);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_back_home = findViewById(R.id.btn_back_home);
        btn_sign_out = findViewById(R.id.btn_sign_out);
        photo_profile = findViewById(R.id.photo_profile);
        nama_depan = findViewById(R.id.nama_depan);
        nama_belakang = findViewById(R.id.nama_belakang);
        passion = findViewById(R.id.passion);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_depan.setText(dataSnapshot.child("nama_depan").getValue().toString());
                nama_belakang.setText(dataSnapshot.child("nama_belakang").getValue().toString());
                passion.setText(dataSnapshot.child("passion").getValue().toString());
                Picasso.with(MyProfileAct.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString()).centerCrop()
                        .fit().into(photo_profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoeditprofile = new Intent(MyProfileAct.this, EditProfileAct.class);
                startActivity(gotoeditprofile);
            }
        });

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gobackhome = new Intent(MyProfileAct.this, HomeScreenAct.class);
                startActivity(gobackhome);
            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference().child("Tiketku").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MyTiket p = dataSnapshot1.getValue(MyTiket.class);
                    list.add(p);
                }
                tiketAdapter = new TiketAdapter(MyProfileAct.this, list);
                my_tiket_place.setAdapter(tiketAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menghapus value username local
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                // berpindah activity
                Intent gosignout = new Intent(MyProfileAct.this, SignInAct.class);
                startActivity(gosignout);
                finish();
            }
        });

    }

        public void getUsernameLocal() {
            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
            username_key_new = sharedPreferences.getString(username_key, "");
        }
}
