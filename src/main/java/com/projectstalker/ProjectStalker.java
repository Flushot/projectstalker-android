package com.projectstalker;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class ProjectStalker extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        TabHost tabHost = getTabHost();

        // 0 - Projects tab
        tabHost.addTab(tabHost
                .newTabSpec("projects")
                .setIndicator("Projects", res.getDrawable(R.drawable.ic_tab_projects))
                .setContent(new Intent().setClass(this, ProjectList.class)));

        // 1 - Map tab
        tabHost.addTab(tabHost
                .newTabSpec("map")
                .setIndicator("Map", res.getDrawable(R.drawable.ic_tab_projects))
                .setContent(new Intent().setClass(this, Map.class)));

        // Default tab
        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_project:
                startActivity(new Intent().setClass(this, AddProject.class));
                return true;
            case R.id.toggle_online:
                toggleOnline();
                return true;
            default:
                return false;
        }
    }

    private void toggleOnline() {
        Intent intent = new Intent(this, PositionTracker.class)
                    .putExtra("sessionKey", "blah");

        if (startService(intent) == null) // Service already running
            stopService(intent);
    }
}
