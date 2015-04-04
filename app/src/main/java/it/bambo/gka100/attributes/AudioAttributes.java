package it.bambo.gka100.attributes;

/**
 * Created by andreas on 04.04.2015.
 */
public enum AudioAttributes {
    SPEAKER("Speaker", "speakerVolume", "6"),
    MICROPHONE("Microphone", "micSensitivity", "6"),
    RINGTONE_MELODY("Ring melody", "ringtoneMelody", "7"),
    RINGTONE_VOLUME("Ring volume", "ringtoneVolume", "7"),
    ALARM_VOLUME("Alarm volume", "alarmVolume", "7"),
    CONFIRM_VOLUME("Confirm. volume", "remoteControlVolume", "5"),
    ;

    private String deviceKey;
    private String preferenceKey;
    private String defaultValue;

    AudioAttributes(String deviceKey, String preferenceKey, String defaultValue) {
        this.deviceKey = deviceKey;
        this.preferenceKey = preferenceKey;
        this.defaultValue = defaultValue;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
