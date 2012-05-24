package com.projectstalker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.projectstalker.model.Project;
import com.projectstalker.util.LazyAdapter;
import com.projectstalker.util.ProjectStalkerService;
import com.projectstalker.util.ProjectsRefreshed;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectList extends ListActivity {
    private Timer refreshTimer;

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;
    private ProjectStalkerService projectStalkerService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectStalkerService = ProjectStalkerService.getInstance();

        items = new ArrayList<String>();
        items.add("First project");

        adapter = new ArrayAdapter<String>(this, R.layout.project_list_item, items);

        //setListAdapter(new LazyAdapter(this, mStrings));
        setListAdapter(adapter);

        ListView listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: Show selected project
                showProject(position);
            }
        });

        projectStalkerService.subscribeToProjectsRefreshed(new ProjectsRefreshed() {
            @Override
            public void projectsRefreshed(final List<Project> projects) {
                // Update project list
                getListView().post(new Runnable() {
                    @Override
                    public void run() {
                        items.clear();
                        for (Project project : projects) {
                            items.add(String.format("%s (d=%.4f, lat=%.6f, lng=%.6f)",
                                    project.getSummary(),
                                    project.getDistance(),
                                    project.getLatitude(),
                                    project.getLongitude()
                            ));
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        projectStalkerService.refreshProjects();
    }

    private void showProject(int position) {
        /*startActivity(new Intent(this, ProjectDetails.class)
                .putExtra("project", mStrings[position]));*/
    }
}