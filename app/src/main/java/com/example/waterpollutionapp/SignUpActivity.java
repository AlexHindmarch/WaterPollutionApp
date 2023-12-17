package com.example.waterpollutionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.waterpollutionapp.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUpActivity extends AppCompatActivity {

    EditText textInputEditTextFullname, textInputEditTextUsername, textInputEditTextPassword, textInputEditTextEmail; // Corrected view type
    Button buttonSignUp;
    ImageButton backButton;
    String userName, fullName, password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextFullname = findViewById(R.id.fullname); // Corrected view type
        textInputEditTextUsername = findViewById(R.id.username); // Corrected view type
        textInputEditTextPassword = findViewById(R.id.password); // Corrected view type
        textInputEditTextEmail = findViewById(R.id.email);       // Corrected view type
        buttonSignUp = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.imageButton3);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = textInputEditTextUsername.getText().toString();
                fullName = textInputEditTextFullname.getText().toString();
                email = textInputEditTextEmail.getText().toString();
                password = textInputEditTextPassword.getText().toString();
                String task = "register";
                BackgroundTask backgroundTask = new BackgroundTask(SignUpActivity.this);
                backgroundTask.execute(task,userName, fullName, email, password);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}