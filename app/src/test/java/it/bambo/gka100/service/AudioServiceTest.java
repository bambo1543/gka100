package it.bambo.gka100.service;


import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class AudioServiceTest {

    private AudioService audioService = AudioService.getInstance();

    @Test
    public void testParseAudioResponse() {
        String message = "Pajero 1.03\n" +
                "--------------\n" +
                "Speaker: 6\n" +
                "Microphone: 6\n" +
                "Ring melody: 7\n" +
                "Ring volume: 7\n" +
                "Alarm volume: 7\n" +
                "Confirm. volume: 5\n";
        String[] audioResponse = audioService.parseResponse(message);
        Assert.assertEquals(6, audioResponse.length);
        Assert.assertEquals("6", audioResponse[0]);
        Assert.assertEquals("6", audioResponse[1]);
        Assert.assertEquals("7", audioResponse[2]);
        Assert.assertEquals("7", audioResponse[3]);
        Assert.assertEquals("7", audioResponse[4]);
        Assert.assertEquals("5", audioResponse[5]);
    }

}
