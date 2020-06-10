package com.thakursaab.bloodbank.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.thakursaab.bloodbank.R;
import com.thakursaab.bloodbank.Utils.Endpoints;
import com.thakursaab.bloodbank.Utils.VolleySingleton;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MakeRequestActivity extends AppCompatActivity {

    private EditText messageEt;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        //FAST ANDROID NETWORKING INIT
        //AndroidNetworking.initialize(getApplicationContext());
        messageEt=findViewById(R.id.message);
        submitBtn=findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageEt.getText().toString().isEmpty()) {
                    messageEt.setError("Message is Required");
                    messageEt.requestFocus();
                    return;
                }
                //code to upload post
                uploadRequest(messageEt.getText().toString());
            }
        });

    }


    private void uploadRequest(final String message){
        final String number = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("number", "12345");
        //Toast.makeText(this, message+" / "+number, Toast.LENGTH_LONG).show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.upload_request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("SUCCESS")){
                    Toast.makeText(MakeRequestActivity.this, response, Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MakeRequestActivity.this,MainActivity.class));
                    //RegisterActivity.this.finish();
                }else{
                    Toast.makeText(MakeRequestActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MakeRequestActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("message", message);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
