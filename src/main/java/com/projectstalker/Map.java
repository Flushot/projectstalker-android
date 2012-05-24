package com.projectstalker;

import android.os.Bundle;
import com.google.android.maps.*;
import com.projectstalker.model.Project;
import com.projectstalker.util.ProjectStalkerService;
import com.projectstalker.util.ProjectsRefreshed;

import java.util.List;

public class Map extends MapActivity {
    private ProjectStalkerService projectStalkerService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        projectStalkerService = ProjectStalkerService.getInstance();

        final MapView mapView = (MapView)findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);

        final ProjectItemizedOverlay projectItemizedOverlay =
                new ProjectItemizedOverlay(
                        getResources().getDrawable(R.drawable.ic_tab_projects_grey), this);
        mapView.getOverlays().add(projectItemizedOverlay);

        projectStalkerService.subscribeToProjectsRefreshed(new ProjectsRefreshed() {
            @Override
            public void projectsRefreshed(final List<Project> projects) {
                // Update project list
                mapView.post(new Runnable() {
                    @Override
                    public void run() {
                        projectItemizedOverlay.setProjects(projects);

                        // Zoom map to the bounding box of the overlays
                        /*mapView.getController().zoomToSpan(
                                projectItemizedOverlay.getLatSpanE6(),
                                projectItemizedOverlay.getLonSpanE6());*/

                        mapView.invalidate();
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

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}