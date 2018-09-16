package com.mrehya;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mrehya.Helper.LocaleHelper;
import com.mrehya.Hire.ShowHireStatus;
import com.mrehya.Shopping.ShowPurchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

import android.Manifest;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private Context context;
    private ProgressDialog pDialog, pDialog_updateuserinfo;
    private String Language, newtoken="empty";
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    //ELEMENTS
    private Button btnSignInOrUp, showPurchases, hireRequestStatus, btnEditProfile,
             BtnInstagram ,btnTelegram, ReserveRequestStatus, btn_save_editprof;
    private AlertDialog ad ;
    private MyTextView profileType, mytextUserInfo, mytextUserInfo2, profname;
    private LinearLayout LinearLayoutprofile1,LinearLayoutprofile2,LinearLayoutprofile3,LinearLayoutprofile4
            ,LinearLayoutprofile5,LinearLayoutprofile6, LinearLayoutprofileReserve;
    private TextView txtEditName,txtEditLastName,txtEditEmail,txtEditMobile,txtEditPhone,txtEditZip,txtEditAddress;
    private TextView txtEditProfile,txtName,txtLastname,txtEmail,txtPhoneNumber,txtTelephone,txtPostcode,txtAddress;
    private ImageView imageViewProfile;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return findViews(inflater, container);
    }
    private View findViews(final LayoutInflater inflater, ViewGroup container){
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        btnSignInOrUp = view.findViewById(R.id.btnSignInOrUp);
        showPurchases = view.findViewById(R.id.showPurchases);
        hireRequestStatus = view.findViewById(R.id.hireRequestStatus);
        ReserveRequestStatus = view.findViewById(R.id.ReserveRequestStatus);

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        context =view.getContext();
        pDialog = new ProgressDialog(context);
        sessionManager = new SessionManager(context);
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
        LinearLayoutprofileReserve = (LinearLayout) view.findViewById(R.id.LinearLayoutprofileReserve);
        imageViewProfile = (ImageView) view.findViewById(R.id.profileImage);
        btnTelegram = view.findViewById(R.id.btnTelegram);
        BtnInstagram = view.findViewById(R.id.BtnInstagram);
        profname = view.findViewById(R.id.profileName);

        setViews();
        loadImageFromStorage("/data/user/0/com.mrehya/app_EHYA");
        setOnclicks();
        return view;
    }
    private void setViews(){
        //Remaining apis
        LinearLayoutprofile4.setVisibility(View.GONE);
        LinearLayoutprofileReserve.setVisibility(View.GONE);
        LinearLayoutprofile3.setVisibility(View.GONE);

        profname.setText(sessionManager.getUserDetails().getFirstname() + " " + sessionManager.getUserDetails().getLastname());
        Language = updateLanguage();
        updateView((String) Paper.book().read("language"));
    }
    private void setOnclicks(){

        //choose image dialog
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()) {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.dialog_profile_image);

                    final TextView title = (TextView) dialog.findViewById(R.id.txtImagedialog);
                    final Button btn_gallery = (Button) dialog.findViewById(R.id.btn_gallery);
                    final Button btn_take = (Button) dialog.findViewById(R.id.btn_take);
                    final Button btn_remove = (Button) dialog.findViewById(R.id.btn_remove);
                    final Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                    if (Language.equals("fa")) {
                        title.setText("تصویر پروفایل");
                        btn_gallery.setText("انتخاب از گالری");
                        btn_take.setText("گرفتن عکس");
                        btn_remove.setText("حذف");
                        btn_cancel.setText("انصراف");
                    } else {
                        title.setText("Profile picture");
                        btn_gallery.setText("Choose from gallery");
                        btn_take.setText("Take new one");
                        btn_remove.setText("Remove");
                        btn_cancel.setText("Cancel");
                    }

                    btn_gallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 1);
                        }
                    });
                    ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                    File directory = cw.getDir("EHYA", Context.MODE_PRIVATE);
                    loadImageFromStorage(directory.getPath());

                    Log.e("path: ",directory.getPath());


                    btn_take.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (checkSelfPermission(context, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        MY_CAMERA_PERMISSION_CODE);
                            } else {
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            }
                        }
                    });

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btn_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imageViewProfile.setImageDrawable(getResources().getDrawable(R.drawable.profile_thumb));
                            File myFile = new File("/data/user/0/com.mrehya/app_EHYA/profile.jpg");
                            if(myFile.exists())
                                myFile.delete();
                        }
                    });

                    DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    dialog.getWindow().setLayout((12 * width) / 13, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });

        btnTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://t.me/memoryinstitute";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        btnSignInOrUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManager.isLoggedIn()){
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("آیا واقعا میخواهید خارج شوید؟");

                    alertDialogBuilder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sessionManager.setLogin(false);
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
        ReserveRequestStatus.setOnClickListener(new View.OnClickListener() {
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
                            ,sessionManager.getUserDetails().getPassword(), null);
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
                            update_token(sessionManager.getUserDetails().getEmail(), sessionManager.getUserDetails().getPassword(), dialog);
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
    }


    //methods
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
                        sessionManager.setLogin(true);
                        sessionManager.setUserDetails(id,firstname,lastname,email,phone,token, image,mobile,address,zip,password,"0");
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
                        sessionManager.setUserDetails(c.getInt("id"),
                                c.getString("first_name"), c.getString("last_name"),
                                c.getString("email"),c.getString("tel_number"),
                                newtoken, sessionManager.getUserDetails().getImage(),c.getString("mobile"),
                                c.getString("address"),c.getString("postal_code"), sessionManager.getUserDetails().getPassword(),"0");

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

    //IMAGE Gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                saveToInternalStorage(bitmapImage);
                imageViewProfile.setImageBitmap(bitmapImage);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void loadImageFromStorage(String path) {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageViewProfile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("EHYA", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    private void getContentResolver() {

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Image TAKE
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == 2){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_profile_image);

                final TextView title = (TextView) dialog.findViewById(R.id.txtImagedialog);
                final Button btn_gallery = (Button) dialog.findViewById(R.id.btn_gallery);
                final Button btn_take = (Button) dialog.findViewById(R.id.btn_take);
                final Button btn_remove = (Button) dialog.findViewById(R.id.btn_remove);
                final Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                if (Language.equals("fa")) {
                    title.setText("تصویر پروفایل");
                    btn_gallery.setText("انتخاب از گالری");
                    btn_take.setText("گرفتن عکس");
                    btn_remove.setText("حذف");
                    btn_cancel.setText("انصراف");
                } else {
                    title.setText("Profile picture");
                    btn_gallery.setText("Choose from gallery");
                    btn_take.setText("Take new one");
                    btn_remove.setText("Remove");
                    btn_cancel.setText("Cancel");
                }

                btn_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);
                    }
                });
                ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                File directory = cw.getDir("EHYA", Context.MODE_PRIVATE);
                loadImageFromStorage(directory.getPath());


                btn_take.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkSelfPermission(context, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setLayout((12 * width) / 13, ViewGroup.LayoutParams.WRAP_CONTENT);

            }else{
                Toast.makeText(getContext(),"اجازه دسترسی به فظای دیسک داده نشده است!",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode ==3){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_profile_image);

                final TextView title = (TextView) dialog.findViewById(R.id.txtImagedialog);
                final Button btn_gallery = (Button) dialog.findViewById(R.id.btn_gallery);
                final Button btn_take = (Button) dialog.findViewById(R.id.btn_take);
                final Button btn_remove = (Button) dialog.findViewById(R.id.btn_remove);
                final Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

                if (Language.equals("fa")) {
                    title.setText("تصویر پروفایل");
                    btn_gallery.setText("انتخاب از گالری");
                    btn_take.setText("گرفتن عکس");
                    btn_remove.setText("حذف");
                    btn_cancel.setText("انصراف");
                } else {
                    title.setText("Profile picture");
                    btn_gallery.setText("Choose from gallery");
                    btn_take.setText("Take new one");
                    btn_remove.setText("Remove");
                    btn_cancel.setText("Cancel");
                }

                btn_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);
                    }
                });
                ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                File directory = cw.getDir("EHYA", Context.MODE_PRIVATE);
                loadImageFromStorage(directory.getPath());


                btn_take.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkSelfPermission(context, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    MY_CAMERA_PERMISSION_CODE);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getWindow().setLayout((12 * width) / 13, ViewGroup.LayoutParams.WRAP_CONTENT);
            }else{
                Toast.makeText(getContext(),"اجازه دسترسی به فظای دیسک داده نشده است!",Toast.LENGTH_SHORT).show();            }
        }
    }
