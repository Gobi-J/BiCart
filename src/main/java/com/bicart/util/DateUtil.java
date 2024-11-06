package com.bicart.util;

import java.util.Date;

/**
 * <p>
 *   Utility class for date operations.
 * </p>
 */
public class DateUtil {

    /**
     * <p>
     *   Get the updated date by adding the given number of days to the given date.
     * </p>
     *
     * @param date the date to be updated
     * @param days the number of days to be added
     * @return {@link Date} new date after adding the given number of days
     */
    public static Date getUpdatedDate(Date date, int days) {
        return new Date(date.getTime() + (long) days * 24 * 60 * 60 * 1000);
    }
}
