package it.bambo.gka100.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by andreas on 06.04.2015.
 */
public class MapViewCamera implements Serializable {

    private double lat;
    private double lng;
    private float zoom;
    private float tilt;
    private float bearing;

    public MapViewCamera() {
    }

    public MapViewCamera(double lat, double lng, float zoom, float tilt, float bearing) {
        this.lat = lat;
        this.lng = lng;
        this.zoom = zoom;
        this.tilt = tilt;
        this.bearing = bearing;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getTilt() {
        return tilt;
    }

    public void setTilt(float tilt) {
        this.tilt = tilt;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }
}
