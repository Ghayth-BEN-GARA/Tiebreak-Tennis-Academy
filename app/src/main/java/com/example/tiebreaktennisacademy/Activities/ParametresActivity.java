package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tiebreaktennisacademy.R;

public class ParametresActivity extends AppCompatActivity {
    private TextView personalAccountInfo, profileInformations, about, passwordSecurity, help;
    private ImageView back;
    private CardView assistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        personalAccountInfo = (TextView) findViewById(R.id.title_infos_account);
        back = (ImageView) findViewById(R.id.back);
        profileInformations = (TextView) findViewById(R.id.title_infos_profil);
        about = (TextView) findViewById(R.id.title_a_propos);
        passwordSecurity = (TextView) findViewById(R.id.title_password_securite);
        assistance = (CardView) findViewById(R.id.card_view);
        help = (TextView) findViewById(R.id.title_help);

        onclickFunctions();
    }

    public void onclickFunctions(){
        personalAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPersonalAccountActivity();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHomeActivity();
            }
        });

        profileInformations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirProfileActivity();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAboutActivity();
            }
        });

        passwordSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirPasswordSecurityActivity();
            }
        });

        assistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAssistanceActivity();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirHelpActivity();
            }
        });
    }

    public void ouvrirPersonalAccountActivity(){
        Intent intent = new Intent(getApplicationContext(), PersonalAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirPasswordSecurityActivity(){
        Intent intent = new Intent(getApplicationContext(), PasswordSecurityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirHelpActivity(){
        Intent intent = new Intent(getApplicationContext(), Help2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirHomeActivity();
    }

    public void ouvrirHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void ouvrirProfileActivity(){
        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirAboutActivity(){
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirAssistanceActivity(){
        Intent intent = new Intent(getApplicationContext(), AssistanceActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}