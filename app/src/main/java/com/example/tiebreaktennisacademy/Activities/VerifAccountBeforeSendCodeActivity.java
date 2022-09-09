package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.tiebreaktennisacademy.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import de.hdodenhof.circleimageview.CircleImageView;

public class VerifAccountBeforeSendCodeActivity extends AppCompatActivity {
    private ImageView back;
    private RadioButton email, numero, radioButton;
    private CircleImageView photo;
    private TextView fullname, erreurMethode;
    private AppCompatButton notYou, next;
    private RadioGroup radioGroup;
    private Dialog dialog;
    private DatabaseReference databaseReference;
    private int checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_account_before_send_code);

        back = (ImageView) findViewById(R.id.back);
        email = (RadioButton) findViewById(R.id.email);
        numero = (RadioButton) findViewById(R.id.numero);
        photo = (CircleImageView) findViewById(R.id.photo_profil);
        fullname = (TextView) findViewById(R.id.fullname);
        notYou = (AppCompatButton) findViewById(R.id.cancel_btn);
        erreurMethode = (TextView) findViewById(R.id.erreur_radio);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        next = (AppCompatButton) findViewById(R.id.continuer_btn);

        onclickFunctions();
        setTextForRadioButton();
        initialiseDataBase();
        setImagePersonne();
        setFullNamePersonne();
        onRadioButtonChangeListener();
    }

    @Override
    public void onBackPressed() {
        ouvrirForgetPassword1Activity();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword1Activity();
            }
        });

        notYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirForgetPassword1Activity();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationFormulaireMethodeRecuperation();
            }
        });
    }

    public void ouvrirForgetPassword1Activity(){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public String getEmailFromIntent(){
        return getIntent().getStringExtra("email");
    }

    public String getPhoneFromIntent(){
        return getIntent().getStringExtra("phone");
    }

    public void setTextForRadioButton(){
        String textEmail = "<font color='#FF000000'>"+getString(R.string.by_email)+"<br></font>";
        String valueEmail = "<font color='#FF000000'>"+getEmailFromIntent()+"</font>";
        String textPhone = "<font color='#FF000000'>"+getString(R.string.by_number)+"<br></font>";
        String valuePhone = "<font color='#FF000000'>" + "(+216) "+ getPhoneFromIntent().substring(0,2) + " " + getPhoneFromIntent().substring(2,5) + " " + getPhoneFromIntent().substring(5,8)+"</font>";

        email.setText(Html.fromHtml("<b>" + textEmail + "</b>" + valueEmail), RadioButton.BufferType.SPANNABLE);
        numero.setText(Html.fromHtml("<b>" + textPhone + "</b>" + valuePhone), RadioButton.BufferType.SPANNABLE);
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void setImagePersonne(){
        databaseReference.child("images_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child(encodeString(getEmailFromIntent())).child("photo").getValue(String.class) != null){
                    Glide.with(getApplicationContext()).load(snapshot.child(encodeString(getEmailFromIntent())).child("photo").getValue(String.class)).centerCrop().fitCenter().into(photo);
                }

                else{
                    photo.setImageResource(R.drawable.user);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void setFullNamePersonne(){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                fullname.setText(snapshot.child(encodeString(getEmailFromIntent())).child("fullname").getValue(String.class));
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

    public void validationFormulaireMethodeRecuperation(){
        if(radioGroup.getCheckedRadioButtonId() == -1){
            setErreurText(erreurMethode,getString(R.string.methode_required));
        }

        else{
            setErreurNull(erreurMethode);
            chargementMethodRecuperationCompte();
        }
    }

    public void onRadioButtonChangeListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setErreurNull(erreurMethode);
                radioButton = (RadioButton) findViewById(checkedId);
                checked = checkedId;
            }
        });
    }

    public void chargementMethodRecuperationCompte(){
        final ProgressDialog progressDialog = new ProgressDialog(VerifAccountBeforeSendCodeActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.getting_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                choisirMehodeRecuperation(progressDialog);
            }
        }).start();
    }

    public void choisirMehodeRecuperation(ProgressDialog progressDialog){
        if(radioButton.getText().toString().equals(email.getText().toString())){
            sendMailToEmail(progressDialog);
        }

        else{
            sendMessageToPhone(progressDialog);
        }
    }

    public void sendMessageToPhone(ProgressDialog progressDialog){
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+216" + getPhoneFromIntent(),60, TimeUnit.SECONDS,VerifAccountBeforeSendCodeActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        showNotificationErrorFonctionnalite();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressDialog.dismiss();
                        ouvrirForgetPassword2Activity(s);
                    }
                });
    }

    public void showNotificationErrorFonctionnalite(){
        dialog = new Dialog(VerifAccountBeforeSendCodeActivity.this);
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
        desc.setText(R.string.forget_not_available);

        TextView title = dialog.findViewById(R.id.title_erreur);
        title.setText(getString(R.string.forget_password_error));

        dialog.show();
    }

    public void ouvrirForgetPassword2Activity(String verificationId){
        Intent intent = new Intent(getApplicationContext(), ForgetPassword2Activity.class);
        intent.putExtra("phone",getPhoneFromIntent());
        intent.putExtra("verificationId",verificationId);
        intent.putExtra("email",getEmailFromIntent());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void sendMailToEmail(ProgressDialog progressDialog){
        String to = getEmailFromIntent();
        String subject = getResources().getString(R.string.security_code_title);
        String code = generateCode()+"";
        String message = getResources().getString(R.string.bonjour) + " " + Html.fromHtml("<b>" + fullname.getText().toString() + "</b>")  + "," + "\n\n" + getResources().getString(R.string.text1_code) + " " + Html.fromHtml("<b>" + code + "</b>") + getResources().getString(R.string.text2_code) +
                "\n\n" + getResources().getString(R.string.text3_code);

        String senderEmail = "miniprojet.groupec@gmail.com";
        String senderPassword = "qzwmxoggcwwmrrlv";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message messages = new MimeMessage(session);
            messages.setFrom(new InternetAddress(senderEmail));
            messages.setRecipient(Message.RecipientType.TO,new InternetAddress(to.trim()));
            messages.setSubject(subject.trim());
            messages.setText(message.trim());
            Transport.send(messages);
            progressDialog.dismiss();
            ouvrirForgetPassword22Activity(code);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public int generateCode(){
        final int min = 111111;
        final int max = 999999;

        return new Random().nextInt((max - min) + 1) + min;
    }

    public void ouvrirForgetPassword22Activity(String code) {
        Intent intent = new Intent(getApplicationContext(), ForgetPassword22Activity.class);
        intent.putExtra("code", code);
        intent.putExtra("email", getEmailFromIntent());
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left, R.anim.stay);
    }
}