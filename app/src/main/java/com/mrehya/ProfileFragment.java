package com.mrehya;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;


public class ProfileFragment extends Fragment {

    SessionManager sessionManager;
    Button btnSignInOrUp,showPurchases,hireRequestStatus,btnEditProfile,BtnInstagram,btnTelegram;
    AlertDialog ad ;
    //new
    MyTextView profileType,mytextUserInfo,mytextUserInfo2;
    LinearLayout LinearLayoutprofile1,LinearLayoutprofile2,LinearLayoutprofile3,LinearLayoutprofile4
            ,LinearLayoutprofile5,LinearLayoutprofile6;

    MyTextView profname;
    TextView txtEditName,txtEditLastName,txtEditEmail,txtEditMobile,txtEditPhone,txtEditZip,txtEditAddress;
    TextView txtEditProfile,txtName,txtLastname,txtEmail,txtPhoneNumber,txtTelephone,txtPostcode,txtAddress;

    Button btn_save_editprof;
    Context context;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog_updateuserinfo;
    String Language, newtoken="empty";
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        btnSignInOrUp = view.findViewById(R.id.btnSignInOrUp);
        showPurchases = view.findViewById(R.id.showPurchases);
        hireRequestStatus = view.findViewById(R.id.hireRequestStatus);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        context =view.getContext();
        pDialog = new ProgressDialog(context);
        session = new SessionManager(context);
        //new
        profileType = view.findViewById(R.id.profileType);
        mytextUserInfo = view.findViewById(R.id.mytextUserInfo);
        mytextUserInfo2 = view.findViewById(R.id.mytextUserInfo2);
        //layouts
        LinearLayoutprofile1 = (LinearLayout) view.findViewById(R.id.LinearLayoutprofile1);
        LinearLayoutprofile2 = (LinearLayout) view.findViewById(R.id.LinearLayoutprofile2);
        LinearLayoutprofile3 = (LinearLayout) view.findViewById(R.id.LinearLayoutprofile3);
        LinearLayoutprofile4 = (LinearLayout) view.findViewById(R.id.LinearLayoutprofile4);
        LinearLayoutprofile5 = (LinearLayout) view.findViewById(R.id.LinearLayoutprofile5);
        LinearLayoutprofile6 = (LinearLayout) view.findViewById(R.id.LinearLayoutprofile6);

        btnTelegram = view.findViewById(R.id.btnTelegram);
        BtnInstagram = view.findViewById(R.id.BtnInstagram);

        profname = view.findViewById(R.id.profileName);
        profname.setText(session.getUserDetails().getFirstname());
        Language = updateLanguage();
        updateView((String) Paper.book().read("language"));

