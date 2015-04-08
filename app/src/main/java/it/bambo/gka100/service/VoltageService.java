package it.bambo.gka100.service;

import android.content.SharedPreferences;

import it.bambo.gka100.utils.StringUtils;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class VoltageService {

    private static VoltageService instance = new VoltageService();

    private VoltageService() {
    }

    public static VoltageService getInstance(){
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
