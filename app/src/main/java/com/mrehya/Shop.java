package com.mrehya;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class Shop extends AppCompatActivity {

    private ProgressDialog pDialog;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> shopList;
    public TextView txtEmptyProductRequest;

    //new
    MyTextView mytextShop;
    String Language;
    View view;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        context =this;
        pDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        shopList = new ArrayList<>();
        adapter = new ProductAdapter(getApplicationContext(), shopList);



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        txtEmptyProductRequest = (TextView) findViewById(R.id.txtEmptyProductRequest);

//        prepareAlbums();
        Paper.init(this);
        Language = updatelanguage(this);

        Product_Api(Language);

        //new
        mytextShop = (MyTextView) findViewById(R.id.mytextShop);
        updateView(Language);
        }

    private void Product_Api(final String Language){
        startDialog();
        String tag_string_req = "req_product";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_Products, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "product Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    if(data.length()>0){
                        txtEmptyProductRequest.setVisibility(View.GONE);
                        for (int i = 0; i < data.length()-1; i++) {
                            JSONObject c = data.getJSONObject(i+"");
                            JSONObject image = c.getJSONObject("image");
                            Log.d("TAG", c.toString());
                            Product a = new Product(c.getInt("id"),c.getString("price"),c.getString("title"),
                                    image.getString("thumb"),image.getString("preview"),c.getString("short_description")
                                    ,c.getInt("stock"));
                            shopList.add(a);
                        }

                        adapter.notifyDataSetChanged();
                    }
                    else{
                        if(Language.equals("fa"))
                            txtEmptyProductRequest.setText("لیست محصولات\u200Cها خالی است!\"");
                        else
                            txtEmptyProductRequest.setText("Empty products!");
                        txtEmptyProductRequest.setVisibility(View.VISIBLE);
                    }
                    Log.d("TAG", "No Object recieved!");
                    hideDialog();
                } catch (JSONException e) {
                    // JSON error
                    Log.d("TAG", "error 1 " + e.getMessage());
                    //e.printStackTrace();
                    if(Language.equals("fa")){
                        txtEmptyProductRequest.setText("لیست محصولات\u200Cها خالی است");
                        Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        txtEmptyProductRequest.setText("Empty products!");
                        Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                    }
                    txtEmptyProductRequest.setVisibility(View.VISIBLE);

                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",  "error2");
                if(Language.equals("fa")){
                    txtEmptyProductRequest.setText("لیست محصولات\u200Cها خالی است");
                    Toast.makeText(context, "مشکلی در اتصال با سرور پیش آمده است", Toast.LENGTH_SHORT).show();
                }

                else{
                    txtEmptyProductRequest.setText("Empty products!");
                    Toast.makeText(context, "Network Connection or Server failed!", Toast.LENGTH_SHORT).show();
                }
                txtEmptyProductRequest.setVisibility(View.VISIBLE);
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

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
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


    private String updatelanguage(Context context){
        Paper.init(context);
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");

        return language;
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this, language);
        Resources resources = context.getResources();
        mytextShop.setText(resources.getString(R.string.Shop));
    }

    private void startDialog(){
        pDialog.setCancelable(false);
        if(Language.equals("fa"))
            pDialog.setMessage("گرفتن لیست آیتم\u200Cهای محصولات...");
        else
            pDialog.setMessage("Loading Hiring Advertisements...");
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
}