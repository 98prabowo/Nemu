package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class TicketDetailAct extends AppCompatActivity {

    Button btn_buy_ticket;
    LinearLayout btn_back_three;
    TextView ticket_title, ticket_address, photo_spot,
            sub_photo_spot, wifi_ticket, sub_wifi_ticket,
            festival_ticket, sub_festival_ticket, description;
    ImageView bg_ticket;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        // mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_back_three = findViewById(R.id.btn_back_three);
        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        bg_ticket = findViewById(R.id.bg_ticket);

        ticket_title = findViewById(R.id.ticket_title);
        ticket_address = findViewById(R.id.ticket_address);
        photo_spot = findViewById(R.id.photo_spot);
        sub_photo_spot = findViewById(R.id.sub_photo_spot);
        wifi_ticket = findViewById(R.id.wifi_ticket);
        sub_wifi_ticket = findViewById(R.id.sub_wifi_ticket);
        festival_ticket = findViewById(R.id.festival_ticket);
        sub_festival_ticket = findViewById(R.id.sub_festival_ticket);
        description = findViewById(R.id.description);

        // mengambil data dari firebase berdsarkan Intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // menimpa data yang ada dengan data yang baru
                ticket_title.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                ticket_address.setText(dataSnapshot.child("lokasi").getValue().toString());
                photo_spot.setText(dataSnapshot.child("photo_spot").getValue().toString());
                sub_photo_spot.setText(dataSnapshot.child("sub_photo_spot").getValue().toString());
                wifi_ticket.setText(dataSnapshot.child("wifi").getValue().toString());
                sub_wifi_ticket.setText(dataSnapshot.child("sub_wifi").getValue().toString());
                festival_ticket.setText(dataSnapshot.child("festival").getValue().toString());
                sub_festival_ticket.setText(dataSnapshot.child("sub_festival").getValue().toString());
                description.setText(dataSnapshot.child("description").getValue().toString());

                Picasso.with(TicketDetailAct.this)
                        .load(Objects.requireNonNull(dataSnapshot.child("url_thumbnail").getValue()).toString()).centerCrop()
                        .fit().into(bg_ticket);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotocheckout = new Intent(TicketDetailAct.this,TicketCheckoutAct.class);
                gotocheckout.putExtra("jenis_tiket",jenis_tiket_baru);
                startActivity(gotocheckout);
            }
        });

        }
}
