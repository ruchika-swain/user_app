package com.tecmanic.gogrocer.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.Config.BaseURL;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tecmanic.gogrocer.Config.BaseURL.ChangePass;

public class NewPassword extends AppCompatActivity {

    CardView cvsubmit;
    EditText et_req_newpassword;
    String getuserphone;
    public static String TAG="Otp";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        et_req_newpassword=findViewById(R.id.et_req_newpassword);
        cvsubmit=findViewById(R.id.cvsubmit);

        getuserphone=getIntent().getStringExtra("user_phone");
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        cvsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_req_newpassword.getText().toString().length()==0){
                    Toast.makeText(NewPassword.this, "Enter New Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.show();
                    setotpverify();
                }
            }
        });

    }
    public void setotpverify() {




        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_phone", getuserphone);
        params.put("user_password",et_req_newpassword.getText().toString());



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                ChangePass, params, new Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());


                try {

                    String  status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {



                        progressDialog.dismiss();

                        Intent intent=new Intent(NewPassword.this,LoginActivity.class);

                        startActivity(intent);



                    } else {

                        progressDialog.dismiss();
                        Toast.makeText(NewPassword.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
//                    Toast.makeText(NewPassword.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
