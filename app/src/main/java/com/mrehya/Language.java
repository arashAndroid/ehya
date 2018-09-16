package com.mrehya;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.mrehya.DashboardPackage.DashboardLists;

import java.util.Locale;

import co.ronash.pushe.Pushe;
import io.paperdb.Paper;

public class Language extends AppCompatActivity {
    Button btnPersian, btnEnglish;
    ImageButton imgBtnPersian, imgBtnEnglish;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        DashboardLists dl = new DashboardLists(this, true);
        Pushe.initialize(this,true);
        btnPersian = (Button) findViewById(R.id.btnPersian);
        imgBtnPersian = (ImageButton) findViewById(R.id.imgBtnPersian);

        //new
        Paper.init(this);

        btnPersian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write("language", "fa");
                Intent intent = new Intent(Language.this,MainActivity.class);
                intent.putExtra("Language", "fa");
                startActivity(intent);
                finish();
            }
        });

        btnEnglish = (Button) findViewById(R.id.btnEnglish);
        imgBtnEnglish = (ImageButton) findViewById(R.id.imgBtnEnglish);
        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select lang
                Paper.book().write("language", "en");

                Intent intent = new Intent(Language.this,MainActivity.class);
                intent.putExtra("Language", "en");
                startActivity(intent);
                finish();
            }
        });

        //new
        //set default direction
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            updateResources(this, "en");
        else
            updateResourcesLegacy(this, "en");

    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String lang) {
        Locale locale = new Locale(lang);
        locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        return context.createConfigurationContext(config);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String lang) {
        Locale locale = new Locale(lang);
        locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            configuration.setLayoutDirection(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

}
