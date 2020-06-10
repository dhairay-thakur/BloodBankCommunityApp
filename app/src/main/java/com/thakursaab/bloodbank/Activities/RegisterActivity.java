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
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.thakursaab.bloodbank.R;
import com.thakursaab.bloodbank.Utils.Endpoints;
import com.thakursaab.bloodbank.Utils.SavedSharedPreferences;
import com.thakursaab.bloodbank.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt,numberEt,bgEt,cityEt,passwordEt;
    private Button submitBtn;
    private List<String> valid_bg = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEt = findViewById(R.id.name);
        cityEt = findViewById(R.id.city);
        numberEt = findViewById(R.id.number);
        bgEt = findViewById(R.id.bg);
        passwordEt = findViewById(R.id.password);
        submitBtn = findViewById(R.id.submitbtn);
        valid_bg.addAll(Arrays.asList("O+","O-","A+","B+","A-","B-","AB+","AB-"));
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, city, number, bg, password;
                name = nameEt.getText().toString();
                if (name.isEmpty()) {
                    nameEt.setError("Name is Required");
                    nameEt.requestFocus();
                    return;
                }
                city = cityEt.getText().toString().trim();
                if (city.isEmpty()) {
                    cityEt.setError("City is Required");
                    cityEt.requestFocus();
                    return;
                }
                number = numberEt.getText().toString().trim();
                if (number.length() != 10) {
                    numberEt.setError("Enter Valid Number\n(Without Country Code)");
                    numberEt.requestFocus();
                    return;
                }
                bg = bgEt.getText().toString().trim().toUpperCase();
                if (bg.isEmpty() || !valid_bg.contains(bg)) {
                    bgEt.setError("Enter Valid Blood Group\nChoose from "+valid_bg);
                    bgEt.requestFocus();
                    return;
                }
                password = passwordEt.getText().toString();
                if (password.isEmpty()) {
                    passwordEt.setError("Password is Required");
                    passwordEt.requestFocus();
                    return;
                }
                //Toast.makeText(RegisterActivity.this, name + '/' + city + '/' + number + '/' + password + '/' + bg, Toast.LENGTH_SHORT).show();
                register(name,city,bg,password,number);
            }
        });

    }
    private void register(final String name, final String city, final String bg, final String password, final String mobile){
        StringRequest stringRequest=new StringRequest(Method.POST, Endpoints.register_url, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("Invalid Credentials")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString("city", city).apply();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    SavedSharedPreferences.setUserName(getApplicationContext(),mobile);
                    RegisterActivity.this.finish();
                }else{
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("city", city);
                params.put("blood_group", bg);
                params.put("password", password);
                params.put("number", mobile);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
