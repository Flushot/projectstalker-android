package com.projectstalker.model;

public class Project {
    private int id;
    private String summary;
    private double distance;
    private double latitude;
    private double longitude;
    private int followCount;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFollowCount() {
        return followCount;
    }
    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
