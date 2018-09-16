package com.mrehya.Hire;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mrehya.AppConfig;
import com.mrehya.AppController;
import com.mrehya.Helper.LocaleHelper;
import com.mrehya.Login;
import com.mrehya.MainActivity;
import com.mrehya.R;
import com.mrehya.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class HireAdapter extends RecyclerView.Adapter<HireAdapter.MyViewHolder> {

    private SessionManager session;
    private Context mContext;
    private List<HireCompanies> hireCompaniesList;

    //new
    private com.mrehya.VerticalTextView text1;
    private LinearLayout LinearLayoutRecyclerItem_hire;
    private ImageView thumbnail;
    private TextView title;
    String Language;
    //new ShowHire dialog xml
    ImageView imgShwhireImg;
    TextView txtCompanyName,txtHireExplainShowhire,txtJobTitleShowhire,txtJobTitleShowhire2
            ,txtJobTimeShowhire,txtJobTimeShowhire2,txtJobPlaceShowhire,txtJobPlaceShowhire2;
    LinearLayout LinearLayoutdialog_show_hire,LinearLayoutdialog_show_hire2
            ,LinearLayoutdialog_show_hire3,LinearLayoutdialog_show_hire4;
    Button btn_sendresume,btn_close;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, city,timeAgo;
        public ImageView thumbnail;
        public RelativeLayout RelativeLayout_hirerecycle;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            timeAgo = (TextView) view.findViewById(R.id.timeAgo);
            city = (TextView) view.findViewById(R.id.city);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            RelativeLayout_hirerecycle = (RelativeLayout) view.findViewById(R.id.RelativeLayout_hirerecycle);
        }
    }


    public HireAdapter(Context mContext, List<HireCompanies> hireCompaniesList, SessionManager session ) {
        this.session = session;
        this.mContext = mContext;
        this.hireCompaniesList = hireCompaniesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hire_recycler_item, parent, false);


        //new
        Paper.init(mContext);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        text1 = itemView.findViewById(R.id.text1);
        LinearLayoutRecyclerItem_hire = itemView.findViewById(R.id.LinearLayoutRecyclerItem_hire);
        Language = updateLanguage();
        updateView(Language);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final HireCompanies HireCompany = hireCompaniesList.get(position);
        holder.title.setText(HireCompany.getCompanyName());
        holder.city.setText(HireCompany.getPlace());
        holder.timeAgo.setText(HireCompany.getTime());
        // loading album cover using Glide library
        Glide.with(mContext)
                .load(HireCompany.getImage())
                .placeholder(R.drawable.logo_eng)
                .error(R.drawable.logo_eng)
                .into(holder.thumbnail);
        holder.RelativeLayout_hirerecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_company_page(HireCompany);
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_company_page(HireCompany);
            }
        });
        //Glide.with(mContext).load(HireCompany.getImage()).into(holder.thumbnail);
    }


    public void go_to_company_page(HireCompanies HireCompany){
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_show_hire);


        imgShwhireImg                   = dialog.findViewById(R.id.imgShwhireImg);

        txtCompanyName                  = dialog.findViewById(R.id.txtCompanyName);
        txtHireExplainShowhire          = dialog.findViewById(R.id.txtHireExplainShowhire);
        txtJobTitleShowhire             = dialog.findViewById(R.id.txtJobTitleShowhire);
        txtJobTitleShowhire2            = dialog.findViewById(R.id.txtJobTitleShowhire2);
        txtJobTimeShowhire              = dialog.findViewById(R.id.txtJobTimeShowhire);
        txtJobTimeShowhire2             = dialog.findViewById(R.id.txtJobTimeShowhire2);
        txtJobPlaceShowhire             = dialog.findViewById(R.id.txtJobPlaceShowhire);
        txtJobPlaceShowhire2            = dialog.findViewById(R.id.txtJobPlaceShowhire2);

        LinearLayoutdialog_show_hire    = dialog.findViewById(R.id.LinearLayoutdialog_show_hire);
        LinearLayoutdialog_show_hire2   = dialog.findViewById(R.id.LinearLayoutdialog_show_hire2);
        LinearLayoutdialog_show_hire3   = dialog.findViewById(R.id.LinearLayoutdialog_show_hire3);
        LinearLayoutdialog_show_hire4   = dialog.findViewById(R.id.LinearLayoutdialog_show_hire4);

        btn_sendresume                  = dialog.findViewById(R.id.btn_sendresume);
        btn_close                       = dialog.findViewById(R.id.btn_close);


        //set views
        // loading album cover using Glide library
        Glide.with(mContext)
                .load(HireCompany.getImage())
                .placeholder(R.drawable.logo_eng)
                .error(R.drawable.logo_eng)
                .into(imgShwhireImg);

        txtHireExplainShowhire.setText(HireCompany.getdesc());
        txtCompanyName.setText(HireCompany.getCompanyName());

       // if(Language.equals("fa")){
            txtJobTitleShowhire.setText(HireCompany.getTitle());
            txtJobTimeShowhire.setText(HireCompany.getTime());
            txtJobPlaceShowhire.setText(HireCompany.getPlace());
     //   }
