package com.tecmanic.gogrocer.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.Activity.Coupen;
import com.tecmanic.gogrocer.Activity.MainActivity;
import com.tecmanic.gogrocer.Activity.OrderSuccessful;
import com.tecmanic.gogrocer.Activity.PaymentGatWay;
import com.tecmanic.gogrocer.Activity.Paytm;
import com.tecmanic.gogrocer.Config.BaseURL;
import com.tecmanic.gogrocer.Config.SharedPref;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.NetworkConnection;
import com.tecmanic.gogrocer.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.VolleyLog.TAG;
import static com.tecmanic.gogrocer.Config.BaseURL.COUPON_CODE;
import static com.tecmanic.gogrocer.Config.BaseURL.Wallet_CHECKOUT;


public class Payment_fragment extends Fragment {
    LinearLayout confirm;
    private DatabaseHandler db_cart;
    private Session_management sessionManagement;
    TextView payble_ammount, my_wallet_ammount, used_wallet_ammount, used_coupon_ammount, order_ammount;
    private String getlocation_id = "";
    private String getstore_id = "";

    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private Double rewards;
    RadioButton rb_Store, rb_Cod, rb_card, rb_Netbanking, rb_paytm;
    CheckBox checkBox_Wallet, checkBox_coupon;
    EditText et_Coupon;
    String getvalue;
    String text;
    String cp;
    String Used_Wallet_amount;
    String total_amount;
    String order_total_amount;
    RadioGroup radioGroup;
    String Prefrence_TotalAmmount;
    String lat,lng;
    String getwallet;
    LinearLayout Promo_code_layout, Coupon_and_wallet;
    RelativeLayout Apply_Coupon_Code, Relative_used_wallet, Relative_used_coupon;

    SharedPreferences sharedPreferences12;
    SharedPreferences.Editor editor12;
    public Payment_fragment() {

    }

    public static Payment_fragment newInstance(String param1, String param2) {
        Payment_fragment fragment = new Payment_fragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_payment_details, container, false);
       // ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment));

        Prefrence_TotalAmmount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);

        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                getvalue = radioButton.getText().toString();
            }
        });

        sharedPreferences12=getActivity().getSharedPreferences("loction", MODE_PRIVATE);
        editor12=sharedPreferences12.edit();
        lat= sharedPreferences12.getString("lat","77.1111");
        lng = sharedPreferences12.getString("lng","22.02002");

        Log.e("LAnt",lat+"\n"+lng);


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Font/Bold.ttf");
        checkBox_Wallet = (CheckBox) view.findViewById(R.id.use_wallet);
        checkBox_Wallet.setTypeface(font);
        rb_Store = (RadioButton) view.findViewById(R.id.use_store_pickup);
        rb_Store.setTypeface(font);
        rb_Cod = (RadioButton) view.findViewById(R.id.use_COD);
        rb_Cod.setTypeface(font);
        rb_card = (RadioButton) view.findViewById(R.id.use_card);
        rb_card.setTypeface(font);
        rb_Netbanking = (RadioButton) view.findViewById(R.id.use_netbanking);
        rb_Netbanking.setTypeface(font);
        rb_paytm = (RadioButton) view.findViewById(R.id.use_wallet_ammount);
        rb_paytm.setTypeface(font);
        checkBox_coupon = (CheckBox) view.findViewById(R.id.use_coupon);
        checkBox_coupon.setTypeface(font);
        et_Coupon = (EditText) view.findViewById(R.id.et_coupon_code);
        Promo_code_layout = (LinearLayout) view.findViewById(R.id.prommocode_layout);
        Apply_Coupon_Code = (RelativeLayout) view.findViewById(R.id.apply_coupoun_code);
        checkBox_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Coupon_code();

