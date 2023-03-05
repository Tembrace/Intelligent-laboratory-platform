package com.shundei.web.model.dto.response.environ;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 半小时环境表返回结果
 *
 * @author huoyouri
 */
@Data
public class ApiEnvironHalfTime implements Serializable {

    private static final long serialVersionUID = 7802632454633486211L;

    private String time;

    private BigDecimal temp;

    private BigDecimal hum;

    private Integer co2;

    private BigDecimal pm25;

    public ApiEnvironHalfTime() {
    }

    public ApiEnvironHalfTime(String time, BigDecimal temp, BigDecimal hum, Integer co2, BigDecimal pm25) {
        this.time = time;
        this.temp = temp;
        this.hum = hum;
        this.co2 = co2;
        this.pm25 = pm25;
    }
}
