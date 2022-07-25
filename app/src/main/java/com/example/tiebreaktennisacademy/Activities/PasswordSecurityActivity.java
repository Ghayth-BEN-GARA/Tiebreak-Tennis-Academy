package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;
import java.util.Calendar;

public class PasswordSecurityActivity extends AppCompatActivity {
    private ImageView back, goEditPassord, goEmail;
    private TextView copiright, descPhone, titleEditPassword, descEditPassword, titleEmail, descEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_security);

        back = (ImageView) findViewById(R.id.back);
        copiright = (TextView) findViewById(R.id.copyright_app);
        descPhone = (TextView) findViewById(R.id.desc_appareil);
        goEditPassord = (ImageView) findViewById(R.id.go_edit_password);
        titleEditPassword = (TextView) findViewById(R.id.title_edit_password);
        descEditPassword = (TextView) findViewById(R.id.desc_title_edit_password);
        goEmail = (ImageView) findViewById(R.id.go_haking);
        titleEmail = (TextView) findViewById(R.id.title_haking);
        descEmail = (TextView) findViewById(R.id.desc_title_haking);

        onclickFunctions();
        setCopyrightText();
        setMarquePhone();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirParametresActivity();
            }
        });

        goEditPassord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirEditPasswordActivity();
            }
        });

        titleEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirEditPasswordActivity();
            }
        });

        descEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirEditPasswordActivity();
            }
        });

        goEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSendEmailActivity();
            }
        });

        titleEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSendEmailActivity();
            }
        });

        descEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirSendEmailActivity();
            }
        });
    }

    public void ouvrirParametresActivity(){
        Intent intent = new Intent(getApplicationContext(), ParametresActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirSendEmailActivity(){
        Intent intent = new Intent(getApplicationContext(), SendMailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirParametresActivity();
    }

    public int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setCopyrightText(){
        copiright.setText(getString(R.string.copiright1) + " " + getCurrentYear() + getString(R.string.copiright2));
    }

    public void setMarquePhone(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            descPhone.setText(model + ".");
        }

        else {
            descPhone.setText(manufacturer + " " + model + ".");
        }
    }

    public void ouvrirEditPasswordActivity(){
        Intent intent = new Intent(getApplicationContext(), EditPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}