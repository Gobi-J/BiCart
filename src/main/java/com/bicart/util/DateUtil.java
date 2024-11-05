package com.bicart.util;

import java.util.Date;

public class DateUtil {

    public static Date getUpdatedDate(Date date, int days) {
        return new Date(date.getTime() + (long) days * 24 * 60 * 60 * 1000);
    }
}
