package it.bambo.gka100.manager;

import android.content.SharedPreferences;

import it.bambo.gka100.Constants;
import it.bambo.gka100.model.PhoneBook;

/**
 * Created by andreas on 05.04.2015.
 */
public class PhoneBookManager implements IManager {

    public static PhoneBookManager instance;
    private static String NO_DESTINATION = "No destination";

    static {
        instance = new PhoneBookManager();
    }

    private PhoneBookManager() {}

    public boolean isResponsibleForMessage(String message) {
        return message.contains("SMS1") && message.contains("SMS2") && message.contains("SMS3");
    }

    public void handleResponse(String message, SharedPreferences preferences) {
        PhoneBook values = parseResponse(message);
        saveResponse(preferences, values);
    }

    PhoneBook parseResponse(String message) {
        PhoneBook phoneBook = new PhoneBook(message.substring(0, message.indexOf(Constants.FIRMWARE_VERSION)).trim());

        for(int i = 1; i <= 6; i++) {
            String sms = "SMS" + i;
            String nextSms = "SMS" + (i + 1);
            int index = message.indexOf(sms) + sms.length();
            String number;
            if(i < 6) {
                number = message.substring(index, message.indexOf(nextSms)).trim();
            } else {
                number = message.substring(index, message.length() - 1).trim();
            }
            number = NO_DESTINATION.equals(number) ? null : number;
            phoneBook.getNumbers().add(i - 1, number);
        }
        return phoneBook;
    }


    private void saveResponse(SharedPreferences preferences, PhoneBook phoneBook) {
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i <= 5; i++) {
            String value = phoneBook.getNumbers().get(i);
            if(value == null) {
                editor.remove("phoneNumber" + (i + 1));
            } else {
                editor.putString("phoneNumber" + (i + 1), value);
            }
        }
        editor.apply();
    }

}
