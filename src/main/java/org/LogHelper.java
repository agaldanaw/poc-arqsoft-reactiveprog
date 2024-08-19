package org;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LogHelper {
    public static void Log(String message)
    {
        // Get the current local date and time
        LocalDateTime localDateTime = LocalDateTime.now();

        // Convert LocalDateTime to ZonedDateTime using the system's default time zone
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        String formattedDateTime = zonedDateTime.format(formatter);

        System.out.println(String.format("(%s) - Thread: %s, Message: %s. ", formattedDateTime, Thread.currentThread().getName(), message));
    }
}
