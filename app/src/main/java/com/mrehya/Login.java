package com.mrehya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class Login extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    AppCompatEditText txtEmail  , txtPassword;
    RelativeLayout relativeLayout ;
    TextInputLayout emailTextInputLayout , passwordTextInputLayout;
    Button btnToRegister , btnLogin , btnToChangePassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private ProgressDialog mProgressDialog;

    private static final int RC_SIGN_IN = 007;
    //new
    String Language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
        btnSignIn.setOnClickListener(this);

        setTextViewAttr();
        setButtonOnClick();
        session = new SessionManager(getApplicationContext());
        pDialog = new ProgressDialog(this);
        //pDialog.setCancelable(false);

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //new
        //LinearLayoutloginorsignup=findViewById(R.id.LinearLayoutloginorsignup);
        Paper.init(this);
        Language=updateLanguage();
        updateView(Language);

    }
    private void setViews(){
        btnToChangePassword = (Button) findViewById(R.id.btnToChangePassword);
        emailTextInputLayout = (TextInputLayout) findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordTextInputLayout);
        txtEmail = (AppCompatEditText) findViewById(R.id.txtEmail);
        txtPassword = (AppCompatEditText) findViewById(R.id.txtPassword);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnToRegister = (Button) findViewById(R.id.btnToRegister);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in_google);
    }
    private void setTextViewAttr(){
        relativeLayout.setOnClickListener(null);
        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtEmail.getText().toString().isEmpty() && !hasFocus){
                    emailTextInputLayout.setErrorEnabled(false);
                }
            }
        });
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!txtEmail.getText().toString().isEmpty()) {
                    emailTextInputLayout.setErrorEnabled(false);
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
        passwordTextInputLayout.setCounterEnabled(true);
        passwordTextInputLayout.setCounterMaxLength(20);
    }
    private void setButtonOnClick(){
        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this , Signup.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().isEmpty()){
                    emailTextInputLayout.setErrorEnabled(true);
                    if(Language.equals("fa"))
                        emailTextInputLayout.setError("لطفا نام کاربری را وارد نمایید.");
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
                if (!txtEmail.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()){
                    startDialog();
                    new LoginTask().execute(0);
                }
            }
        });
    }
    private void checkLogin (final String email , final String password){

        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIGNIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        JSONObject c = jObj.getJSONObject("data");
                        get_credentials(c.getString("token"), c.getString("username"), password);
                        Intent intent = new Intent(Login.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                        hideDialog();
                    } else {
                        // Error in login. Get the error message
                        if(Language.equals("fa")){
                            Toast.makeText(getApplicationContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(),
                                "", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 ");
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
                Log.e("TAG",  "ایمیل موجود نمی‌باشد" + " " + error.getMessage());
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


                params.put("email", email);
                params.put("password", password);
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
    }
    private void get_credentials(final String token, final String email, final String passwordd){

        String tag_string_req = "req_login";
        Log.e("TOKEN" , token);
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
                                image =" ",mobile=" ",address=" ", zip=" ",password=" ",resume=" ";
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
                        resume = c.getString("resumeId");
                        password = passwordd;
                        session.setLogin(true);
                        session.setUserDetails(id,firstname,lastname,email,phone,token, image,mobile,address,zip,password,resume);
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

    //////////////////////////////////////////////////////////////////////////////
        // The params are dummy and not used
    class LoginTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
                // some long running task will run here. We are using sleep as a dummy to delay execution
                checkLogin(txtEmail.getText().toString(), txtPassword.getText().toString());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            // do whatever needs to be done next
            Toast.makeText(Login.this, "Connecting to server...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }


    private void startDialog(){
        pDialog.setCancelable(false);
        if(Language.equals("fa"))
            pDialog.setMessage("در حال وارد شدن...");
        else
            pDialog.setMessage("Logging in...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();
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
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                    // and the GoogleSignInResult will be available instantly.
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
                    // If the user has not previously signed in on this device or the sign-in has expired,
                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    showProgressDialog();
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            hideProgressDialog();
                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
                signIn();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Login.this , LoginOrSignup.class);
        startActivity(intent);
        finish();
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
            Loginparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            Loginparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayout);
            Loginparams.setMargins(0,25,32,0);

            Registerparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            Registerparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayout);
            Registerparams.setMargins(32,25,0,0);
        }
        else{
                Registerparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                Registerparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayout);
                Registerparams.setMargins(0,25,32,0);

                Loginparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                Loginparams.addRule(RelativeLayout.BELOW, R.id.passwordTextInputLayout);
                Loginparams.setMargins(32,25,0,0);
        }
        btnLogin.setLayoutParams(Loginparams);
        btnToRegister.setLayoutParams(Registerparams);
        emailTextInputLayout.setHint(resources.getString(R.string.EmailAddressTitle));
        passwordTextInputLayout.setHint(resources.getString(R.string.Password));
        btnToRegister.setText(resources.getString(R.string.Signup));
        btnToChangePassword.setText(resources.getString(R.string.ForgotPassword));
        btnLogin.setText(resources.getString(R.string.Login));
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();


            txtEmail.setText(email);
            /*Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

        } else {
            // Signed out, show unauthenticated UI.
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("در حال ارتباط با سرور...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }



}
