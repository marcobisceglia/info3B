package com.example.progettobarcaioli;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button login;
    Button register;
    EditText user;
    EditText psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.logInButton);
        register = findViewById(R.id.registrerButton);
        user = findViewById(R.id.userTextView);
        psw = findViewById(R.id.PswTextView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString() == "Giulia" && psw.getText().toString() == "Giulia"){
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(), OwnerActivity.class);
                    startActivity(intent);
                }



            }
        });


    }
}