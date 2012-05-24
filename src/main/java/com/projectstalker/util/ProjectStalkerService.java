package com.projectstalker.util;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import com.projectstalker.model.Project;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class ProjectStalkerService {
    private static final String BASE_HOST = "192.168.1.3";
    private static final String BASE_URL = "http://" + BASE_HOST + ":3000";

    private HttpClient client;
    private HttpContext localContext;
    private CookieStore cookieStore;

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;
    private JsonParser jsonParser;

    private boolean loggedIn;
    private Timer projectsRefreshTimer;
    private List<ProjectsRefreshed> projectsRefreshedSubscribers;

    public ProjectStalkerService() {
        // Init HTTP client
        client = new DefaultHttpClient();
        localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        // Init JSON
        objectMapper = new ObjectMapper();
        jsonFactory = objectMapper.getJsonFactory();
        try {
            jsonParser = jsonFactory.createJsonParser("{\"k1\":\"v1\"}");
        }
        catch (IOException ex) {
            throw new RuntimeException("Error creating JSON parser: " + ex.getMessage(), ex);
        }

        projectsRefreshedSubscribers = new LinkedList<ProjectsRefreshed>();

        // Start timer to refresh project list
        projectsRefreshTimer = new Timer();
        projectsRefreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    refreshProjects();
                } catch (RuntimeException ex) {
                    Log.e("ProjectList", "Error refreshing projects: " + ex.getMessage());
                }
            }
        }, 0, 5 * 1000);
    }

    private static ProjectStalkerService instance;
    public static ProjectStalkerService getInstance() {
        if (instance == null)
            instance = new ProjectStalkerService();

        return instance;
    }

    public synchronized boolean signup(String firstName, String lastName, String email, String password) {
        return false;
    }

    public synchronized boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Logs in to server.
     *
     * @param email the user's email address.
     * @param password the user's password.
     * @return whether or not login succeeded.
     */
    public synchronized boolean login(String email, String password) {
        try {
            HttpPost request = new HttpPost(BASE_URL + "/sessions");
            request.addHeader("Host", BASE_HOST);
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");

            String body = new JSONObject()
                            .put("email", email)
                            .put("password", password).toString();
            request.setEntity(new StringEntity(body, "UTF-8"));

            //DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request, localContext);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                loggedIn = true;
                return true;
            }

            // TODO: Log login failure from statusLine.getStatusCode() and statusLine.getReasonPhrase()
        }
        catch (JSONException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        catch (IOException ex) {
            // TODO: Log I/O error instead
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return false;
    }

    /**
     * Logs out of server.
     *
     * @return whether or not logout succeeded.
     */
    public synchronized boolean logout() {
        try {
            HttpDelete request = new HttpDelete(BASE_URL + "/sessions/current");
            request.addHeader("Host", BASE_HOST);

            //DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request, localContext);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                loggedIn = false;
                return true;
            }
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
    public synchronized boolean updatePosition(Context context, Location location) {
        if (location == null || !loggedIn)
            return false;

        try {
            HttpPost request = new HttpPost(BASE_URL + "/positions");
            request.addHeader("Host", BASE_HOST);
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");

            String body = new JSONObject()
                            .put("device", Utils.getDeviceId(context))
                            .put("latitude", location.getLatitude())
                            .put("longitude", location.getLongitude())
                            .put("accuracy", location.hasAccuracy() ? location.getAccuracy() : -1).toString();
            request.setEntity(new StringEntity(body, "UTF-8"));

            //DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request, localContext);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200)
                return true;
        }
        catch (JSONException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        catch (IOException ex) {
            // TODO: Log I/O error instead
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return false;
    }

    public synchronized void subscribeToProjectsRefreshed(ProjectsRefreshed event) {
        projectsRefreshedSubscribers.add(event);
    }

    public synchronized List<Project> getProjects() {
        List<Project> projects = new ArrayList<Project>();
        if (!loggedIn)
            return projects;

        try {
            HttpGet request = new HttpGet(BASE_URL + "/projects");
            request.addHeader("Host", BASE_HOST);
            request.addHeader("Accept", "application/json");

            HttpResponse response = client.execute(request, localContext);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.getEntity().getContent());
                for (JsonNode node : root.get("projects")) {
                    Project project = new Project();
                    project.setId(node.get("id").asInt());
                    project.setSummary(node.get("summary").asText());
                    project.setDistance(node.get("distance").asDouble());
                    project.setLatitude(node.get("latitude").asDouble());
                    project.setLongitude(node.get("longitude").asDouble());
                    project.setFollowCount(node.get("follow_count").asInt());
                    projects.add(project);
                }
            }
        }
        catch (IOException ex) {
            // TODO: Log I/O error instead
            throw new RuntimeException(ex.getMessage(), ex);
        }

        // Notify subscribers
        for (ProjectsRefreshed subscriber : projectsRefreshedSubscribers)
            subscriber.projectsRefreshed(projects);

        return projects;
    }

    public void refreshProjects() {
        // TODO: Reset timer
        getProjects();
    }
}
