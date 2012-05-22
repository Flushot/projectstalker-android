package com.projectstalker.util;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ProjectStalkerService {
    private static final String BASE_HOST = "192.168.1.3";
    private static final String BASE_URL = "http://" + BASE_HOST + ":3000";

    private String sessionKey;

    /**
     * Logs in to server.
     *
     * @param email the user's email address.
     * @param password the user's password.
     * @return whether or not login succeeded.
     */
    public boolean login(String email, String password) {
        try {
            HttpPost post = new HttpPost(BASE_URL + "/sessions");
            post.addHeader("Host", BASE_HOST);
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/json");

            String body = new JSONObject()
                            .put("email", email)
                            .put("password", password).toString();
            post.setEntity(new StringEntity(body, "UTF-8"));

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                // TODO: Get _session_id cookie
                String cookie = response.getHeaders("Set-Cookie")[0].getValue();
                return true;
            }

            // TODO: Log login failure from statusLine.getStatusCode() and statusLine.getReasonPhrase()
        }
        catch (JSONException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        catch (IOException ex) {
            // TODO: Log I/O error instead
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return false;
    }

    /**
     * Updates the user's GPS position.
     *
     * @param context the context of the caller.
     * @param location the GPS Location.
     * @return whether or not the update succeeded.
     */
    private boolean updatePosition(Context context, Location location) {
        if (location == null || sessionKey == null)
            return false;

        try {
            HttpPost post = new HttpPost(BASE_URL + "/positions");
            post.addHeader("Host", BASE_HOST);
            post.addHeader("Accept", "application/json");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Cookie", "_session_id=" + sessionKey);

            String body = new JSONObject()
                            .put("device", Utils.getDeviceId(context))
                            .put("latitude", location.getLatitude())
                            .put("longitude", location.getLongitude())
                            .put("accuracy", location.hasAccuracy() ? location.getAccuracy() : -1).toString();
            post.setEntity(new StringEntity(body, "UTF-8"));

            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200)
                return true;
        }
        catch (JSONException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        catch (IOException ex) {
            // TODO: Log I/O error instead
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return false;
    }
}
