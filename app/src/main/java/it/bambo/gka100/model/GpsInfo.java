package it.bambo.gka100.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

import it.bambo.gka100.Constants;

/**
 * Created by andreas on 05.04.2015.
 */
public class GpsInfo implements Serializable {

    private String name;
    private Date time;
    private int speed;
    private double lat;
    private double lng;
    private float alt;
    private int satelliteCount;

    public GpsInfo() {
    }

    public GpsInfo(String name, Date time, int speed, double lat, double lng, float alt, int satelliteCount) {
        this.name = name;
        this.time = time;
        this.speed = speed;
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
        this.satelliteCount = satelliteCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public float getAlt() {
        return alt;
    }

    public void setAlt(float alt) {
        this.alt = alt;
    }

    public int getSatelliteCount() {
        return satelliteCount;
    }

    public void setSatelliteCount(int satelliteCount) {
        this.satelliteCount = satelliteCount;
    }

    public String getSnippet() {
        return Constants.TIME_DATE_FORMAT.format(time) + "\n"
                + speed + "km/h\n"
                + alt + "m";
    }
}
