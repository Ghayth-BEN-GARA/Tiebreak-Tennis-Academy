package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    private TextView fullname, emailP, phone, gender, naissance, taille, poid;
    private ImageView back, updatePhoto;
    private Dialog dialog;
    private CircleImageView imageAlert, imageViewProfil;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        fullname = (TextView) findViewById(R.id.fullname);
        emailP = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.mobile);
        gender = (TextView) findViewById(R.id.gender);
        naissance = (TextView) findViewById(R.id.naissance);
        taille = (TextView) findViewById(R.id.taille);
        poid = (TextView) findViewById(R.id.poid);
        back = (ImageView) findViewById(R.id.back);
        updatePhoto = (ImageView) findViewById(R.id.update_photo);
        imageViewProfil = (CircleImageView) findViewById(R.id.photo_profil);

        onclickFunctions();
        initialiseDataBase();
        setDataPersonne();
        setImagePersonne();
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void setDataPersonne(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullname.setText(snapshot.child(encodeString(email)).child("fullname").getValue(String.class));
                emailP.setText(email);
                String ch = snapshot.child(encodeString(email)).child("phone").getValue(String.class);
                phone.setText("(+216) " + ch.substring(0,2) + " " + ch.substring(2,5) + " " + ch.substring(5,8));
                gender.setText(snapshot.child(encodeString(email)).child("gender").getValue(String.class));
                naissance.setText(stylingDateNaissance(snapshot.child(encodeString(email)).child("naissance").getValue(String.class)));
                taille.setText(snapshot.child(encodeString(email)).child("taille").getValue(String.class) + " cm");
                poid.setText(snapshot.child(encodeString(email)).child("poid").getValue(String.class) + " kg");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String stylingDateNaissance(String dateNaissance){
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat formatter2 = new SimpleDateFormat("EEE dd MMM yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter1.parse(dateNaissance);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter2.format(date);
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });

        updatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormUpdatePhotoProfil();
            }
        });
    }


    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
    }

    public void showFormUpdatePhotoProfil(){
        dialog = new Dialog(ProfilActivity.this);
        dialog.setContentView(R.layout.item_update_image);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView cancel = dialog.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imageAlert = dialog.findViewById(R.id.image_profil);
        imageAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        AppCompatButton updateButtonAlert = (AppCompatButton) dialog.findViewById(R.id.update_btn);
        updateButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chargementUpdateImage();
            }
        });

        dialog.show();
    }

    public void selectImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0 && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageAlert.setImageURI(imageUri);
        }
    }

    public void chargementUpdateImage(){
        final ProgressDialog progressDialog = new ProgressDialog(ProfilActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.update_image_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                updateImageToFirebase(progressDialog);
            }
        }).start();
    }

    public void updateImageToFirebase(ProgressDialog progressDialog){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(fullname.getText().toString()).child(imageUri.getLastPathSegment());
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadURI = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            String result = task.getResult().toString();
                            databaseReference.child("images_users").child(encodeString(email)).child("email").setValue(encodeString(email));
                            databaseReference.child("images_users").child(encodeString(email)).child("photo").setValue(result);
                            setImagePersonne();
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    public void setImagePersonne(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();


        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("photo").getValue(String.class) != null){

                    Glide
                            .with(getApplicationContext())
                            .load(snapshot.child(encodeString(email)).child("photo").getValue(String.class))
                            .centerCrop()
                            .into(imageViewProfil);
                }

                else{
                    imageViewProfil.setImageResource(R.drawable.user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}