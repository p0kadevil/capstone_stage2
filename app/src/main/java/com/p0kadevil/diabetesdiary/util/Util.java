package com.p0kadevil.diabetesdiary.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getFormattedDateString(Date date, boolean withTime)
    {
        if(date == null)
        {
            return "";
        }

        SimpleDateFormat fmt = new SimpleDateFormat(!withTime ? "dd.MM.yyyy" : "dd.MM.yyyy HH:mm");
        return fmt.format(date);
    }

    public static String getDateStringSql(Date date)
    {
        if(date == null)
        {
            return "";
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(date);
    }

    public static boolean isInteger(String string, boolean signed)
    {
        return string != null && signed ? string.matches("^\\d+$") :
                string != null && string.matches("^-?\\d+$");
    }
}
