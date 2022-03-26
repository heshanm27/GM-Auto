package com.example.gmauto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelomeScreen extends AppCompatActivity {

    TextView login;
    Button createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welome_screen);

        login = findViewById(R.id.loginText);
        createAccount=findViewById(R.id.createaccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dash = new Intent(WelomeScreen.this,login.class);
                startActivity(dash);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createaccount = new Intent(WelomeScreen.this,signup.class);
                startActivity(createaccount);
            }
        });
    }
}