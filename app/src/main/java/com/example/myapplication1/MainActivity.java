package com.example.myapplication1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private  static String TAG = "MainActivity";
    private  ActivityMainBinding variableBinding;


    @Override
    protected void onPause() {
        super.onPause();
        Log.w( TAG, "The application no longer responds to user input.");


        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = prefs.edit();

        String emailAddress = variableBinding.emailedit.getText().toString();
        edit.putString("LoginName",  emailAddress );

        edit.putFloat("Hi",4.5f);
        edit.putInt("Age",35);


        edit.apply();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( TAG, "Any memory used by the application is freed.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( TAG, "The application is no longer visible.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( TAG, "The application is now responding to user input.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( TAG, "The application is now visible on screen.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        Log.w( TAG, "In onCreate() - Loading Widgets");



       variableBinding.loginButton.setOnClickListener(clk->{
           Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
           nextPage.putExtra("emailedit",variableBinding.emailedit.getText().toString());




           startActivity(nextPage);

       });

    }
}