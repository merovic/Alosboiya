package alosboiya.jeddahwave.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alosboiya.jeddahwave.Adapters.SalesCommentAdapter;
import alosboiya.jeddahwave.Adapters.SugesstionAdapter;
import alosboiya.jeddahwave.Fragments.AddCommentFragment;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.Models.SallesCommentItems;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.AddButtonClick;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

public class HarageDetailsActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    TextView dtitle, ddate, dsname, dlocation, ddescripe ,callnum;
    ImageView share;
    LinearLayout callcustomer;

    RecyclerView commentsRv,suggestRv;

    SalesCommentAdapter salesitemcommentsAdapter;
    SugesstionAdapter sugRvAdapter;

    ArrayList<SallesCommentItems> commentsItems;
    ArrayList<SalesItems> suggestionItems;

    RequestQueue requestQueue;

    SliderLayout imgslider;

    TinyDB tinyDB;

    String title,datee,description,city,saller_name,saller_phone,post_url,img1,img2,img3,img4,img5,img6,img7,img8,post_id,department;

    Button comment;

    List<String> pics;

    ImageButton home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harage_details);

        tinyDB = new TinyDB(this);

        home = findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HarageDetailsActivity.this , Main2Activity.class);
                startActivity(intent);
                finish();

            }
        });

        dtitle = findViewById(R.id.d_sales_title);
        ddate = findViewById(R.id.d_sales_date);
        dsname = findViewById(R.id.d_saller_name);
        dlocation =findViewById(R.id.d_sales_location);
        ddescripe = findViewById(R.id.d_sales_disc);
        callnum = findViewById(R.id.call_number);
        share = findViewById(R.id.share_sales);
        callcustomer = findViewById(R.id.callcustomer);

        comment = findViewById(R.id.comment);

        commentsRv = findViewById(R.id.sales_comments_rv);
        suggestRv = findViewById(R.id.sug_sales_Rv);

        suggestRv.setHasFixedSize(true);

        GridLayoutManager mGridlayoutManager = new GridLayoutManager(getApplicationContext(),2);
        suggestRv.setLayoutManager(mGridlayoutManager);

        commentsRv.setHasFixedSize(false);

        //SpanningLinearLayoutManager llm = new SpanningLinearLayoutManager(this , SpanningLinearLayoutManager.VERTICAL,false);
        //commentsRv.setLayoutManager(llm);

        LinearLayoutManager llm = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL,false);
        commentsRv.setLayoutManager(llm);

        commentsItems = new ArrayList<>();
        suggestionItems = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            title = extras.getString("item_title");
            datee = extras.getString("item_date");
            description = extras.getString("item_description");
            city = extras.getString("item_city");
            saller_name = extras.getString("item_owner");
            saller_phone = extras.getString("item_phone");
            post_url = extras.getString("item_url");
            img1 = extras.getString("item_img1");
            img2 = extras.getString("item_img2");
            img3 = extras.getString("item_img3");
            img4 = extras.getString("item_img4");
            img5 = extras.getString("item_img5");
            img6 = extras.getString("item_img6");
            img7 = extras.getString("item_img7");
            img8 = extras.getString("item_img8");
            post_id = extras.getString("item_id");
            department = extras.getString("item_department");

        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, "http://alosboiya.com.sa"+ post_url);
                startActivity(Intent.createChooser(intent2, "Share via"));

            }
        });

        callcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+saller_phone));
                startActivity(intent);

            }
        });

        dtitle.setText(title);
        ddate.setText(datee);
        dsname.setText(saller_name);
        dlocation.setText(city);
        callnum.setText(saller_phone);
        ddescripe.setText(description);

        pics = new ArrayList<>();

        pics.add(img1);
        pics.add(img2);
        pics.add(img3);
        pics.add(img4);
        pics.add(img5);
        pics.add(img6);
        pics.add(img7);
        pics.add(img8);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("isLoggedIn").equals("True"))
                {
                    final FragmentManager fm = getFragmentManager();
                    AddCommentFragment addCommentFragment = new AddCommentFragment();

                    addCommentFragment.show(fm,"TV_tag");
                }else
                    {
                        showMessage("سجل الدخول اولا");
                    }
            }
        });



        imgslider = findViewById(R.id.myslider);


        HashMap<String,String> url_maps = new HashMap<>();

        for(int i=0;i<pics.size();i++)
        {
            if(!pics.get(i).equals("images/imgposting.png"))
            {
                url_maps.put("Picture "+String.valueOf(i + 1),pics.get(i));
            }
        }

        if(url_maps.isEmpty())
        {
            url_maps.put("No Pictures","http://alosboiya.com.sa/images/imgposting.png");
            imgslider.stopAutoCycle();
            imgslider.setEnabled(false);
        }


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            imgslider.addSlider(textSliderView);
        }

        imgslider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imgslider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imgslider.setCustomAnimation(new DescriptionAnimation());
        imgslider.setDuration(4000);
        imgslider.addOnPageChangeListener(this);




        JSON_DATA_WEB_CALL("http://alosboiya.com.sa/webs.asmx/select_post_suggest_by_department?Department="+department);


        JSON_DATA_WEB_CALL2("http://alosboiya.com.sa/webs.asmx/select_comment_post?id_post="+post_id);



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onButtonClick(AddButtonClick addButtonClick)
    {

        commentsItems.clear();

        String name = addButtonClick.getEvent();

        volleyConnection(tinyDB.getString("user_id"),name,tinyDB.getString("user_name"),tinyDB.getString("user_img"));

        JSON_DATA_WEB_CALL2("http://alosboiya.com.sa/webs.asmx/select_comment_post?id_post="+post_id);


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



    public void JSON_DATA_WEB_CALL(String URL){

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

                        showMessage("");


                    }
                }
        );

        requestQueue = Volley.newRequestQueue(getApplication());

        requestQueue.add(stringRequest);
    }

    public void JSON_DATA_WEB_CALL2(String URL){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL2(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showMessage("");


                    }
                }
        );

        requestQueue = Volley.newRequestQueue(getApplication());

        requestQueue.add(stringRequest);
    }

    private void JSON_PARSE_DATA_AFTER_WEBCALL2(String response) {

        try {

            JSONArray js = new JSONArray(response);

            for(int i = 0; i<js.length(); i++) {

                JSONObject childJSONObject = js.getJSONObject(i);

                SallesCommentItems oursales = new SallesCommentItems();

                oursales.setID(childJSONObject.getString("Id"));

                oursales.setScommenttext(childJSONObject.getString("Comment"));

                oursales.setScommentdate(childJSONObject.getString("datee"));

                oursales.setScommentuser(childJSONObject.getString("name"));


                commentsItems.add(oursales);

            }

            salesitemcommentsAdapter = new SalesCommentAdapter(commentsItems);
            commentsRv.setAdapter(salesitemcommentsAdapter);

            salesitemcommentsAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void JSON_PARSE_DATA_AFTER_WEBCALL(String Jobj){

        try {

            JSONArray js = new JSONArray(Jobj);

            for(int i = 0; i<js.length(); i++) {

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


                suggestionItems.add(oursales);

            }

            sugRvAdapter = new SugesstionAdapter(suggestionItems);
            suggestRv.setAdapter(sugRvAdapter);

            sugRvAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void volleyConnection(final String id_member, final String comment, final String name_member, final String image)
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/insert_comment?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


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
                params.put("id_member", id_member);
                params.put("id_post", post_id);
                params.put("comment", comment);
                params.put("name_member", name_member);
                params.put("imageeee", image);
                params.put("path_catagory", department);

                return params;
            }



        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        //showMessage(String.valueOf(position));

        Intent intent = new Intent(getApplicationContext(), ImagePreviewActivity.class);

        intent.putExtra("item_img1",img1);
        intent.putExtra("item_img2",img2);
        intent.putExtra("item_img3",img3);
        intent.putExtra("item_img4",img4);
        intent.putExtra("item_img5",img5);
        intent.putExtra("item_img6",img6);
        intent.putExtra("item_img7",img7);
        intent.putExtra("item_img8",img8);
        this.startActivity(intent);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
