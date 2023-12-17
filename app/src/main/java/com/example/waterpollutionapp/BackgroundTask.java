package com.example.waterpollutionapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public BackgroundTask(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        preferences = context.getSharedPreferences("MYPREFS", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("flag", "0");
        editor.apply();

        String urlRegistration = "http://alexhindmarch.com/LoginAndRegister-register.php";
        String urlLogin = "http://alexhindmarch.com/LoginAndRegister-login.php";

        String task = params[0];
        String result = null;

        if (task.equals("register")) {
            String regUserName = params[1];
            String regFullName = params[2];
            String regEmail = params[3];
            String regPassword = params[4];

            try {
                URL url = new URL(urlRegistration);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("identifier_userName", "UTF-8") + "=" + URLEncoder.encode(regUserName, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_fullName", "UTF-8") + "=" + URLEncoder.encode(regFullName, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_email", "UTF-8") + "=" + URLEncoder.encode(regEmail, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_password", "UTF-8") + "=" + URLEncoder.encode(regPassword, "UTF-8");
                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                editor.putString("flag", "register");
                editor.apply();
                result = "Successfully Registered " + regUserName;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (task.equals("login")) {
            String loginUser = params[1];
            String loginPassword = params[2];

            try {
                URL url = new URL(urlLogin);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String myData = URLEncoder.encode("identifier_loginUserName", "UTF-8") + "=" + URLEncoder.encode(loginUser, "UTF-8") + "&"
                        + URLEncoder.encode("identifier_loginPassword", "UTF-8") + "=" + URLEncoder.encode(loginPassword, "UTF-8");
                bufferedWriter.write(myData);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder dataResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    dataResponse.append(inputLine);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                editor.putString("flag", "login");
                editor.apply();
                result = dataResponse.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        String flag = preferences.getString("flag", "0");

        if (flag.equals("register")) {
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        } else if (flag.equals("login")) {
            if (s != null && !s.isEmpty()) {
                String test = "false";
                String name = "";
                String email = "";
                String[] serverResponse = s.split("[,]");
                if (serverResponse.length >= 4) {
                    test = serverResponse[0];
                    name = serverResponse[1];
                    email = serverResponse[3];
                }

                if (test.equals("true")) {
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.apply();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else {
                    display("Login Failed...", "That username and password do not match our records.");
                }
            } else {
                display("Login Failed...", "Something went wrong.");
            }
        } else {
            display("Error", "Unknown task.");
        }
    }

    private void display(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}