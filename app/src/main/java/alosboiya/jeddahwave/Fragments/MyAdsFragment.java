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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alosboiya.jeddahwave.Adapters.SalesAdapter;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class MyAdsFragment extends Fragment {

    public static final String TAG = "ass9";

    ProgressBar progressBar;
    RecyclerView recyclerView;

    SalesAdapter salleslistAdapter;
    ArrayList<SalesItems> salesitems = new ArrayList<>();

    RequestQueue requestQueue;

    TinyDB tinyDB;

    String id;

    public MyAdsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my_ads, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tinyDB = new TinyDB(getContext());

        id = tinyDB.getString("user_id");

        progressBar = getActivity().findViewById(R.id.progressBar1);
        recyclerView = getActivity().findViewById(R.id.rv);

        progressBar.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL,false));
        salleslistAdapter = new SalesAdapter(salesitems);

        JSON_DATA_WEB_CALL();

    }



    private void JSON_DATA_WEB_CALL(){

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://alosboiya.com.sa/webs.asmx/select_post_by_id?id_member="+id,

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


        requestQueue = Volley.newRequestQueue(getContext());

        requestQueue.add(stringRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(String Jobj) {


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

                oursales.setLocation(childJSONObject.getString("City"));

                oursales.setSalesdate(childJSONObject.getString("datee"));

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


                salesitems.add(oursales);

            }

            salleslistAdapter = new SalesAdapter(salesitems);
            recyclerView.setAdapter(salleslistAdapter);

            salleslistAdapter.notifyDataSetChanged();


            progressBar.setVisibility(View.GONE);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }


}

