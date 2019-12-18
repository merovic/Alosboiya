package alosboiya.jeddahwave.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import alosboiya.jeddahwave.R;

public class GaredaIndexSelectionActivity extends AppCompatActivity {

    ImageButton gareda,index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garedaindexselection);

        gareda = findViewById(R.id.garedabutton);
        index = findViewById(R.id.indexbutton);

        gareda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(GaredaIndexSelectionActivity.this , GaredaActivity.class);
                    startActivity(intent);

            }
        });
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(GaredaIndexSelectionActivity.this , IndexActivity.class);
                    startActivity(intent);

            }
        });
    }
}
