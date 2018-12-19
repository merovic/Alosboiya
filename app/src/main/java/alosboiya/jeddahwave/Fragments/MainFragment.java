package alosboiya.jeddahwave.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import alosboiya.jeddahwave.Activities.HaragActivity;
import alosboiya.jeddahwave.Activities.MembersActivity;
import alosboiya.jeddahwave.Activities.GaredaActivity;
import alosboiya.jeddahwave.R;

public class MainFragment extends Fragment {


    public static final String TAG = "ass";

    ImageButton membersbutton,haragebutton,garedabutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        membersbutton = getActivity().findViewById(R.id.membersbutton);
        haragebutton = getActivity().findViewById(R.id.haragebutton);
        garedabutton = getActivity().findViewById(R.id.garedabutton);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            // 6.5inch device or bigger

        }else{
            // smaller device
        }

        haragebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HaragActivity.class);
                getActivity().startActivity(intent);
            }
        });

        membersbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MembersActivity.class);
                getActivity().startActivity(intent);
            }
        });

        garedabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GaredaActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }
}
