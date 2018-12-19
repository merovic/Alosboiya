package alosboiya.jeddahwave.Fragments;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;
import info.hoang8f.android.segmented.SegmentedGroup;

public class RechargeFragment extends Fragment implements AdapterView.OnItemSelectedListener,BillingProcessor.IBillingHandler{

    public static final String TAG = "ass4";

    TextView username,userbalance;
    ImageView userimage;
    EditText n1,n2,n3;
    Spinner rechargespinner;
    Button rechargebutton,restorebutton;
    LinearLayout threelayout;

    EditText addcopon;

    TinyDB tinyDB;

    String user_id,catfinal,productID;

    List<String> cards;

    BillingProcessor bp;

    SegmentedGroup tabsgroup;

    RadioButton cardstab,visatab,cobonstab;

    String twenty,fifty,hundred;

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

        bp = BillingProcessor.newBillingProcessor(Objects.requireNonNull(getContext()), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhaX84u5V+FAyLE7wRcjlqcINs3lBfn+r3dF72rByLO1V/M5KypzZX4i3zj7sX5Wu5RJVtk4LY1ui3hhUooO2LCNzmdwxKs/3gvoxfNLIK+ThQ2ihH9DKdcPFxGNaNukH/m4y8qvgg1KNwAPzBfjYw9IOypGYAFzB4G+0ozbiYbK5a6JTczWW9881ZibT3/DOeGjEVDIFpqxCeIVhUGti2NHH7Eh/RhLqOyp1wTHBVOJdxg6MkbwDElEs/YOmzAvWuMRKUj5zU5P9CmDzTsYMG6E7ePb/GZ1fuoYYmC0IFRzbFczhWj7pR+qKaiSqvV5q/lg6K7wHxSxijFy+CTCxyQIDAQAB", this);
        bp.initialize();

        username = Objects.requireNonNull(getActivity()).findViewById(R.id.username);
        userbalance = getActivity().findViewById(R.id.userbalance);
        userimage = getActivity().findViewById(R.id.userimage);
        n1 = getActivity().findViewById(R.id.n1);
        n2 = getActivity().findViewById(R.id.n2);
        n3 = getActivity().findViewById(R.id.n3);
        rechargespinner = getActivity().findViewById(R.id.rechargespinner);
        rechargebutton = getActivity().findViewById(R.id.rechargebutton);
        addcopon = getActivity().findViewById(R.id.add_copon);

        setupSpinner();

        username.setText(tinyDB.getString("user_name"));
        userbalance.setText(tinyDB.getString("user_balance"));

        user_id = tinyDB.getString("user_id");

        if(tinyDB.getString("user_img").equals("images/imgposting.png") || tinyDB.getString("user_img").equals(""))
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


        tabsgroup = getActivity().findViewById(R.id.segmented2);

        cardstab = getActivity().findViewById(R.id.button3);
        visatab = getActivity().findViewById(R.id.button2);
        cobonstab = getActivity().findViewById(R.id.button1);

        cardstab.setChecked(true);
        visatab.setChecked(false);
        cobonstab.setChecked(false);

        threelayout = getActivity().findViewById(R.id.threelayout);
        restorebutton = getActivity().findViewById(R.id.restorebutton);

        cardstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                threelayout.setVisibility(View.VISIBLE);
                restorebutton.setVisibility(View.INVISIBLE);
                addcopon.setVisibility(View.GONE);
                rechargespinner.setVisibility(View.VISIBLE);
                rechargebutton.setText("شحن الرصيد");
            }
        });

        visatab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                threelayout.setVisibility(View.GONE);
                restorebutton.setVisibility(View.VISIBLE);
                addcopon.setVisibility(View.GONE);
                rechargespinner.setVisibility(View.VISIBLE);
                rechargebutton.setText("شحن الرصيد");
            }
        });

        cobonstab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                threelayout.setVisibility(View.GONE);
                restorebutton.setVisibility(View.GONE);
                addcopon.setVisibility(View.VISIBLE);
                rechargespinner.setVisibility(View.GONE);
                rechargebutton.setText("شحن الكوبون");
            }
        });

        restorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bp.loadOwnedPurchasesFromGoogle();
                showMessage("تم استرجاع المدفوعات");

            }
        });



        rechargebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addcopon.getVisibility()==View.VISIBLE)
                {
                    rechargeCopon();

                }else if(threelayout.getVisibility()==View.VISIBLE)
                {

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
                                                showMessage(response);
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
                                    params.put("cat",catfinal);
                                    params.put("id_member", user_id);
                                    params.put("id_card",n1.getText().toString());
                                    params.put("n2",n2.getText().toString());
                                    params.put("n3",n3.getText().toString());
                                    return params;

                                }

                            };

                            RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
                        }

                }else
                    {

                        if(rechargespinner.getSelectedItemPosition()==0)
                        {
                            showMessage("اختر فئة الكارت اولا");
                        }else
                            {
                                if(rechargespinner.getSelectedItemPosition()==1)
                                {
                                    if(tinyDB.getString("twentyToken").equals("com.alosboiya.20riyal"))
                                    {
                                        Boolean consumed = bp.consumePurchase("com.alosboiya.20riyal");
                                        if(consumed)
                                        {
                                            catfinal = "20";
                                            updateBalance();
                                        }

                                    }else
                                    {
                                        catfinal = "20";
                                        productID = "com.alosboiya.20riyal";

                                        bp.purchase(getActivity(), productID);
                                    }

                                }else if(rechargespinner.getSelectedItemPosition()==2)
                                {
                                    if(tinyDB.getString("fiftyToken").equals("com.alosboiya.50riyal"))
                                    {
                                        Boolean consumed = bp.consumePurchase("com.alosboiya.50riyal");
                                        if(consumed)
                                        {
                                            catfinal = "50";
                                            updateBalance();
                                        }

                                    }else
                                    {
                                        catfinal = "50";
                                        productID = "com.alosboiya.50riyal";

                                        bp.purchase(getActivity(), productID);
                                    }

                                }else if(rechargespinner.getSelectedItemPosition()==3)
                                {
                                    if(tinyDB.getString("hundredToken").equals("com.alosboiya.100riyal"))
                                    {
                                        Boolean consumed = bp.consumePurchase("com.alosboiya.100riyal");
                                        if(consumed)
                                        {
                                            catfinal = "100";
                                            updateBalance();
                                        }

                                    }else
                                    {
                                        catfinal = "100";
                                        productID = "com.alosboiya.100riyal";

                                        bp.purchase(getActivity(), productID);
                                    }
                                }
                            }
                    }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
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


    private void rechargeCopon()
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/copon?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.contains("كود الكبون خطاء"))
                        {
                            showMessage(response);
                        }else if(response.contains("مسبقا"))
                        {
                            showMessage("هذا الكوبون تم استخدامه مسبقأ");
                        }else
                            {
                                userbalance.setText(response);

                                tinyDB.putString("user_balance",response);

                                showMessage("تم شحن الكوبون بنجاح");
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
                params.put("copon_text", addcopon.getText().toString());

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

        rechargespinner = Objects.requireNonNull(getActivity()).findViewById(R.id.rechargespinner);
        ArrayAdapter<String> elmadenaAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),android.R.layout.simple_spinner_item, cards);
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

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {


        switch (productId) {
            case "com.alosboiya.20riyal":
                twenty = Objects.requireNonNull(bp.getPurchaseTransactionDetails(productId)).purchaseInfo.purchaseData.purchaseToken;
                tinyDB.putString("twentyToken", twenty);

                break;
            case "com.alosboiya.50riyal":
                fifty = Objects.requireNonNull(bp.getPurchaseTransactionDetails(productId)).purchaseInfo.purchaseData.purchaseToken;
                tinyDB.putString("fiftyToken", fifty);

                break;
            case "com.alosboiya.100riyal":
                hundred = Objects.requireNonNull(bp.getPurchaseTransactionDetails(productId)).purchaseInfo.purchaseData.purchaseToken;
                tinyDB.putString("hundredToken", hundred);

                break;
        }

        updateBalance();

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

        if(errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED)
        {
            showMessage("خطأ فى الشحن");
        }
    }

    @Override
    public void onBillingInitialized() {

    }
}
