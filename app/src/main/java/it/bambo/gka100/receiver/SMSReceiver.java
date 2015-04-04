package it.bambo.gka100.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import it.bambo.gka100.service.AudioService;

/**
 * Created by andreas on 23.03.14.
 */
public class SMSReceiver extends BroadcastReceiver {

    private AudioService audioService = AudioService.getInstance();

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            SharedPreferences preferences = context.getSharedPreferences("com.example.GKA100_preferences", Context.MODE_PRIVATE);
            String phoneNumber = preferences.getString("phoneNumber", "");

            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");
            if(messages.length == 1) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) messages[0]);
                if(phoneNumber.equals(smsMessage.getDisplayOriginatingAddress())) {
                    toastMessage(context, smsMessage);

                    String message = smsMessage.getDisplayMessageBody();
                    if(message.contains("Speaker")) {
                        audioService.handleResponse(message, preferences);
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
}
