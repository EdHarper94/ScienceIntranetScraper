package egwh.scienceintranetscraper.Coursework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @file CourseworkGlobals.java
 * @author Ed Harper
 * @date 20/04/2017
 *
 * Some global Coursework variables
 *
 */
public class CourseworkGlobals {

    public static final String TYPE = "type";
    public static final String CURRENT_CW = "c";
    public static final String RECEIVED_CW = "r";
    public static final String FUTURE_CW = "f";

    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final DateFormat DATE_FORMAT= new SimpleDateFormat("dd/MM/yyyy");
}
