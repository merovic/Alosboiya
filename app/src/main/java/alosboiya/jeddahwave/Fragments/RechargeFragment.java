package alosboiya.jeddahwave.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

public class RechargeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "ass4";

    TextView username,userbalance;
    ImageView userimage;
    EditText n1,n2,n3;
    Spinner rechargespinner;
    Button rechargebutton;

    TinyDB tinyDB;

    String user_id,catfinal;

    List<String> cards;

    public RechargeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recharge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tinyDB = new TinyDB(getContext());

        username = getActivity().findViewById(R.id.username);
        userbalance = getActivity().findViewById(R.id.userbalance);
        userimage = getActivity().findViewById(R.id.userimage);
        n1 = getActivity().findViewById(R.id.n1);
        n2 = getActivity().findViewById(R.id.n2);
        n3 = getActivity().findViewById(R.id.n3);
        rechargespinner = getActivity().findViewById(R.id.rechargespinner);
        rechargebutton = getActivity().findViewById(R.id.rechargebutton);

        setupSpinner();

        username.setText(tinyDB.getString("user_name"));
        userbalance.setText(tinyDB.getString("user_balance"));

        user_id = tinyDB.getString("user_id");

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

        rechargebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rechargespinner.getSelectedItemPosition()==0)
                {
                    showMessage("اختر فئة الكارت اولا");

                }else
                    {
                        if(rechargespinner.getSelectedItemPosition()==1)
                        {
                            catfinal = "20";

                        }else if(rechargespinner.getSelectedItemPosition()==2)
                        {
                            catfinal = "50";

                        }else if(rechargespinner.getSelectedItemPosition()==3)
                        {
                            catfinal = "100";
                        }

                        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/add_balance?";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        if(response.contains("تم"))
                                        {

                                            updateBalance();

                                        }else
                                        {
                                            showMessage("خطأ فى الشحن");
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                showMessage("خطأ فى الشبكة");

                            }
                        }) {

                            @Override
                            protected Map<String,String> getParams(){
                                Map<String,String> params = new HashMap<>();
                                params.put("id_member", user_id);
                                params.put("cat",catfinal);
                                params.put("id_card",n1.getText().toString());
                                params.put("n2",n2.getText().toString());
                                params.put("n3",n3.getText().toString());
                                return params;
                            }



                        };

                        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

                    }


            }
        });

    }


    private void updateBalance()
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/balance_in_app?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (Objects.equals(response, "True"))
                        {

                            String finalBalance = String.valueOf(Integer.parseInt(catfinal) + Integer.parseInt(userbalance.getText().toString()));

                            userbalance.setText(finalBalance);

                            tinyDB.putString("user_balance",finalBalance);

                            showMessage("تم شحن الكارت بنجاح");

                        }
                        else
                        {
                            showMessage("خطأ فى تحديث بيانات الرصيد");

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
                params.put("id_member", user_id);
                params.put("amount", catfinal);

                return params;
            }



        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private void showMessage(String _s) {
        Toast.makeText(getContext(), _s, Toast.LENGTH_LONG).show();
    }


    public void setupSpinner()
    {
        cards = new ArrayList<>();

        cards.add("اختر فئة الكارت");
        cards.add("٢٠ ريال");
        cards.add("٥٠ ريال");
        cards.add("١٠٠ ريال");

        rechargespinner = getActivity().findViewById(R.id.rechargespinner);
        ArrayAdapter<String> elmadenaAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, cards);
        elmadenaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rechargespinner.setAdapter(elmadenaAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(rechargespinner.getSelectedItemPosition()==1)
                {

                    catfinal = "20";

                }else if(rechargespinner.getSelectedItemPosition()==2)
                {
                    catfinal = "50";

                }else if(rechargespinner.getSelectedItemPosition()==3)
                {
                    catfinal = "100";

                }

                showMessage(catfinal);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
