package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInAct extends AppCompatActivity {

    TextView txt_new_account;
    Button btn_sign_in;
    EditText usernameku, passwordku;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txt_new_account = findViewById(R.id.txt_new_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        usernameku = findViewById(R.id.usernameku);
        passwordku = findViewById(R.id.passwordku);

        txt_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(SignInAct.this,RegisterOneAct.class);
                startActivity(gotoregisterone);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ubah state menjadi loading
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText(R.string.loading);

                final String username = usernameku.getText().toString();
                final String password = passwordku.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Masukan username anda"
                            , Toast.LENGTH_SHORT).show();
                    // ubah state menjadi normal
                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText(R.string.signin);

                }
                else {
                    if (password.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Masukan passwor anda"
                                , Toast.LENGTH_SHORT).show();
                        // ubah state menjadi normal
                        btn_sign_in.setEnabled(true);
                        btn_sign_in.setText(R.string.signin);
                    }

                    else {
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // ambil data password dari firebase
                                    String passswordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    // validasi password dengan firebase
                                    if (password.equals(passswordFromFirebase)) {
                                        // Simpan username (key) pada local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, usernameku.getText().toString());
                                        editor.apply();

                                        // Pindah activity
                                        startActivity(new Intent(SignInAct.this,HomeScreenAct.class));
                                        finish();

                                    }else {
                                        Toast.makeText(getApplicationContext(), "Password anda salah"
                                                , Toast.LENGTH_SHORT).show();
                                        // ubah state menjadi normal
                                        btn_sign_in.setEnabled(true);
                                        btn_sign_in.setText(R.string.signin);
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Username belum terdaftar"
                                            , Toast.LENGTH_SHORT).show();
                                    // ubah state menjadi normal
                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText(R.string.signin);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

            }
        });
    }
}
