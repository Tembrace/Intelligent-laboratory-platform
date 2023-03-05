package com.shundei.netty.common;


import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

/**
 * 字符串时间转为byte数组
 *
 * @author huoyouri
 */
@Slf4j
public class TimeStringToBytes {

    /**
     * Byte数组转Date
     *
     * @param time 时间
     * @return 数组
     */
    public static Date transformToDate(byte[] time) {
        if (time.length != 6) {
            throw new RuntimeException("时间数组长度错误");
        }
        int seconds = getBCDInt(time[0]);
        int minutes = getBCDInt(time[1]);
        int hour = getBCDInt(time[2]);
        int day = getBCDInt(time[3]);
        int month = getIntOfMonthAndWeek(time[4])[1];
        // 这里只用一个字节来表示年份，默认加2000
        int year = getBCDInt(time[5]) + 2000;
        Calendar calendar = Calendar.getInstance();
        // 月份从0开始
        calendar.set(year, month - 1, day, hour, minutes, seconds);
        return calendar.getTime();
    }

    /**
     * 特定时间转Byte数组
     *
     * @param calendar 时间
     * @return 数组
     */
    public static byte[] transformToBytes(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        byte[] res = new byte[6];
        res[0] = getBcd(second);
        res[1] = getBcd(minute);
        res[2] = getBcd(hour);
        res[3] = getBcd(day);
        // 月份自身问题，处理即可
        res[4] = getMonAndWeek(month + 1, dayOfWeek);
        res[5] = getBcd(year);
        return res;
    }

    /**
     * 当前时间转Byte数组
     *
     * @return 数组
     */
    public static byte[] curTransformToBytes() {
        Calendar calendar = Calendar.getInstance();
        return transformToBytes(calendar);
    }

    /**
     * 整数转BCD码
     *
     * @param num 数字
     * @return BCD码
     */
    private static byte getBcd(int num) {
        // 只用后两位
        int bit = num % 10;
        num /= 10;
        int ten = num % 10;
        return (byte) (ten * 16 + bit);
    }

    /**
     * 字节转BCD整数
     *
     * @param num 字节
     * @return 整数
     */
    public static int getBCDInt(byte num) {
        int low = Byte.toUnsignedInt((byte) (num & 0x0f));
        num >>= 4;
        int high = Byte.toUnsignedInt((byte) (num & 0x0f));
        return high * 10 + low;
    }

    /**
     * 字节转整数 月份和周几
     *
     * @param num 字节
     * @return 数组，0是week，1是month
     */
    private static int[] getIntOfMonthAndWeek(byte num) {
        int low = Byte.toUnsignedInt((byte) (num & 0x0f));
        num >>= 4;
        int high = Byte.toUnsignedInt((byte) (num & 0x01));
        num >>= 1;
        int month = high * 10 + low;
        int week = Byte.toUnsignedInt((byte) (num & 0x07));
        return new int[]{week, month};
    }

    /**
     * 根据协议获得第四字节的内容
     * 周日：1，周六：7
     *
     * @param month     月
     * @param dayOfWeek 周几
     * @return 字节
     */
    private static byte getMonAndWeek(int month, int dayOfWeek) {
        log.info("月份：{}", month);
        log.info("周{}", dayOfWeek - 1 == 0 ? "日" : dayOfWeek - 1);
        if (dayOfWeek - 1 == 0) {
            dayOfWeek = 7;
        } else {
            dayOfWeek -= 1;
        }
        int bit = month % 10;
        month /= 10;
        int ten = month % 10;
        return (byte) (ten * 16 + bit + (dayOfWeek << 5));
    }
}
