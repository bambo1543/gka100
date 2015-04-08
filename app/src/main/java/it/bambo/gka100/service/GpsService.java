package it.bambo.gka100.service;

import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.Date;

import it.bambo.gka100.Constants;
import it.bambo.gka100.model.GpsInfo;
import it.bambo.gka100.utils.StringUtils;

/**
 * Created by andreas on 05.04.2015.
 */
public class GpsService {

    private static GpsService instance = new GpsService();

    private GpsService() {
    }

    public static GpsService getInstance(){
        return instance;
    }

    public GpsInfo handleResponse(String message, SharedPreferences preferences) throws ParseException {
        GpsInfo gpsInfo = parseResponse(message);
        saveResponse(preferences, gpsInfo);
        return gpsInfo;
    }

    GpsInfo parseResponse(String message) {
        GpsInfo gpsInfo = new GpsInfo();

        gpsInfo.setName(message.substring(0, message.indexOf(Constants.FIRMWARE_VERSION)).trim());

        int indexOfTime = message.indexOf("Time:") + "Time:".length();
        try {
            String date = Constants.DATE_FORMAT.format(new Date());
            gpsInfo.setTime(Constants.TIME_DATE_FORMAT.parse(message.substring(indexOfTime, message.indexOf("Speed:")).trim() + " " + date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int indexOfSpeed = message.indexOf("Speed:") + "Speed:".length();
        gpsInfo.setSpeed(Integer.valueOf(message.substring(indexOfSpeed, message.indexOf("km/h")).trim()));

        int indexOfLat = message.indexOf("Latitude:") + "Latitude:".length();
        String lat = StringUtils.cutEnd(message.substring(indexOfLat, message.indexOf("Longitude:")).trim(), 1);
        int indexOfLng = message.indexOf("Longitude:") + "Longitude:".length();
        String lng = StringUtils.cutEnd(message.substring(indexOfLng, message.indexOf("Altitude:")).trim(), 1);
        gpsInfo.setLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

        int indexOfAlt = message.indexOf("Altitude:") + "Altitude:".length();
        gpsInfo.setAlt(Float.valueOf(StringUtils.cutEnd(message.substring(indexOfAlt, message.indexOf("Sat. in used:")).trim(), 1)));

        int indexOfSat = message.indexOf("Sat. in used:") + "Sat. in used:".length();
        gpsInfo.setSatelliteCount(Integer.valueOf(message.substring(indexOfSat).trim()));

        return gpsInfo;
    }


    private void saveResponse(SharedPreferences preferences, GpsInfo gpsInfo) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("gps_name", gpsInfo.getName());
        editor.putLong("gps_time", gpsInfo.getTime().getTime());
        editor.putInt("gps_speed", gpsInfo.getSpeed());
        editor.putFloat("gps_lat", Double.valueOf(gpsInfo.getLatLng().latitude).floatValue());
        editor.putFloat("gps_lng", Double.valueOf(gpsInfo.getLatLng().longitude).floatValue());
        editor.putFloat("gps_alt", gpsInfo.getAlt());
        editor.putInt("gps_sat_count", gpsInfo.getSatelliteCount()).apply();
    }
}
