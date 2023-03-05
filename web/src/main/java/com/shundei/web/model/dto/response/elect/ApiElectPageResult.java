package com.shundei.web.model.dto.response.elect;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电表查询结果
 *
 * @author huoyouri
 */
@Data
public class ApiElectPageResult implements Serializable {

    private static final long serialVersionUID = -4573158820694037757L;

    private BigDecimal elect1;

    private BigDecimal elect2;

    private Date createTime;

    public ApiElectPageResult() {
    }

    public ApiElectPageResult(BigDecimal elect1, BigDecimal elect2, Date createTime) {
        this.elect1 = elect1;
        this.elect2 = elect2;
        this.createTime = createTime;
    }
}
