package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ControlAccountActivity extends AppCompatActivity {
    private TextView copiright;
    private ImageView back;
    private RadioButton delete, disable;
    private Dialog dialog;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_account);

        copiright = (TextView) findViewById(R.id.copyright_app);
        back = (ImageView) findViewById(R.id.back);
        delete = (RadioButton) findViewById(R.id.radioSupprimer);
        disable = (RadioButton) findViewById(R.id.radioDesactiver);

        setCopyrightText();
        onclickFunctions();
        initialiseDataBase();
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPersonalAccountActivity();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gererDeleteRadio();
                showQuestionDeleteNotification();
            }
        });

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gererDisableRadio();
                showQuestionDisableNotification();
            }
        });

    }

    public void ouvrirPersonalAccountActivity(){
        Intent intent = new Intent(getApplicationContext(), PersonalAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirPersonalAccountActivity();
    }

    public void gererDeleteRadio(){
        if(delete.isChecked()){
            removeCheckedRadio(disable);
        }
    }

    public void removeCheckedRadio(RadioButton radio){
        radio.setChecked(false);
    }

    public void gererDisableRadio(){
        if(disable.isChecked()){
            removeCheckedRadio(delete);
        }
    }

    public void showQuestionDisableNotification(){
        dialog = new Dialog(ControlAccountActivity.this);
        dialog.setContentView(R.layout.item_question_control_account);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView desc = dialog.findViewById(R.id.desc_title);
        desc.setText(getString(R.string.desc_item_disable));

        TextView cancel = dialog.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatButton control = dialog.findViewById(R.id.control_btn);
        control.setText(getString(R.string.disable));
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chargementDisableAccount();
            }
        });

        dialog.show();
    }

    public void chargementDisableAccount(){
        final ProgressDialog progressDialog = new ProgressDialog(ControlAccountActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.disable_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                disableAccount(progressDialog);
            }
        }).start();
    }

    public void disableAccount(ProgressDialog progressDialog){
        databaseReference.child("compte_users").child(encodeString(emailSession())).child("email").setValue(encodeString(emailSession()));
        databaseReference.child("compte_users").child(encodeString(emailSession())).child("active").setValue("false");
        updateJournal("Disable",encodeString(emailSession()),progressDialog);
    }

    public void updateJournal(String text, String email, ProgressDialog progressDialog){
        String key = databaseReference.child("journal_users").push().getKey();
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("action").setValue(text);
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("date").setValue(getCurrentDate());
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("time").setValue(getCurrentTime());
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("phone").setValue(getAppareilUsed());
        checkIfFirebaseUpdated(progressDialog);
    }

    public String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return (formatter.format(date));
    }

    public String getCurrentTime(){
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return (formatter.format(time));
    }

    public String getAppareilUsed(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return (model + ".");
        }

        else {
            return (manufacturer + " " + model + ".");
        }
    }

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void checkIfFirebaseUpdated(ProgressDialog progressDialog){
        databaseReference.child("compte_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(encodeString(emailSession())).child("active").getValue(String.class).equals("false")){
                    showSuccessNotificationFireBaseUpdated();
                }

                else{
                    showErreurNotificationFireBaseNotUpdated();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void showSuccessNotificationFireBaseUpdated(){
        dialog = new Dialog(ControlAccountActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.account_desactiver_success);
        desc.setText(R.string.desactive_success);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logoutSession();
                ouvrirLoginActivity();
            }
        });

        dialog.show();
    }

    public void ouvrirLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void showErreurNotificationFireBaseNotUpdated(){
        dialog = new Dialog(ControlAccountActivity.this);
        dialog.setContentView(R.layout.item_erreur);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        AppCompatButton cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView desc = dialog.findViewById(R.id.desc_title_erreur);
        desc.setText(R.string.error_account_disabled);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.account_desactiver_error));

        dialog.show();
    }

    public void logoutSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();

        if(session.checkEmailApplication() != null){
            session.removeSessionEmail();
        }
    }

    public void showQuestionDeleteNotification(){
        dialog = new Dialog(ControlAccountActivity.this);
        dialog.setContentView(R.layout.item_question_control_account);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView desc = dialog.findViewById(R.id.desc_title);
        desc.setText(getString(R.string.desc_item_delete));

        TextView cancel = dialog.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        AppCompatButton control = dialog.findViewById(R.id.control_btn);
        control.setText(getString(R.string.delete));
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chargementDeleteAccount();
            }
        });

        dialog.show();
    }

    public void chargementDeleteAccount(){
        final ProgressDialog progressDialog = new ProgressDialog(ControlAccountActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.delete_account_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                gestionDeleteAccount(progressDialog);
            }
        }).start();
    }

    public void gestionDeleteAccount(ProgressDialog progressDialog){
        deleteReservations(emailSession());
        deleteJournales(emailSession());
        deleteStorageImage(emailSession());
        deleteImages(emailSession());
        deleteSecondeInfoUsers(emailSession());
        deleteUsers(emailSession());
        deleteAccount(emailSession(), progressDialog);
    }

    public void deleteReservations(String email){
        databaseReference.child("reservations").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteJournales(String email){
        databaseReference.child("journal_users").child(encodeString(email)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteStorageImage(String email){
        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(encodeString(email)).child("photo").getValue(String.class) != null){
                    String url = snapshot.child(encodeString(email)).child("photo").getValue(String.class);
                    firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference photoRef = firebaseStorage.getReferenceFromUrl(url);
                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception exception) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteImages(String email){
        databaseReference.child("images_users").child(encodeString(email)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteSecondeInfoUsers(String email){
        databaseReference.child("second_infos_users").child(encodeString(email)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteUsers(String email){
        databaseReference.child("users").child(encodeString(email)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void deleteAccount(String email, ProgressDialog progressDialog){
        databaseReference.child("compte_users").child(encodeString(email)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    data.getRef().removeValue();
                }
                progressDialog.dismiss();
                showSuccessNotificationCompteDeleted();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void showSuccessNotificationCompteDeleted(){
        dialog = new Dialog(ControlAccountActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.account_deleted_success);
        desc.setText(R.string.delete_success);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                logoutSession();
                ouvrirLoginActivity();
            }
        });

        dialog.show();
    }
}