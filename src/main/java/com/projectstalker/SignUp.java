package com.projectstalker;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.projectstalker.util.ProjectStalkerService;
import com.projectstalker.util.StringUtils;

public class SignUp extends Activity {
    private EditText firstName, lastName, email, password, passwordConfirmation;
    private Button signupButton;
    private ProjectStalkerService projectStalkerService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        projectStalkerService = ProjectStalkerService.getInstance();

        firstName = (EditText)findViewById(R.id.signup_first_name_text);
        lastName = (EditText)findViewById(R.id.signup_last_name_text);
        email = (EditText)findViewById(R.id.signup_email_text);
        password = (EditText)findViewById(R.id.signup_password_text);
        passwordConfirmation = (EditText)findViewById(R.id.signup_password2_text);

        signupButton = (Button)findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signup()) {
                    finish();
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SignUp.this);
                    dialog.setTitle("Sign Up Failed");
                    dialog.setMessage("Something went wrong when registering your account.");
                    dialog.show();
                }
            }
        });
    }

    private boolean signup() {
        // TODO: Validate form fields
        String passwordVal = password.getText().toString(),
               passwordConfirmationVal = passwordConfirmation.getText().toString();
        //if (StringUtils.isBlank(passwordVal) || StringUtils.isBlank(passwordConfirmationVal))
        //

        // Sign up
        return projectStalkerService.signup(
                firstName.getText().toString(),
                lastName.getText().toString(),
                email.getText().toString(),
                passwordVal
        );
    }
}