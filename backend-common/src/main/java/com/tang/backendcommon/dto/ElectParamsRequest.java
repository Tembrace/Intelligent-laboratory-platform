package com.tang.backendcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电表参数请求体
 *
 * @author huoyouri
 */
@Data
public class ElectParamsRequest implements Serializable {

    private static final long serialVersionUID = 8645996394244127360L;

    /**
     * 电表ID
     */
    private Integer id;

    /**
     * 电量
     */
    private BigDecimal capacity;

    /**
     * 时间
     */
    private Date currentTime;

    /**
     * 该数据是否全为FF，那么该数据插入的时候插上一条数据，在web服务操作
     */
    private Boolean isFF;

    public ElectParamsRequest() {
    }

    public ElectParamsRequest(Integer id, BigDecimal capacity, Date currentTime, Boolean isFF) {
        this.id = id;
        this.capacity = capacity;
        this.currentTime = currentTime;
        this.isFF = isFF;
    }
}
