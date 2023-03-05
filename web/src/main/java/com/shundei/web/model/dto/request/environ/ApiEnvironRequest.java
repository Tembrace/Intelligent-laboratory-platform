package com.shundei.web.model.dto.request.environ;

import lombok.Data;

import java.io.Serializable;

/**
 * 首页环境表请求
 *
 * @author huoyouri
 */
@Data
public class ApiEnvironRequest implements Serializable {

    private static final long serialVersionUID = 8085872931950150060L;

    /**
     * 环境表ID
     */
    private Integer environId;

    /**
     * 半小时个数
     */
    private Integer halfHourPoint;

    /**
     * 半天个数
     */
    private Integer halfDayPoint;

    /**
     * 时间
     */
    private String currentTime;
}
