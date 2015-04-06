package it.bambo.gka100;

import java.util.Arrays;
import java.util.List;

import it.bambo.gka100.sms.SMSReceiver;

/**
 * Created by andreas on 05.04.2015.
 */
public class Env {

    public static SMSReceiver smsReceiver = null;

    public static boolean isProd = true;
    public static boolean isMock = true;

    private static int NEXT_TEST_GPS_RESPONSE = 0;
    public static List<String> TEST_GPS_RESPONSE = Arrays.asList("Pajero 1.03 \n" +
                    "Time: 17:39:19\n" +
                    "Speed: 0km/h\n" +
                    "Latitude:\n" +
                    "47.856476N\n" +
                    "Longitude:\n" +
                    "012.192916E\n" +
                    "Altitude: 413.1m\n" +
                    "Sat. in used: 05",
            "Pajero 1.03 \n" +
                    "Time: 17:45:19\n" +
                    "Speed: 53km/h\n" +
                    "Latitude:\n" +
                    "48.127353N\n" +
                    "Longitude:\n" +
                    "012.133637E\n" +
                    "Altitude: 413.1m\n" +
                    "Sat. in used: 05",
            "Pajero 1.03 \n" +
                    "Time: 17:53:19\n" +
                    "Speed: 88km/h\n" +
                    "Latitude:\n" +
                    "48.098768N\n" +
                    "Longitude:\n" +
                    "011.597551E\n" +
                    "Altitude: 413.1m\n" +
                    "Sat. in used: 05"
    );

    public static String getNextTestGpsResponse() {
        if(NEXT_TEST_GPS_RESPONSE == 3)
            NEXT_TEST_GPS_RESPONSE = 0;

        String result = TEST_GPS_RESPONSE.get(NEXT_TEST_GPS_RESPONSE);
        NEXT_TEST_GPS_RESPONSE++;
        return result;
    }
}
