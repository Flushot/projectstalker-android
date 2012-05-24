package com.projectstalker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.projectstalker.model.Project;
import com.projectstalker.util.ProjectStalkerService;
import com.projectstalker.util.Utils;

public class AddProject extends Activity {
    private EditText description;
    private Button addProjectButton;

    private ProjectStalkerService projectStalkerService;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);

        projectStalkerService = ProjectStalkerService.getInstance();

        description = (EditText)findViewById(R.id.add_project_description);

        // Add project
        addProjectButton = (Button)findViewById(R.id.add_project_button);
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveProject();
            }
        });
    }

    private void saveProject() {
        Project project = new Project();
        project.setSummary(description.getText().toString());

        // TODO: Validate form

        if (projectStalkerService.addProject(project)) {
            finish();
        }
        else {
            Utils.alert(AddProject.this,
                    "Add Project Failed",
                    "Something went wrong when adding your project.");
        }
    }
}