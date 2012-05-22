package com.projectstalker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectDetails extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_details);

        String projectId = getIntent().getExtras().getString("projectId");

        TextView projectIdView = (TextView)findViewById(R.id.project_id_text);
        projectIdView.setText(projectId);
    }
}