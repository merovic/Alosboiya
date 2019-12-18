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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

public class EditPostFragment extends DialogFragment {

    ImageView quit;
    EditText text1,text2,text3;
    Button submit;

    TinyDB tinyDB;

    String id,phone,title,content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_edit_post,container);

        tinyDB = new TinyDB(getActivity());

        id = tinyDB.getString("postID");
        phone = tinyDB.getString("postPhone");
        title = tinyDB.getString("postTitle");
        content = tinyDB.getString("postDescription");

        this.getDialog().setTitle("تعديل الأعلان");

        quit = rootView.findViewById(R.id.quit);
        text1 = rootView.findViewById(R.id.text1);
        text2 = rootView.findViewById(R.id.text2);
        text3 = rootView.findViewById(R.id.text3);
        submit = rootView.findViewById(R.id.submit);

        text1.setText(phone);
        text2.setText(title);
        text3.setText(content);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editComment();
            }
        });


        return rootView;


    }


    private void editComment()
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/edite_post?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("True"))
                        {
                            showMessage("تم تحديث الأعلان");
                            dismiss();
                        }else
                            {
                                showMessage("خطأ");
                            }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showMessage(error.toString());

            }

        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id_post", id);
                params.put("phone", text1.getText().toString());
                params.put("address", text2.getText().toString());
                params.put("des", text3.getText().toString());
                return params;
            }



        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void showMessage(String _s) {
        Toast.makeText(getActivity(), _s, Toast.LENGTH_LONG).show();
    }
}
