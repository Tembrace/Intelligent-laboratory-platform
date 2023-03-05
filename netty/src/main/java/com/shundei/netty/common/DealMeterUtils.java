package com.shundei.netty.common;


import com.tang.backendcommon.dto.ElectParamsRequest;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 处理表数据
 *
 * @author huoyouri
 */
public class DealMeterUtils {

    /**
     * 处理环境表数据
     *
     * @param data 数组
     * @return 环境表
     */
    public static List<EnvironmentParamsRequest> dealEnvirons(byte[] data) {
        int low = Byte.toUnsignedInt(data[0]);
        int high = Byte.toUnsignedInt(data[1]);
        int count = high * 256 + low;
        List<EnvironmentParamsRequest> res = new ArrayList<>();
        int start = count * 34 + 2;
        int end = start + 6;
        Date currentTime = TimeStringToBytes.transformToDate(Arrays.copyOfRange(data, start, end));
        for (int i = 0; i < count; i++) {
            int id = Byte.toUnsignedInt(data[3 + i * 34]);
            if (isFFData(Arrays.copyOfRange(data, 4 + i * 34, 2 + (i + 1) * 34))) {
                // 全是ff，那么查询上一条数据并插入，这里就让请求加个属性即可
                res.add(new EnvironmentParamsRequest(
                        id, null, null, null, null, currentTime, true
                ));
            } else {
                int co2 = dealCo2OrPm25(data[4 + i * 34], data[5 + i * 34]);
                BigDecimal temp = new BigDecimal(dealTempOrHum(data[6 + i * 34], data[7 + i * 34]));
                BigDecimal hum = new BigDecimal(dealTempOrHum(data[8 + i * 34], data[9 + i * 34]));
                BigDecimal pm25 = new BigDecimal(dealCo2OrPm25(data[10 + i * 34], data[11 + i * 34]));
                res.add(new EnvironmentParamsRequest(
                        id, temp, hum, pm25, co2, currentTime, false
                ));
            }
        }
        return res;
    }

    /**
     * 处理电表数据
     *
     * @param data 数组
     * @return 环境表
     */
    public static List<ElectParamsRequest> dealElects(byte[] data) {
        int low = Byte.toUnsignedInt(data[0]);
        int high = Byte.toUnsignedInt(data[1]);
        int count = high * 256 + low;
        List<ElectParamsRequest> res = new ArrayList<>();
        int start = count * 34 + 2;
        int end = start + 6;
        Date currentTime = TimeStringToBytes.transformToDate(Arrays.copyOfRange(data, start, end));
        for (int i = 0; i < count; i++) {
            int id = Byte.toUnsignedInt(data[3 + i * 34]);
            if (isFFData(Arrays.copyOfRange(data, 4 + i * 34, 2 + (i + 1) * 34))) {
                // 全是ff，那么查询上一条数据并插入，这里就让请求加个属性即可
                res.add(new ElectParamsRequest(
                        id, null, currentTime, true
                ));
            } else {
                BigDecimal capacity = new BigDecimal(dealCapacity(data[4 + i * 34], data[5 + i * 34], data[6 + i * 34], data[7 + i * 34]));
                res.add(new ElectParamsRequest(
                        id, capacity, currentTime, false
                ));
            }
        }
        return res;
    }

    /**
     * 判断数据是否全是FF，如果都是，补充上一条数据
     *
     * @param arr
     * @return
     */
    private static boolean isFFData(byte[] arr) {
        boolean flag = true;
        for (byte b : arr) {
            if (b != (byte) 0xff) {
                flag = false;
            }
        }
        return flag;
    }

    private static int dealCo2OrPm25(byte low, byte high) {
        return Byte.toUnsignedInt(high) * 256 + Byte.toUnsignedInt(low);
    }

    private static String dealTempOrHum(byte integer, byte decimal) {
        return String.valueOf(Byte.toUnsignedInt(integer) + Byte.toUnsignedInt(decimal) * 0.1);
    }

    private static String dealCapacity(byte tenth, byte ten, byte thousand, byte hundredThousand) {
        int bcdTenth = TimeStringToBytes.getBCDInt(tenth);
        int bcdTen = TimeStringToBytes.getBCDInt(ten);
        int bcdThousand = TimeStringToBytes.getBCDInt(thousand);
        int bcdHundredThousand = TimeStringToBytes.getBCDInt(hundredThousand);
        return String.valueOf(bcdHundredThousand * 10000 + bcdThousand * 100 + bcdTen + bcdTenth * 0.01);
    }
}
