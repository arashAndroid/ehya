package com.mrehya;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "mrehya";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setNotifStatus(boolean isNotifOn) {

        editor.putBoolean("isNotifOn", isNotifOn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isNotifOn(){
        return pref.getBoolean("isNotifOn", true);
    }
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setUserDetails(int id ,  String firstname,String lastname ,String email, String phone, String token,String image, String mobile, String address, String zip
            ,String password , String resume){

        editor.putInt("user_id" , id);

        editor.putString("firstname",firstname);
        editor.commit();
        editor.putString("lastname",lastname);
        editor.commit();
        editor.putString("email",email);
        editor.commit();
        editor.putString("image",image);
        editor.commit();
        editor.putString("token",token);
        editor.commit();
        editor.putString("phone",phone);
        editor.commit();
        editor.putString("mobile",mobile);
        editor.commit();
        editor.putString("address",address);
        editor.commit();
        editor.putString("zip",zip);
        editor.commit();
        editor.putString("password",password);
        editor.commit();

        editor.putString("resume",resume);
        editor.commit();


    }
    public User getUserDetails(){

        int id = pref.getInt("user_id", 0);
        int password_change = pref.getInt("password_change", 0);
        String firstname = pref.getString("firstname" , null);
        String lastname = pref.getString("lastname" , null);
        String email = pref.getString("email" , null);
        String image = pref.getString("image" , null);
        String token = pref.getString("token" , null);
        String address = pref.getString("address" , null);
        String phone = pref.getString("phone" , null);
        String mobile = pref.getString("mobile" , null);
        String zip = pref.getString("zip" , null);
        String password = pref.getString("password" , null);
        String resume = pref.getString("resume" , null);

        User user = new User(id,firstname,lastname,email,resume);
        user.setAddress(address);user.setPhone(phone);user.setMobile(mobile);user.setImage(image);user.setZip(zip);
        user.setToken(token);user.setPassword(password);user.setResume(resume);
        return user;
    }
    public void setUserEmail(String email){
        editor.putString("email",email);
        editor.commit();
    }
    public void setChangePassword(int changePassword){
        editor.putInt("password_change" , changePassword);
        editor.commit();
    }
}