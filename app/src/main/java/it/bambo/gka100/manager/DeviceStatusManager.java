package it.bambo.gka100.manager;

import android.content.SharedPreferences;

import java.text.ParseException;

import it.bambo.gka100.Constants;
import it.bambo.gka100.model.DeviceStatus;
import it.bambo.gka100.utils.StringUtils;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class DeviceStatusManager {

    private static DeviceStatusManager instance = new DeviceStatusManager();

    private DeviceStatusManager() {
    }

    public static DeviceStatusManager getInstance(){
        return instance;
    }

    public void handleResponse(String message, SharedPreferences preferences) {
        DeviceStatus values = parseResponse(message);
        saveResponse(preferences, values);
    }

    DeviceStatus parseResponse(String message) {
        DeviceStatus deviceStatus = new DeviceStatus();

        deviceStatus.setName(message.substring(0, message.indexOf(Constants.FIRMWARE_VERSION)).trim());
        int indexOfTime = message.indexOf(Constants.FIRMWARE_VERSION) + Constants.FIRMWARE_VERSION.length();
        try {
            deviceStatus.setTime(Constants.SHORT_TIME_DATE_FORMAT.parse(message.substring(indexOfTime, message.indexOf("Alarm:")).trim()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int index = message.indexOf("Alarm:") + "Alarm:".length();
        deviceStatus.setAlarm(message.substring(index, message.indexOf("GSM:")).trim().equals("on"));

        index = message.indexOf("GSM:") + "GSM:".length();
        deviceStatus.setGsm(Integer.valueOf(StringUtils.cutEnd(message.substring(index, message.indexOf("Accu:")).trim(), 1)));

        index = message.indexOf("Accu:") + "Accu:".length();
        deviceStatus.setAccu(Integer.valueOf(StringUtils.cutEnd(message.substring(index, message.indexOf("Area:")).trim(), 1)));

        index = message.indexOf("Area:") + "Area:".length();
        deviceStatus.setArea(message.substring(index, message.indexOf("Shock:")).trim().equals("on"));

        index = message.indexOf("Shock:") + "Shock:".length();
        deviceStatus.setShock(message.substring(index, message.indexOf("/10")).trim());

        index = message.indexOf("Volt.:") + "Volt.:".length();
        deviceStatus.setVolt(Float.valueOf(StringUtils.cutEnd(message.substring(index, message.indexOf("HoldAlarm:")).trim(), 1)));

        index = message.indexOf("HoldAlarm:") + "HoldAlarm:".length();
        deviceStatus.setHoldAlarm(message.substring(index, message.indexOf("IN1:")).trim().equals("on"));

        index = message.indexOf("IN1:") + "IN1:".length();
        deviceStatus.setIn1(message.substring(index, message.indexOf("IN2:")).trim().equals("high"));

        index = message.indexOf("IN2:") + "IN2:".length();
        deviceStatus.setIn2(message.substring(index, message.indexOf("OUT1:")).trim().equals("high"));
        
        index = message.indexOf("OUT1:") + "OUT1:".length();
        deviceStatus.setIn2(message.substring(index, message.indexOf("OUT2:")).trim().equals("on"));

        index = message.indexOf("OUT2:") + "OUT2:".length();
        deviceStatus.setIn2(message.substring(index, message.length() - 1).trim().equals("on"));

        return deviceStatus;
    }

    private void saveResponse(SharedPreferences preferences, DeviceStatus deviceStatus) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("deviceStatus_alarm", deviceStatus.isAlarm())
                .putLong("deviceStatus_time", deviceStatus.getTime().getTime())
                .putInt("deviceStatus_gsm", deviceStatus.getGsm())
                .putInt("deviceStatus_accu", deviceStatus.getAccu())
                .putBoolean("deviceStatus_area", deviceStatus.isArea())
                .putString("shock", deviceStatus.getShock())
                .putString("deviceStatus_shock", deviceStatus.getShock())
                .putFloat("deviceStatus_volt", deviceStatus.getVolt())
                .putBoolean("deviceStatus_holdAlarm", deviceStatus.isHoldAlarm())
                .putBoolean("deviceStatus_in1", deviceStatus.isIn1())
                .putBoolean("deviceStatus_in2", deviceStatus.isIn2())
                .putBoolean("deviceStatus_out1", deviceStatus.isOut1())
                .putBoolean("deviceStatus_out2", deviceStatus.isOut2())
                .putBoolean("device_changed_flag", true)
                .apply();
        if(!deviceStatus.isAlarm()) {
            editor.remove("alarm_released").apply();
        }
    }
}