//        else{
//            txtJobTitleShowhire2.setText(HireCompany.getTitle());
//            txtJobTimeShowhire2.setText(HireCompany.getTime());
//            txtJobPlaceShowhire2.setText(HireCompany.getPlace());
//        }


        final int id = HireCompany.getId();
        btn_sendresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getUserDetails().getToken()==null){
                    if(Language.equals("fa"))
                        Toast.makeText(mContext, "برای ارسال رزومه باید لاکین باشد", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mContext, "You must login first", Toast.LENGTH_SHORT).show();
                }
                else{
                    //+"(مشاهده وضعیت درخواست در پروفایل شخصی)"
                    Toast.makeText(mContext, "در حال بررسی در خواست", Toast.LENGTH_SHORT).show();
                    sendResume(id);
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        updateView_dialog();
        dialog.show();
        dialog.getWindow().setLayout((12 * width)/13, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public int getItemCount() {
        return hireCompaniesList.size();
    }



    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(mContext, language);
        Resources resources = context.getResources();

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams thumbnailparams = new RelativeLayout.LayoutParams(210, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams titleparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams LinearLayoutrecyclerItemhireparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        if(language.equals("fa")){
            text1.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            text1.setLayoutParams(params);

            thumbnailparams.addRule(RelativeLayout.RIGHT_OF, R.id.text1);
            thumbnailparams.setMargins(2,0,2,0);
            thumbnail.setLayoutParams(thumbnailparams);

            titleparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            titleparams.setMargins(0,0,15,0);
            title.setLayoutParams(titleparams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutRecyclerItem_hire.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            LinearLayoutrecyclerItemhireparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            LinearLayoutrecyclerItemhireparams.addRule(RelativeLayout.BELOW, R.id.title);
            LinearLayoutrecyclerItemhireparams.setMargins(15,10,0,0);
            LinearLayoutRecyclerItem_hire.setLayoutParams(LinearLayoutrecyclerItemhireparams);
        }
        else{
            text1.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
            text1.setLayoutParams(params);

            thumbnailparams.addRule(RelativeLayout.LEFT_OF, R.id.text1);
            thumbnail.setLayoutParams(thumbnailparams);

            titleparams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            titleparams.setMargins(15,10,0,0);
            title.setLayoutParams(titleparams);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                LinearLayoutRecyclerItem_hire.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            LinearLayoutrecyclerItemhireparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            LinearLayoutrecyclerItemhireparams.addRule(RelativeLayout.BELOW, R.id.title);
            LinearLayoutrecyclerItemhireparams.setMargins(0,10,35,0);
            LinearLayoutRecyclerItem_hire.setLayoutParams(LinearLayoutrecyclerItemhireparams);

            text1.setText(resources.getString(R.string.Hire));
        }
    }

    private void sendResume(int id){

        String tag_string_req = "req_sendresume";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_RESUME+id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("req_sendresume", "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    String success = jObj.getString("success");

                    // Check for error node in json
                    if (success.equalsIgnoreCase("true")) {
                        String result = jObj.getString("data");
                        if(Language.equals("fa")){
                            Toast.makeText(mContext,  result   , Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext, "Done. Check profile for results", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(Language.equals("fa")){
                            Toast.makeText(mContext, "شما باید دوباره لاگین کنید", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(mContext, "you must login again", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    Log.d("req_sendresume", "error 1: " + e.getMessage());
                    if(Language.equals("fa")){
                        Toast.makeText(mContext, "مشکلی در داده ها پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(mContext, "Problem in data", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("req_sendresume",  "error2: " + error.getMessage());
                if(Language.equals("fa")){
                    Toast.makeText(mContext, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(mContext, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("auth", session.getUserDetails().getToken());
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


    private void updateView_dialog() {
        Context context = LocaleHelper.setLocale(mContext, Language);
        Resources resources = context.getResources();

        if(Language.equals("fa")){
//            LinearLayoutdialog_show_hire.setGravity(Gravity.RIGHT);
//            LinearLayoutdialog_show_hire2.setGravity(Gravity.RIGHT);
//            LinearLayoutdialog_show_hire3.setGravity(Gravity.RIGHT);
//            LinearLayoutdialog_show_hire4.setGravity(Gravity.RIGHT);

            txtJobTitleShowhire2.setText(resources.getString(R.string.JobTitleShowhire));
            txtJobTimeShowhire2.setText(resources.getString(R.string.JobTimeShowhire));
            txtJobPlaceShowhire2.setText(resources.getString(R.string.JobPlaceShowhire));
        }
        else{
//            LinearLayoutdialog_show_hire.setGravity(Gravity. LEFT);
//            LinearLayoutdialog_show_hire2.setGravity(Gravity.LEFT);
//            LinearLayoutdialog_show_hire3.setGravity(Gravity.LEFT);
//            LinearLayoutdialog_show_hire4.setGravity(Gravity.LEFT);

            txtJobTitleShowhire2.setText(resources.getString(R.string.JobTitleShowhire));
            txtJobTimeShowhire2.setText(resources.getString(R.string.JobTimeShowhire));
            txtJobPlaceShowhire2.setText(resources.getString(R.string.JobPlaceShowhire));
        }

        btn_close.setText(resources.getString(R.string.Close));
        btn_sendresume.setText(resources.getString(R.string.Send_Resume));
    }
}