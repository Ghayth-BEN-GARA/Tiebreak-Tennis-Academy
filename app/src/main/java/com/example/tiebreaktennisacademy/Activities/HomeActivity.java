package com.example.tiebreaktennisacademy.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tiebreaktennisacademy.Fragements.AccountFragment;
import com.example.tiebreaktennisacademy.Fragements.HomeFragment;
import com.example.tiebreaktennisacademy.Models.Session;
import com.example.tiebreaktennisacademy.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout linearLayoutDashboard, linearLayoutProfile, linearLayoutLogout, linearLayoutOther;
    private ImageView imageViewDashboard, imageViewProfile, imageViewLogout, imageViewOther;
    private TextView textViewDashboard, textViewProfile, textViewLogout, textViewOther, fullname;
    private Dialog dialog;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View header;
    private DatabaseReference databaseReference;
    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        linearLayoutDashboard = (LinearLayout) findViewById(R.id.dashboard_item);
        linearLayoutProfile = (LinearLayout) findViewById(R.id.profile_item);
        linearLayoutLogout = (LinearLayout) findViewById(R.id.logout_item);
        linearLayoutOther = (LinearLayout) findViewById(R.id.other_item);
        imageViewDashboard = (ImageView) findViewById(R.id.dashboard_img);
        imageViewProfile =(ImageView) findViewById(R.id.profile_img);
        imageViewLogout = (ImageView) findViewById(R.id.logout_img);
        imageViewOther = (ImageView) findViewById(R.id.other_img);
        textViewDashboard = (TextView) findViewById(R.id.dashboard_txt);
        textViewProfile = (TextView) findViewById(R.id.profile_txt);
        textViewLogout = (TextView) findViewById(R.id.logout_txt);
        textViewOther = (TextView) findViewById(R.id.other_txt);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        header = navigationView.getHeaderView(0);
        fullname = header.findViewById(R.id.fullname);

        onClickFunctions();
        setHomeFragmentByDefault();
        setSwipeDrawerDisable();
        setConfigNavigationView();
        initialiseDataBase();
        setFullnamePersonne();
    }

    public void onClickFunctions(){
        linearLayoutDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configLinearLayoutDashboard();
            }
        });

        linearLayoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configLinearLayoutProfile();
            }
        });


        linearLayoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configLinearLayoutLogout();
            }
        });

        linearLayoutOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configLinearLayoutOther();
            }
        });
    }

    public void configLinearLayoutDashboard(){
        if(selectedTab != 1){
            setVisibilityGone(textViewProfile);
            setVisibilityGone(textViewLogout);
            setVisibilityGone(textViewOther);

            setImageRessource(imageViewProfile,R.drawable.person);
            setImageRessource(imageViewLogout,R.drawable.exit);
            setImageRessource(imageViewOther,R.drawable.drawer);

            setBackgroundColor(linearLayoutProfile,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutLogout,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutOther,getResources().getColor(R.color.transparent));

            setVisibilityVisible(textViewDashboard);
            setImageRessource(imageViewDashboard,R.drawable.dashboard_selected);
            setBackgroundRessource(linearLayoutDashboard,R.drawable.round_back_navigation_bottom);

            setLinearLayoutAnimation(linearLayoutDashboard,0.0f);
            setPositionTabSelected(1);
            openFragmentClicked(HomeFragment.class);

        }
    }

    public void configLinearLayoutProfile(){
        if(selectedTab != 2){
            setVisibilityGone(textViewDashboard);
            setVisibilityGone(textViewLogout);
            setVisibilityGone(textViewOther);

            setImageRessource(imageViewDashboard,R.drawable.dashboard);
            setImageRessource(imageViewLogout,R.drawable.exit);
            setImageRessource(imageViewOther,R.drawable.drawer);

            setBackgroundColor(linearLayoutDashboard,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutLogout,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutOther,getResources().getColor(R.color.transparent));

            setVisibilityVisible(textViewProfile);
            setImageRessource(imageViewProfile,R.drawable.person_selected);
            setBackgroundRessource(linearLayoutProfile,R.drawable.round_back_navigation_bottom);

            setLinearLayoutAnimation(linearLayoutProfile,1.0f);
            setPositionTabSelected(2);
            openFragmentClicked(AccountFragment.class);
        }
    }

    public void configLinearLayoutLogout(){
        if(selectedTab != 3){
            setVisibilityGone(textViewDashboard);
            setVisibilityGone(textViewProfile);
            setVisibilityGone(textViewOther);

            setImageRessource(imageViewDashboard,R.drawable.dashboard);
            setImageRessource(imageViewProfile,R.drawable.person);
            setImageRessource(imageViewOther,R.drawable.drawer);

            setBackgroundColor(linearLayoutDashboard,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutProfile,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutOther,getResources().getColor(R.color.transparent));

            setVisibilityVisible(textViewLogout);
            setImageRessource(imageViewLogout,R.drawable.exit_selected);
            setBackgroundRessource(linearLayoutLogout,R.drawable.round_back_navigation_bottom);

            setLinearLayoutAnimation(linearLayoutLogout,1.0f);
            setPositionTabSelected(3);
            showQuestionLogout();
        }
    }

    public void configLinearLayoutOther(){
        if(selectedTab != 4){
            setVisibilityGone(textViewDashboard);
            setVisibilityGone(textViewProfile);
            setVisibilityGone(textViewLogout);

            setImageRessource(imageViewDashboard,R.drawable.dashboard);
            setImageRessource(imageViewProfile,R.drawable.person);
            setImageRessource(imageViewLogout,R.drawable.exit);

            setBackgroundColor(linearLayoutDashboard,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutProfile,getResources().getColor(R.color.transparent));
            setBackgroundColor(linearLayoutLogout,getResources().getColor(R.color.transparent));

            setVisibilityVisible(textViewOther);
            setImageRessource(imageViewOther,R.drawable.drawer_selected);
            setBackgroundRessource(linearLayoutOther,R.drawable.round_back_navigation_bottom);

            setLinearLayoutAnimation(linearLayoutOther,1.0f);
            setPositionTabSelected(4);
            openDrawerLayout();
        }
    }

    public void setVisibilityGone(TextView input){
        input.setVisibility(View.GONE);
    }

    public void setImageRessource(ImageView input, int id){
        input.setImageResource(id);
    }

    public void setBackgroundColor(LinearLayout input, int id){
        input.setBackgroundColor(id);
    }

    public void setVisibilityVisible(TextView input){
        input.setVisibility(View.VISIBLE);
    }

    public void setBackgroundRessource(LinearLayout input, int id){
        input.setBackgroundResource(id);
    }

    public void setLinearLayoutAnimation(LinearLayout input, float i){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f,1f,1f, Animation.RELATIVE_TO_SELF,i,Animation.RELATIVE_TO_SELF,0.0f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        input.startAnimation(scaleAnimation);
    }

    public void setPositionTabSelected(int position){
        selectedTab = position;
    }

    public void setHomeFragmentByDefault(){
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.main_container, HomeFragment.class,null).commit();
        openDrawerLayout();
    }

    public void openFragmentClicked(Class fragment){
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.right_to_left, R.anim.stay).setReorderingAllowed(true).replace(R.id.main_container, fragment,null).commit();
    }

    public void showQuestionLogout(){
        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.item_logout_question);
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
                setVisibilityGone(textViewProfile);
                setVisibilityGone(textViewLogout);
                setVisibilityGone(textViewOther);

                setImageRessource(imageViewProfile,R.drawable.person);
                setImageRessource(imageViewLogout,R.drawable.exit);
                setImageRessource(imageViewOther,R.drawable.drawer);

                setBackgroundColor(linearLayoutProfile,getResources().getColor(R.color.transparent));
                setBackgroundColor(linearLayoutLogout,getResources().getColor(R.color.transparent));
                setBackgroundColor(linearLayoutOther,getResources().getColor(R.color.transparent));

                setVisibilityVisible(textViewDashboard);
                setImageRessource(imageViewDashboard,R.drawable.dashboard_selected);
                setBackgroundRessource(linearLayoutDashboard,R.drawable.round_back_navigation_bottom);

                setLinearLayoutAnimation(linearLayoutDashboard,0.0f);
                setPositionTabSelected(1);
                openFragmentClicked(HomeFragment.class);
            }
        });

        AppCompatButton logout = dialog.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                chargementLogout();
            }
        });

        dialog.show();
    }

    public void chargementLogout(){
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this, R.style.chargement);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.logout_progress));
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {
                logoutSession();
                ouvrirLoginActivity();
                progressDialog.dismiss();
            }
        }).start();
    }

    public void logoutSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();

        if(session.checkEmailApplication() != null){
            session.removeSessionEmail();
        }
    }

    public void ouvrirLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.stay);
    }

    public void openDrawerLayout(){
        linearLayoutOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
                configLinearLayoutOther();
            }
        });
    }

    public void setSwipeDrawerDisable(){
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),R.string.not_return,Toast.LENGTH_LONG).show();
    }

    public void setConfigNavigationView(){
        navigationView.setItemIconTintList(null);
    }

    public void initialiseDataBase(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tiebreak-tennis--1657542982200-default-rtdb.firebaseio.com/");
    }

    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    public void setFullnamePersonne(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        String email = session.getEmailSession();

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullname.setText(snapshot.child(encodeString(email)).child("fullname").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}