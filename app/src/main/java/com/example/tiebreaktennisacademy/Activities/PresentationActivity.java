package com.example.tiebreaktennisacademy.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tiebreaktennisacademy.Adapters.PresentationAdapter;
import com.example.tiebreaktennisacademy.Models.Presentation;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class PresentationActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout dots;
    private AppCompatButton start;
    private ImageButton next, before;
    private TextView skip;
    private PresentationAdapter presentationAdapter;
    private List<Presentation> presentationItems;
    private Animation show, gone;
    private Boolean testEcran = false;
    private int curentItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dots = (TabLayout) findViewById(R.id.dots);
        start = (AppCompatButton) findViewById(R.id.get_started);
        skip = (TextView) findViewById(R.id.skip);
        next = (ImageButton) findViewById(R.id.next);
        before = (ImageButton) findViewById(R.id.before);

        initialisationListePresentation();
        remplirListePresentation();
        attacherAdapterAvecItems();
        configurerIndicateurs();
        indicateurListener();
        onclickFunctions();
    }

    public void initialisationListePresentation(){
        presentationItems = new ArrayList<>();
    }

    public void remplirListePresentation(){
        presentationItems.add(new Presentation(R.drawable.coures,getString(R.string.courses),getString(R.string.courses_description)));
        presentationItems.add(new Presentation(R.drawable.coach2,getString(R.string.coaches),getString(R.string.coaches_description)));
        presentationItems.add(new Presentation(R.drawable.players,getString(R.string.players),getString(R.string.players_description)));
        presentationItems.add(new Presentation(R.drawable.planning,getString(R.string.planning),getString(R.string.planing_description)));
        presentationItems.add(new Presentation(R.drawable.court,getString(R.string.court),getString(R.string.court_description)));
    }

    public void attacherAdapterAvecItems(){
        presentationAdapter = new PresentationAdapter(getApplicationContext(),presentationItems);
        viewPager.setAdapter(presentationAdapter);
    }

    public void configurerIndicateurs(){
        dots.setupWithViewPager(viewPager);
    }

    public void indicateurListener(){
        dots.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    before.setVisibility(View.INVISIBLE);
                    testEcran = false;
                }

                else if(tab.getPosition() == 1) {
                    before.setVisibility(View.VISIBLE);
                    testEcran = false;
                }

                else if(tab.getPosition() == presentationItems.size() -1){
                    skip.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.VISIBLE);
                    setAnimationShowButton();
                    testEcran = true;
                }

                else if(tab.getPosition() == presentationItems.size() - 2){
                    if(testEcran == true){
                        skip.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        start.setVisibility(View.INVISIBLE);
                        setAnimationgGoneButton();
                        testEcran = false;
                    }

                    else{
                        skip.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);
                        start.setVisibility(View.INVISIBLE);
                        testEcran = false;
                    }
                }
                curentItemPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setAnimationShowButton(){
        show = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.show);
        start.setAnimation(show);
    }

    public void setAnimationgGoneButton(){
        gone = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.gone);
        start.setAnimation(gone);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"You cannot return to the previous screen !",Toast.LENGTH_LONG).show();
    }

    public void ouvrirChoixAuthentificationActivity(){
        Intent intent = new Intent(getApplicationContext(), ChoixLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void backToPrevious(){
        viewPager.setCurrentItem(curentItemPosition - 1);
    }

    public void goToNext(){
        viewPager.setCurrentItem(curentItemPosition + 1);
    }


    public void onclickFunctions(){
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirChoixAuthentificationActivity();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirChoixAuthentificationActivity();
            }
        });

        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPrevious();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNext();
            }
        });
    }
}