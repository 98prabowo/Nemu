package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {

    LinearLayout btn_back_one;
    Button btn_continue_one;
    EditText username, password, email_address;
    DatabaseReference reference, reference_username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        btn_back_one = findViewById(R.id.btn_back_one);
        btn_continue_one = findViewById(R.id.btn_continue_one);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        btn_back_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_continue_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ubah state menjadi loading
                btn_continue_one.setEnabled(false);
                btn_continue_one.setText(R.string.loading);

                final String usernameku = username.getText().toString();
                final String passwordku = password.getText().toString();
                final String email_addressku = email_address.getText().toString();

                // mengambil usernam pada database
                reference_username = FirebaseDatabase.getInstance()
                        .getReference().child("Users").child(username.getText().toString());
                reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // jika usernam ada
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "Username sudah digunakan!"
                                    , Toast.LENGTH_SHORT).show();
                            // ubah state menjadi normal
                            btn_continue_one.setEnabled(true);
                            btn_continue_one.setText(R.string.continue_label);
                        }
                        else {
                            if (usernameku.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Username anda belum ada"
                                        , Toast.LENGTH_SHORT).show();
                                // ubah state menjadi normal
                                btn_continue_one.setEnabled(true);
                                btn_continue_one.setText(R.string.continue_label);
                            }
                            else {
                                if (passwordku.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Password anda belum ada"
                                            , Toast.LENGTH_SHORT).show();
                                    // ubah state menjadi normal
                                    btn_continue_one.setEnabled(true);
                                    btn_continue_one.setText(R.string.continue_label);
                                }
                                else {
                                    if (email_addressku.isEmpty()){
                                        Toast.makeText(getApplicationContext(), "Email anda belum ada"
                                                , Toast.LENGTH_SHORT).show();
                                        // ubah state menjadi normal
                                        btn_continue_one.setEnabled(true);
                                        btn_continue_one.setText(R.string.continue_label);
                                    }
                                    else {
                                        // menyimpan data kepada local storage (handphone)
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, username.getText().toString());
                                        editor.apply();

                                        // menyimpan data kepada database
                                        reference = FirebaseDatabase.getInstance().getReference()
                                                .child("Users").child(username.getText().toString());
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                                dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                                dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                                dataSnapshot.getRef().child("user_ballance").setValue(800);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        Intent gotoregistertwo = new Intent(RegisterOneAct.this,RegisterTwoAct.class);
                                        startActivity(gotoregistertwo);
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }


        });
    }
}
