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
import alosboiya.jeddahwave.Models.NotificationMessageItem;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class MessagesFragment extends Fragment {

    public static final String TAG = "ass13";

    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView title;

    NotificationMessageAdapter notificationMessageAdapter;
    ArrayList<NotificationMessageItem> notificationMessageItems = new ArrayList<>();

    RequestQueue requestQueue;

    TinyDB tinyDB;

    public MessagesFragment() {}

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
        title.setText("الرسائل");

        JSON_DATA_WEB_CALL();
    }


    private void JSON_DATA_WEB_CALL(){

        progressBar.setVisibility(View.VISIBLE);

        String url;

        url = "http://alosboiya.com.sa/wsnew.asmx/select_message_recevice?id_user_recive="+tinyDB.getString("user_id");

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

                notificationMessageItem.setBody(childJSONObject.getString("messagedetails"));

                notificationMessageItem.setDate(childJSONObject.getString("datee"));

                notificationMessageItem.setImg(childJSONObject.getString("img"));

                notificationMessageItem.setSenderID(childJSONObject.getString("id_user_send"));

                notificationMessageItem.setPostID(childJSONObject.getString("id_post"));

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
