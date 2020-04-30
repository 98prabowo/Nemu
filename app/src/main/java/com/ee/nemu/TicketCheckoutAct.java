package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Objects;
import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_checkout, btn_pls, btn_min;
    LinearLayout btn_back_four;
    TextView txt_jumlah_tiket, txt_total_saldo, txt_total_harga, nama_wisata, lokasi, deskripsi;
    ImageView warning;
    Integer val_jumlah_tiket = 1, total_saldo = 0, val_total_harga = 0, val_harga_tiket = 0, sisa_ballance;
    String date_wisata = "", time_wisata = "";

    DatabaseReference reference, reference1, reference2, reference3;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    // generate nomor integer secara random agar nomor transaksi bersifat unik
    Integer nomor_transaksi = new Random().nextInt();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        // mengambil data dari Intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        // load component
        btn_checkout = findViewById(R.id.btn_checkout);
        btn_back_four = findViewById(R.id.btn_back_four);
        btn_pls = findViewById(R.id.btn_pls);
        btn_min = findViewById(R.id.btn_min);
        txt_jumlah_tiket = findViewById(R.id.txt_jumlah_tiket);
        warning = findViewById(R.id.warning);

        txt_total_saldo = findViewById(R.id.txt_total_saldo);
        txt_total_harga = findViewById(R.id.txt_total_harga);

        nama_wisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        deskripsi = findViewById(R.id.deskripsi);

        // setting value default component
        txt_jumlah_tiket.setText(val_jumlah_tiket.toString());
        warning.setVisibility(View.GONE);

        // set intro
        btn_min.animate().alpha(0).setDuration(300).start();
        btn_min.setEnabled(false);

        // mengambil data user dari firebase
        reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total_saldo = Integer.valueOf(dataSnapshot.child("user_ballance").getValue().toString());
                txt_total_saldo.setText("US$ " + total_saldo+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // mengambil data dari firebase berdasarkan Intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // menimpa data yang ada dengan data yang baru
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                deskripsi.setText(dataSnapshot.child("description").getValue().toString());

                date_wisata= dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                val_harga_tiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                val_total_harga = val_harga_tiket * val_jumlah_tiket;
                txt_total_harga.setText("US$ " + val_total_harga+"");
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_pls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_jumlah_tiket+=1;
                txt_jumlah_tiket.setText(val_jumlah_tiket.toString());

                val_total_harga = val_harga_tiket * val_jumlah_tiket;
                txt_total_harga.setText("US$ " + val_total_harga+"");

                if (val_jumlah_tiket > 1){
                    btn_min.animate().alpha(1).setDuration(300).start();
                    btn_min.setEnabled(true);
                    val_total_harga = val_harga_tiket * val_jumlah_tiket;
                    txt_total_harga.setText("US$ " + val_total_harga+"");
                }
                if (val_total_harga > total_saldo){
                    btn_checkout.animate().translationY(250).alpha(0).setDuration(350).start();
                    btn_checkout.setEnabled(false);
                    txt_total_saldo.setTextColor(Color.parseColor("#D1206B"));
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                val_jumlah_tiket-=1;
                txt_jumlah_tiket.setText(val_jumlah_tiket.toString());

                val_total_harga = val_harga_tiket * val_jumlah_tiket;
                txt_total_harga.setText("US$ " + val_total_harga+"");

                if (val_jumlah_tiket < 2){
                    btn_min.animate().alpha(0).setDuration(300).start();
                    btn_min.setEnabled(false);
                }
                if (val_total_harga < total_saldo){
                    btn_checkout.animate().translationY(0).alpha(1).setDuration(350).start();
                    btn_checkout.setEnabled(true);
                    txt_total_saldo.setTextColor(Color.parseColor("#145758"));
                    warning.setVisibility(View.GONE);
                }
            }
        });

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menyimpan data user ke firebase dan membuat table baru "Tiketku"
                reference2 = FirebaseDatabase.getInstance().getReference()
                        .child("Tiketku").child(username_key_new).child(nama_wisata.getText().toString() + nomor_transaksi);
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference2.getRef().child("id_tiket").setValue(nama_wisata.getText().toString());
                        reference2.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference2.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference2.getRef().child("description").setValue(deskripsi.getText().toString());
                        reference2.getRef().child("jumlah_tiket").setValue(val_jumlah_tiket.toString());
                        reference2.getRef().child("date_wisata").setValue(date_wisata);
                        reference2.getRef().child("time_wisata").setValue(time_wisata);

                        startActivity(new Intent(TicketCheckoutAct.this,SuccesBuyAct.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // update data balance kepada users (yang saat ini login)
                // mengambil data dari firebase
                reference3 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_ballance = total_saldo - val_total_harga;
                        reference3.getRef().child("user_ballance").setValue(sisa_ballance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_back_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
