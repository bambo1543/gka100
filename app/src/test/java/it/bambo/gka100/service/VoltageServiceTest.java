package it.bambo.gka100.service;


import junit.framework.Assert;

import org.junit.Test;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class VoltageServiceTest {

    private VoltageService service = VoltageService.getInstance();

    @Test
    public void testParseResponse() {
        String message = "Paj 1.03\n" +
                "Voltage:  0.3V\n" +
                "Min. voltage:  8.0V\n" +
                "HYS. VOLT: 0.1V\n" +
                "P+: low\n";
        String[] response = service.parseResponse(message);
        Assert.assertEquals(1, response.length);
        Assert.assertEquals("8.0", response[0]);
    }

    @Test
    public void testParseResponseOff() {
        String message = "Paj 1.03\n" +
                "Voltage:  0.3V\n" +
                "Min. voltage:  off\n" +
                "HYS. VOLT: 0.1V\n" +
                "P+: low\n";
        String[] response = service.parseResponse(message);
        Assert.assertEquals(1, response.length);
        Assert.assertEquals("off", response[0]);
    }

}
