package com.thakursaab.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.thakursaab.bloodbank.R;
import com.thakursaab.bloodbank.Utils.Endpoints;
import com.thakursaab.bloodbank.Utils.SavedSharedPreferences;
import com.thakursaab.bloodbank.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText numberEt,passwordEt;
    Button loginBtn, signupBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        numberEt=findViewById(R.id.number);
        passwordEt=findViewById(R.id.password);
        loginBtn=findViewById(R.id.loginbtn);
        signupBtn=findViewById(R.id.signupbtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = numberEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                if (number.length() != 10) {
                    numberEt.setError("Enter Valid Number\n(Without Country Code)");
                    numberEt.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    passwordEt.setError("Password is Required");
                    passwordEt.requestFocus();
                    return;
                }
                login(number,password);
            }
        });
       
    }

    private void login(final String number, final String password) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("SUCCESS")){
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    SavedSharedPreferences.setUserName(getApplicationContext(),number);
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString("number", number).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString("city", response).apply();
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("number", number);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
