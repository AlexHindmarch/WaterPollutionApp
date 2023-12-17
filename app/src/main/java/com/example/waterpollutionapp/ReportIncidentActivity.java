package com.example.waterpollutionapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import com.example.waterpollutionapp.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReportIncidentActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener    {

    private Spinner spinner;
    private Button submitButton;

    private ImageButton backButton;
    private Button locationButton;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView longitude, latitude, address;
    private final static int REQUEST_CODE=100;
    EditText textInputEditTextUsername, textInputEditTextAdditional, textInputEditTextType, textInputEditTextLatitude, textInputEditTextLongitude;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_incident);


        backButton = findViewById(R.id.imageButton2);
        locationButton = findViewById(R.id.locationButton);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);

        // Recognise logged in user opening activity
        textInputEditTextUsername = MainActivity.userEditText;

        // Initialise fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the address TextView
        address = findViewById(R.id.address);


        // Initialise the report fields
        textInputEditTextAdditional = findViewById(R.id.textAdditional);
        textInputEditTextType = findViewById(R.id.type);



                // Initialize Submit Button
                submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedIncident = spinner.getSelectedItem().toString();
                Toast.makeText(ReportIncidentActivity.this, "Selected incident: " + selectedIncident, Toast.LENGTH_SHORT).show();
            }
        });

        // Set a listener for the return button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });
        // Set a listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDetails();
                openCompletedReportActivity();
            }
        });
        // Set a listener for the get location button
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
    }
    public void openHomeActivity()  {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void openCompletedReportActivity()  {
        Intent intent = new Intent(this, CompletedReportActivity.class);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedIncident = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "Selected incident: " + selectedIncident, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(ReportIncidentActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    latitude.setText("Latitude :" + addresses.get(0).getLatitude());
                                    longitude.setText("Longitude :" + addresses.get(0).getLongitude());
                                    address.setText("Address :" + addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else  {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(ReportIncidentActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void submitDetails()    {
        if (address == null)    {
            Toast.makeText(ReportIncidentActivity.this, "Please provide location", Toast.LENGTH_SHORT).show();

        }else {
            String userName, additional, type=null, latitude, longitude;;
            userName = String.valueOf(MainActivity.userEditText);
            additional = String.valueOf(findViewById(R.id.textAdditional));
            latitude = String.valueOf(findViewById(R.id.latitude));
            longitude = String.valueOf(findViewById(R.id.longitude));

            // Creating array for parameters
            String[] field = new String[5];
            //Param 0 is automaticaly incremented id number
            field[1] = "userName";
            field[2] = "type";
            field[3] = "latitude";
            field[4] = "longitude";
            field[5] = "additional";
            // Creating array for the data
            String[] data = new String[5];
            //Param 0 is automaticaly incremented id number
            data[1] = userName;
            data[2] = type;
            data[3] = latitude;
            data[4] = longitude;
            data[5] = additional;
            // Put data into database
            PutData putData = new PutData("http://alexhindmarch.com/Incident-report.php", "POST", field, data);
            if(putData.startPut())  {
                if(putData.onComplete())    {
                    String result = putData.getResult();
                    if(result.equals("Sign Up Success"))    {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}



