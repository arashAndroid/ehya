package com.mrehya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mrehya.Helper.LocaleHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;


public class DashboardFragment extends Fragment {

    //new
    private MyTextView mytextPsycoTest,mytextHireBestCompanies,
            mytextPsycoTests1,mytextPsycoTests2,mytextPsycoTests3,mytextPsycoTests4,mytextPsycoTests5,mytextPsycoTests6;
    private String Language;

    private ImageView imgViewE1,imgViewE2,imgViewE3,imgViewE4,imgViewE5,imgViewE6;
    private TextView txtQnums1,txtQnums2,txtQnums3,txtQnums4,txtQnums5,txtQnums6;


    private TextView txtEmptyExams;

    private RecyclerView recyclerView;
    private dashAdapter adapter;
    private List<HireCompanies> companyList;
    Context context;
    RelativeLayout test1,test2,test3,test4,test5,test6 ;

    //
    private ProgressDialog pDialog;
    private TextView txtEmptyCompanies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        test1 = (RelativeLayout) view.findViewById(R.id.test1);
        test2 = (RelativeLayout) view.findViewById(R.id.test2);
        test3 = (RelativeLayout) view.findViewById(R.id.test3);
        test4 = (RelativeLayout) view.findViewById(R.id.test4);
        test5 = (RelativeLayout) view.findViewById(R.id.test5);
        test6 = (RelativeLayout) view.findViewById(R.id.test6);
        context =view.getContext();
        pDialog = new ProgressDialog(context);
        setToSquare(test1);
        setToSquare(test2);
        setToSquare(test3);
        setToSquare(test4);
        setToSquare(test5);
        setToSquare(test6);

        // new
        txtEmptyExams = (TextView) view.findViewById(R.id.txtEmptyExams);
        //new
        Paper.init(context);
        Language = updateLanguage();

