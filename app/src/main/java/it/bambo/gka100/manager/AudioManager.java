package it.bambo.gka100.manager;

import android.content.SharedPreferences;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class AudioManager implements IManager {

    public static AudioManager instance;

    static {
        instance = new AudioManager();
    }

    private AudioManager() {}

    public boolean isResponsibleForMessage(String message) {
        return message.contains("Speaker:");
    }

    public void handleResponse(String message, SharedPreferences preferences) {
        String[] values = parseResponse(message);
        saveResponse(preferences, values);
    }

    String[] parseResponse(String message) {
        String[] values = new String[6];
        int index = message.indexOf("Speaker:") + "Speaker:".length();
        values[0] = message.substring(index + 1, index + 2 );

        index = message.indexOf("Microphone:") + "Microphone:".length();
        values[1] = message.substring(index + 1, index + 2 );

        index = message.indexOf("Ring melody:") + "Ring melody:".length();
        values[2] = message.substring(index + 1, index + 2 );

        index = message.indexOf("Ring volume:") + "Ring volume:".length();
        values[3] = message.substring(index + 1, index + 2 );

        index = message.indexOf("Alarm volume:") + "Alarm volume:".length();
        values[4] = message.substring(index + 1, index + 2 );

        index = message.indexOf("Confirm. volume:") + "Confirm. volume:".length();
        values[5] = message.substring(index + 1, index + 2 );

        return values;
    }

    private void saveResponse(SharedPreferences preferences, String[] values) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("speakerVolume", values[0])
        .putString("micSensitivity", values[1])
        .putString("ringtoneMelody", values[2])
        .putString("ringtoneVolume", values[3])
        .putString("alarmVolume", values[4])
        .putString("remoteControlVolume", values[5])
                .apply();
    }

}
