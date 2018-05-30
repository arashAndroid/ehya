package com.mrehya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class Signup extends AppCompatActivity {
    AppCompatEditText  txtPassword , txtPasswordAgain,txtFirstname,txtLastname,txtMobile,txtEmail,txtPhone;
    RelativeLayout relativeLayout ;
    TextInputLayout firstnameTextInputLayout,lastnameTextInputLayout,emailTextInputLayout , passwordTextInputLayout , passwordTextInputLayoutAgain;
    Button btnToLogin , btnRegister;
    private ProgressDialog pDialog;
    private SessionManager session;
    private String USERNAME="auth_key";
    private String PASSWORD="";

    //new
    String Language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setViews();
        setTextViewsAtrr();
        setButtonsOnClick();
        session = new SessionManager(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //new
        Paper.init(this);
        Language=updateLanguage();
        updateView(Language);
    }
    private void setViews(){
        lastnameTextInputLayout = (TextInputLayout) findViewById(R.id.lastnameTextInputLayout);
        firstnameTextInputLayout = (TextInputLayout) findViewById(R.id.firstnameTextInputLayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayoutAgain = (TextInputLayout) findViewById(R.id.passwordTextInputLayoutAgain);
        txtPassword = (AppCompatEditText) findViewById(R.id.txtPassword);
        txtPasswordAgain = (AppCompatEditText) findViewById(R.id.txtPasswordAgain);
        txtFirstname = (AppCompatEditText) findViewById(R.id.txtFirstname);
        txtLastname = (AppCompatEditText) findViewById(R.id.txtLastname);
        txtMobile = (AppCompatEditText) findViewById(R.id.txtMobile);
        txtEmail = (AppCompatEditText) findViewById(R.id.txtEmailLogin);
        txtPhone = (AppCompatEditText) findViewById(R.id.txtPhone);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_signup);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnToLogin = (Button) findViewById(R.id.btnToLogin);
    }
    private void setTextViewsAtrr(){
        relativeLayout.setOnClickListener(null);

        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(20);
        passwordTextInputLayoutAgain.setCounterEnabled(true);
        passwordTextInputLayoutAgain.setCounterMaxLength(20);
        txtFirstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtFirstname.getText().toString().isEmpty() && !hasFocus){
                    firstnameTextInputLayout.setErrorEnabled(false);
                }
            }
        });
        txtFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtFirstname.getText().toString().isEmpty()) {
                    firstnameTextInputLayout.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtPassword.getText().toString().isEmpty() && !hasFocus){
                    passwordTextInputLayout.setErrorEnabled(false);
                }
            }
        });
        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtPassword.getText().toString().isEmpty()) {
                    passwordTextInputLayout.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase(txtPassword.getText().toString())){
                    passwordTextInputLayoutAgain.setErrorEnabled(false);
                    passwordTextInputLayoutAgain.setHintTextAppearance(R.style.FlatButton);
                }else{
                    passwordTextInputLayoutAgain.setErrorEnabled(true);
                    passwordTextInputLayoutAgain.setError(" ");
                    passwordTextInputLayoutAgain.setHintTextAppearance(R.style.CharacterOverflow);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtPasswordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && txtPasswordAgain.getText().toString().isEmpty()){
                    passwordTextInputLayoutAgain.setErrorEnabled(false);
                }
            }
        });
    }
    private void setButtonsOnClick(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtFirstname.getText().toString().isEmpty()){
                    firstnameTextInputLayout.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        firstnameTextInputLayout.setError("لطفا نام را وارد نمایید.");
                    else
                        firstnameTextInputLayout.setError("Please fill Username field.");
                }
                if (txtLastname.getText().toString().isEmpty()){
                    lastnameTextInputLayout.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        lastnameTextInputLayout.setError("لطفا نام خانوادگی را وارد نمایید.");
                    else
                        lastnameTextInputLayout.setError("Please fill Username field.");
                }if (txtLastname.getText().toString().isEmpty()){
                    emailTextInputLayout.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        emailTextInputLayout.setError("لطفا ایمیل را وارد نمایید.");
                    else
                        emailTextInputLayout.setError("Please fill Username field.");
                }
                if (txtPassword.getText().toString().isEmpty()){
                    passwordTextInputLayout.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        passwordTextInputLayout.setError("لطفا رمز ورود را وارد نمایید.");
                    else
                        passwordTextInputLayout.setError("Please fill Password field.");

                }
                if (txtPasswordAgain.getText().toString().isEmpty()){
                    passwordTextInputLayoutAgain.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        passwordTextInputLayoutAgain.setError("لطفا رمز ورود را دوباره وارد نمایید.");
                    else
                        passwordTextInputLayoutAgain.setError("Please fill Confirm Password field.");
                }
                if (!txtPassword.getText().toString().isEmpty() && !txtPasswordAgain.getText().toString().isEmpty() &&
                        !txtPasswordAgain.getText().toString().equalsIgnoreCase(txtPassword.getText().toString())){
                    passwordTextInputLayoutAgain.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        passwordTextInputLayoutAgain.setError("رمزها یکسان نیستند.");
                    else
                        passwordTextInputLayoutAgain.setError("Passwords do not match!");

                }
                if (!txtLastname.getText().toString().isEmpty() && !txtEmail.getText().toString().isEmpty() && !txtFirstname.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty() &&
                        txtPasswordAgain.getText().toString().equalsIgnoreCase(txtPassword.getText().toString())){
                    Log.e("txtEmail",txtEmail.getText().toString());
                    Log.e("txtFirstname",txtFirstname.getText().toString());
                    Log.e("txtLastname",txtLastname.getText().toString());
                    Log.e("txtPassword",txtPassword.getText().toString());
                    Log.e("txtMobile",txtMobile.getText().toString());
                    Log.e("txtPhone",txtPhone.getText().toString());


                    signUp(txtEmail.getText().toString(), txtFirstname.getText().toString()
                            , txtLastname.getText().toString(), txtPassword.getText().toString()
                            , txtMobile.getText().toString(), txtPhone.getText().toString());
                }
            }
        });
        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this , Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void signUp(final String email, final String firstname, final String lastname, final String password, final String mobile, final String phone){

        ////////////////////////

        ////////////////////////
        pDialog.setMessage("در حال ثبت نام...");
        showDialog();
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIGNUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        LogUser(email,password);
                    } else {
                        // Error in login. Get the error message
                        if(Language.equals("fa")){
                            Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "ایمیل تکراری می‌باشد.");
                if(Language.equals("fa")){
                    Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("first_name", firstname);
                params.put("last_name", lastname);
                params.put("mobile", mobile);
                params.put("email", email);
                params.put("password", password);
                params.put("tel_number", phone);

                return params;
            }


            //basic auth
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = AppConfig.AUTH_USERNAME+":"+AppConfig.AUTH_PASS;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.URL_SAFE|Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Signup.this , LoginOrSignup.class);
        startActivity(intent);
        finish();
    }


    public void LogUser(final String Email, final String Password){
        final ArrayList<String> creds = new ArrayList<String>();
        String tag_string_req = "req_login";
        final int[] responsed = {-1};
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIGNIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");
                    responsed[0] = Integer.parseInt( jObj.getString("status"));
                    Log.d("TAG", success + response.toString());
                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                       JSONObject c = jObj.getJSONObject("data");

                        get_credentials(c.getString("token"), c.getString("username"), Password);

                        Intent intent = new Intent(Signup.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                        hideDialog();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("status");
                        if(Language.equals("fa")){
                            Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "ایمیل تکراری می‌باشد. یا " + error);
                if(Language.equals("fa")){
                    Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                params.put("email", Email);
                params.put("password", Password);
                params.put("type", "1");

                return params;
            }


            //basic auth
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = AppConfig.AUTH_USERNAME+":"+AppConfig.AUTH_PASS;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.URL_SAFE|Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //Toast.makeText(this, "reponse : " + responsed[0], Toast.LENGTH_SHORT).show();
    }
    private void get_credentials(final String token, final String email, final String passwordd){

        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VIEW_Profile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "View Profile Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        JSONObject c = jObj.getJSONObject("data");
                        int id =0;
                        String firstname=" ",lastname=" " , email=" ", phone= " ", token=" ",
                                image =" ",mobile=" ",address=" ", zip=" ",password=" ";
                        // Now store the user in SQLite
                        id = c.getInt("id");
                        firstname = c.getString("first_name");
                        lastname = c.getString("last_name");
                        email = c.getString("email");
                        token = c.getString("auth_key");
                        phone = c.getString("tel_number");
                        image = c.getString("avatar");
                        mobile = c.getString("mobile");
                        address = c.getString("address");
                        zip = c.getString("postal_code");
                        password = passwordd;
                        session.setLogin(true);
                        session.setUserDetails(id,firstname,lastname,email,phone,token, image,mobile,address,zip,password);
                        //hideDialog();
                    } else {
                        Log.d("TAG", "failed to get user");
                        // Error in login. Get the error message
                        hideDialog();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 ");
                    e.printStackTrace();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2 : " + error.getMessage());
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("auth", token);

                return params;
            }


            //basic auth
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials = AppConfig.AUTH_USERNAME+":"+AppConfig.AUTH_PASS;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.URL_SAFE|Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();


        RelativeLayout.LayoutParams Loginparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams Registerparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(language.equals("fa")){
            Registerparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            Registerparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayoutAgain);
            Registerparams.setMargins(0,16,32,0);

            Loginparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            Loginparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayoutAgain);
            Loginparams.setMargins(32,16,0,0);
        }
        else{
            Loginparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            Loginparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayoutAgain);
            Loginparams.setMargins(0,16,32,0);

            Registerparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            Registerparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayoutAgain);
            Registerparams.setMargins(32,16,0,0);

        }
        btnToLogin.setLayoutParams(Loginparams);
        btnRegister.setLayoutParams(Registerparams);
        firstnameTextInputLayout.setHint(resources.getString(R.string.first_name));
        lastnameTextInputLayout.setHint(resources.getString(R.string.last_name));
        passwordTextInputLayout.setHint(resources.getString(R.string.Password));
        passwordTextInputLayoutAgain.setHint(resources.getString(R.string.PasswordAgain));
        btnToLogin.setText(resources.getString(R.string.LoginPage));
        btnRegister.setText(resources.getString(R.string.Submit));

    }

}