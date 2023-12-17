package com.example.waterpollutionapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button reportButton;
    private Button mapButton;
    private Button educationButton;
    private ImageButton backButton;
    TextView name, full;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the views
        reportButton = findViewById(R.id.button3);
        mapButton = findViewById(R.id.button4);
        educationButton = findViewById(R.id.button5);
        backButton = findViewById(R.id.imageButton);

        // Display name of logged in user
        name = (TextView) findViewById(R.id.textUser);
        full = (TextView) findViewById(R.id.textFull);

        preferences = this.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE);

        String mName = preferences.getString("name","Error getting name");

        // Set a listener for the return button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        // Set event listener for the MAP button
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });
        // Set event listener for the report button
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReportIncidentActivity();
            }
        });
    }
    public void openMainActivity()  {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    public void openMapsActivity()  {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openReportIncidentActivity()  {
        Intent intent = new Intent(this, ReportIncidentActivity.class);
        startActivity(intent);
    }

}
