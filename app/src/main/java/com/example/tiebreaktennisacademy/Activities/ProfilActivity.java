package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    private TextView fullname, emailP, phone, gender, naissance, taille, poid, description, erreurPhoto, erreurOldPassword, erreurNewPassword,
    erreurFullnameE, erreurNaissanceE, erreurPhoneE, erreurGenderE, erreurTailleE, erreurPoidE, cinTitle, cin, adresseTitle, adresse;
    private ImageView back, updatePhoto;
    private Dialog dialog;
    private CircleImageView imageAlert, imageViewProfil;
    private Uri imageUri;
    private AppCompatButton editPassword, editProfil, addInformations;
    private TextInputEditText oldPassword, newPassword, fullnameEdit, emailEdit, naissanceEdit, phoneEdit, tailleEdit, poidEdit;
    private AutoCompleteTextView genderEdit;
    private DatabaseReference databaseReference;
    private DatePickerDialog.OnDateSetListener date;
    private ArrayAdapter<String> arrayAdapter;
    private static final int PICK_IMAGE = 100;
    private Boolean isPhoto = false, isOldPassword = true, isNewPassword = true, isFullname = true, isNaissance = true, isPhone = true, isGender = true, isTaille = true, isPoid = true;
    private String removedPhoneFromString, normalNaissance, kilo, cm;
    private String[] genderItems;

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
        cinTitle = (TextView) findViewById(R.id.title_cin);
        cin = (TextView) findViewById(R.id.cin);
        adresse = (TextView) findViewById(R.id.adress);
        adresseTitle = (TextView) findViewById(R.id.title_adresse);
        description = (TextView) findViewById(R.id.description);

        onclickFunctions();
        initialiseDataBase();
        setDataPersonne();
        setDescriptionPersonne();
        setImagePersonne();
        checkIfSecondDataExist();
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
            public void onDataChange(DataSnapshot snapshot) {
                fullname.setText(snapshot.child(encodeString(email)).child("fullname").getValue(String.class));
                emailP.setText(email);
                String ch = snapshot.child(encodeString(email)).child("phone").getValue(String.class);
                phone.setText("(+216) " + ch.substring(0,2) + " " + ch.substring(2,5) + " " + ch.substring(5,8));
                gender.setText(snapshot.child(encodeString(email)).child("gender").getValue(String.class));
                naissance.setText(stylingDateNaissance(snapshot.child(encodeString(email)).child("naissance").getValue(String.class)));
                taille.setText(snapshot.child(encodeString(email)).child("taille").getValue(String.class) + " cm");
                poid.setText(snapshot.child(encodeString(email)).child("poid").getValue(String.class) + " kg");
                removedPhoneFromString = ch;
                normalNaissance = snapshot.child(encodeString(email)).child("naissance").getValue(String.class);
                kilo = snapshot.child(encodeString(email)).child("poid").getValue(String.class);
                cm = snapshot.child(encodeString(email)).child("taille").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setDescriptionPersonne(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("seconde_name").getValue(String.class) != null){
                    description.setVisibility(View.VISIBLE);
                    description.setText("(" + snapshot.child(encodeString(email)).child("seconde_name").getValue(String.class) + ")");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

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
                showFormEditProfile();
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

        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("photo").getValue(String.class) != null){
                    Glide.with(getApplicationContext()).load(snapshot.child(encodeString(email)).child("photo").getValue(String.class)).centerCrop().fitCenter().into(imageAlert);
                }

                else{
                    imageAlert.setImageResource(R.drawable.user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

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
                    public void onComplete(Task<Uri> task) {
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
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("photo").getValue(String.class) != null){
                    Glide.with(getApplicationContext()).load(snapshot.child(encodeString(email)).child("photo").getValue(String.class)).centerCrop().fitCenter().into(imageViewProfil);
                }

                else{
                    imageViewProfil.setImageResource(R.drawable.user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

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
            isOldPassword = false;
        }

        else if(!isMinuscule(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_minisucle));
            isOldPassword = false;
        }

        else if(!isMajuscule(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_majuscule));
            isOldPassword = false;
        }

        else if(!isChiffre(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_number));
            isOldPassword = false;
        }

        else if(!isLength(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_length));
            isOldPassword = false;
        }

        else{
            setErreurNull(erreurOldPassword);
            isOldPassword = true;
        }
    }

    public void validateNewPassword(){
        if(isEmpty(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_required));
            isNewPassword = false;
        }

        else if(!isMinuscule(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_minisucle));
            isNewPassword = false;
        }

        else if(!isMajuscule(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_majuscule));
            isNewPassword = false;
        }

        else if(!isChiffre(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_number));
            isNewPassword = false;
        }

        else if(!isLength(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_length));
            isNewPassword = false;
        }

        else{
            setErreurNull(erreurNewPassword);
            isNewPassword = true;
        }
    }

    public void validateFormUpdatePassword(){
        if(isEmpty(oldPassword.getText().toString())){
            setErreurText(erreurOldPassword,getString(R.string.password_required));
        }

        else if(isEmpty(newPassword.getText().toString())){
            setErreurText(erreurNewPassword,getString(R.string.password_required));
        }

        else if(isOldPassword == true && isNewPassword == true){
            setErreurNull(erreurOldPassword);
            setErreurNull(erreurNewPassword);
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
            public void onDataChange(DataSnapshot snapshot) {
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
            public void onCancelled(DatabaseError error) {

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

    public void showFormEditProfile(){
        dialog = new Dialog(ProfilActivity.this);
        dialog.setContentView(R.layout.item_edit_profil);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        fullnameEdit = dialog.findViewById(R.id.fullname_edit);
        emailEdit = dialog.findViewById(R.id.email_edit);
        naissanceEdit = dialog.findViewById(R.id.naissance_edit);
        phoneEdit = dialog.findViewById(R.id.mobile_edit);
        genderEdit = dialog.findViewById(R.id.gender_edit);
        poidEdit = dialog.findViewById(R.id.poid_edit);
        tailleEdit = dialog.findViewById(R.id.taille_edit);
        erreurFullnameE = dialog.findViewById(R.id.erreur_fullname_edit);
        erreurNaissanceE = dialog.findViewById(R.id.erreur_naissance_edit);
        erreurPhoneE = dialog.findViewById(R.id.erreur_phone_edit);
        erreurGenderE = dialog.findViewById(R.id.erreur_gender_edit);
        erreurPoidE = dialog.findViewById(R.id.erreur_poid_edit);
        erreurTailleE = dialog.findViewById(R.id.erreur_taille_edit);

        TextView cancel = dialog.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatButton update = dialog.findViewById(R.id.update_profil_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormUpdateForm();
            }
        });

        naissanceEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurerCalandrier();
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                actionSurCalandrier(year, month, dayOfMonth, naissanceEdit);
            }
        };

        setEditProfilInputsInformations();
        onChangeEditProfilFunctions();
        setGenderItems();
        dialog.show();
    }

    public void ouvrirAddInformationsProfilActivity(){
        Intent intent = new Intent(getApplicationContext(), AddInformationsProfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void setEditProfilInputsInformations(){
        fullnameEdit.setText(fullname.getText().toString());
        emailEdit.setText(emailP.getText().toString());
        naissanceEdit.setText(normalNaissance);
        phoneEdit.setText(removedPhoneFromString);
        genderEdit.setText(gender.getText().toString());
        poidEdit.setText(kilo);
        tailleEdit.setText(cm);
    }

    public  void configurerCalandrier(){
        Calendar agenda = Calendar.getInstance();
        int year = agenda.get(Calendar.YEAR);
        int month = agenda.get(Calendar.MONTH);
        int day = agenda.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(ProfilActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, year , month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void actionSurCalandrier(int year, int month, int day, TextInputEditText naissanceE){
        month = month + 1;
        String chaine = year + "-" + month + "-" + day;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(chaine);
            naissanceE.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setGenderItems(){
        genderItems = getResources().getStringArray(R.array.gender_items);
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.items_gender,genderItems);
        genderEdit.setAdapter(arrayAdapter);
    }

    public void validateFormUpdateForm(){
        if(isEmpty(fullnameEdit.getText().toString())){
            setErreurText(erreurFullnameE,getString(R.string.username_required));
        }

        else if(isEmpty(naissanceEdit.getText().toString())){
            setErreurText(erreurNaissanceE,getString(R.string.naissance_required));
        }

        else if(isEmpty(phoneEdit.getText().toString())){
            setErreurText(erreurPhoneE,getString(R.string.phone_required));
        }

        else if(isEmpty(poidEdit.getText().toString())){
            setErreurText(erreurPoidE,getString(R.string.poid_required));
        }

        else if(isEmpty(tailleEdit.getText().toString())){
            setErreurText(erreurTailleE,getString(R.string.taille_required));
        }

        else if(isFullname == true && isNaissance == true && isPhone == true && isGender == true && isTaille == true && isPoid == true){
            setErreurNull(erreurFullnameE);
            setErreurNull(erreurNaissanceE);
            setErreurNull(erreurPhoneE);
            setErreurNull(erreurGenderE);
            setErreurNull(erreurPoidE);
            setErreurNull(erreurTailleE);
            chargementCheckPhoneExist();
        }
    }

    public boolean isLetter(String text) {
        Matcher matcher = Pattern.compile("^[a-z A-Z]*$").matcher(text);
        return matcher.matches();
    }

    public boolean isNumber(String text) {
        Matcher matcher = Pattern.compile("^[0-9]*$").matcher(text);
        return matcher.matches();
    }

    public boolean isPhoneLength(String text){
        return text.length() >= 8;
    }

    public void onChangeEditProfilFunctions(){
        fullnameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateFullname();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        naissanceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateNaissance();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        genderEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateGender();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tailleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateTaille();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        poidEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePoid();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateFullname(){
        if(isEmpty(fullnameEdit.getText().toString())){
            setErreurText(erreurFullnameE,getString(R.string.username_required));
            isFullname = false;
        }

        else if(!isLetter(fullnameEdit.getText().toString())){
            setErreurText(erreurFullnameE,getString(R.string.username_letter));
            isFullname = false;
        }

        else{
            setErreurNull(erreurFullnameE);
            isFullname = true;
        }
    }

    public void validateNaissance(){
        if(isEmpty(naissanceEdit.getText().toString())){
            setErreurText(erreurNaissanceE,getString(R.string.naissance_required));
            isNaissance = false;
        }

        else{
            setErreurNull(erreurNaissanceE);
            isNaissance = true;
        }
    }

    public void validatePhone(){
        if(isEmpty(phoneEdit.getText().toString())){
            setErreurText(erreurPhoneE,getString(R.string.phone_required));
            isPhone = false;
        }

        else if(!isNumber(phoneEdit.getText().toString())){
            setErreurText(erreurPhoneE,getString(R.string.phone_number));
            isPhone = false;
        }

        else if(!isPhoneLength(phoneEdit.getText().toString())){
            setErreurText(erreurPhoneE,getString(R.string.phone_length));
            isPhone = false;
        }

        else{
            setErreurNull(erreurPhoneE);
            isPhone = true;
        }
    }

    public void validateGender(){
        if(isEmpty(genderEdit.getText().toString())){
            setErreurText(erreurGenderE,getString(R.string.gender_required));
            isGender = false;
        }

        else{
            setErreurNull(erreurGenderE);
            isGender = true;
        }
    }

    public void validateTaille(){
        if(isEmpty(tailleEdit.getText().toString())){
            setErreurText(erreurTailleE,getString(R.string.taille_required));
            isTaille = false;
        }

        else if(!isNumber(tailleEdit.getText().toString())){
            setErreurText(erreurTailleE,getString(R.string.taille_number));
            isTaille = false;
        }

        else{
            setErreurNull(erreurTailleE);
            isTaille = true;
        }
    }

    public void validatePoid(){
        if(isEmpty(poidEdit.getText().toString())){
            setErreurText(erreurPoidE,getString(R.string.poid_required));
            isPoid = false;
        }

        else if(!isNumber(poidEdit.getText().toString())){
            setErreurText(erreurPoidE,getString(R.string.poid_number));
            isPoid = false;
        }

        else{
            setErreurNull(erreurPoidE);;
            isPoid = true;
        }
    }

    public void updateProfile(ProgressDialog progressDialog){
        databaseReference.child("users").child(encodeString(emailP.getText().toString())).child("fullname").setValue(fullnameEdit.getText().toString());
        databaseReference.child("users").child(encodeString(emailP.getText().toString())).child("gender").setValue(genderEdit.getText().toString());
        databaseReference.child("users").child(encodeString(emailP.getText().toString())).child("naissance").setValue(naissanceEdit.getText().toString());
        databaseReference.child("users").child(encodeString(emailP.getText().toString())).child("phone").setValue(phoneEdit.getText().toString());
        databaseReference.child("users").child(encodeString(emailP.getText().toString())).child("poid").setValue(poidEdit.getText().toString());
        databaseReference.child("users").child(encodeString(emailP.getText().toString())).child("taille").setValue(tailleEdit.getText().toString());
        refreshDataActivity();
        dialog.dismiss();
        progressDialog.dismiss();
        showSuccessUpdateProfileNotification();
    }

    public void showSuccessUpdateProfileNotification(){
        dialog = new Dialog(ProfilActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.profil_changed);
        desc.setText(R.string.profil_changed_desc);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void chargementCheckPhoneExist(){
        final ProgressDialog progressDialog = new ProgressDialog(ProfilActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.update_profil_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                checkIfPhoneExist(progressDialog);
            }
        }).start();
    }

    public void checkIfPhoneExist(ProgressDialog progressDialog){
        databaseReference.child("users").orderByChild("phone").equalTo(phoneEdit.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue() != null && !snapshot.hasChild(encodeString(emailEdit.getText().toString()))){
                    setErreurText(erreurPhoneE,getString(R.string.phone_exist));
                    progressDialog.dismiss();
                }

                else{
                    setErreurNull(erreurPhoneE);
                    updateProfile(progressDialog);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void refreshDataActivity(){
        fullname.setText(fullnameEdit.getText().toString());
        String ch = phoneEdit.getText().toString();
        phone.setText("+216) " + ch.substring(0,2) + " " + ch.substring(2,5) + " " + ch.substring(5,8));
        gender.setText(genderEdit.getText().toString());
        naissance.setText(stylingDateNaissance(naissanceEdit.getText().toString()));
        taille.setText(tailleEdit.getText().toString() + " cm");
        poid.setText(poidEdit.getText().toString() + " kg");
    }

    public void checkIfSecondDataExist(){
        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChild(encodeString(emailP.getText().toString()))){
                    setTextViewVisible(cinTitle);
                    setTextViewVisible(cin);
                    setTextViewVisible(adresseTitle);
                    setTextViewVisible(adresse);
                    setSecondDataPersonne();
                }

                else{
                    setTextViewInvisible(cinTitle);
                    setTextViewInvisible(cin);
                    setTextViewInvisible(adresseTitle);
                    setTextViewInvisible(adresse);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setTextViewVisible(TextView input){
        input.setVisibility(View.VISIBLE);
    }

    public void setTextViewInvisible(TextView input){
        input.setVisibility(View.INVISIBLE);
    }

    public void setSecondDataPersonne(){
        databaseReference.child("second_infos_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cin.setText(snapshot.child(encodeString(emailP.getText().toString())).child("cin").getValue(String.class));
                adresse.setText(snapshot.child(encodeString(emailP.getText().toString())).child("adresse").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}