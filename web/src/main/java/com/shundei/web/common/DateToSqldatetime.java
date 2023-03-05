package com.shundei.web.common;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JavaDate转数据库的datetime
 *
 * @author huoyouri
 */
public class DateToSqldatetime {

    /**
     * Java Date转datetime
     *
     * @param d
     * @return
     */
    public static Timestamp dateTodatetime(Date d) {
        if (null == d) {
            return null;
        }
        SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = st.format(d);
        // 精度原因，变成秒数的时候会有精度损失，使用字符串即可
        return Timestamp.valueOf(s);
    }

    /**
     * datetime转Java Date
     *
     * @param t
     * @return
     */
    public static Date datetimeToDate(Timestamp t) {
        if (null == t) {
            return null;
        }
        return new Date(t.getTime());
    }
}
