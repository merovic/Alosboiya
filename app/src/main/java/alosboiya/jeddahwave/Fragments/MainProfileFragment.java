package alosboiya.jeddahwave.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import alosboiya.jeddahwave.Adapters.ProfileCommentsAdapter;
import alosboiya.jeddahwave.Adapters.ProfilePostsAdapter;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.Models.SallesCommentItems;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;
import info.hoang8f.android.segmented.SegmentedGroup;

public class MainProfileFragment extends Fragment {

    public static final String TAG = "ass10";

    TextView username,userbalance,usernameedit,userphoneedit,useremailedit,usercityedit,usercountyedit;

    ImageView userimage;

    TinyDB tinyDB;

    LinearLayout notabslayout;

    RequestQueue requestQueue;

    RecyclerView commentsrecycler,postsrecycler;

    ProfileCommentsAdapter adapter1;

    ProfilePostsAdapter adapter2;

    List<SallesCommentItems> commentItems = new ArrayList<>();

    List<SalesItems> postItems = new ArrayList<>();

    String id;

    SegmentedGroup tabsgroup;

    RadioButton comments,expire,all;

    public MainProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tinyDB = new TinyDB(getContext());

        id = tinyDB.getString("user_id");

        notabslayout = Objects.requireNonNull(getActivity()).findViewById(R.id.notabslayout);

        noTaps();

        tabsgroup = getActivity().findViewById(R.id.segmented2);

        comments = getActivity().findViewById(R.id.button1);
        expire = getActivity().findViewById(R.id.button2);
        all = getActivity().findViewById(R.id.button3);

        comments.setChecked(false);
        expire.setChecked(false);
        all.setChecked(false);

        commentsrecycler = getActivity().findViewById(R.id.commentsrecycler);

        commentsrecycler.setHasFixedSize(false);

