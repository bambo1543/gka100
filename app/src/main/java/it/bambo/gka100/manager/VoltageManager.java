package it.bambo.gka100.manager;

import android.content.SharedPreferences;

import it.bambo.gka100.utils.StringUtils;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class VoltageManager {

    private static VoltageManager instance = new VoltageManager();

    private VoltageManager() {
    }

    public static VoltageManager getInstance(){
        return instance;
    }

    public void handleResponse(String message, SharedPreferences preferences) {
        String[] values = parseResponse(message);
        saveResponse(preferences, values);
    }

    String[] parseResponse(String message) {
        String[] values = new String[1];
        int index = message.indexOf("Min. voltage:") + "Min. voltage:".length();
        values[0] = message.substring(index, message.indexOf("HYS. VOLT:")).trim();
        if(values[0].endsWith("V")) {
            values[0] = StringUtils.cutEnd(values[0], 1);
        }

        return values;
    }

    private void saveResponse(SharedPreferences preferences, String[] values) {
        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("minVoltage", Float.valueOf(values[0]))
        editor.putString("minVoltage", values[0]).apply();
    }

}
