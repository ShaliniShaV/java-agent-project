package utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    public static void info(String agent, String msg) {
        System.out.println(String.format("%s | %s | %s", 
            DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now()), agent, msg));
    }
}
