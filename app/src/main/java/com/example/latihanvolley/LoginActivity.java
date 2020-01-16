package com.example.latihanvolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.latihanvolley.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper helper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setUser(new User());
        helper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = binding.getUser();
                if(user.getUsername() == null || user.getUsername().equals("") || user.getPassword() == null || user.getPassword().equals("")) {
                    Toast.makeText(LoginActivity.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(helper.loginUser(user)) {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        sessionManager.createLoginSession(user.getUsername());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                    else Toast.makeText(LoginActivity.this, "Something Error when login", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnMoveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