        sessionManager = new SessionManager(getContext());
        btnSignInOrUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.isLoggedIn()){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("آیا واقعا میخواهید خارج شوید؟");

                    alertDialogBuilder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            session.setLogin(false);
                            Intent intent = new Intent(getActivity() , Language.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ad.dismiss();
                        }
                    });
                    ad = alertDialogBuilder.create();
                    ad.show();
                }else{
                    Intent intent = new Intent(getActivity(),LoginOrSignup.class);
                    startActivity(intent);
                }

            }
        });
        hireRequestStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(getActivity(), ShowHireStatus.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginOrSignup.class);
                    startActivity(intent);
                }
            }
        });
        showPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(getActivity(), ShowPurchase.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginOrSignup.class);
                    startActivity(intent);
                }
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLoggedIn()) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_edit_profile);

                    pDialog_updateuserinfo = new ProgressDialog(getActivity());
                    startpDialog_updateuserinfo();
                    get_credentials(sessionManager.getUserDetails().getToken(),sessionManager.getUserDetails().getEmail()
                            ,session.getUserDetails().getPassword(), null);
                    //txts
                    txtEditProfile = dialog.findViewById(R.id.txtEditProfile);
                    txtName = dialog.findViewById(R.id.txtName);
                    txtLastname = dialog.findViewById(R.id.txtLastname);
                    txtEmail = dialog.findViewById(R.id.txtEmail);
                    txtPhoneNumber = dialog.findViewById(R.id.txtPhoneNumber);
                    txtTelephone = dialog.findViewById(R.id.txtTelephone);
                    txtPostcode = dialog.findViewById(R.id.txtPostcode);
                    txtAddress = dialog.findViewById(R.id.txtAddress);
                    btn_save_editprof = dialog.findViewById(R.id.btn_save_editprof);

                    txtEditName = dialog.findViewById(R.id.txtEditName);
                    txtEditLastName = dialog.findViewById(R.id.txtEditLastName);
                    txtEditEmail = dialog.findViewById(R.id.txtEditEmail);
                    txtEditMobile = dialog.findViewById(R.id.txtEditMobile);
                    txtEditPhone = dialog.findViewById(R.id.txtEditPhone);
                    txtEditZip = dialog.findViewById(R.id.txtEditZip);
                    txtEditAddress = dialog.findViewById(R.id.txtEditAddress);



                    //user credentials
                    User user = sessionManager.getUserDetails();
                    txtEditName.setText(user.getFirstname());
                    txtEditLastName.setText(sessionManager.getUserDetails().getLastname());
                    txtEditEmail.setText(sessionManager.getUserDetails().getEmail());
                    txtEditMobile.setText(sessionManager.getUserDetails().getMobile());
                    txtEditPhone.setText(sessionManager.getUserDetails().getPhone());
                    txtEditZip.setText(sessionManager.getUserDetails().getZip());
                    txtEditAddress.setText(sessionManager.getUserDetails().getAddress());

                    updateView_dialog(Language);

                    DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    //int height = metrics.heightPixels;


                    btn_save_editprof.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pDialog = new ProgressDialog(getActivity());
                            startDialog();
                            update_token(sessionManager.getUserDetails().getEmail(), session.getUserDetails().getPassword(), dialog);
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    dialog.getWindow().setLayout((12 * width)/13, ViewGroup.LayoutParams.WRAP_CONTENT);


                }else{
                    Toast.makeText(getContext(), "not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }
    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(getActivity(), language);
        Resources resources = context.getResources();

        //Linear Layouts
        if(language.equals("fa")){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutprofile1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile3.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile4.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile5.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile6.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo.setGravity(Gravity.RIGHT);
                mytextUserInfo2.setGravity(Gravity.RIGHT);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutprofile1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile3.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile4.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile5.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                LinearLayoutprofile6.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo.setGravity(Gravity.RIGHT);
                mytextUserInfo2.setGravity(Gravity.RIGHT);

            }

            btnSignInOrUp.setGravity(Gravity.RIGHT|Gravity.CENTER);
            showPurchases.setGravity(Gravity.RIGHT|Gravity.CENTER);
            hireRequestStatus.setGravity(Gravity.RIGHT|Gravity.CENTER);
            btnEditProfile.setGravity(Gravity.RIGHT|Gravity.CENTER);
            BtnInstagram.setGravity(Gravity.RIGHT|Gravity.CENTER);
            btnTelegram.setGravity(Gravity.RIGHT|Gravity.CENTER);
        }
        else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutprofile1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile4.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile5.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile6.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mytextUserInfo.setGravity(Gravity.LEFT);
                mytextUserInfo2.setGravity(Gravity.LEFT);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutprofile1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile4.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile5.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile6.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mytextUserInfo.setGravity(Gravity.LEFT);
                mytextUserInfo2.setGravity(Gravity.LEFT);
            }

            btnSignInOrUp.setGravity(Gravity.LEFT|Gravity.CENTER);
            showPurchases.setGravity(Gravity.LEFT|Gravity.CENTER);
            hireRequestStatus.setGravity(Gravity.LEFT|Gravity.CENTER);
            btnEditProfile.setGravity(Gravity.LEFT|Gravity.CENTER);
            BtnInstagram.setGravity(Gravity.LEFT|Gravity.CENTER);
            btnTelegram.setGravity(Gravity.LEFT|Gravity.CENTER);
        }
        btnSignInOrUp.setText(resources.getString(R.string.SignInOrUp));
        if (session.isLoggedIn()){
            btnSignInOrUp.setText(resources.getString(R.string.LogOut));
        }
        showPurchases.setText(resources.getString(R.string.showPurchases));
        hireRequestStatus.setText(resources.getString(R.string.hireRequestStatus));
        btnEditProfile.setText(resources.getString(R.string.EditProfile));
        BtnInstagram.setText(resources.getString(R.string.Instagram));
        btnTelegram.setText(resources.getString(R.string.Telegram));
        profileType.setText(resources.getString(R.string.profileType));
        mytextUserInfo.setText(resources.getString(R.string.UserInfo));
        mytextUserInfo2.setText(resources.getString(R.string.UserInfo2));


    }
    public void updateView_dialog(String language){
        Context context = LocaleHelper.setLocale(getActivity(), language);
        Resources resources = context.getResources();
        txtEditProfile.setText(resources.getString(R.string.EditProfile));
        txtName.setText(resources.getString(R.string.Name));
        txtLastname.setText(resources.getString(R.string.Lastname));
        txtEmail.setText(resources.getString(R.string.Email));
        txtPhoneNumber.setText(resources.getString(R.string.PhoneNumber));
        txtTelephone.setText(resources.getString(R.string.Telephone));
        txtPostcode.setText(resources.getString(R.string.Postcode));
        txtAddress.setText(resources.getString(R.string.Address));
    }

    private void update_token(final String email , final String password, final Dialog dialog){

        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SIGNIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Get Token Tag", "Token Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        JSONObject c = jObj.getJSONObject("data");
                        newtoken = c.getString("token");
                        if(dialog != null)
                            get_credentials(newtoken, email, password, dialog);
                    }
                    else
                        hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 ");
                    e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(getContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error 2 in getting token! " + " " + error.getMessage());
                hideDialog();
                if(Language.equals("fa")){
                    Toast.makeText(getContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
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
    private void get_credentials(final String token, final String email, final String passwordd, final Dialog dialog){

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
                        session.setUserDetails(id,firstname,lastname,email,phone,token, image,mobile,address,zip,password,"0");
                        pDialog_updateuserinfo.setProgress(50);
                        if(dialog != null)
                            update_user(token, dialog);

                        hidestartpDialog_updateuserinfo();
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
                hidestartpDialog_updateuserinfo();
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
    private void update_user(final String token, final Dialog dialog){

        String tag_string_req = "req_edituser";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDIT_Profile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("EditUser TAG", "Edit user Response: " + response.toString());
                try {
                    newtoken="empty";
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        // user successfully logged in
                        JSONObject c = jObj.getJSONObject("data");
                        // some long running task will run here. We are using sleep as a dummy to delay execution
                        session.setUserDetails(c.getInt("id"),
                                c.getString("first_name"), c.getString("last_name"),
                                c.getString("email"),c.getString("tel_number"),
                                newtoken, session.getUserDetails().getImage(),c.getString("mobile"),
                                c.getString("address"),c.getString("postal_code"), session.getUserDetails().getPassword(),"0");

                        if(dialog.isShowing())
                            dialog.dismiss();
                        hideDialog();
                        if(Language.equals("fa")){
                            Toast.makeText(getContext(), "تغییرات اعمال شد", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(context, "Your request is successfully done", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error in login. Get the error message
                        if(Language.equals("fa")){
                            Toast.makeText(getContext(), "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 ");
                    e.printStackTrace();
                    if(Language.equals("fa")){
                        Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error 2",  error.getMessage() + " error");
                if(Language.equals("fa")){
                    Toast.makeText(context, "مشکلی در سرور یا ورودی\u200Cهایتان موجود است!", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(context, "There is problem in server or your inputs!", Toast.LENGTH_SHORT).show();
                }
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("auth", token);
                params.put("first_name", txtEditName.getText().toString());
                params.put("last_name", txtEditLastName.getText().toString());
                params.put("email", txtEditEmail.getText().toString());
                if(txtEditAddress.getText().toString().equals(null))
                    txtEditAddress.setText("empty");
                params.put("address", txtEditAddress.getText().toString());
                if(txtEditZip.getText().toString().equals(null))
                    txtEditZip.setText(0);
                params.put("postal_code", txtEditZip.getText().toString());
                if(txtEditPhone.getText().toString().equals(null))
                    txtEditPhone.setText(0);
                params.put("tel_number", txtEditPhone.getText().toString());
                if(txtEditMobile.getText().toString().equals(null))
                    txtEditMobile.setText(0);
                params.put("mobile", txtEditMobile.getText().toString());
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


    //Progress Dialog
    private void startDialog(){
        pDialog.setCancelable(true);
        if(Language.equals("fa"))
            pDialog.setMessage("در حال پردازش...");
        else
            pDialog.setMessage("Proccessing...");
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


    private void startpDialog_updateuserinfo(){
        pDialog_updateuserinfo.setCancelable(true);
        if(Language.equals("fa"))
            pDialog_updateuserinfo.setMessage("گرفتن اطلاعات کاربری...");
        else
            pDialog_updateuserinfo.setMessage("Getting Credentials...");
        pDialog_updateuserinfo.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        showstartpDialog_updateuserinfo();
    }
    private void showstartpDialog_updateuserinfo() {
        if (!pDialog_updateuserinfo.isShowing())
            pDialog_updateuserinfo.show();
    }
    private void hidestartpDialog_updateuserinfo() {
        if (pDialog_updateuserinfo.isShowing()) {
            pDialog_updateuserinfo.setProgress(100);
            pDialog_updateuserinfo.dismiss();
        }
    }

}