//    void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            imageViewProfile.setImageBitmap(photo);
//        }
//    }

    //usual methods
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
                LinearLayoutprofileReserve.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
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
                LinearLayoutprofileReserve.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo2.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                mytextUserInfo.setGravity(Gravity.RIGHT);
                mytextUserInfo2.setGravity(Gravity.RIGHT);

            }

            btnSignInOrUp.setGravity(Gravity.RIGHT|Gravity.CENTER);
            showPurchases.setGravity(Gravity.RIGHT|Gravity.CENTER);
            hireRequestStatus.setGravity(Gravity.RIGHT|Gravity.CENTER);
            ReserveRequestStatus.setGravity(Gravity.RIGHT|Gravity.CENTER);
            btnEditProfile.setGravity(Gravity.RIGHT|Gravity.CENTER);
            //BtnInstagram.setGravity(Gravity.RIGHT|Gravity.CENTER);
            //btnTelegram.setGravity(Gravity.RIGHT|Gravity.CENTER);
        }
        else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutprofile1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile2.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile3.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile4.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile5.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofile6.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                LinearLayoutprofileReserve.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
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
                LinearLayoutprofileReserve.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                mytextUserInfo.setGravity(Gravity.LEFT);
                mytextUserInfo2.setGravity(Gravity.LEFT);
            }

            btnSignInOrUp.setGravity(Gravity.LEFT|Gravity.CENTER);
            showPurchases.setGravity(Gravity.LEFT|Gravity.CENTER);
            hireRequestStatus.setGravity(Gravity.LEFT|Gravity.CENTER);
            ReserveRequestStatus.setGravity(Gravity.LEFT|Gravity.CENTER);
            btnEditProfile.setGravity(Gravity.LEFT|Gravity.CENTER);
            //BtnInstagram.setGravity(Gravity.LEFT|Gravity.CENTER);
            //btnTelegram.setGravity(Gravity.LEFT|Gravity.CENTER);
        }
        btnSignInOrUp.setText(resources.getString(R.string.SignInOrUp));
        if (sessionManager.isLoggedIn()){
            btnSignInOrUp.setText(resources.getString(R.string.LogOut));
        }
        showPurchases.setText(resources.getString(R.string.showPurchases));
        hireRequestStatus.setText(resources.getString(R.string.hireRequestStatus));
        ReserveRequestStatus.setText(resources.getString(R.string.ReserveRequestStatus));
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


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted1");
                return true;
            } else {

                Log.v("TAG","Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted2");
                return true;
            } else {

                Log.v("TAG","Permission is revoked2");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted2");
            return true;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 2:
//                Log.d(TAG, "External storage2");
//                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
//                    //resume tasks needing this permission
//                    downloadPdfFile();
//                }else{
//                    progress.dismiss();
//                }
//                break;
//
//            case 3:
//                Log.d(TAG, "External storage1");
//                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
//                    //resume tasks needing this permission
//                    SharePdfFile();
//                }else{
//                    progress.dismiss();
//                }
//                break;
//        }
//    }

}
