package alosboiya.jeddahwave.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class MyAccountFragment extends Fragment {

    public static final String TAG = "ass9";

    TextView username,userbalance,usernameedit,userphoneedit,useremailedit,usercityedit,usercountyedit;

    ImageView userimage;

    TinyDB tinyDB;

    public MyAccountFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tinyDB = new TinyDB(getContext());

        username = getActivity().findViewById(R.id.username);
        userbalance = getActivity().findViewById(R.id.userbalance);
        usernameedit = getActivity().findViewById(R.id.usernameedit);
        userphoneedit = getActivity().findViewById(R.id.userphoneedit);
        useremailedit = getActivity().findViewById(R.id.useremailedit);
        usercityedit = getActivity().findViewById(R.id.usercityedit);
        usercountyedit = getActivity().findViewById(R.id.usercountryedit);

        userimage = getActivity().findViewById(R.id.userimage);


        usernameedit.setText(tinyDB.getString("user_name"));
        userphoneedit.setText(tinyDB.getString("user_phone"));
        useremailedit.setText(tinyDB.getString("user_email"));
        usercityedit.setText(tinyDB.getString("user_city"));
        usercountyedit.setText(tinyDB.getString("user_country"));

        userbalance.setText(tinyDB.getString("user_balance"));
        username.setText(tinyDB.getString("user_name"));


        if(tinyDB.getString("user_img").equals("images/imgposting.png") || tinyDB.getString("user_img").equals(""))
        {
            Glide.with(this).load(R.drawable.user).into(userimage);

        }else if (tinyDB.getString("user_img").contains("~")) {
            String replaced = tinyDB.getString("user_img").replace("~", "");
            String finalstring = "http://alosboiya.com.sa" + replaced;
            Glide.with(this).load(finalstring).into(userimage);

        }else
        {
            Glide.with(this).load(tinyDB.getString("user_img")).into(userimage);
        }

    }
}
