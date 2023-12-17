package com.example.waterpollutionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText textInputEditTextUsername;
    private EditText textInputEditTextPassword;
    private Button loginButton;
    private Button registerUserButton;
    public static EditText userEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        textInputEditTextUsername = findViewById(R.id.textUsername);
        textInputEditTextPassword = findViewById(R.id.textPassword);
        loginButton = findViewById(R.id.button);
        registerUserButton = findViewById(R.id.button2);

        // Set userEditText to the reference of textInputEditTextUsername
        userEditText = textInputEditTextUsername;

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = textInputEditTextUsername.getText().toString();
                String password = textInputEditTextPassword.getText().toString();

                // Perform login by calling the BackgroundTask with "login" task
                BackgroundTask backgroundTask = new BackgroundTask(MainActivity.this);
                backgroundTask.execute("login", username, password);

                // Clear the input fields after the login button is clicked
                textInputEditTextUsername.setText("");
                textInputEditTextPassword.setText("");
            }
        });

        // Set up the register button click listener
        registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
