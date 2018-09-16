package com.mrehya.UserAccount;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.AppConfig;
import com.mrehya.AppController;
import com.mrehya.Login;
import com.mrehya.MainActivity;
import com.mrehya.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hgjhghgjh on 8/8/2018.
 */

public class checkLogin {

    private Context context;
    private SessionManager session;
    public checkLogin(Context context, SessionManager session) {
        this.context = context;
        this.session = session;
    }
    public void execute(){
        new LoginTask().execute(0);
    }
    private void checkLogin (final String email , final String password){
        //Log.d("checkLogin", "Login info: " + email + " " + password);
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIGNIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("TAG", "Login Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        JSONObject c = jObj.getJSONObject("data");
                        get_credentials(c.getString("token"), c.getString("username"), password);
                    } else {
                        Log.e("req_login",  "req_login " + " مشکلی پیش آمد");
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("req_login", "error 1 ");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("req_login",  "error 2 " + " " + error.getMessage());
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

        String tag_string_req = "req_creds";
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
                        // Now store ttag okokhe user in SQLiteokok;
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
                        if(c.has("resumeId"))
                            resume =c.getInt("resumeId")+"";
                        else
                            resume =null;
                        password = passwordd;
                        session.setLogin(true);
                        Log.e("req_creds", "user logged");
                        session.setUserDetails(id,firstname,lastname,email,phone,token, image,mobile,address,zip,password,resume);
                    } else {
                        Log.d("req_creds", "failed to get user");
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("req_creds", "error 1 " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("req_creds",  "error2 : " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", token);
                return params;
            }
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private class LoginTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(2000);
                // some long running task will run here. We are using sleep as a dummy to delay execution
                checkLogin(session.getUserDetails().getEmail(), session.getUserDetails().getPassword());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

}
