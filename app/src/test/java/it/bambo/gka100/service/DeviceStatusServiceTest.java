package it.bambo.gka100.service;


import junit.framework.Assert;

import org.junit.Test;

import java.text.ParseException;

import it.bambo.gka100.Constants;
import it.bambo.gka100.model.DeviceStatus;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class DeviceStatusServiceTest {

    private DeviceStatusService service = DeviceStatusService.getInstance();

    @Test
    public void testParseResponse() throws ParseException {
        String message = "Paj 1.03\n" +
                "21:31 06.04.15\n" +
                "Alarm: on\n" +
                "GSM: 37%\n" +
                "Accu: 100%\n" +
                "Area: off\n" +
                "Shock: 10/10\n" +
                "Volt.: 12.2V\n" +
                "HoldAlarm: off\n" +
                "IN1: low\n" +
                "IN2: low\n" +
                "OUT1: off\n" +
                "OUT2: off\n";
        DeviceStatus deviceStatus = service.parseResponse(message);

        Assert.assertEquals(Constants.SHORT_TIME_DATE_FORMAT.parse("21:31 06.04.15"), deviceStatus.getTime());
        Assert.assertEquals(true, deviceStatus.isAlarm());
        Assert.assertEquals(37, deviceStatus.getGsm());
        Assert.assertEquals(100, deviceStatus.getAccu());
        Assert.assertEquals(false, deviceStatus.isArea());
        //Shock
        Assert.assertEquals(12.2f, deviceStatus.getVolt());
        Assert.assertEquals(false, deviceStatus.isHoldAlarm());
        Assert.assertEquals(false, deviceStatus.isIn1());
        Assert.assertEquals(false, deviceStatus.isIn2());
        Assert.assertEquals(false, deviceStatus.isOut1());
        Assert.assertEquals(false, deviceStatus.isOut2());
    }

}
