package alosboiya.jeddahwave.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import alosboiya.jeddahwave.Adapters.IteamListAdapter;
import alosboiya.jeddahwave.Adapters.SalesAdapter;
import alosboiya.jeddahwave.Models.ItemList;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.AddButtonClick;
import alosboiya.jeddahwave.Utils.TinyDB;

public class HaragActivity extends Activity implements AdapterView.OnItemSelectedListener {

    RecyclerView rvitems,sallesrv;

    IteamListAdapter rvadapter;
    ArrayList<ItemList> itemLists;

    SalesAdapter salleslistAdapter;
    ArrayList<SalesItems> salesitems = new ArrayList<>();

    private SwipeRefreshLayout swipeContainer;

    ImageButton adde;

    RequestQueue requestQueue;

    ProgressBar progressBar;

    Spinner cityesSpinner,countriesSpinner;

    ArrayAdapter<String> adapter,adapter2;

    List<String> cities = new ArrayList<>();
    List<String> countries = new ArrayList<>();

    TinyDB tinyDB;

    String selectedDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harag);

        selectedDepartment = "";

        tinyDB = new TinyDB(getApplicationContext());

        progressBar = findViewById(R.id.progressBar1);

        rvitems = findViewById(R.id.items_types);
        rvitems.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL,false);
        rvitems.setLayoutManager(llm);

        itemLists = new ArrayList<>();
        itemLists.add(new ItemList(R.drawable.akarat));
        itemLists.add(new ItemList(R.drawable.cars));
        itemLists.add(new ItemList(R.drawable.jobsicon));
        itemLists.add(new ItemList(R.drawable.devices));
        itemLists.add(new ItemList(R.drawable.athath));
        itemLists.add(new ItemList(R.drawable.service));
        itemLists.add(new ItemList(R.drawable.animals));
        itemLists.add(new ItemList(R.drawable.family));
        itemLists.add(new ItemList(R.drawable.monsf));

        rvadapter = new IteamListAdapter(itemLists);
        rvitems.setAdapter(rvadapter);

        sallesrv = findViewById(R.id.sales_list);

        sallesrv.setHasFixedSize(false);

        sallesrv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL,false));

        salleslistAdapter = new SalesAdapter(salesitems);

        //JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj?");


        //sallesrv.setAdapter(salleslistAdapter);

        adde = findViewById(R.id.addee);

        adde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("isLoggedIn").equals("True"))
                {
                    Intent intent = new Intent(HaragActivity.this, AddPostActivity.class);
                    startActivity(intent);
                }else
                    {
                        showMessage("سجل الدخول اولا");
                    }
            }
        });




        cityesSpinner = findViewById(R.id.cities);
        countriesSpinner = findViewById(R.id.countries);


        countries.add("السعودية");
        cities.add("كل المدن");
        cities.add("الرياض");
        cities.add("مكة المكرمة");
        cities.add("الدمام");
        cities.add("جده");
        cities.add("المدينة المنورة");
        cities.add("الأحساء");
        cities.add("الطائف");
        cities.add("بريدة");
        cities.add("تبوك");
        cities.add("القطيف");
        cities.add("خميس مشيط");
        cities.add("حائل");
        cities.add("حفر الباطن");
        cities.add("الجبيل");
        cities.add("الخرج");
        cities.add("أبها");
        cities.add("نجران");
        cities.add("ينبع");
        cities.add("القنفذة");
        cities.add("جازان");
        cities.add("القصيم");
        cities.add("عسير");
        cities.add("الباحه");
        cities.add("الظهران");
        cities.add("الخبر");
        cities.add("الدوادمى");
        cities.add("الشرقية");
        cities.add("الحدود الشمالية");
        cities.add("الجوف");
        cities.add("عنيزة");


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityesSpinner.setAdapter(adapter);
        cityesSpinner.setOnItemSelectedListener(this);

        adapter.notifyDataSetChanged();

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countriesSpinner.setAdapter(adapter2);
        countriesSpinner.setOnItemSelectedListener(this);

        adapter2.notifyDataSetChanged();

        swiptorefresch();
    }

    public void swiptorefresch()
    {
        // Lookup the swipe container view
        swipeContainer = findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                salesitems.clear();

                JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj?");

                salleslistAdapter.notifyDataSetChanged();

                swipeContainer.setRefreshing(false);

            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onButtonClick(AddButtonClick addButtonClick)
    {
        String event = addButtonClick.getEvent();

        salesitems.clear();
        salleslistAdapter.notifyDataSetChanged();

        if(cityesSpinner.getSelectedItemPosition()==0)
        {

            switch (event)
            {

                case "0":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=عقارات");
                    selectedDepartment = "عقارات";
                    break;

                case "1":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=سيارات");
                    selectedDepartment = "سيارات";
                    break;

                case "2":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=وظائف");
                    selectedDepartment = "وظائف";
                    break;


                case "3":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=الاجهزه");
                    selectedDepartment = "الاجهزه";
                    break;


                case "4":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=اثاث");
                    selectedDepartment = "اثاث";
                    break;


                case "5":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=الخدمات");
                    selectedDepartment = "الخدمات";
                    break;


                case "6":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=مواشى وحيوانات وطيور");
                    selectedDepartment = "مواشى وحيوانات وطيور";
                    break;


                case "7":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=الاسرة المنتجة");
                    selectedDepartment = "الاسرة المنتجة";
                    break;


                case "8":
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department=قسم غير مصنف");
                    selectedDepartment = "قسم غير مصنف";
                    break;

            }

        }else
            {

                switch (event)
                {

                    case "0":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"عقارات");
                        selectedDepartment = "عقارات";
                        break;

                    case "1":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"سيارات");
                        selectedDepartment = "سيارات";
                        break;

                    case "2":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"وظائف");
                        selectedDepartment = "وظائف";
                        break;


                    case "3":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"الاجهزه");
                        selectedDepartment = "الاجهزه";
                        break;


                    case "4":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"اثاث");
                        selectedDepartment = "اثاث";
                        break;


                    case "5":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"الخدمات");
                        selectedDepartment = "الخدمات";
                        break;


                    case "6":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"مواشى وحيوانات وطيور");
                        selectedDepartment = "مواشى وحيوانات وطيور";
                        break;


                    case "7":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"الاسرة المنتجة");
                        selectedDepartment = "الاسرة المنتجة";
                        break;

                    case "8":
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cityesSpinner.getSelectedItem().toString()+"&department="+"قسم غير مصنف");
                        selectedDepartment = "قسم غير مصنف";
                        break;
                }

            }


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



private void JSON_DATA_WEB_CALL(String URL){

    progressBar.setVisibility(View.VISIBLE);

    StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,

            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSON_PARSE_DATA_AFTER_WEBCALL(response);

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    showMessage("خطأ فى الشبكة");


                }
            }
    );


    requestQueue = Volley.newRequestQueue(this);

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

                oursales.setIdMember(childJSONObject.getString("IdMember"));

                oursales.setLocation(childJSONObject.getString("City"));

                oursales.setSalesdate(childJSONObject.getString("datee_c"));

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

            //salleslistAdapter = new SalesAdapter(salesitems);
            sallesrv.setAdapter(salleslistAdapter);

            salleslistAdapter.notifyDataSetChanged();


            progressBar.setVisibility(View.GONE);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        salesitems.clear();


        if(position==0)
        {
            if(selectedDepartment.isEmpty())
            {
                JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj?");
            }else
                {
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_Department?Department="+selectedDepartment);
                }

        }else
            {
                if(selectedDepartment.isEmpty())
                {
                    JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city?city="+ cities.get(position));
                }else
                    {
                        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/wsnew.asmx/select_haraj_by_search_city_and_department?city="+ cities.get(position)+"&department="+selectedDepartment);
                    }

            }

        salleslistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
