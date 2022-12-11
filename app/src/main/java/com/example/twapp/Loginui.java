package com.example.twapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Loginui extends AppCompatActivity {
    Button btnlogin;
    EditText inputemail,inputpwd;
    TextView createAcc;
    private Handler handler=null;
    private boolean info;
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        handler = new Handler();
        btnlogin = findViewById(R.id.btnlogin);
        inputemail = findViewById(R.id.inputemail);
        inputpwd = findViewById(R.id.inputpwd);
        createAcc = findViewById(R.id.createAcc);
        Bundle bundle =this.getIntent().getExtras();
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Loginui.this,registerui.class));

            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            private String pwd;
            private String email;
            private String value;

            @Override
            public void onClick(View v) {

                class login extends AsyncTask<Void, Void,String> {
                    OkHttpClient client = new OkHttpClient();
                    @Override
                    protected String doInBackground(Void... voids) {
                        String result = null;
                        JSONObject jsonObject = new JSONObject();
                        EditText inputemail = (EditText)findViewById(R.id.inputemail);
                        EditText inputpwd = (EditText)findViewById(R.id.inputpwd);
                        try {

                            jsonObject.put("email", inputemail.getText().toString());
                            jsonObject.put("password", inputpwd.getText().toString());
                            System.out.println(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                        RequestBody body = RequestBody.create(jsonObject.toString(), mediaType);
                        Request request = new Request.Builder()
                                .url("https://c05b-2001-b011-b800-d21e-c0d0-aa95-1cdf-229d.ngrok.io/api/EmergencyContact/login")
                                .method("POST", body)
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
                                            .url("https://c05b-2001-b011-b800-d21e-c0d0-aa95-1cdf-229d.ngrok.io/api/Device/create/1")
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
                new login().execute();

                new Thread(new Runnable() {
                    @Override

                    public void run() {

                        while (true) {
                            // write code

                            class sostrg extends AsyncTask<Void, Void, Boolean> {
                                @Override
                                protected Boolean doInBackground(Void... voids) {
                                    Request request = new Request.Builder()
                                            .url("https://c05b-2001-b011-b800-d21e-c0d0-aa95-1cdf-229d.ngrok.io/api/Gps/sostrigger/1")
                                            .build();

                                    try (Response response = client.newCall(request).execute()) {
                                        if (response.code() == 200) {
                                            String result = response.body().string();
                                            JSONObject jsonObject = new JSONObject(result);
                                            info = jsonObject.getBoolean("sosInfo");
                                            System.out.println(info);
                                            return info;
                                        }
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return info;
                                }

                                protected void onPostExecute(String result) {

                                }
                            }


                            new sostrg().execute();

                            try {
                                Thread.sleep(30000);

                            } catch (InterruptedException e) {

                                e.printStackTrace();

                            }
                        }
                    }

                }).start();

            }
        });
    }

}

