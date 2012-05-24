package com.projectstalker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.projectstalker.util.ProjectStalkerService;

public class Login extends Activity {
    private EditText email, password;
    private Button loginButton, signupButton;
    private ProjectStalkerService projectStalkerService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        projectStalkerService = ProjectStalkerService.getInstance();

        email = (EditText)findViewById(R.id.login_email_text);
        password = (EditText)findViewById(R.id.login_password_text);

        loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (projectStalkerService.login(
                        email.getText().toString(),
                        password.getText().toString())) {
                    finish();
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    dialog.setTitle("Login Failed");
                    dialog.setMessage("Please check your email or password and try again.");
                    dialog.show();
                }
            }
        });

        signupButton = (Button)findViewById(R.id.login_signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(Login.this, SignUp.class));
            }
        });
    }
}