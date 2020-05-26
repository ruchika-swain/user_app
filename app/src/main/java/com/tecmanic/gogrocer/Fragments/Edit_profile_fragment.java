package com.tecmanic.gogrocer.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.tecmanic.gogrocer.Activity.MainActivity;
import com.tecmanic.gogrocer.Config.BaseURL;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.tecmanic.gogrocer.Config.BaseURL.EDIT_PROFILE_URL;
import static com.tecmanic.gogrocer.Config.BaseURL.updatenotifyby;


public class Edit_profile_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Edit_profile_fragment.class.getSimpleName();

    private EditText et_phone, et_name, et_email, et_house;
    private RelativeLayout btn_update;
    private TextView tv_phone, tv_name, tv_email, tv_house, tv_socity, btn_socity;
    private ImageView iv_profile;
    SharedPreferences myPrefrence;

    private String getsocity = "";
    private String filePath = "";
    private static final int GALLERY_REQUEST_CODE1 = 201;
    private Bitmap bitmap;
    private Uri imageuri;
    String image;
    String getphone ,  getid,user_id ;
    String getname;
    String getemail , getpassword;
    private Session_management sessionManagement;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    CardView circle1,circle2,circle3,circle4,circle5,circle6;
    boolean Email_Status=false,Sms_Status=false,In_App=false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView update_profile;

    public Edit_profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);

        sharedPreferences = getContext().getSharedPreferences("User_profile", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ((MainActivity) getActivity()).setTitle("Edit Profile");



        Email_Status= sharedPreferences.getBoolean("Email",true);
        Sms_Status= sharedPreferences.getBoolean("Sms",true);
        In_App= sharedPreferences.getBoolean("App",true);
        sessionManagement = new Session_management(getActivity());
        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        et_phone = (EditText) view.findViewById(R.id.et_pro_phone);
        et_name = (EditText) view.findViewById(R.id.et_pro_name);
        et_email = (EditText) view.findViewById(R.id.et_pro_email);
        btn_update = (RelativeLayout) view.findViewById(R.id.btn_pro_edit);

         getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        getpassword = sessionManagement.getUserDetails().get(BaseURL.KEY_PASSWORD);
        String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
         getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
         getphone = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
         getid = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            Log.d("dd",getid);
        String getpin = sessionManagement.getUserDetails().get(BaseURL.KEY_PINCODE);
        String gethouse = sessionManagement.getUserDetails().get(BaseURL.KEY_HOUSE);
        getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);

        et_name.setText(getname);
        et_phone.setText(getphone);
        update_profile=view.findViewById(R.id.update_profile);
        circle1= view.findViewById(R.id.circle1);
        circle2= view.findViewById(R.id.circle2);
        circle3= view.findViewById(R.id.circle3);
        circle4= view.findViewById(R.id.circle4);
        circle5= view.findViewById(R.id.circle5);
        circle6= view.findViewById(R.id.circle6);


        if (Email_Status){
            circle1.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            circle2.setCardBackgroundColor(getResources().getColor(R.color.grey));
            circle1.setEnabled(false);
            circle2.setEnabled(true);

        }
        else {
            circle2.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            circle1.setCardBackgroundColor(getResources().getColor(R.color.grey));
            circle2.setEnabled(false);
            circle1.setEnabled(true);

        }
        if (Sms_Status){
            circle3.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            circle4.setCardBackgroundColor(getResources().getColor(R.color.grey));
            circle3.setEnabled(false);
            circle4.setEnabled(true);

        }
        else {
            circle4.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            circle3.setCardBackgroundColor(getResources().getColor(R.color.grey));
            circle4.setEnabled(false);
            circle3.setEnabled(true);
        }

        if (In_App){
            circle5.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            circle6.setCardBackgroundColor(getResources().getColor(R.color.grey));
            circle5.setEnabled(false);
            circle6.setEnabled(true);

        }
        else {
            circle6.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            circle5.setCardBackgroundColor(getResources().getColor(R.color.grey));
            circle6.setEnabled(false);
            circle5.setEnabled(true);
        }

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_,sms_;
//                editor=sharedPreferences.edit();
                editor.putBoolean("Email",Email_Status);
                editor.putBoolean("Sms",Sms_Status);
                editor.putBoolean("App",In_App);
                editor.commit();
                editor.apply();
                //recreate();
                if (Email_Status){
                    email_="1";

                }
                else {
                    email_="0";
                }
                if (Sms_Status){
                    sms_="1";
                }
                else {
                    sms_="0";
                }
                if (In_App){
                    sms_="1";
                }
                else {
                    sms_="0";
                }
                Notif(user_id,email_,"1",sms_);

            }
        });


        circle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circle1.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                circle2.setCardBackgroundColor(getResources().getColor(R.color.grey));
                circle1.setEnabled(false);
                Email_Status= true;

                circle2.setEnabled(true);

            }
        });
        circle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email_Status=false;
                circle2.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                circle1.setCardBackgroundColor(getResources().getColor(R.color.grey));
                circle2.setEnabled(false);
                circle1.setEnabled(true);

            }
        });
        circle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sms_Status = true;
                circle3.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                circle4.setCardBackgroundColor(getResources().getColor(R.color.grey));
                circle3.setEnabled(false);
                circle4.setEnabled(true);

            }
        });
        circle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sms_Status= false;
                circle4.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                circle3.setCardBackgroundColor(getResources().getColor(R.color.grey));
                circle4.setEnabled(false);
                circle3.setEnabled(true);
            }
        });
        circle5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                In_App = true;
                circle5.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                circle6.setCardBackgroundColor(getResources().getColor(R.color.grey));
                circle5.setEnabled(false);
                circle6.setEnabled(true);

            }
        });
        circle6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                In_App= false;
                circle6.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                circle5.setCardBackgroundColor(getResources().getColor(R.color.grey));
                circle6.setEnabled(false);
                circle5.setEnabled(true);
            }
        });

        if (!TextUtils.isEmpty(getimage)) {

//            Glide.with(getActivity())
//                    .load(IMG_PROFILE_URL + getimage)
//                    .centerCrop()
//                    .placeholder(R.drawable.splashicon)
//                    .crossFade()
//                    .into(iv_profile);
        }

        if (!TextUtils.isEmpty(getemail)) {
            et_email.setText(getemail);
        }

        /*if (!TextUtils.isEmpty(gethouse)){
            et_house.setText(gethouse);
        }*/

        btn_update.setOnClickListener(this);
        //btn_socity.setOnClickListener(this);
