package com.projectstalker;

import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        MapView mapView = (MapView)findViewById(R.id.map_view);
        mapView.setBuiltInZoomControls(true);

        ProjectItemizedOverlay projectItemizedOverlay =
                new ProjectItemizedOverlay(
                        getResources().getDrawable(R.drawable.ic_tab_projects_grey), this);
        mapView.getOverlays().add(projectItemizedOverlay);

        GeoPoint p = mapView.getProjection().fromPixels(20, 20);
        projectItemizedOverlay.addOverlay(new OverlayItem(p, "Project A", "Blah blah blah"));
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}