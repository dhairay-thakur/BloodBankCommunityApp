package com.thakursaab.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thakursaab.bloodbank.DataModels.RequestDataModel;
import com.thakursaab.bloodbank.R;
import com.thakursaab.bloodbank.Utils.Endpoints;
import com.thakursaab.bloodbank.Utils.SavedSharedPreferences;
import com.thakursaab.bloodbank.Utils.VolleySingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private List<String> valid_bg = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        valid_bg.addAll(Arrays.asList("O+","O-","A+","B+","A-","B-","AB+","AB-"));
        final EditText et_blood_group, et_city;
        et_blood_group = findViewById(R.id.et_blood_group);
        et_city = findViewById(R.id.et_city);
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bg = et_blood_group.getText().toString();
                String city = et_city.getText().toString().trim();
                if (bg.isEmpty() || !valid_bg.contains(bg)) {
                    et_blood_group.setError("Enter Valid Blood Group\nChoose from "+valid_bg);
                    et_blood_group.requestFocus();
                    return;
                }
                if (city.isEmpty()) {
                    et_city.setError("City is Required");
                    et_city.requestFocus();
                    return;
                }
                get_search_results(bg, city);
            }
        });
    }

    private void get_search_results(final String blood_group, final String city) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Endpoints.search_donors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //JSON response
                Intent intent = new Intent(SearchActivity.this, SearchResult.class);
                intent.putExtra("city", city);
                intent.putExtra("blood_group", blood_group);
                intent.putExtra("json", response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                params.put("blood_group", blood_group);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
