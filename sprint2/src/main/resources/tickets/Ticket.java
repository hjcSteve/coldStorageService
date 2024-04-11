package tickets;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket {

    private final String FORMAT;
    private final Integer TIMEOUT;
    private final String timestamp;
    private final Float FW;
    private final String id;

    public Ticket(Float weight, Integer timeout, String format) {
        FW = weight;
        TIMEOUT = timeout;
        FORMAT = format;
        timestamp = new SimpleDateFormat(FORMAT).format(System.currentTimeMillis());
        id = "t" + RandomStringUtils.random(7, true, true);
    }

    public String getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Float getWeight() {
        return FW;
    }

    public Boolean isValid() {
        try {
            long difference = new Date().getTime() - new SimpleDateFormat(FORMAT).parse(timestamp).getTime();
            return  (difference <= TIMEOUT);
        } catch (ParseException e) {
            return false;
        }
    }
}
