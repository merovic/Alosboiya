package alosboiya.jeddahwave.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import alosboiya.jeddahwave.Activities.LoginActivity;
import alosboiya.jeddahwave.Activities.MyAccountActivity;
import alosboiya.jeddahwave.Activities.RegistrationActivity;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class MembersFragment extends Fragment {

    public static final String TAG = "ass2";

    ImageButton a3daa, regest;

    TinyDB tinyDB;

    Toolbar toolbar;

    public MembersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_members, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        tinyDB = new TinyDB(getContext());

        a3daa = getActivity().findViewById(R.id.do5olAla3daa);
        regest = getActivity().findViewById(R.id.tasgelGeded);

        a3daa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("isLoggedIn").equals("True"))
                {
                    Intent intent = new Intent(getActivity() , MyAccountActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(getActivity() , LoginActivity.class);
                    startActivity(intent);
                }


            }
        });
        regest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("isLoggedIn").equals("True"))
                {
                    Intent intent = new Intent(getActivity() , MyAccountActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(getActivity() , RegistrationActivity.class);
                    startActivity(intent);
                }


            }
        });

    }
}