//        iv_profile.setOnClickListener(this);

        return view;
    }
    private void Notif (String user_id, String email1,String app,String sms){

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("sms", sms);
        params.put("app", app);
        params.put("email", email1);

        CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, updatenotifyby, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag",response.toString());

                try {
                    String message= response.getString("message");

                    String status= response.getString("status");

                    if (status.contains("1"))
                    {
                          Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                          Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //   Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_pro_edit) {

            if (et_email.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "enter email address", Toast.LENGTH_SHORT).show();
            } else {
                if (et_email.getText().toString().trim().matches(emailPattern)) {
                    getphone = et_phone.getText().toString();
                    getname = et_name.getText().toString();
                    getemail = et_email.getText().toString();

                    storeImage(bitmap);

                    updateprofile();

                }

                else {
                    Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }

//        } else if (id == R.id.iv_pro_img) {
//            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            // Start the Intent
//            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE1);
        }
    }

    private void attemptEditProfile() {

       // tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
//        tv_email.setText(getResources().getString(R.string.tv_login_email));
//        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        /*tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));*/

//        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
       // tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
//        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));
        /*tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));*/



        Log.d("jj",getname+getphone);
        /*String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);*/


    }

    private boolean isPhoneValid(String phoneno) {
        return phoneno.length() > 9;
    }

    private void storeImage(Bitmap thumbnail) {
//        if (iv_profile.getDrawable() == null) {
//            //  Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
//
//        } else {
//            myPrefrence = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor edit = myPrefrence.edit();
//            edit.remove("image_data");
//            edit.commit();
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");
//            FileOutputStream fo;
//            try {
//                destination.createNewFile();
//                fo = new FileOutputStream(destination);
//                fo.write(bytes.toByteArray());
//                fo.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            iv_profile.setImageBitmap(thumbnail);
//            byte[] b = bytes.toByteArray();
//            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//            edit.putString("image_data", encodedImage);
//            edit.commit();
//
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if ((requestCode == GALLERY_REQUEST_CODE1)) {
            if (resultCode == getActivity().RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    //filePath = imgDecodableString;

                    Bitmap b = BitmapFactory.decodeFile(imgDecodableString);
                    Bitmap out = Bitmap.createScaledBitmap(b, 1200, 1024, false);

                    //getting image from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);


                    File file = new File(imgDecodableString);
                    filePath = file.getAbsolutePath();
                    FileOutputStream fOut;
                    try {
                        fOut = new FileOutputStream(file);
                        out.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                        fOut.flush();
                        fOut.close();
                        //b.recycle();
                        //out.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (requestCode == GALLERY_REQUEST_CODE1) {

                        // Set the Image in ImageView after decoding the String
                        iv_profile.setImageBitmap(bitmap);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateprofile() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name" , getname);
        params.put("user_id", getid);
        params.put("user_phone", getphone);
        params.put("user_email",getemail);
        params.put("user_password",getpassword);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, EDIT_PROFILE_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");


                    if (status.contains("1")) {


                        try {
                            JSONObject obj = response.getJSONObject("data");
                            String user_id = obj.getString("user_id");
                            String user_fullname = obj.getString("user_name");
                            String user_email = obj.getString("user_email");
                            String user_phone = obj.getString("user_phone");
                            String password = obj.getString("user_password");

                            Session_management sessionManagement = new Session_management(getContext());
                            SharedPreferences.Editor editor = getContext().getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
                            editor.putString(BaseURL.KEY_MOBILE, user_phone);
                            editor.putString(BaseURL.KEY_PASSWORD, password);
                            editor.apply();
                            sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, password);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);


                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


                        }
                        catch (Exception e){

                        }


                        Edit_profile_fragment fm = new Edit_profile_fragment();

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)

                        .addToBackStack(null).commit();

                        Toast.makeText(getActivity(), ""+message , Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(Search.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    // asynctask for upload data with image or not image using HttpOk
//    public class editProfile extends AsyncTask<Void, Void, Void> {
//
//        ProgressDialog progressDialog;
//        JSONParser jsonParser;
//        ArrayList<NameValuePair> nameValuePairs;
//        boolean response;
//        String error_string, success_msg;
//
//        String getphone;
//        String getname;
//        String getpin;
//        String gethouse;
//        String getsocity;
//        String getimage;
//        String getwallet;
//        String getrewardpoints;
//
//        public editProfile(String user_id, String name, String phone) {
//            nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new NameValuePair("user_id", user_id));
//        nameValuePairs.add(new NameValuePair("user_fullname", name));
//        nameValuePairs.add(new NameValuePair("user_mobile", phone));
//            /*nameValuePairs.add(new NameValuePair("pincode", pincode));
//            nameValuePairs.add(new NameValuePair("socity_id", socity_id));
//            nameValuePairs.add(new NameValuePair("house_no", house));*/
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.uploading_profile_data), true);
//            jsonParser = new JSONParser(getActivity());
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            String json_responce = null;
//            try {
//                if (filePath == "") {
//                    json_responce = jsonParser.execPostScriptJSON(BaseURL.EDIT_PROFILE_URL, nameValuePairs);
//                } else {
//                    json_responce = jsonParser.execMultiPartPostScriptJSON(BaseURL.EDIT_PROFILE_URL, nameValuePairs, "image/png", filePath, "image");
//                }
//                Log.e(TAG, json_responce + "," + filePath);
//
//                JSONObject jObj = new JSONObject(json_responce);
//                if (jObj.getBoolean("responce")) {
//                    response = true;
//                    //success_msg = jObj.getString("data");
//                    JSONObject obj = jObj.getJSONObject("data");
//                    getphone = obj.getString("user_phone");
//                    getname = obj.getString("user_fullname");
//                    getpin = obj.getString("pincode");
//                    gethouse = obj.getString("house_no");
//                    getsocity = obj.getString("socity_id");
//                    getimage = obj.getString("user_image");
//                    getwallet = obj.getString("wallet");
//                    getrewardpoints = obj.getString("rewards");
//                } else {
//                    response = false;
//                    error_string = jObj.getString("error");
//                    return null;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }

//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            if (progressDialog != null) {
//                progressDialog.hide();
//                progressDialog.dismiss();
//                progressDialog = null;
//            }
//
//            if (response) {
//
//                sessionManagement.updateData(getname, getphone, getpin, getsocity, getimage, getwallet, getrewardpoints, gethouse);
//
//                ((MainActivity) getActivity()).updateHeader();
//
//                Toast.makeText(getActivity(), getResources().getString(R.string.profile_updated), Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getActivity(), "" + error_string, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            if (progressDialog != null) {
//                progressDialog.hide();
//                progressDialog.dismiss();
//                progressDialog = null;
//            }
//        }
//
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

      /*  MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return false;
    }

}
