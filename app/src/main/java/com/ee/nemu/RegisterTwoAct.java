package com.ee.nemu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoAct extends AppCompatActivity {

    LinearLayout btn_back_two;
    Button btn_continue_two, btn_add_photo;
    ImageView photo_regis_user;
    EditText nama_depan, nama_belakang, passion;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();

        btn_back_two = findViewById(R.id.btn_back_two);
        btn_continue_two = findViewById(R.id.btn_continue_two);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        photo_regis_user = findViewById(R.id.photo_regis_user);
        nama_depan = findViewById(R.id.nama_depan);
        nama_belakang = findViewById(R.id.nama_belakang);
        passion = findViewById(R.id.passion);

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        btn_back_two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_continue_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ubah state menjadi loading
                btn_continue_two.setEnabled(false);
                btn_continue_two.setText(R.string.loading);

                final String nama_depanku = nama_depan.getText().toString();
                final String nama_belakangku = nama_belakang.getText().toString();
                final String passionku = passion.getText().toString();

                if (nama_depanku.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nama depanmu belum ada"
                            , Toast.LENGTH_SHORT).show();
                    // ubah state menjadi normal
                    btn_continue_two.setEnabled(true);
                    btn_continue_two.setText(R.string.continue_label);
                }
                else {
                    if (nama_belakangku.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Nama belakangmu belum ada"
                                , Toast.LENGTH_SHORT).show();
                        // ubah state menjadi normal
                        btn_continue_two.setEnabled(true);
                        btn_continue_two.setText(R.string.continue_label);
                    }
                    else {
                        if (passionku.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Hobbimu belum ada"
                                    , Toast.LENGTH_SHORT).show();
                            // ubah state menjadi normal
                            btn_continue_two.setEnabled(true);
                            btn_continue_two.setText(R.string.continue_label);
                        }
                        else {
                            // menyimpan ke firebase
                            reference = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(username_key_new.toString());
                            storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

                            // validasi untuk file (adakah?)
                            if (photo_location != null) {
                                final StorageReference storageReference1 =
                                        storage.child(System.currentTimeMillis() + "." +
                                                getFileExtension(photo_location));

                                storageReference1.putFile(photo_location)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String uri_photo = uri.toString();
                                                        reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                                        reference.getRef().child("nama_depan").setValue(nama_depan.getText().toString());
                                                        reference.getRef().child("nama_belakang").setValue(nama_belakang.getText().toString());
                                                        reference.getRef().child("passion").setValue(passion.getText().toString());
                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        // berpindah activity
                                                        Intent gotosuccessregister = new Intent(RegisterTwoAct.this,SuccesRegisterAct.class);
                                                        startActivity(gotosuccessregister);
                                                    }
                                                });

                                            }
                                        });
                            }
                        }
                    }



                }


            }
        });
    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(photo_regis_user);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
