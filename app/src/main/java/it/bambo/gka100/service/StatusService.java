package it.bambo.gka100.service;

import android.content.SharedPreferences;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class StatusService {

    private static StatusService instance = new StatusService();

    private StatusService() {
    }

    public static StatusService getInstance(){
        return instance;
    }

    public void handleResponse(String message, SharedPreferences preferences) {
        String[] values = parseResponse(message);
        saveResponse(preferences, values);
    }

    private void saveResponse(SharedPreferences preferences, String[] values) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("speakerVolume", values[0]);
        editor.putString("micSensitivity", values[1]);
        editor.putString("ringtoneMelody", values[2]);
        editor.putString("ringtoneVolume", values[3]);
        editor.putString("alarmVolume", values[4]);
        editor.putString("remoteControlVolume", values[5]).apply();
    }

    String[] parseResponse(String message) {
        String[] values = new String[11];
        int indexOfSpeaker = message.indexOf("Speaker:") + "Speaker:".length();
        values[0] = message.substring(indexOfSpeaker + 1, indexOfSpeaker + 2 );

        int indexOfMicrophone = message.indexOf("Microphone:") + "Microphone:".length();
        values[1] = message.substring(indexOfMicrophone + 1, indexOfMicrophone + 2 );

        int indexOfRingMelody = message.indexOf("Ring melody:") + "Ring melody:".length();
        values[2] = message.substring(indexOfRingMelody + 1, indexOfRingMelody + 2 );

        int indexOfRingVolume = message.indexOf("Ring volume:") + "Ring volume:".length();
        values[3] = message.substring(indexOfRingVolume + 1, indexOfRingVolume + 2 );

        int indexOfAlarmVolume = message.indexOf("Alarm volume:") + "Alarm volume:".length();
        values[4] = message.substring(indexOfAlarmVolume + 1, indexOfAlarmVolume + 2 );

        int indexOfConfirmVolume = message.indexOf("Confirm. volume:") + "Confirm. volume:".length();
        values[5] = message.substring(indexOfConfirmVolume + 1, indexOfConfirmVolume + 2 );

        return values;
    }

}
