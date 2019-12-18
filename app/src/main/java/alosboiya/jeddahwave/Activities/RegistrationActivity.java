package alosboiya.jeddahwave.Activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import alosboiya.jeddahwave.Fragments.AddCommentFragment;
import alosboiya.jeddahwave.Fragments.ConfirmFragment;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.AddButtonClick;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

public class RegistrationActivity extends AppCompatActivity {

    private static final String[] country ={"السعودية"};
    private static final String[] elmadena = {"الرياض" ,"مكة المكرمة" ,  "الدمام",
            "المدينة المنورة","جده","الأحساء","الطائف","بريدة","تبوك","القطيف","خميس مشيط","حائل","حفر الباطن","الجبيل","الخرج","أبها","نجران","ينبع","القنفذة","جازان","القصيم","عسير","الباحه","الظهران","الخبر","الدوادمى","الشرقية","الحدود الشمالية","الجوف","عنيزة"};
    Spinner countrey ,  elamdenaa;
    EditText regname , regemail ,regpass,regpass2 ,regphone;
    Button register;
    String GET_JSON_DATA_HTTP_URL;
    TinyDB tinyDB ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        tinyDB = new TinyDB(getApplicationContext());

        regname =findViewById(R.id.reg_name);
        regemail =findViewById(R.id.reg_email);
        regpass =findViewById(R.id.reg_pass);
        regpass2 =findViewById(R.id.reg_pass2);
        regphone =findViewById(R.id.reg_phone);

        countrySpiner();
        elmadenaSpiner();

        register = findViewById(R.id.registration_user);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(regphone.getText().toString().startsWith("05") && regphone.getText().toString().length()==10)
                {
                    //volleyConnection();
                    volleyConnection2("112267");
                }else
                    {
                        showMessage("الرقم خطأ");
                    }

            }
        });

    }


    public void volleyConnection()
    {
        GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/register?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("sent code")){

                            final FragmentManager fm = getFragmentManager();
                            ConfirmFragment confirmFragment = new ConfirmFragment();

                            confirmFragment.show(fm,"TV_tag");


                        }else {
                            showMessage(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //showMessage(error.toString());
                register.setEnabled(false);

                final FragmentManager fm = getFragmentManager();
                ConfirmFragment confirmFragment = new ConfirmFragment();

                confirmFragment.show(fm,"TV_tag");

            }
        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("name", regname.getText().toString());
                params.put("email", regemail.getText().toString());
                params.put("password", regpass.getText().toString());
                params.put("phone", regphone.getText().toString());
                params.put("countryy", countrey.getSelectedItem().toString());
                params.put("cityyyyyyyyyyyyyyyy", elamdenaa.getSelectedItem().toString());
                return params;
            }

        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    public void volleyConnection2(final String code)
    {
        if(code.equals("nosms"))
        {
            GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/register_nosendsms?";
        }else
            {
                GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/register_save?";
            }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            try {

                                if(code.equals("nosms"))
                                {
                                    showMessage("تم التسجيل و سيتم تنشيط الحساب قريباً");
                                }else
                                {
                                    showMessage("تم التسجيل بنجاح");

                                    tinyDB.putString("isLoggedIn","True");

                                    JSONArray js = new JSONArray(response);

                                    JSONObject userdate = js.getJSONObject(0);

                                    String user_id = String.valueOf(userdate.get("Id")) ;
                                    String user_name = (String) userdate.get("Name");
                                    String user_email = (String) userdate.get("Email");
                                    String user_pass = (String) userdate.get("Password");
                                    String user_phone= (String) userdate.get("Phone");
                                    String user_country = (String) userdate.get("Country");
                                    String user_city = (String) userdate.get("City");
                                    String user_url = (String) userdate.get("URL");
                                    String user_img = (String) userdate.get("ImageProfile");
                                    String user_balance = (String) userdate.get("Balance");

                                    tinyDB.putString("user_id",user_id);
                                    tinyDB.putString("user_name",user_name);
                                    tinyDB.putString("user_email",user_email);
                                    tinyDB.putString("user_pass",user_pass);
                                    tinyDB.putString("user_phone",user_phone);
                                    tinyDB.putString("user_country",user_country);
                                    tinyDB.putString("user_city",user_city);
                                    tinyDB.putString("user_url",user_url);
                                    tinyDB.putString("user_img","images/imgposting.png");
                                    tinyDB.putString("user_balance",user_balance);
                                }

                                onBackPressed();

                            } catch (JSONException e) {
                                e.printStackTrace();
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
                params.put("name", regname.getText().toString());
                params.put("cityyyyyyyyyyyyyyyy", elamdenaa.getSelectedItem().toString());
                params.put("countryy", countrey.getSelectedItem().toString());
                params.put("email", regemail.getText().toString());
                params.put("password", regpass.getText().toString());
                params.put("phone", regphone.getText().toString());
                params.put("code",code);
                return params;
            }

        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onButtonClick(AddButtonClick addButtonClick)
    {

        String name = addButtonClick.getEvent();

        volleyConnection2(name);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



    private void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_LONG).show();
    }


    public void countrySpiner()
    {
        countrey = findViewById(R.id.country);
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, country);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrey.setAdapter(countryAdapter);
    }
    public void elmadenaSpiner()
    {
        elamdenaa = findViewById(R.id.elmadenaa);
        ArrayAdapter<String> elmadenaAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, elmadena);
        elmadenaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elamdenaa.setAdapter(elmadenaAdapter);
    }

}