        commentsrecycler.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL,false));

        JSON_DATA_WEB_CALL();

        postsrecycler = getActivity().findViewById(R.id.postsrecycler);

        postsrecycler.setHasFixedSize(false);

        postsrecycler.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL,false));



        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notabslayout.setVisibility(View.GONE);
                commentsrecycler.setVisibility(View.VISIBLE);
                postsrecycler.setVisibility(View.GONE);


            }
        });

        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postItems.clear();

                JSON_DATA_WEB_CALL2("select_expire_post_for_member");

                notabslayout.setVisibility(View.GONE);
                commentsrecycler.setVisibility(View.GONE);
                postsrecycler.setVisibility(View.VISIBLE);


            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postItems.clear();

                JSON_DATA_WEB_CALL2("select_post_for_member");

                notabslayout.setVisibility(View.GONE);
                commentsrecycler.setVisibility(View.GONE);
                postsrecycler.setVisibility(View.VISIBLE);

            }
        });



    }

    private void noTaps() {

        username = Objects.requireNonNull(getActivity()).findViewById(R.id.username);
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


        if(tinyDB.getString("user_img").equals("images/imgposting.png"))
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

        notabslayout.setVisibility(View.VISIBLE);

    }


    private void JSON_DATA_WEB_CALL(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://alosboiya.com.sa/wsnew.asmx/select_comment_profile?id_member="+id,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showMessage("No Connection");


                    }
                }
        );


        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        requestQueue.add(stringRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(String Jobj) {


        try {

            JSONArray js = new JSONArray(Jobj);

            for (int i = 0; i < js.length(); i++) {

                JSONObject childJSONObject = js.getJSONObject(i);

                SallesCommentItems oursales = new SallesCommentItems();


                oursales.setID(childJSONObject.getString("Id"));

                oursales.setScommentuser(childJSONObject.getString("name"));

                oursales.setScommentdate(childJSONObject.getString("datee"));

                oursales.setScommenttext(childJSONObject.getString("Comment"));


                commentItems.add(oursales);

            }

            adapter1 = new ProfileCommentsAdapter(commentItems);
            commentsrecycler.setAdapter(adapter1);

            adapter1.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void JSON_DATA_WEB_CALL2(String part){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://alosboiya.com.sa/wsnew.asmx/"+part+"?id_member="+id,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL2(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showMessage("No Connection");


                    }
                }
        );


        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        requestQueue.add(stringRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL2(String Jobj) {


        try {

            JSONArray js = new JSONArray(Jobj);

            for (int i = 0; i < js.length(); i++) {

                JSONObject childJSONObject = js.getJSONObject(i);

                SalesItems oursales = new SalesItems();

                if (childJSONObject.getString("Image") != null) {

                    if (childJSONObject.getString("Image").contains("~")) {
                        String replaced = childJSONObject.getString("Image").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setSellseimage(finalstring);

                    } else {
                        oursales.setSellseimage(childJSONObject.getString("Image"));
                    }

                } else {
                    oursales.setSellseimage("images/imgposting.png");

                }

                oursales.setID(childJSONObject.getString("Id"));

                oursales.setIdMember(childJSONObject.getString("IdMember"));

                oursales.setLocation(childJSONObject.getString("City"));

                oursales.setDate(childJSONObject.getString("datee_c"));

                oursales.setSalesname(childJSONObject.getString("Title"));

                oursales.setSallername(childJSONObject.getString("NameMember"));

                oursales.setDescription(childJSONObject.getString("Des"));

                oursales.setDepartment(childJSONObject.getString("Department"));

                oursales.setUrl(childJSONObject.getString("URL"));

                oursales.setPhone(childJSONObject.getString("Phone"));

                oursales.setEmail(childJSONObject.getString("Email"));

                oursales.setSubdepartment(childJSONObject.getString("SubDep"));

                if (childJSONObject.getString("Image_2") != null) {

                    if (childJSONObject.getString("Image_2").contains("~")) {
                        String replaced = childJSONObject.getString("Image_2").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage2(finalstring);

                    } else {
                        oursales.setImage2(childJSONObject.getString("Image_2"));
                    }

                } else {
                    oursales.setImage2("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_3") != null) {

                    if (childJSONObject.getString("Image_3").contains("~")) {
                        String replaced = childJSONObject.getString("Image_3").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage3(finalstring);

                    } else {
                        oursales.setImage3(childJSONObject.getString("Image_3"));
                    }

                } else {
                    oursales.setImage3("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_4") != null) {

                    if (childJSONObject.getString("Image_4").contains("~")) {
                        String replaced = childJSONObject.getString("Image_4").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage4(finalstring);

                    } else {
                        oursales.setImage4(childJSONObject.getString("Image_4"));
                    }

                } else {
                    oursales.setImage4("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_5") != null) {

                    if (childJSONObject.getString("Image_5").contains("~")) {
                        String replaced = childJSONObject.getString("Image_5").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage5(finalstring);

                    } else {
                        oursales.setImage5(childJSONObject.getString("Image_5"));
                    }

                } else {
                    oursales.setImage5("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_6") != null) {

                    if (childJSONObject.getString("Image_6").contains("~")) {
                        String replaced = childJSONObject.getString("Image_6").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage6(finalstring);

                    } else {
                        oursales.setImage6(childJSONObject.getString("Image_6"));
                    }

                } else {
                    oursales.setImage6("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_7") != null) {

                    if (childJSONObject.getString("Image_7").contains("~")) {
                        String replaced = childJSONObject.getString("Image_7").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage7(finalstring);

                    } else {
                        oursales.setImage7(childJSONObject.getString("Image_7"));
                    }

                } else {
                    oursales.setImage7("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_8") != null) {

                    if (childJSONObject.getString("Image_8").contains("~")) {
                        String replaced = childJSONObject.getString("Image_8").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        oursales.setImage8(finalstring);

                    } else {
                        oursales.setImage8(childJSONObject.getString("Image_8"));
                    }

                } else {
                    oursales.setImage8("images/imgposting.png");

                }


                postItems.add(oursales);

            }

            adapter2 = new ProfilePostsAdapter(postItems);
            postsrecycler.setAdapter(adapter2);

            adapter2.notifyDataSetChanged();



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

}