        companyList = new ArrayList<>();
        adapter = new dashAdapter(getContext(), companyList);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.removeAllViews();
        RecyclerView.LayoutManager mLayoutManager;
        if(Language.equals("fa")) {
            mLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, true);
        }
        else{
            mLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //new
        mytextPsycoTest =  view.findViewById(R.id.mytextPsycoTest);
        mytextHireBestCompanies =  view.findViewById(R.id.mytextHireBestCompanies);
        mytextPsycoTests1 =  view.findViewById(R.id.mytextPsycoTests1);
        mytextPsycoTests2 =  view.findViewById(R.id.mytextPsycoTests2);
        mytextPsycoTests3 =  view.findViewById(R.id.mytextPsycoTests3);
        mytextPsycoTests4 =  view.findViewById(R.id.mytextPsycoTests4);
        mytextPsycoTests5 =  view.findViewById(R.id.mytextPsycoTests5);
        mytextPsycoTests6 =  view.findViewById(R.id.mytextPsycoTests6);

        txtEmptyCompanies =  view.findViewById(R.id.txtEmptyCompanies);

        imgViewE1 = view.findViewById(R.id.imgViewE1);
        imgViewE2 = view.findViewById(R.id.imgViewE2);
        imgViewE3 = view.findViewById(R.id.imgViewE3);
        imgViewE4 = view.findViewById(R.id.imgViewE4);
        imgViewE5 = view.findViewById(R.id.imgViewE5);
        imgViewE6 = view.findViewById(R.id.imgViewE6);

        txtQnums1 = (TextView) view.findViewById(R.id.txtQnums1);
        txtQnums2 = (TextView) view.findViewById(R.id.txtQnums2);
        txtQnums3 = (TextView) view.findViewById(R.id.txtQnums3);
        txtQnums4 = (TextView) view.findViewById(R.id.txtQnums4);
        txtQnums5 = (TextView) view.findViewById(R.id.txtQnums5);
        txtQnums6 = (TextView) view.findViewById(R.id.txtQnums6);

        //new
        updateView((String) Paper.book().read("language"));
        prepareCompanies();
        Exam_Api(Language);
        return view;
    }

    private void setToSquare(View mLayout){

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels;
        float dpWidth = displayMetrics.widthPixels ;
        final LinearLayout.LayoutParams layoutparams = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        double mlayouWidth = dpWidth *0.25 ;

        layoutparams.width = (int) mlayouWidth+10;
        layoutparams.height = (int) mlayouWidth;

        mLayout.setLayoutParams(layoutparams);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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

        mytextPsycoTest.setText(resources.getString(R.string.PsycoTest));
        mytextHireBestCompanies.setText(resources.getString(R.string.HireBestCompanies));


        if(Language.equals("fa")){
            Context context2= LocaleHelper.setLocale(getActivity(), "en");
        }

    }

    //Progress Dialog
    private void startDialog(){
        pDialog.setCancelable(true);
        if(Language.equals("fa"))
            pDialog.setMessage("در حال اتصال...");
        else
            pDialog.setMessage("Loading Companies...");
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


    private void Exam_Api(final String Language){
        startDialog();
        String tag_string_req = "req_Exams";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_DashExam+"2", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Exams Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.length()>0){
                        txtEmptyExams.setVisibility(View.GONE);
                        Show_Hide_Exams(true);
                        set_Content(data);
                        //adapter.notifyDataSetChanged();
                    }
                    else{
                        Show_Hide_Exams(false);
                        if(Language.equals("fa"))
                            txtEmptyExams.setText("لیست آزمون\u200Cها خالی است!");
                        else
                            txtEmptyExams.setText("Empty Hire Advertisements!");
                        txtEmptyExams.setVisibility(View.VISIBLE);
                    }
                    Log.d("TAG", "No Object recieved!");
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    Show_Hide_Exams(false);
                    if(Language.equals("fa")){
                        txtEmptyExams.setText("لیست آزمون\u200Cها خالی است");
                        Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        txtEmptyExams.setText("Empty Hire Advertisements!");
                        Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    txtEmptyExams.setVisibility(View.VISIBLE);

                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Show_Hide_Exams(false);
                Log.e("TAG",  "error2");
                if(Language.equals("fa")){
                    txtEmptyExams.setText("لیست آزمون\u200Cها خالی است");
                    Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    txtEmptyExams.setText("Empty Hire Advertisements!");
                    Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                txtEmptyExams.setVisibility(View.VISIBLE);
                hideDialog();
            }
        }) {
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
    //CHECK DATA.LENGTH NEED -1
    public void set_Content(JSONObject data) throws JSONException {

        Random rand = new Random();
        int length = data.length()-1;
        if(length>6){
            length=6;
        }
        List<Integer> Seen_examsList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int number = 0;
            do {
                number=rand.nextInt(data.length()-1);
            }
            while(Seen_examsList.contains(number));
            final int take = number;
            Seen_examsList.add(take);
            JSONObject c = data.getJSONObject(take+"");
            Log.d("Fragment part String: ", c.toString());
            switch (i){
                case 0:
                    if(Language.equals("fa"))
                        mytextPsycoTests1.setText(c.getString("title"));
                    else
                        mytextPsycoTests1.setText(c.getString("title_en"));
                    txtQnums1.setText(c.getString("price"));
                    if(c.getJSONObject("image").getString("thumb").length()>1) {
                        Glide.with(context)
                                .load(c.getJSONObject("image").getString("thumb"))
                                .placeholder(R.drawable.logo_eng)
                                .error(R.drawable.logo_eng)
                                .into(imgViewE1);
                        mytextPsycoTests1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                send(take);
                            }
                        });
                    }
                    break;
                case 1:
                    if(Language.equals("fa"))
                        mytextPsycoTests2.setText(c.getString("title"));
                    else
                        mytextPsycoTests2.setText(c.getString("title_en"));
                    txtQnums2.setText(c.getString("price"));
                    if(c.getJSONObject("image").getString("thumb").length()>1)
                    {
                        Glide.with(context)
                                .load(c.getJSONObject("image").getString("thumb"))
                                .placeholder(R.drawable.logo_eng)
                                .error(R.drawable.logo_eng)
                                .into(imgViewE2);
                        mytextPsycoTests2.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                send(take);
                            }
                        });
                    }
                    break;
                case 2:
                    if(Language.equals("fa"))
                        mytextPsycoTests3.setText(c.getString("title"));
                    else
                        mytextPsycoTests3.setText(c.getString("title_en"));
                    txtQnums3.setText(c.getString("price"));
                    if(c.getJSONObject("image").getString("thumb").length()>1) {
                        Glide.with(context)
                                .load(c.getJSONObject("image").getString("thumb"))
                                .placeholder(R.drawable.logo_eng)
                                .error(R.drawable.logo_eng)
                                .into(imgViewE3);
                        mytextPsycoTests3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                send(take);
                            }
                        });
                    }
                    break;
                case 3:
                    if(Language.equals("fa"))
                        mytextPsycoTests4.setText(c.getString("title"));
                    else
                        mytextPsycoTests4.setText(c.getString("title_en"));
                    txtQnums4.setText(c.getString("price"));
                    if(c.getJSONObject("image").getString("thumb").length()>1) {
                        Glide.with(context)
                                .load(c.getJSONObject("image").getString("thumb"))
                                .placeholder(R.drawable.logo_eng)
                                .error(R.drawable.logo_eng)
                                .into(imgViewE4);
                        mytextPsycoTests4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                send(take);
                            }
                        });
                    }
                    break;
                case 4:
                    if(Language.equals("fa"))
                        mytextPsycoTests5.setText(c.getString("title"));
                    else
                        mytextPsycoTests5.setText(c.getString("title_en"));
                    txtQnums5.setText(c.getString("price"));
                    if(c.getJSONObject("image").getString("thumb").length()>1) {
                        Glide.with(context)
                                .load(c.getJSONObject("image").getString("thumb"))
                                .placeholder(R.drawable.logo_eng)
                                .error(R.drawable.logo_eng)
                                .into(imgViewE5);
                        mytextPsycoTests5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                send(take);
                            }
                        });
                    }
                    break;
                case 5:
                    if(Language.equals("fa"))
                        mytextPsycoTests6.setText(c.getString("title"));
                    else
                        mytextPsycoTests6.setText(c.getString("title_en"));
                    txtQnums6.setText(c.getString("price"));
                    if(c.getJSONObject("image").getString("thumb").length()>1) {
                        Glide.with(context)
                                .load(c.getJSONObject("image").getString("thumb"))
                                .placeholder(R.drawable.logo_eng)
                                .error(R.drawable.logo_eng)
                                .into(imgViewE6);
                        mytextPsycoTests6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                send(take);
                            }
                        });
                    }
                    break;
                default:
                    Log.d("no more", c.toString());
            }
        }
    }
    //SHOW EXAMS OR GONE THEM
    public void Show_Hide_Exams(boolean show){
        if(show){
            test1.setVisibility(View.VISIBLE);
            test2.setVisibility(View.VISIBLE);
            test3.setVisibility(View.VISIBLE);
            test4.setVisibility(View.VISIBLE);
            test5.setVisibility(View.VISIBLE);
            test6.setVisibility(View.VISIBLE);
        }
        else{
            test1.setVisibility(View.GONE);
            test2.setVisibility(View.GONE);
            test3.setVisibility(View.GONE);
            test4.setVisibility(View.GONE);
            test5.setVisibility(View.GONE);
            test6.setVisibility(View.GONE);
        }
    }
    //send or intent to exam which we just meant!
    public void send(int pos) {
        Intent intent = new Intent(this.getActivity(),Test.class);
        intent.putExtra("examId",pos);
        context.startActivity(intent);
    }


    private void prepareCompanies() {
        String tag_string_req = "req_Companies";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Hires, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Company Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.length()>0){
                        txtEmptyCompanies.setVisibility(View.GONE);
                        set_Content_Company(data);
                    }
                    else{
                        if(Language.equals("fa"))
                            txtEmptyCompanies.setText("لیست شرکت\u200Cها خالی است!\"");
                        else
                            txtEmptyCompanies.setText("Empty Companies List!");
                        txtEmptyCompanies.setVisibility(View.VISIBLE);
                    }
                    Log.d("TAG", "No Object recieved!");
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    Show_Hide_Exams(false);
                    if(Language.equals("fa")){
                        txtEmptyCompanies.setText("لیست شرکت\u200Cها خالی است");
                        Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        txtEmptyCompanies.setText("Empty Companies List!");
                        Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    txtEmptyCompanies.setVisibility(View.VISIBLE);
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Show_Hide_Exams(false);
                Log.e("TAG",  "error2");
                if(Language.equals("fa")){
                    txtEmptyCompanies.setText("لیست شرکت\u200Cها خالی است");
                    Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    txtEmptyCompanies.setText("Empty Companies List!");
                    Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                txtEmptyCompanies.setVisibility(View.VISIBLE);
                hideDialog();
            }
        }) {
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
    public void set_Content_Company(JSONObject data) throws JSONException {
        //check language to add item to list
        if(Language.equals("fa")) {
            for (int i = 0; i < data.length() - 1; i++) {
                JSONObject c = data.getJSONObject(i + "");
                HireCompanies a = new HireCompanies(c.getString("brand_name"), c.getString("image"));
                companyList.add(a);
            }
        }
        else{
            for (int i = 0; i < data.length() - 1; i++) {
                JSONObject c = data.getJSONObject(i + "");
                HireCompanies a = new HireCompanies(c.getString("brand_name"), c.getString("image"));
                companyList.add(a);
            }
        }
            adapter.notifyDataSetChanged();
    }

}
