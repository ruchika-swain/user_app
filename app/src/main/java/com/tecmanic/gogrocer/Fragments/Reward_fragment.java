package com.tecmanic.gogrocer.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.Activity.MainActivity;
import com.tecmanic.gogrocer.Config.BaseURL;
import com.tecmanic.gogrocer.Config.SharedPref;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;
import com.tecmanic.gogrocer.util.GifImageView;
import com.tecmanic.gogrocer.util.NetworkConnection;
import com.tecmanic.gogrocer.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tecmanic.gogrocer.Config.BaseURL.myprofile;
import static com.tecmanic.gogrocer.Config.BaseURL.redeem_rewards;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class Reward_fragment extends Fragment {
    private GifImageView gifImageView;
    ProgressDialog progressDialog;

    private static String TAG = Reward_fragment.class.getSimpleName();
    RelativeLayout Reedeem_Points;
    TextView Rewards_Points;
    private Session_management sessionManagement;

    public Reward_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_reward_points, container, false);
        ((MainActivity) getActivity()).setTitle("Reward");
        sessionManagement = new Session_management(getActivity());
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        String getrewards = sessionManagement.getUserDetails().get(BaseURL.KEY_REWARDS_POINTS);
        Rewards_Points = (TextView) view.findViewById(R.id.reward_points);
        //  Rewards_Points.setText(getrewards);
        gifImageView = (GifImageView) view.findViewById(R.id.gif_image);
        gifImageView.setGifImageResource(R.drawable.pay);

        Reedeem_Points = (RelativeLayout) view.findViewById(R.id.reedme_point);
        Reedeem_Points.setEnabled(false);
        Reedeem_Points.setAlpha(0.5f);
        Reedeem_Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redeemPoints();
                gifImageView.setVisibility(View.VISIBLE);
                final View myview = gifImageView;

                view.postDelayed(new Runnable() {
                    public void run() {
                        myview.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        });

        if (ConnectivityReceiver.isConnected()) {
            getRewards();
        }
        return view;

    }




    private void redeemPoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, redeem_rewards, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Prod detail", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("1")) {
                        Rewards_Points.setText("0");
                        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Try after sometime", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                Session_management session_management = new Session_management(getContext());
                Log.d("hj",session_management.getUserDetails().get(BaseURL.KEY_ID));
                params.put("user_id",session_management.getUserDetails().get(BaseURL.KEY_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }






    private void getRewards() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, myprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Prod detail", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        JSONObject jsonArray = jsonObject.getJSONObject("data");

                        String rewards = jsonArray.getString("rewards");

                        Rewards_Points.setText(rewards);
                        if (rewards.equals("0")){
                            Reedeem_Points.setEnabled(false);
                            Reedeem_Points.setAlpha(0.8f);
                        }else {
                            Reedeem_Points.setEnabled(true);
                            Reedeem_Points.setAlpha(1f);
                        }
                    } else {


                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                Session_management session_management = new Session_management(getContext());
                Log.d("hj",session_management.getUserDetails().get(BaseURL.KEY_ID));
                params.put("user_id",session_management.getUserDetails().get(BaseURL.KEY_ID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }








//    public void getRewards() {
//        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//        RequestQueue rq = Volley.newRequestQueue(getActivity());
//        StringRequest strReq = new StringRequest(Request.Method.POST, RewardUrl + user_id,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jObj = new JSONObject(response);
//                            if (jObj.optString("success").equalsIgnoreCase("success")) {
//                                String rewards_points = jObj.getString("total_rewards");
//                                if (rewards_points.equals("null")) {
//                                    Rewards_Points.setText("0");
//                                } else {
//                                    Rewards_Points.setText(rewards_points);
//                                    SharedPref.putString(getActivity(), BaseURL.KEY_REWARDS_POINTS, rewards_points);
//                                }
//
//                            } else {
//                                // Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//        };
//        rq.add(strReq);
//    }

    private void Shift_Reward_to_WAllet() {
        final String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        final String getreward = Rewards_Points.getText().toString();
        final String getwallet = SharedPref.getString(getActivity(), BaseURL.KEY_WALLET_Ammount);
        if (NetworkConnection.connectionChecking(getActivity())) {
            RequestQueue rq = Volley.newRequestQueue(getActivity());
            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.BASE_URL+"index.php/api/shift",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray Jarray = object.getJSONArray("wallet_amount");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json_data = Jarray.getJSONObject(i);
                                    String final_amount = json_data.getString("final_amount");
                                    String final_rewards = json_data.getString("final_rewards");
                                    Rewards_Points.setText(final_rewards);
                                    SharedPref.putString(getActivity(), BaseURL.KEY_WALLET_Ammount, final_amount);
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
                    params.put("user_id", user_id);
                    params.put("rewards", getreward);
                    params.put("amount", getwallet);
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Intent intent = new Intent(getActivity(), NetworkError.class);
            startActivity(intent);
        }

    }


}