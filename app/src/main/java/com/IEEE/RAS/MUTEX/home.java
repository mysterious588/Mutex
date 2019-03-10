package com.IEEE.RAS.MUTEX;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private Button scanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        scanButton = findViewById(R.id.scanButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (ContextCompat.checkSelfPermission(home.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            if (ActivityCompat.shouldShowRequestPermissionRationale(home.this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(home.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(home.this, "NOT SCREW YOU", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the
                    Toast.makeText(home.this, "SCREW YOU", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this,MainActivity.class));
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="ds253468.mlab.com";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(home.this,response.substring(0,500),Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this,"Failed Bitch",Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
