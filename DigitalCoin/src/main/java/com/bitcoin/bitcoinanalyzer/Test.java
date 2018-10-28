/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitcoin.bitcoinanalyzer;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author amoghantarkar
 */
public class Test {
    private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";

    public static void main(String[] args) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        String dateInString = "22-01-2015 10:15:55 AM";
        Date date = formatter.parse(dateInString);
        TimeZone tz = TimeZone.getDefault();

        // From TimeZone Asia/Singapore
        System.out.println("TimeZone : " + tz.getID() + " - " + tz.getDisplayName());
        System.out.println("TimeZone : " + tz);
        System.out.println("Date (Singapore) : " + formatter.format(date));

        // To TimeZone America/New_York
        SimpleDateFormat sdfAmerica = new SimpleDateFormat(DATE_FORMAT);
        DateTime dt = new DateTime(date);
        DateTimeZone dtZone = DateTimeZone.forID("America/New_York");
        DateTime dtus = dt.withZone(dtZone);
        TimeZone tzInAmerica = dtZone.toTimeZone();
        Date dateInAmerica = dtus.toLocalDateTime().toDate(); //Convert to LocalDateTime first

        sdfAmerica.setTimeZone(tzInAmerica);

        System.out.println("\nTimeZone : " + tzInAmerica.getID() + " - " + tzInAmerica.getDisplayName());
        System.out.println("TimeZone : " + tzInAmerica);
        System.out.println("DateTimeZone : " + dtZone);
        System.out.println("DateTime : " + dtus);

        System.out.println("dateInAmerica (Formatter) : " + formatter.format(dateInAmerica));
        System.out.println("dateInAmerica (Object) : " + dateInAmerica);

    }
}
