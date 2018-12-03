package alosboiya.jeddahwave.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class MembersActivity extends AppCompatActivity {

    ImageButton a3daa, regest;

    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        tinyDB = new TinyDB(this);

        a3daa = findViewById(R.id.do5olAla3daa);
        regest = findViewById(R.id.tasgelGeded);

        a3daa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("isLoggedIn").equals("True"))
                {
                    Intent intent = new Intent(MembersActivity.this , MyAccountActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(MembersActivity.this , LoginActivity.class);
                    startActivity(intent);
                }


            }
        });
        regest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("isLoggedIn").equals("True"))
                {
                    Intent intent = new Intent(MembersActivity.this , MyAccountActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(MembersActivity.this , RegistrationActivity.class);
                    startActivity(intent);
                }


            }
        });
    }
}
