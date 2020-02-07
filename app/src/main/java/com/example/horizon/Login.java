package com.example.horizon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class Login extends AppCompatActivity {
    private Button loginButton;
    private Button createButton;
    private EditText enterEmail;
    private EditText enterPassword;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.sign_in_button);
        createButton = findViewById(R.id.create_account_button);
        enterEmail = findViewById(R.id.email);
        enterPassword = findViewById(R.id.password);


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, CreateAccount.class));
            }
        });


    }
}
