package com.example.twapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;


public class registerui extends AppCompatActivity {

    Button rbtn;
    EditText rusername, riptpwd, rphone,riacc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rbtn = findViewById(R.id.rbtn);
        rusername = findViewById(R.id.rusername);
        riacc = findViewById(R.id.ripacc);
        riptpwd = findViewById(R.id.riptpwd);
        rphone = findViewById(R.id.rphone);



        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( TextUtils.isEmpty(rusername.getText().toString()) && TextUtils.isEmpty(riptpwd.getText().toString()) && TextUtils.isEmpty(rphone.getText().toString()) && TextUtils.isEmpty(riacc.getText().toString())) {

                    String message = "All input required";
                    Toast.makeText(registerui.this, message, Toast.LENGTH_LONG).show();

                } else {
                    Registerreq registerRequest = new Registerreq();
                    registerRequest.setPassword(riptpwd.getText().toString());
                    registerRequest.setContactPerson(rusername.getText().toString());
                    registerRequest.setContactNo(rphone.getText().toString());
                    registerRequest.setContactNo(riacc.getText().toString());

                    class login extends AsyncTask<Void, Void,String> {
                        OkHttpClient client = new OkHttpClient();
                        @Override
                        protected String doInBackground(Void... voids) {
                            String result = null;
                            JSONObject jsonObject = new JSONObject();
                            EditText inputpwd = (EditText)findViewById(R.id.inputpwd);
                            try {

                                jsonObject.put("name", rusername.getText().toString());
                                jsonObject.put("telephone_number", rphone.getText().toString());
                                jsonObject.put("password", riptpwd.getText().toString());
                                jsonObject.put("account", riacc.getText().toString());
                                System.out.println(jsonObject);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                            RequestBody body = RequestBody.create(jsonObject.toString(), mediaType);
                            Request request = new Request.Builder()
                                    .url("https://3f70-49-213-197-9.jp.ngrok.io:443/Guardian/verify/123456/123456")
                                    .method("GET", body)
                                    .build();


                            try (Response response = client.newCall(request).execute()) {
                                if (response.code() == 200) {

                                    result = response.body().string();
                                    jsonObject = new JSONObject(result);
                                    JSONObject data = jsonObject.getJSONObject("deviceCode");
                                    long devicecode = data.getLong("deviceCode");
                                    boolean bind = data.getBoolean("bind");
//                                String email=
                                    if(bind ==false) {
                                        OkHttpClient client = new OkHttpClient();
                                        jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("bind", true);
                                            jsonObject.put("deviceCode", devicecode);
                                            System.out.println(jsonObject);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        MediaType a = MediaType.parse("application/json; charset=utf-8");
                                        RequestBody b = RequestBody.create(jsonObject.toString(), a);
                                        Request re = new Request.Builder()
                                                .url("https://3f70-49-213-197-9.jp.ngrok.io:443/Guardian/create")
                                                .method("PATCH", b)
                                                .build();
                                        try (Response rp = client.newCall(re).execute()) {
                                            if (rp.code() == 200) {

                                                String result1 = rp.body().string();
                                                jsonObject = new JSONObject(result1);
                                                System.out.println(result1);
                                            }
                                        } catch (IOException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }else{
                                    System.out.println("fail");
                                }
                            }
                            catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            return result;
                        }

                        protected void onPostExecute(String result) {
                            System.out.println(result);
                            if (result != null){
                                startActivity(new Intent(Loginui.this,MainActivity.class));
                            }
                            else{
                                String message = "密碼錯誤";
                                Toast.makeText(Loginui.this,message,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
//                    registerUser(registerRequest);
                }
            }
        });
    }
}