package alosboiya.jeddahwave.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.AddButtonClick;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

public class ReplyMessageFragment extends DialogFragment {

    ImageView quit;
    EditText text;
    Button submit;
    TextView title;

    TinyDB tinyDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_add_comment,container);

        this.getDialog().setTitle("ارسال رسالة");

        tinyDB = new TinyDB(getActivity());

        quit = rootView.findViewById(R.id.quit);
        text = rootView.findViewById(R.id.text);
        submit = rootView.findViewById(R.id.submit);
        title = rootView.findViewById(R.id.title);

        text.setHint("اكتب الرسالة");
        submit.setText("ارسل");
        title.setText("ارسال رسالة");

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSON_DATA_WEB_CALL3("http://alosboiya.com.sa/wsnew.asmx/insert_message?",text.getText().toString().trim());

                dismiss();
            }
        });


        return rootView;


    }



    public void JSON_DATA_WEB_CALL3(String URL, final String content){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // showMessage(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id_user_send", tinyDB.getString("user_id"));
                params.put("sender", tinyDB.getString("user_name"));
                params.put("id_user_recive", tinyDB.getString("messagesenderID"));
                params.put("id_post", tinyDB.getString("messagePostID"));
                params.put("messagedetails", content);
                params.put("img", tinyDB.getString("user_img"));
                return params;
            }

        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
