package com.tecmanic.gogrocer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.Activity.MainActivity;
import com.tecmanic.gogrocer.Activity.RechargeWallet;
import com.tecmanic.gogrocer.Config.BaseURL;
import com.tecmanic.gogrocer.Config.SharedPref;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;
import com.tecmanic.gogrocer.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Wallet_fragment extends Fragment {

    private static String TAG = Wallet_fragment.class.getSimpleName();

    TextView Wallet_Ammount;


    RelativeLayout Recharge_Wallet;
    private Session_management sessionManagement;

    public Wallet_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wallet_ammount, container, false);
        ((MainActivity) getActivity()).setTitle("Wallet");
        sessionManagement = new Session_management(getActivity());
        //   String getwallet = sessionManagement.getUserDetails().get(BaseURL.KEY_WALLET_Ammount);
        Wallet_Ammount = (TextView) view.findViewById(R.id.wallet_ammount);
        //     Wallet_Ammount.setText(getwallet);
        Recharge_Wallet = (RelativeLayout) view.findViewById(R.id.recharge_wallet);
        Recharge_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), RechargeWallet.class);
                startActivity(intent);
            }
        });
        if (ConnectivityReceiver.isConnected()) {
            getRefresrh();
        }


        return view;

    }

    public void getRefresrh() {
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST, BaseURL.WALLET_REFRESH + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String status = jObj.getString("status");
                            if (status.equals("1")) {
                                String wallet_amount = jObj.getString("data");
                                Wallet_Ammount.setText(wallet_amount);
                                SharedPref.putString(getActivity(), BaseURL.KEY_WALLET_Ammount, wallet_amount);
                            } else {
                                // Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

        };
        rq.add(strReq);
    }

}