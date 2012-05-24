package com.projectstalker;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.projectstalker.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectItemizedOverlay extends ItemizedOverlay {
    private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
    private Context context;

    public ProjectItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }
    public ProjectItemizedOverlay(Drawable defaultMarker, Context context) {
        this(defaultMarker);
        this.context = context;
    }

    public void clearOverlays() {
        clearOverlays(true);
    }
    public void clearOverlays(boolean populate) {
        overlays.clear();

        if (populate)
            populate();
    }

    public void addProject(Project project) {
        addProject(project, true);
    }
    private void addProject(Project project, boolean populate) {
        overlays.add(
                new OverlayItem(
                        new GeoPoint(
                                (int)(project.getLatitude() * 1e6),
                                (int)(project.getLongitude() * 1e6)),
                        String.format("Project - %.4f miles", project.getDistance()),
                        project.getSummary()));

        if (populate)
            populate();
    }

    public void setProjects(List<Project> projects) {
        clearOverlays(false);
        for (Project project : projects)
            addProject(project, false);

        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlays.get(i);
    }

    @Override
    public int size() {
        return overlays.size();
    }

    @Override
    protected boolean onTap(int i) {
        OverlayItem item = overlays.get(i);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }
}
