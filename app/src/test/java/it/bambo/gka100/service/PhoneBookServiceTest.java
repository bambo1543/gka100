package it.bambo.gka100.service;


import junit.framework.Assert;

import org.junit.Test;

import java.text.ParseException;

import it.bambo.gka100.model.PhoneBook;

/**
 * @author <a href="mailto:andreas.bga@gmail.com">Andreas Baumgartner</a> on 04.04.15.
 */
public class PhoneBookServiceTest {

    private PhoneBookService service = PhoneBookService.getInstance();

    @Test
    public void testParseResponse() throws ParseException {
        String message = "Pajero 1.03\n" +
                "SMS1\n" +
                "+4915771543434\n" +
                "SMS2\n" +
                "233333\n" +
                "SMS3\n" +
                "No destination\n" +
                "SMS4\n" +
                "No destination\n" +
                "SMS5\n" +
                "No destination\n" +
                "SMS6\n" +
                "No destination\n";
        PhoneBook response = service.parseResponse(message);

        Assert.assertEquals("Pajero", response.getName());
        Assert.assertEquals(6, response.getNumbers().size());
        Assert.assertEquals("+4915771543434", response.getNumbers().get(0));
        Assert.assertEquals("233333", response.getNumbers().get(1));
        Assert.assertEquals(null, response.getNumbers().get(2));
        Assert.assertEquals(null, response.getNumbers().get(3));
        Assert.assertEquals(null, response.getNumbers().get(4));
        Assert.assertEquals(null, response.getNumbers().get(5));
    }

}
