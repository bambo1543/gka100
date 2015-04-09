package it.bambo.gka100.manager;

import android.content.SharedPreferences;

import java.util.Date;

import it.bambo.gka100.utils.StringUtils;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class AlarmManager implements IManager {

    public static AlarmManager instance = new AlarmManager();

    static {
        instance = new AlarmManager();
    }

    private AlarmManager() {}

    public boolean isResponsibleForMessage(String message) {
        return message.contains("ALARM:");
    }

    public void handleResponse(String message, SharedPreferences preferences) {
        String[] values = parseResponse(message);
        saveResponse(preferences, values);
    }

    String[] parseResponse(String message) {
        String[] values = new String[2];

        int index = message.indexOf("Lat:") + "Lat:".length();
        values[0] = StringUtils.cutEnd(message.substring(index, message.indexOf("Long:")).trim(), 1);

        index = message.indexOf("Long:") + "Long:".length();
        values[1] = StringUtils.cutEnd(message.substring(index, message.length()).trim(), 1);

        return values;
    }

    private void saveResponse(SharedPreferences preferences, String[] values) {
        SharedPreferences.Editor editor = preferences.edit();
        long time = new Date().getTime();
        editor.putLong("alarm_released", time)
            .putLong("deviceStatus_alarmReleased", time)
            .putFloat("gps_lat", Double.valueOf(values[0]).floatValue())
            .putFloat("gps_lng", Double.valueOf(values[1]).floatValue())
            .putBoolean("device_changed_flag", true)
                .apply();
    }

}
