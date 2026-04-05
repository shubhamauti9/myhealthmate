package com.example.myhealthmate;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Shared_Pref_Name = "session";
    String Session_Key = "session_user";

    public SessionManagement(Context context){

        sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }



    public void saveSession(User user){
        //save session
        String id = user.getId();

        editor.putString(Session_Key,id).commit();


    }
    public String getSession(){
        //return user
        return sharedPreferences.getString(Session_Key,"-1");
    }

    public void removeSession(){
        editor.putString(Session_Key,"-1").commit();
    }



}
