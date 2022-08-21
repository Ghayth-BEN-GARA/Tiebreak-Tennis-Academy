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
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout linearLayoutDashboard, linearLayoutProfile, linearLayoutLogout, linearLayoutOther;
    private ImageView imageViewDashboard, imageViewProfile, imageViewLogout, imageViewOther,photoProfil, paramatresHeader;
    private TextView textViewDashboard, textViewProfile, textViewLogout, textViewOther, fullname, showProfil;
    private Dialog dialog;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Menu menuItem;
    private MenuItem logoutItem, profilItem, supportItem, coachesItem, CourtsItem, playersItem;
    private View header;
    private CircleImageView imageViewProfil;
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
        photoProfil = (ImageView) header.findViewById(R.id.photo_profil);
        showProfil = (TextView) header.findViewById(R.id.view_my_profil);
        paramatresHeader = (ImageView) header.findViewById(R.id.config_header);
        menuItem = navigationView.getMenu();
        imageViewProfil = (CircleImageView) header.findViewById(R.id.photo_profil);

        onClickFunctions();
        setHomeFragmentByDefault();
        setSwipeDrawerDisable();
        setConfigNavigationView();
        initialiseDataBase();
        setFullnamePersonne();
        setImagePersonne();
        setItemsMenuAction();
        setHeaderMenuAction();
        checkIfCountIsActive();
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
                updateJournal(getString(R.string.logout),emailSession());
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

    public String emailSession(){
        Session session = new Session(getApplicationContext());
        session.initialiserSharedPreferences();
        return(session.getEmailSession());
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

    public void setItemsMenuAction(){
        logoutItem = menuItem.findItem(R.id.logout);
        profilItem = menuItem.findItem(R.id.my_profil);
        supportItem = menuItem.findItem(R.id.support);
        playersItem = menuItem.findItem(R.id.my_players_res);
        coachesItem = menuItem.findItem(R.id.my_coaches_res);

        logoutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showQuestionLogout();
                return true;
            }
        });

        profilItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ouvrirProfilActivity();
                return true;
            }
        });

        supportItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ouvrirSupportActivity();
                return true;
            }
        });

        playersItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ouvrirPlayersActivity();
                return true;
            }
        });

        coachesItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ouvrirCoachesActivity();
                return true;
            }
        });
    }

    public void ouvrirProfilActivity(){
        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirParametresActivity(){
        Intent intent = new Intent(getApplicationContext(), ParametresActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirSupportActivity(){
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirPlayersActivity(){
        Intent intent = new Intent(getApplicationContext(), PlayersActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void ouvrirCoachesActivity(){
        Intent intent = new Intent(getApplicationContext(), CoachesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_to_left,R.anim.stay);
    }

    public void setHeaderMenuAction(){
        fullname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirProfilActivity();
            }
        });

        photoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirProfilActivity();
            }
        });

        showProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirProfilActivity();
            }
        });

        paramatresHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirParametresActivity();
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

    public void checkIfCountIsActive(){
        databaseReference.child("compte_users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(encodeString(emailSession())).child("active").getValue(String.class).equals("false")){
                    activeAccount(emailSession());
                    updateJournal("Enable",encodeString(emailSession()));
                    showNotificationAfterTime();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void activeAccount(String email){
        databaseReference.child("compte_users").child(encodeString(email)).child("active").setValue("true");
    }

    public void showNotificationAfterTime(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showNotificationSuccessActiveAccount();
            }
        }, 500);
    }

    public void showNotificationSuccessActiveAccount(){
        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.item_success);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.content_erreur_notification));
        }

        TextView title = (TextView) dialog.findViewById(R.id.title_success);
        TextView desc = (TextView) dialog.findViewById(R.id.desc_title_success);

        title.setText(R.string.account_activer_success);
        desc.setText(R.string.active_success);

        TextView cancel = dialog.findViewById(R.id.exit_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void updateJournal(String text, String email){
        String key = databaseReference.child("journal_users").push().getKey();
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("action").setValue(text);
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("action").setValue(text);
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("date").setValue(getCurrentDate());
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("time").setValue(getCurrentTime());
        databaseReference.child("journal_users").child(encodeString(email)).child(key).child("phone").setValue(getAppareilUsed());
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


}