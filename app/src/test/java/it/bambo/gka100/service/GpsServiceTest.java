package it.bambo.gka100.service;


import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import it.bambo.gka100.Constants;
import it.bambo.gka100.Env;
import it.bambo.gka100.model.GpsInfo;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class GpsServiceTest {

    private GpsService service = GpsService.getInstance();

    @Before
    public void setUp() throws Exception {
        Env.isProd = false;
    }

    @Test
    public void testParseResponse() throws ParseException {
        String message = "Pajero 1.03 \n" +
                "Time: 09:44:23\n" +
                "Speed: 55km/h\n" +
                "Latitude:\n" +
                "47.856735N\n" +
                "Longitude:\n" +
                "012.193315E\n" +
                "Altitude: 398.2m\n" +
                "Sat. in used: 05";
        GpsInfo gpsInfo = service.parseResponse(message);

        String date = Constants.DATE_FORMAT.format(new Date());
        Assert.assertEquals(Constants.TIME_DATE_FORMAT.parse("09:44:23" + " " + date), gpsInfo.getTime());
        Assert.assertEquals(55, gpsInfo.getSpeed());
        Assert.assertEquals(new LatLng(47.856735, 12.193315), gpsInfo.getLatLng());
        Assert.assertEquals(398.2f, gpsInfo.getAlt());
        Assert.assertEquals(5, gpsInfo.getSatelliteCount());
    }

}
