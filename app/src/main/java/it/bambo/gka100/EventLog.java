package it.bambo.gka100;

/**
 * Created by andreas on 04.04.2015.
 */
public class EventLog {

    private static EventLog instance = new EventLog();

    private EventLog() {
    }

    public static EventLog getInstance(){
        return instance;
    }


}
