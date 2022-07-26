package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.tiebreaktennisacademy.R;

public class AssistanceActivity extends AppCompatActivity {
    private ImageView back;
    private CardView cardView1, cardView2, cardView3, cardView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);

        back = (ImageView) findViewById(R.id.back);
        cardView1 = (CardView) findViewById(R.id.card_view_partage);
        cardView2 = (CardView) findViewById(R.id.card_view_securite);
        cardView3 = (CardView) findViewById(R.id.card_view_search);
        cardView4 = (CardView) findViewById(R.id.card_view_donnes);

        onclickFunctions();
    }

    public void onclickFunctions(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirParametresActivity();
            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAssistanceCard1();
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAssistanceCard2();
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAssistanceCard3();
            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAssistanceCard4();
            }
        });
    }

    public void ouvrirParametresActivity(){
        Intent intent = new Intent(getApplicationContext(), ParametresActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ouvrirParametresActivity();
    }

    public void ouvrirAssistanceCard1(){
        Intent intent = new Intent(getApplicationContext(), AssistaceCard1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirAssistanceCard2(){
        Intent intent = new Intent(getApplicationContext(), AssistanceCard2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirAssistanceCard3(){
        Intent intent = new Intent(getApplicationContext(), AssistanceCard3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirAssistanceCard4(){
        Intent intent = new Intent(getApplicationContext(), AssistanceCard4Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }
}