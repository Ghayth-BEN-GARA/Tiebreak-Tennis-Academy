package com.example.tiebreaktennisacademy.Models;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private Context context;
    private SharedPreferences session;

    public Session(Context ctx) {
        this.context = ctx;
    }

    public void initialiserSharedPreferences(){
        session = context.getSharedPreferences("personne",Context.MODE_PRIVATE);
    }

    public void saveEtatApplication(Boolean test){
        SharedPreferences.Editor editor = session.edit();
        editor.putBoolean("etatApplication",test);
        editor.commit();
    }

    public void saveEmailApplication(String test){
        SharedPreferences.Editor editor = session.edit();
        editor.putString("emailApplication",test);
        editor.commit();
    }

    public boolean getEtatApplication(){
        return session.getBoolean("etatApplication",false);
    }

    public String checkEmailApplication(){
        return session.getString("emailApplication",null);
    }

    public void removeSessionEmail(){
        SharedPreferences.Editor editor = session.edit();
        editor.remove("emailApplication");
        editor.commit();
    }

    public String getEmailSession(){
        return session.getString("emailApplication",null);
    }
}
