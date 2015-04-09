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

import it.bambo.gka100.manager.AlarmManager;
import it.bambo.gka100.manager.AudioManager;
import it.bambo.gka100.manager.DeviceStatusManager;
import it.bambo.gka100.manager.GpsManager;
import it.bambo.gka100.manager.PhoneBookManager;
import it.bambo.gka100.manager.VoltageManager;

/**
 * Created by andreas on 23.03.14.
 */
public class SMSReceiver extends BroadcastReceiver {

    private AudioManager audioManager = AudioManager.getInstance();
    private GpsManager gpsManager = GpsManager.getInstance();
    private PhoneBookManager phoneBookManager = PhoneBookManager.getInstance();
    private DeviceStatusManager deviceStatusManager = DeviceStatusManager.getInstance();
    private VoltageManager voltageManager = VoltageManager.getInstance();
    private AlarmManager alarmManager = AlarmManager.getInstance();

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
                        audioManager.handleResponse(message, preferences);
                    } else if(message.contains("Latitude:") && message.contains("Longitude:") && message.contains("Speed:")) {
                        try {
                            gpsManager.handleResponse(message, preferences);
                        } catch (ParseException e) {
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG);
                        }
                    } else if(message.contains("SMS1") && message.contains("SMS2") && message.contains("SMS3")) {
                        phoneBookManager.handleResponse(message, preferences);
                    } else if(message.contains("ALARM:")) {
                        alarmManager.handleResponse(message, preferences);
                    } else if(message.contains("Alarm:") && message.contains("GSM") && message.contains("Accu")) {
                        deviceStatusManager.handleResponse(message, preferences);
                    } else if(message.contains("Min. voltage:")) {
                        voltageManager.handleResponse(message, preferences);
                    }
                }
            }
//            if(preferences.contains("alarm_released")) {
//                AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                Intent pintent = new Intent(context, AlarmReceiver.class);
//                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, pintent, 0);
//
//                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
//                                1000, alarmIntent);
//            }
        }
    }

    private void toastMessage(Context context, SmsMessage smsMessage) {
        String toast = "Received SMS from: " + smsMessage.getDisplayOriginatingAddress();
        toast += "\nMessage: " + smsMessage.getDisplayMessageBody();
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

//    private class AlarmReceiver extends Service {
//
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            return super.onStartCommand(intent, flags, startId);
//
//        }
//    }

}
