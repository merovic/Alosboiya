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
import android.widget.TextView;
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

import alosboiya.jeddahwave.Adapters.NotificationMessageAdapter;
import alosboiya.jeddahwave.Adapters.SalesAdapter;
import alosboiya.jeddahwave.Models.NotificationMessageItem;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class NotificationsFragment extends Fragment {

    public static final String TAG = "ass12";

    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView title;

    NotificationMessageAdapter notificationMessageAdapter;
    ArrayList<NotificationMessageItem> notificationMessageItems = new ArrayList<>();

    RequestQueue requestQueue;

    TinyDB tinyDB;

    public NotificationsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notificationmessage, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tinyDB = new TinyDB(getActivity());

        progressBar = getActivity().findViewById(R.id.progressBar1);
        recyclerView = getActivity().findViewById(R.id.rv);

        progressBar.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL,false));
        notificationMessageAdapter = new NotificationMessageAdapter(notificationMessageItems);

        title = getActivity().findViewById(R.id.title);
        title.setText("الأشعارات");

        JSON_DATA_WEB_CALL();
    }


    private void JSON_DATA_WEB_CALL(){

        progressBar.setVisibility(View.VISIBLE);

        String url;

        url = "http://alosboiya.com.sa/wsnew.asmx/select_note?id_user_recive="+tinyDB.getString("user_id");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                       // showMessage(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showMessage("خطأ فى الشبكة");


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

                NotificationMessageItem notificationMessageItem = new NotificationMessageItem();

                notificationMessageItem.setHeader(childJSONObject.getString("sender"));

                notificationMessageItem.setBody(childJSONObject.getString("notetitle"));

                notificationMessageItem.setDate(childJSONObject.getString("datee"));

                notificationMessageItem.setImg(childJSONObject.getString("img"));


                if (childJSONObject.getString("Image") != null) {

                    if (childJSONObject.getString("Image").contains("~")) {
                        String replaced = childJSONObject.getString("Image").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setSellseimage(finalstring);

                    } else {
                        notificationMessageItem.setSellseimage(childJSONObject.getString("Image"));
                    }

                } else {
                    notificationMessageItem.setSellseimage("images/imgposting.png");

                }


                notificationMessageItem.setID(childJSONObject.getString("id_post"));

                notificationMessageItem.setIdMember(childJSONObject.getString("IdMember"));

                notificationMessageItem.setLocation(childJSONObject.getString("City"));

                notificationMessageItem.setSalesdate(childJSONObject.getString("datee_c"));

                notificationMessageItem.setSalesname(childJSONObject.getString("Title"));

                notificationMessageItem.setSallername(childJSONObject.getString("NameMember"));

                notificationMessageItem.setDescription(childJSONObject.getString("Des"));

                notificationMessageItem.setDepartment(childJSONObject.getString("Department"));

                notificationMessageItem.setUrl(childJSONObject.getString("URL"));

                notificationMessageItem.setPhone(childJSONObject.getString("Phone"));

                notificationMessageItem.setEmail(childJSONObject.getString("Email"));

                notificationMessageItem.setSubdepartment(childJSONObject.getString("SubDep"));

                if (childJSONObject.getString("Image_2") != null) {

                    if (childJSONObject.getString("Image_2").contains("~")) {
                        String replaced = childJSONObject.getString("Image_2").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage2(finalstring);

                    } else {
                        notificationMessageItem.setImage2(childJSONObject.getString("Image_2"));
                    }

                } else {
                    notificationMessageItem.setImage2("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_3") != null) {

                    if (childJSONObject.getString("Image_3").contains("~")) {
                        String replaced = childJSONObject.getString("Image_3").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage3(finalstring);

                    } else {
                        notificationMessageItem.setImage3(childJSONObject.getString("Image_3"));
                    }

                } else {
                    notificationMessageItem.setImage3("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_4") != null) {

                    if (childJSONObject.getString("Image_4").contains("~")) {
                        String replaced = childJSONObject.getString("Image_4").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage4(finalstring);

                    } else {
                        notificationMessageItem.setImage4(childJSONObject.getString("Image_4"));
                    }

                } else {
                    notificationMessageItem.setImage4("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_5") != null) {

                    if (childJSONObject.getString("Image_5").contains("~")) {
                        String replaced = childJSONObject.getString("Image_5").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage5(finalstring);

                    } else {
                        notificationMessageItem.setImage5(childJSONObject.getString("Image_5"));
                    }

                } else {
                    notificationMessageItem.setImage5("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_6") != null) {

                    if (childJSONObject.getString("Image_6").contains("~")) {
                        String replaced = childJSONObject.getString("Image_6").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage6(finalstring);

                    } else {
                        notificationMessageItem.setImage6(childJSONObject.getString("Image_6"));
                    }

                } else {
                    notificationMessageItem.setImage6("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_7") != null) {

                    if (childJSONObject.getString("Image_7").contains("~")) {
                        String replaced = childJSONObject.getString("Image_7").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage7(finalstring);

                    } else {
                        notificationMessageItem.setImage7(childJSONObject.getString("Image_7"));
                    }

                } else {
                    notificationMessageItem.setImage7("images/imgposting.png");

                }

                if (childJSONObject.getString("Image_8") != null) {

                    if (childJSONObject.getString("Image_8").contains("~")) {
                        String replaced = childJSONObject.getString("Image_8").replace("~", "");
                        String finalstring = "http://alosboiya.com.sa" + replaced;
                        notificationMessageItem.setImage8(finalstring);

                    } else {
                        notificationMessageItem.setImage8(childJSONObject.getString("Image_8"));
                    }

                } else {
                    notificationMessageItem.setImage8("images/imgposting.png");

                }




                notificationMessageItems.add(notificationMessageItem);

            }


            recyclerView.setAdapter(notificationMessageAdapter);

            notificationMessageAdapter.notifyDataSetChanged();


            progressBar.setVisibility(View.GONE);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }


}
