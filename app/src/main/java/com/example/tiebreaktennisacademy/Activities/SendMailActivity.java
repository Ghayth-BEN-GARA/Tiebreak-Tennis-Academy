package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import java.util.Calendar;

public class SendMailActivity extends AppCompatActivity {
    private ImageView back;
    private TextView copiright, objet, message, erreurObjet, erreurMessage;
    private AppCompatButton cancel, send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        cancel = (AppCompatButton) findViewById(R.id.cancel_btn);
        objet = (TextView) findViewById(R.id.objet);
        message = (TextView) findViewById(R.id.message);
        erreurObjet = (TextView) findViewById(R.id.erreur_objet);
        erreurMessage = (TextView) findViewById(R.id.erreur_message);
        send = (AppCompatButton) findViewById(R.id.send_btn);

        onclickFunctions();
        onChangeFunctions();
        setCopyrightText();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirParametresSecurityActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllInputs();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFormSendEmail();
            }
        });
    }

    public void ouvrirParametresSecurityActivity(){
        Intent intent = new Intent(getApplicationContext(), PasswordSecurityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirParametresSecurityActivity();
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void resetAllInputs(){
        objet.setText("");
        message.setText("");
        setErreurNull(erreurObjet);
        setErreurNull(erreurMessage);
    }

    public void setErreurNull(TextView text){
        text.setText(null);
    }

    public void setErreurText(TextView text, String message){
        text.setText(message);
    }

    public boolean isEmpty(String text){
        return text.isEmpty();
    }

    public void validateFormSendEmail(){
        if(isEmpty(objet.getText().toString())){
            setErreurText(erreurObjet,getString(R.string.objet_required));
        }

        else if(isEmpty(message.getText().toString())){
            setErreurText(erreurMessage,getString(R.string.message_required));
        }

        else{
            setErreurNull(erreurObjet);
            setErreurNull(erreurMessage);
            sendMail();
        }
    }

    public void onChangeFunctions(){
        objet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateObjet();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateMessage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void validateObjet(){
        setErreurNull(erreurObjet);
    }

    public void validateMessage(){
        setErreurNull(erreurMessage);
    }

    public void sendMail(){
        String email = "tiebreak.tennis.contact@gmail.com";
        String[] list = email.split(",");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto"));
        intent.putExtra(Intent.EXTRA_EMAIL,list);
        intent.putExtra(Intent.EXTRA_SUBJECT,objet.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT,message.getText().toString());
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));
    }
}