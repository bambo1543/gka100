package it.bambo.gka100.manager;


import junit.framework.Assert;

import org.junit.Test;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class AlarmManagerTest {

    private AlarmManager manager = AlarmManager.instance;

    @Test
    public void testParseResponse() {
        String message = "Pajero 1.03\n" +
                "ALARM:\n" +
                "Auto alarm was released\n" +
                "GPS position:\n" +
                "Lat:\n" +
                "47.856105N\n" +
                "Long:\n" +
                "012.193365E";
        String[] response = manager.parseResponse(message);
        Assert.assertEquals(2, response.length);
        Assert.assertEquals("47.856105", response[0]);
        Assert.assertEquals("012.193365", response[1]);
    }

}
