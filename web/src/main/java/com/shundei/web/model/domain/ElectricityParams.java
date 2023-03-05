package com.shundei.web.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电表
 *
 * @author huoyouri
 */
@TableName(value = "electricity_params")
@Data
public class ElectricityParams implements Serializable {
    /**
     * 电表记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 电表ID
     */
    private Integer electricityID;

    /**
     * 电量
     */
    private BigDecimal capacity;

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
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date currentTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}