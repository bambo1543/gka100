package it.bambo.gka100.sms;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.text.ParseException;

import it.bambo.gka100.utils.StringUtils;

/**
 * Created by andreas on 05.04.2015.
 */
public class SMSSender {

    private static SMSSender instance = new SMSSender();

    private SmsManager smsManager;

    private String phoneNumber;
    private String phonePin;

    private SMSSender() {
        smsManager = SmsManager.getDefault();

    }

    public static SMSSender getInstance(){
        return instance;
    }


    public void sendAction(Context context, String action) {
        try {
            updatePhoneNumberAndPin(context);
            smsManager.sendTextMessage(phoneNumber, null, action + " #" + phonePin, null, null);
            Toast.makeText(context, "Sent Action: " + action, Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Toast.makeText(context, "Unable to parse: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updatePhoneNumberAndPin(Context context) throws ParseException {
        SharedPreferences preferences = context.getSharedPreferences("it.bambo.gka100_preferences", Context.MODE_PRIVATE);

        phoneNumber = preferences.getString("phoneNumber", null);
        if(StringUtils.isEmpty(phoneNumber)) throw new ParseException("phoneNumber", 0);

        phonePin = preferences.getString("phonePin", "1513");
        if(StringUtils.isEmpty(phonePin)) throw new ParseException("phonePin", 0);
    }

}
