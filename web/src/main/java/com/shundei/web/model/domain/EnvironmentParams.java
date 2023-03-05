package com.shundei.web.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 环境表
 *
 * @author huoyouri
 */
@TableName(value = "environment_params")
@Data
public class EnvironmentParams implements Serializable {
    /**
     * 环境表记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 环境表ID
     */
    private Integer environmentID;

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
     * co2
     */
    private Integer co2;

    /**
     * 时间
     */
    private Date currentTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}