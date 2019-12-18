package alosboiya.jeddahwave.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import alosboiya.jeddahwave.Fragments.AddPostFragment;
import alosboiya.jeddahwave.Fragments.MainProfileFragment;
import alosboiya.jeddahwave.Fragments.RechargeFragment;
import alosboiya.jeddahwave.Fragments.SettingFragment;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class MyAccountActivity extends AppCompatActivity {

    Button logeout, credit, setting, myprofile;

    ImageButton myads;

    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        tinyDB = new TinyDB(this);

        final RechargeFragment card = new RechargeFragment();
        final SettingFragment settingFragment = new SettingFragment();
        final AddPostFragment accountFragment = new AddPostFragment();
        final MainProfileFragment announcement = new MainProfileFragment();


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.account_container ,accountFragment);
        fragmentTransaction.commit();

        myads = findViewById(R.id.announcepage);
        myads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container ,accountFragment);
                fragmentTransaction.commit();
            }
        });

        myprofile = findViewById(R.id.myprofile);
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container ,announcement);
                fragmentTransaction.commit();
            }
        });

        credit = findViewById(R.id.cridet);
        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container ,card);
                fragmentTransaction.commit();
            }
        });

        setting = findViewById(R.id.settingaccount);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.account_container ,settingFragment);
                fragmentTransaction.commit();
            }
        });

        logeout = findViewById(R.id.el5orog);
        logeout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putString("isLoggedIn","False");

                Intent intent = new Intent(MyAccountActivity.this , Main2Activity.class);
                startActivity(intent);
            }
        });



    }
}
