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

public class ListAdapterJobs extends BaseAdapter implements ListAdapter {
    private ArrayList<Job> list = new ArrayList<Job>();
    private Context context;
    private Activity activity;
    private ListView listView;

    //new
    private LinearLayout LinearLayoutitemjobs1;
    private TextView txtJobTitle;

    public ListAdapterJobs(ArrayList<Job> list, Context context, Activity activity,ListView listView) {
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
            view = inflater.inflate(R.layout.list_item_jobs, null);
        }

        //Handle TextView and display string from your list
        Job job = list.get(position);
        TextView txtJobTitle = (TextView)view.findViewById(R.id.txtJobTitle);
        TextView txtJobCompany = (TextView)view.findViewById(R.id.txtJobCompany);
        TextView txtJobFrom = (TextView)view.findViewById(R.id.txtJobFrom);
        TextView txtJobTo = (TextView)view.findViewById(R.id.txtJobTo);

        txtJobTitle.setText(job.getRole());
        txtJobCompany.setText(job.getCompany());
        txtJobFrom.setText(job.getFrom());
        txtJobTo.setText(job.getTo());

        //Handle buttons and add onClickListeners
        ImageButton btnDelete =view.findViewById(R.id.btnDeleteJob);
        ImageButton btnEdit = view.findViewById(R.id.btnEditJob);

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
                Job job2 = list.get(position);
                showDialog(job2.getRole(),job2.getCompany(),job2.getFrom(),job2.getTo(),position);
                notifyDataSetChanged();
            }
        });
        //new
        LinearLayoutitemjobs1=view.findViewById(R.id.LinearLayoutitemjobs1);
        this.txtJobTitle=view.findViewById(R.id.txtJobTitle);
        //updateView(updateLanguage());

        return view;
    }
    private void showDialog(String jobRole,String jobCompany,String jobFrom,String jobTo, final int index){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.list_dialog_jobs);
        final EditText txtEditRole = dialog.findViewById(R.id.txtEditDialogJobRole);
        final EditText txtEditCompany = dialog.findViewById(R.id.txtEditDialogJobCompany);
        final Spinner txtEditFrom = dialog.findViewById(R.id.spinnerFromJob);
        final Spinner txtEditTo = dialog.findViewById(R.id.spinnerToJob);
        final EditText txtEditJobtitle = dialog.findViewById(R.id.txtEditDialogJobtitle);

        txtEditRole.setText(jobRole);
        txtEditCompany.setText(jobCompany);
        for (int i=0;i<txtEditFrom.getCount();i++){
            if (txtEditFrom.getItemAtPosition(i).toString().equalsIgnoreCase(jobFrom)){
                txtEditFrom.setSelection(i,true);
                break;
            }
        }
        for (int i=0;i<txtEditTo.getCount();i++){
            if (txtEditTo.getItemAtPosition(i).toString().equalsIgnoreCase(jobTo)){
                txtEditTo.setSelection(i,true);
                break;
            }
        }
        ImageButton btnEdit = dialog.findViewById(R.id.btnEditDialogJob);
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job job = new Job(txtEditJobtitle.getText().toString(),txtEditFrom.getSelectedItem().toString(),txtEditTo.getSelectedItem().toString(),txtEditRole.getText().toString(),txtEditCompany.getText().toString());
                list.set(index,job);
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
            txtJobTitle.setGravity(Gravity.RIGHT|Gravity.CENTER);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutitemjobs1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutitemjobs1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
        else{
            txtJobTitle.setGravity(Gravity.LEFT|Gravity.CENTER);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                LinearLayoutitemjobs1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LinearLayoutitemjobs1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
    }
}
