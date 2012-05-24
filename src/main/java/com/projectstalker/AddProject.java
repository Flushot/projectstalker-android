package com.projectstalker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddProject extends Activity {
    private Button addProjectButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);

        // Add project
        addProjectButton = (Button)findViewById(R.id.add_project_button);
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveProject();
            }
        });
    }

    private void saveProject() {

    }
}