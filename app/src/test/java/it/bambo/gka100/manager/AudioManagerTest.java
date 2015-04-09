package it.bambo.gka100.manager;


import junit.framework.Assert;

import org.junit.Test;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class AudioManagerTest {

    private AudioManager manager = AudioManager.instance;

    @Test
    public void testParseResponse() {
        String message = "Pajero 1.03\n" +
                "--------------\n" +
                "Speaker: 6\n" +
                "Microphone: 6\n" +
                "Ring melody: 7\n" +
                "Ring volume: 7\n" +
                "Alarm volume: 7\n" +
                "Confirm. volume: 5\n";
        String[] response = manager.parseResponse(message);
        Assert.assertEquals(6, response.length);
        Assert.assertEquals("6", response[0]);
        Assert.assertEquals("6", response[1]);
        Assert.assertEquals("7", response[2]);
        Assert.assertEquals("7", response[3]);
        Assert.assertEquals("7", response[4]);
        Assert.assertEquals("5", response[5]);
    }

}
