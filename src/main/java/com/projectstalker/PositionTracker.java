package com.projectstalker;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.projectstalker.util.ProjectStalkerService;
import com.projectstalker.util.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class PositionTracker extends Service {
    private static final String TAG = PositionTracker.class.getName();

    private LocationManager locationManager;
    private LocationUpdateListener locationUpdateListener;
    private ProjectStalkerService projectStalkerService;
    //private PowerManager.WakeLock wakeLock;

    // Object that receives interaction from clients
    private final IBinder binder = new LocationBinder();
    public class LocationBinder extends Binder {
        PositionTracker getService() {
            return PositionTracker.this;
        }
    }

    private class LocationUpdateListener implements LocationListener {
        public void onLocationChanged(Location location) {
            //if (!location.hasAccuracy() || location.getAccuracy() <= 10 /*meters*/)
                projectStalkerService.updatePosition(PositionTracker.this, location);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        Log.d(TAG, "Initializing location service...");

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationUpdateListener = new LocationUpdateListener();
        projectStalkerService = ProjectStalkerService.getInstance();

        /*wakeLock = ((PowerManager)getSystemService(POWER_SERVICE))
                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ProjectStalkerWakeLock");*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(Intent intent, int startId) {
        Utils.toastAndDebugLog(this, TAG, "You are online");

        //Bundle extra = intent.getExtras();
        //sessionKey = extra.getString("sessionKey");

        // Ensure the device doesn't fully go to sleep and shut down CPU
        //wakeLock.acquire();

        // Immediately record last known location
        projectStalkerService.updatePosition(this,
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        // Subscribe to location updates
        Criteria criteria = new Criteria();
        //criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        locationManager.requestLocationUpdates(
                locationManager.getBestProvider(criteria, true),
                5 * 1000, // Minimum time interval (in milliseconds)
                5, // Minimum distance (in meters); 1 mile = 1609.344 meters
                locationUpdateListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        Utils.toastAndDebugLog(this, TAG, "You are offline");

        // Allow the device to fully sleep
        //wakeLock.release();

        // Unsubscribe from location updates
        locationManager.removeUpdates(locationUpdateListener);
    }
}
