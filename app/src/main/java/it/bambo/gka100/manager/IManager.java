package it.bambo.gka100.manager;

import android.content.SharedPreferences;

import java.text.ParseException;

/**
 * Created by andreas on 09.04.2015.
 */
public interface IManager {

    public boolean isResponsibleForMessage(String message);

    public void handleResponse(String message, SharedPreferences preferences) throws ParseException;

}
