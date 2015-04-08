package it.bambo.gka100.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;

import it.bambo.gka100.MainActivity;
import it.bambo.gka100.model.GpsInfo;
import it.bambo.gka100.service.AudioService;
import it.bambo.gka100.service.DeviceStatusService;
import it.bambo.gka100.service.GpsService;
import it.bambo.gka100.service.PhoneBookService;

/**
 * Created by andreas on 23.03.14.
 */
public class SMSReceiver extends BroadcastReceiver {

    private AudioService audioService = AudioService.getInstance();
    private GpsService gpsService = GpsService.getInstance();
    private PhoneBookService phoneBookService = PhoneBookService.getInstance();
    private DeviceStatusService deviceStatusService = DeviceStatusService.getInstance();

    private MainActivity mainActivityHandler;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            SharedPreferences preferences = context.getSharedPreferences("it.bambo.gka100_preferences", Context.MODE_PRIVATE);
            String phoneNumber = preferences.getString("phoneNumber", "");

            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            if(messages.length == 1) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) messages[0]);
                if(phoneNumber.equals(smsMessage.getDisplayOriginatingAddress())) {
                    toastMessage(context, smsMessage);

                    String message = smsMessage.getDisplayMessageBody();
                    Log.i("SMSReceiver", message);
                    if(message.contains("Speaker")) {
                        audioService.handleResponse(message, preferences);
                    } else if(message.contains("Latitude:") && message.contains("Longitude:") && message.contains("Speed:")) {
                        try {
                            GpsInfo gpsInfo = gpsService.handleResponse(message, preferences);
                            mainActivityHandler.updateGpsInfo(gpsInfo);
                        } catch (ParseException e) {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                        }
                    } else if(message.contains("SMS1") && message.contains("SMS2") && message.contains("SMS3")) {
                        phoneBookService.handleResponse(message, preferences);
                    } else if(message.contains("Alarm:")) {
                        deviceStatusService.handleResponse(message, preferences);
                    }
                }
            }
        }
    }

    private void toastMessage(Context context, SmsMessage smsMessage) {
        String toast = "Received SMS from: " + smsMessage.getDisplayOriginatingAddress();
        toast += "\nMessage: " + smsMessage.getDisplayMessageBody();
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

    public void setMainActivityHandler(MainActivity mainActivityHandler) {
        this.mainActivityHandler = mainActivityHandler;
    }

}
