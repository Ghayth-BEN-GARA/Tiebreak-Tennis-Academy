package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    private TextView fullname, emailP, phone, gender, naissance, taille, poid, erreurPhoto, erreurOldPassword, erreurNewPassword;
    private ImageView back, updatePhoto;
    private Dialog dialog;
    private CircleImageView imageAlert, imageViewProfil;
    private Uri imageUri;
    private AppCompatButton editPassword, editProfil, addInformations;
    private TextInputLayout inputLayoutOldPassword, inputLayoutNewPassword;
    private TextInputEditText oldPassword, newPassword;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE = 100;
    private Boolean isPhoto = false, isOldPassword = false, isNewPassword = false;

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
        editPassword = (AppCompatButton) findViewById(R.id.btn_edit_password);
        editProfil = (AppCompatButton) findViewById(R.id.edit_profil);
        addInformations = (AppCompatButton) findViewById(R.id.adding_profil);

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

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFormUpdatePassword();
            }
        });

        editProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirEditProfilActivity();
            }
        });

        addInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAddInformationsProfilActivity();
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

        erreurPhoto = (TextView) dialog.findViewById(R.id.erreur_photo);

        AppCompatButton updateButtonAlert = (AppCompatButton) dialog.findViewById(R.id.update_btn);
        updateButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfImageVide(dialog);
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
            isPhoto = true;
            setErreurNull(erreurPhoto);
        }
    }

    public void checkIfImageVide(Dialog dialog){
        if(isPhoto == false){
            setErreurText(erreurPhoto,getString(R.string.image_reqired));
        }

        else{
            setErreurNull(erreurPhoto);
            dialog.dismiss();
            chargementUpdateImage();
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

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(encodeString(email)).child(imageUri.getLastPathSegment());
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
                            isPhoto = false;
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

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public void showFormUpdatePassword(){
        dialog = new Dialog(ProfilActivity.this);
        dialog.setContentView(R.layout.item_update_password);
        dialog.getWindow().getDecorView().setLeft(30);
        dialog.getWindow().getDecorView().setRight(30);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        oldPassword = (TextInputEditText) dialog.findViewById(R.id.old_password);
        newPassword = (TextInputEditText) dialog.findViewById(R.id.new_password);
        inputLayoutOldPassword = (TextInputLayout) dialog.findViewById(R.id.inputlayout_old_password);
        inputLayoutNewPassword = (TextInputLayout) dialog.findViewById(R.id.inputlayout_new_password);
        erreurOldPassword = (TextView) dialog.findViewById(R.id.erreur_old_password);
        erreurNewPassword = (TextView) dialog.findViewById(R.id.erreur_new_password);

        AppCompatButton update = (AppCompatButton) dialog.findViewById(R.id.update_password_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormUpdatePassword();
            }
        });

        onChangeFunctions();

        dialog.show();
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public boolean isMinuscule(String text) {
        Matcher matcher = Pattern.compile("((?=.*[a-z]).{1,100})").matcher(text);
        return matcher.matches();
    }

    public boolean isMajuscule(String text) {
        Matcher matcher = Pattern.compile("((?=.*[A-Z]).{1,100})").matcher(text);
        return matcher.matches();
    }

    public boolean isChiffre(String text) {
        Matcher matcher = Pattern.compile("((?=.*[0-9]).{1,100})").matcher(text);
        return matcher.matches();
    }

    public boolean isLength(String text){
        return text.length() >= 5;
    }

    public void onChangeFunctions(){
        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateOldPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNewPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateOldPassword(){
        if(isEmpty(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_required));
            setInputLayoutErrors(inputLayoutOldPassword,oldPassword);
            isOldPassword = false;
        }

        else if(!isMinuscule(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_minisucle));
            setInputLayoutErrors(inputLayoutOldPassword,oldPassword);
            isOldPassword = false;
        }

        else if(!isMajuscule(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_majuscule));
            setInputLayoutErrors(inputLayoutOldPassword,oldPassword);
            isOldPassword = false;
        }

        else if(!isChiffre(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_number));
            setInputLayoutErrors(inputLayoutOldPassword,oldPassword);
            isOldPassword = false;
        }

        else if(!isLength(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_length));
            setInputLayoutErrors(inputLayoutOldPassword,oldPassword);
            isOldPassword = false;
        }

        else{
            setErreurNull(erreurOldPassword);
            setInputLayoutNormal(inputLayoutOldPassword,oldPassword);
            isOldPassword = true;
        }
    }

    public void validateNewPassword(){
        if(isEmpty(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_required));
            setInputLayoutErrors(inputLayoutNewPassword,newPassword);
            isNewPassword = false;
        }

        else if(!isMinuscule(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_minisucle));
            setInputLayoutErrors(inputLayoutNewPassword,newPassword);
            isNewPassword = false;
        }

        else if(!isMajuscule(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_majuscule));
            setInputLayoutErrors(inputLayoutNewPassword,newPassword);
            isNewPassword = false;
        }

        else if(!isChiffre(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_number));
            setInputLayoutErrors(inputLayoutNewPassword,newPassword);
            isNewPassword = false;
        }

        else if(!isLength(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_length));
            setInputLayoutErrors(inputLayoutNewPassword,newPassword);
            isNewPassword = false;
        }

        else{
            setErreurNull(erreurNewPassword);
            setInputLayoutNormal(inputLayoutNewPassword,newPassword);
            isNewPassword = true;
        }
    }

    public void setInputLayoutErrors(TextInputLayout input, TextInputEditText text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input.setBackground(getDrawable(R.drawable.edit_text_background_erreur));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(com.google.android.material.R.color.design_default_color_error)));
            }
        }
    }

    public void setInputLayoutNormal(TextInputLayout input, TextInputEditText text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            input.setBackground(getDrawable(R.drawable.edi_text_background));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                text.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.black)));
            }
        }
    }

    public void validateFormUpdatePassword(){
        if(isEmpty(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_required));
            setInputLayoutErrors(inputLayoutOldPassword,oldPassword);
        }

        else if(isEmpty(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_required));
            setInputLayoutErrors(inputLayoutNewPassword,newPassword);
        }

        else if(isOldPassword == true && isNewPassword == true){
            setErreurNull(erreurOldPassword);
            setErreurNull(erreurNewPassword);
            setInputLayoutNormal(inputLayoutOldPassword,oldPassword);
            setInputLayoutNormal(inputLayoutNewPassword,newPassword);
            chargementUpdatePassword();
        }
    }

    public void chargementUpdatePassword(){
        final ProgressDialog progressDialog = new ProgressDialog(ProfilActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.update_password_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkRightOldPassword(progressDialog);
            }
        }).start();
    }

    public void checkRightOldPassword(ProgressDialog progressDialog){
        String oldPasswordHashed = hashPassword(oldPassword.getText().toString());
        String newPasswordHashed = hashPassword(newPassword.getText().toString());
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("password").getValue(String.class).equals(oldPasswordHashed)){
                    if(snapshot.child(encodeString(email)).child("password").getValue(String.class).equals(newPasswordHashed)){
                        setErreurText(erreurNewPassword,getString(R.string.old_new_password));
                        progressDialog.dismiss();
                    }

                    else{
                        setErreurNull(erreurNewPassword);
                        updatePassword(progressDialog,email);
                    }
                }

                else{
                    setErreurText(erreurOldPassword,getString(R.string.old_password_invalid));
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static String hashPassword(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void updatePassword(ProgressDialog progressDialog, String email){
        if(isPasswordChanged(email)){
            progressDialog.dismiss();
            dialog.dismiss();
            showSuccessNotification();
        }
    }

    public boolean isPasswordChanged(String email){
        String passwordHashed = hashPassword(newPassword.getText().toString());

        databaseReference.child("users").child(encodeString(email)).child("password").setValue(passwordHashed);
        return true;
    }

    public void showSuccessNotification(){
        dialog = new Dialog(ProfilActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.password_changed);
        desc.setText(R.string.password_changed_desc);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void ouvrirEditProfilActivity(){
        Intent intent = new Intent(getApplicationContext(), EditProfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirAddInformationsProfilActivity(){
        Intent intent = new Intent(getApplicationContext(), AddInformationsProfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}