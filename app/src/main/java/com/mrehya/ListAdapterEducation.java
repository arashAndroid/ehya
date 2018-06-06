package com.mrehya;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mrehya.Helper.LocaleHelper;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by Rubick on 2/15/2018.
 */

public class ListAdapterEducation extends BaseAdapter implements ListAdapter {
    private ArrayList<Education> list = new ArrayList<Education>();
    private Context context;
    private Activity activity;
    private ListView listView;

    //new
    LinearLayout LinearLayoutitemeducation1;
    TextView txtEducationTitle;


    public ListAdapterEducation(ArrayList<Education> list, Context context, Activity activity, ListView listView) {
        this.list = list;
        this.context = context;
        this.activity = activity;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        if(list!=null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_education, null);
        }

        //Handle TextView and display string from your list
        Education edu = list.get(position);
        TextView txtEducationTitle = (TextView)view.findViewById(R.id.txtEducationTitle);
        TextView txtEducationCompany = (TextView)view.findViewById(R.id.txtEducationCompany);
        TextView txtEducationFrom = (TextView)view.findViewById(R.id.txtEducationFrom);
        TextView txtEducationTo = (TextView)view.findViewById(R.id.txtEducationTo);

        txtEducationTitle.setText(edu.getField());
        txtEducationCompany.setText(edu.getPlace());
        txtEducationFrom.setText(edu.getFrom());
        txtEducationTo.setText(edu.getTo());

        //Handle buttons and add onClickListeners
        ImageButton btnDelete =view.findViewById(R.id.btnDeleteEducation);
        ImageButton btnEdit = view.findViewById(R.id.btnEditEducation);

        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
                justifyListViewHeightBasedOnChildren(listView);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Education edu2 = list.get(position);
                showDialog(edu2.getField(),edu2.getPlace(),edu2.getFrom(),edu2.getTo(),position);
                notifyDataSetChanged();
            }
        });

         //new
        LinearLayoutitemeducation1=view.findViewById(R.id.LinearLayoutitemeducation1);
        txtEducationTitle=view.findViewById(R.id.txtEducationTitle);
        //updateView(updateLanguage());

        return view;
    }
    private void showDialog(String educationField,String educationPlace,String educationFrom,String educationTo, final int index){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.list_dialog_education);
        final EditText txtEditField = dialog.findViewById(R.id.txtEditDialogEducationField);
        final EditText txtEditPlace = dialog.findViewById(R.id.txtEditDialogEducationPlace);
        final Spinner txtEditFrom = dialog.findViewById(R.id.spinnerFromEducation);
        final Spinner txtEditTo = dialog.findViewById(R.id.spinnerToEducation);
        final Spinner txtEditgrade = dialog.findViewById(R.id.spinnerFromEducation);

        txtEditField.setText(educationField);
        txtEditPlace.setText(educationPlace);
        for (int i=0;i<txtEditFrom.getCount();i++){
            if (txtEditFrom.getItemAtPosition(i).toString().equalsIgnoreCase(educationFrom)){
                txtEditFrom.setSelection(i,true);
                break;
            }
        }
        for (int i=0;i<txtEditTo.getCount();i++){
            if (txtEditTo.getItemAtPosition(i).toString().equalsIgnoreCase(educationTo)){
                txtEditTo.setSelection(i,true);
                break;
            }
        }
        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogEducation);
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Education edu = new Education(txtEditgrade.getSelectedItem().toString(), txtEditField.getText().toString(),txtEditPlace.getText().toString(),txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString());
                list.set(index,edu);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout((12 * width)/13, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    private String updateLanguage(){
        //Default language is fa
        String language = Paper.book().read("language");
        if(language==null)
            Paper.book().write("language", "fa");
        return language;
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this.context  , language);

        if(language.equals("fa")){
            txtEducationTitle.setGravity(Gravity.RIGHT|Gravity.CENTER);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutitemeducation1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutitemeducation1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
        else{
            txtEducationTitle.setGravity(Gravity.LEFT|Gravity.CENTER);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutitemeducation1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutitemeducation1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
    }
}
