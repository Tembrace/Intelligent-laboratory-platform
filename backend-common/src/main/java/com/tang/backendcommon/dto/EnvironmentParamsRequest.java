package com.tang.backendcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 环境表参数请求体
 *
 * @author huoyouri
 */
@Data
public class EnvironmentParamsRequest implements Serializable {

    private static final long serialVersionUID = 5461458490582478968L;

    /**
     * 表号
     */
    private Integer id;

    /**
     * 温度
     */
    private BigDecimal temperature;

    /**
     * 湿度
     */
    private BigDecimal humidity;

    /**
     * PM2.5
     */
    private BigDecimal pm2_5;

    /**
     * CO2
     */
    private Integer co2;

    /**
     * 时间
     */
    private Date currentTime;

    /**
     * 该数据是否全为FF，那么该数据插入的时候插上一条数据，在web服务操作
     */
    private Boolean isFF;

    public EnvironmentParamsRequest() {
    }

    public EnvironmentParamsRequest(Integer id, BigDecimal temperature, BigDecimal humidity, BigDecimal pm2_5, Integer co2, Date currentTime, Boolean isFF) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pm2_5 = pm2_5;
        this.co2 = co2;
        this.currentTime = currentTime;
        this.isFF = isFF;
    }
}