startActivity(new Intent(getActivity(), Coupen.class));
            }
        });

        sessionManagement = new Session_management(getActivity());


        Coupon_and_wallet = (LinearLayout) view.findViewById(R.id.coupon_and_wallet);
        Relative_used_wallet = (RelativeLayout) view.findViewById(R.id.relative_used_wallet);
        Relative_used_coupon = (RelativeLayout) view.findViewById(R.id.relative_used_coupon);

        //Show  Wallet
        getwallet = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
        my_wallet_ammount = (TextView) view.findViewById(R.id.user_wallet);
        my_wallet_ammount.setText(getwallet+getActivity().getString(R.string.currency));
        db_cart = new DatabaseHandler(getActivity());
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()

        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {


                   /* androidx.fragment.app.Fragment fm = new HomeeeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();*/


                    return true;
                }
                return false;
            }
        });


        total_amount = getArguments().getString("total");
        order_total_amount = getArguments().getString("total");
        getdate = getArguments().getString("getdate");
        gettime = getArguments().getString("gettime");
        getlocation_id = getArguments().getString("getlocationid");
        getstore_id = getArguments().getString("getstoreid");
        payble_ammount = (TextView) view.findViewById(R.id.payable_ammount);
        order_ammount = (TextView) view.findViewById(R.id.order_ammount);
        used_wallet_ammount = (TextView) view.findViewById(R.id.used_wallet_ammount);
        used_coupon_ammount = (TextView) view.findViewById(R.id.used_coupon_ammount);
        payble_ammount.setText(total_amount+getActivity().getString(R.string.currency));
        order_ammount.setText(order_total_amount+getActivity().getString(R.string.currency));


        checkBox_Wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Use_Wallet_Ammont();

                    Coupon_and_wallet.setVisibility(View.VISIBLE);
                    Relative_used_wallet.setVisibility(View.VISIBLE);
                    if (rb_card.isChecked() || rb_Netbanking.isChecked() || rb_paytm.isChecked()) {
                        rb_card.setChecked(false);
                        rb_Netbanking.setChecked(false);
                        rb_paytm.setChecked(false);
                    }
                } else {
                    if (payble_ammount != null) {
                        rb_Cod.setText("COD");
                        rb_card.setClickable(true);
                        rb_card.setTextColor(getResources().getColor(R.color.black));
                        rb_Netbanking.setClickable(true);
                        rb_Netbanking.setTextColor(getResources().getColor(R.color.black));
                        rb_paytm.setClickable(true);
                        rb_paytm.setTextColor(getResources().getColor(R.color.black));
                        checkBox_coupon.setClickable(true);
                        checkBox_coupon.setTextColor(getResources().getColor(R.color.black));
                    }
                    final String Ammount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
                    final String WAmmount = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
                    my_wallet_ammount.setText(WAmmount+getActivity().getResources().getString(R.string.currency));
                    payble_ammount.setText(Ammount+getResources().getString(R.string.currency));
                    used_wallet_ammount.setText("");
                    Relative_used_wallet.setVisibility(View.GONE);
                    if (checkBox_coupon.isChecked()) {
                        final String ammount = SharedPref.getString(getActivity(), BaseURL.COUPON_TOTAL_AMOUNT);
                        payble_ammount.setText(ammount+getResources().getString(R.string.currency));
                    }
                }
            }
        });
        checkBox_coupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Promo_code_layout.setVisibility(View.VISIBLE);
                    Coupon_and_wallet.setVisibility(View.VISIBLE);
                    Relative_used_coupon.setVisibility(View.VISIBLE);
                    if (rb_Store.isChecked() || rb_Cod.isChecked() || rb_card.isChecked() || rb_Netbanking.isChecked() || rb_paytm.isChecked()) {
                        rb_Store.setChecked(false);
                        rb_Cod.setChecked(false);
                        rb_card.setChecked(false);
                        rb_Netbanking.setChecked(false);
                        rb_paytm.setChecked(false);
                    }
                } else {
                    et_Coupon.setText("");
                    Relative_used_coupon.setVisibility(View.GONE);
                    Promo_code_layout.setVisibility(View.GONE);
                }
            }
        });


        confirm =  view.findViewById(R.id.confirm_order);
        confirm.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {

                    confirm.setEnabled(false);
                    if (checkBox_Wallet.isChecked()){
                        getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                        Usewalletfororder(getuser_id,Used_Wallet_amount);
                        checked();

                    }
                    else {
                        checked();

                    }



                } else {
                    confirm.setEnabled(true);

                  //  ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });
        return view;
    }

    private void attemptOrder() {
        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
        rewards = Double.parseDouble(db_cart.getColumnRewards());
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("qty", map.get("qty"));
                    jObjP.put("unit_value", map.get("unit_value"));
                    jObjP.put("unit", map.get("unit"));
                    jObjP.put("price", map.get("price"));
                    jObjP.put("rewards", map.get("rewards"));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {

                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());


    makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id, passArray);


            }
        }
    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, String store_id, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        params.put("time", gettime);
        params.put("user_id", userid);
        params.put("location", location);
        params.put("store_id", store_id);
        params.put("total_ammount",total_amount);
        params.put("payment_method", getvalue);
        params.put("data", passArray.toString());
        params.put("lat",lat);
        params.put("lng",lng);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        String msg = response.getString("data");
                        String msg_arb=response.getString("data_arb");
                        db_cart.clearCart();
                        Intent intent=new Intent(getActivity(), OrderSuccessful.class);
                        intent.putExtra("msg",msg);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               /* if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Usewalletfororder(String userid, String Wallet) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("wallet_amount", Wallet);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, Wallet_CHECKOUT, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("responce");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Use_Wallet_Ammont() {
        final String Wallet_Ammount = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
        final String Coupon_Ammount = SharedPref.getString(getActivity(), BaseURL.COUPON_TOTAL_AMOUNT);
        final String Ammount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
        if (NetworkConnection.connectionChecking(getActivity())) {
            RequestQueue rq = Volley.newRequestQueue(getActivity());
            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.BASE_URL+"index.php/api/wallet_on_checkout",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray Jarray = object.getJSONArray("final_amount");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json_data = Jarray.getJSONObject(i);
                                    String Wallet_amount = json_data.getString("wallet");
                                     Used_Wallet_amount = json_data.getString("used_wallet");
                                    total_amount = json_data.getString("total");
                                    if (total_amount.equals("0")) {
                                        rb_Cod.setText("Home Delivery");
                                        getvalue = rb_Cod.getText().toString();
                                        rb_card.setClickable(false);
                                        rb_card.setTextColor(getResources().getColor(R.color.hintColor));
                                        rb_Netbanking.setClickable(false);

                                        rb_Netbanking.setTextColor(getResources().getColor(R.color.hintColor));
                                        rb_paytm.setClickable(false);
                                        rb_paytm.setTextColor(getResources().getColor(R.color.hintColor));
                                        checkBox_coupon.setClickable(false);
                                        checkBox_coupon.setTextColor(getResources().getColor(R.color.hintColor));
                                    } else {
                                        if (total_amount != null) {
                                            rb_Cod.setText("Cash On Delivery");
                                            rb_card.setClickable(true);
                                            rb_card.setTextColor(getResources().getColor(R.color.black));
                                            rb_Netbanking.setClickable(true);
                                            rb_Netbanking.setTextColor(getResources().getColor(R.color.black));
                                            rb_paytm.setClickable(true);
                                            rb_paytm.setTextColor(getResources().getColor(R.color.black));
                                            checkBox_coupon.setClickable(true);
                                            checkBox_coupon.setTextColor(getResources().getColor(R.color.black));
                                        }
                                    }
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    used_wallet_ammount.setText("(" + getResources().getString(R.string.currency) + Used_Wallet_amount + ")");
                                    SharedPref.putString(getActivity(), BaseURL.WALLET_TOTAL_AMOUNT, total_amount);
                                    my_wallet_ammount.setText(Wallet_amount+getResources().getString(R.string.currency));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    if (checkBox_Wallet.isChecked()){
                        params.put("wallet_amount", Wallet_Ammount);
                    }else {
                        params.put("total_amount", Ammount);

                    }

                    if (checkBox_coupon.isChecked()) {
                        params.put("total_amount", Coupon_Ammount);
                    } else {
                        params.put("total_amount", Ammount);

                    }
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Intent intent = new Intent(getActivity(), NetworkError.class);
            startActivity(intent);
        }
    }

    private void Coupon_code() {
        final String Ammount = SharedPref.getString(getActivity(), BaseURL.TOTAL_AMOUNT);
        final String Wallet_Ammount = SharedPref.getString(getActivity(), BaseURL.WALLET_TOTAL_AMOUNT);
        final String Coupon_code = et_Coupon.getText().toString();
        if (NetworkConnection.connectionChecking(getActivity())) {
            RequestQueue rq = Volley.newRequestQueue(getActivity());
            StringRequest postReq = new StringRequest(Request.Method.POST, COUPON_CODE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                total_amount = obj.getString("Total_amount");
                                String Used_coupon_amount = obj.getString("coupon_value");
                                if (obj.optString("responce").equals("true")) {
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    SharedPref.putString(getActivity(), BaseURL.COUPON_TOTAL_AMOUNT, total_amount);
                                    Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                    used_coupon_ammount.setText("(" + getActivity().getResources().getString(R.string.currency) + Used_coupon_amount + ")");
                                    Promo_code_layout.setVisibility(View.GONE);

                                } else {
                                    Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                    et_Coupon.setText("");
                                    used_coupon_ammount.setText("");
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    Promo_code_layout.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("coupon_code", Coupon_code);
                    if (checkBox_Wallet.isChecked()) {
                        params.put("payable_amount", Wallet_Ammount);
                    } else {
                        params.put("payable_amount", Ammount);
                    }
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Toast.makeText(getActivity(), "Somthing Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }


    private void checked() {
        if (checkBox_Wallet.isChecked()) {
            if (rb_Store.isChecked() || rb_Cod.isChecked()) {
                attemptOrder();
            } else {
                Toast.makeText(getActivity(), "Please Select One", Toast.LENGTH_SHORT).show();
            }

        }
        if (rb_Store.isChecked()) {
            attemptOrder();
        }
        if (rb_Cod.isChecked()) {

            attemptOrder();
        }
        if (rb_card.isChecked()) {
            Intent myIntent = new Intent(getActivity(), PaymentGatWay.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent.putExtra("total", total_amount);
            } else {
                myIntent.putExtra("total", Prefrence_TotalAmmount);
                myIntent.putExtra("getdate", getdate);
                myIntent.putExtra("gettime", gettime);
                myIntent.putExtra("getlocationid", getlocation_id);
                myIntent.putExtra("getstoreid", getstore_id);
                myIntent.putExtra("getpaymentmethod", getvalue);
            }
            getActivity().startActivity(myIntent);
        }
        if (rb_Netbanking.isChecked()) {
            Intent myIntent1 = new Intent(getActivity(), PaymentGatWay.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent1.putExtra("total", total_amount);

            } else {
                myIntent1.putExtra("total", Prefrence_TotalAmmount);
                myIntent1.putExtra("getdate", getdate);
                myIntent1.putExtra("gettime", gettime);
                myIntent1.putExtra("getlocationid", getlocation_id);
                myIntent1.putExtra("getstoreid", getstore_id);
                myIntent1.putExtra("getpaymentmethod", getvalue);
            }
            getActivity().startActivity(myIntent1);
        }
               if (rb_paytm.isChecked()) {
            Intent myIntent1 = new Intent(getActivity(), Paytm.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent1.putExtra("total", total_amount);

            } else {
                myIntent1.putExtra("total", Prefrence_TotalAmmount);
                myIntent1.putExtra("getdate", getdate);
                myIntent1.putExtra("gettime", gettime);
                myIntent1.putExtra("getlocationid", getlocation_id);
                myIntent1.putExtra("getstoreid", getstore_id);
                myIntent1.putExtra("getpaymentmethod", getvalue);
            }
            getActivity().startActivity(myIntent1);

        }
        if (checkBox_coupon.isChecked()) {
            if (rb_Store.isChecked() || rb_Cod.isChecked()) {
                attemptOrder();
            } else {
                Toast.makeText(getActivity(), "Select Store Or Cod", Toast.LENGTH_SHORT).show();
            }


        }



    }


}
