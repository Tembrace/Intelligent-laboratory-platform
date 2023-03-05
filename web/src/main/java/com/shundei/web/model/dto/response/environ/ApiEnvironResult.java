package com.shundei.web.model.dto.response.environ;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 首页环境表返回结果
 *
 * @author huoyouri
 */
@Data
public class ApiEnvironResult implements Serializable {

    private static final long serialVersionUID = -148451875521523202L;

    private List<ApiEnvironHalfTime> halfTimes;

    private List<ApiEnvironHalfDay> halfDays;

    public ApiEnvironResult() {
    }

    public ApiEnvironResult(List<ApiEnvironHalfTime> halfTimes, List<ApiEnvironHalfDay> halfDays) {
        this.halfTimes = halfTimes;
        this.halfDays = halfDays;
    }
}
