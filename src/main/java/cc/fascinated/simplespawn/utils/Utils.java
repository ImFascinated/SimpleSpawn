package cc.fascinated.simplespawn.utils;

import java.util.concurrent.TimeUnit;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 16/06/2021
 */
public class Utils {

    /**
     * Returns the shortened formatted time until the provided {@link Long} is present
     * Formatted as: "1mo 2w 3d 4h 5m 6s"
     *
     * @param end The time to get until
     * @return The shortened formatted time until the provided date
     */
    public static String getTimeUntil(long end) {
        if (end == -1) {
            return "Permanent";
        }
        long left = end - System.currentTimeMillis();
        StringBuilder leftString = new StringBuilder();
        if (left <= 0) {
            return "now";
        }
        TimeUnit[] units = {TimeUnit.DAYS, TimeUnit.DAYS, TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS};
        int[] amounts = {30, 7, 1, 1, 1, 1, 1};
        String[] suffixes = {"mo ", "w ", "d ", "h ", "m ", "s"};
        for (int i = 0; i < 6; i++) {
            long asMillis = units[i].toMillis(amounts[i]);
            if (left - asMillis >= 1) {
                leftString.append(left / asMillis).append(suffixes[i]);
                left %= asMillis;
            }
        }
        return leftString.toString();
    }

    /**
     * Format the given time into a readable {@link String}
     *
     * @param time the time to format in millis
     * @param shortTime whether or not to have short suffixes for the time
     * @return the formatted time
     */
    public static String formatTime(long time, boolean shortTime) {
        time = (int) (time / 1000L);
        if (time <= 0L)
            return "0" + (shortTime ? "s" : " seconds");
        int remainder = (int) time % 86400;
        int days = (int) (time / 86400);
        int hours = remainder / 3600;
        int minutes = remainder / 60 - hours * 60;
        int seconds = remainder % 3600 - minutes * 60;
        String daysString = (days > 0) ? (" " + days + (shortTime ? "d" : " day") + ((days > 1) ? shortTime ? "" : "s" : "")) : "";
        String hoursString = (hours > 0) ? (" " + hours + (shortTime ? "h" : " hour") + ((hours > 1) ? shortTime ? "" : "s" : "")) : "";
        String minutesString = (minutes > 0) ? (" " + minutes + (shortTime ? "m" : " minute") + ((minutes > 1) ? shortTime ? "" : "s" : "")) : "";
        String secondsString = (seconds > 0) ? (" " + seconds + (shortTime ? "s" : " second") + ((seconds > 1) ? shortTime ? "" : "s" : "")) : "";
        return (daysString + hoursString + minutesString + secondsString).trim();
    }
}
