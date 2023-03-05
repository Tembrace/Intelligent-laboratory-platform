package com.shundei.web.model.dto.response.environ;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 环境表查询结果
 *
 * @author huoyouri
 */
@Data
public class ApiEnvironPageResult implements Serializable {

    private static final long serialVersionUID = 8080018666567752631L;

    private Integer environId;

    private BigDecimal temp;

    private BigDecimal hum;
    
    private Integer co2;

    private BigDecimal pm2_5;

    private Date createTime;

    public ApiEnvironPageResult() {
    }

    public ApiEnvironPageResult(Integer environId, BigDecimal temp, BigDecimal hum, Integer co2, BigDecimal pm2_5, Date createTime) {
        this.environId = environId;
        this.temp = temp;
        this.hum = hum;
        this.co2 = co2;
        this.pm2_5 = pm2_5;
        this.createTime = createTime;
    }
}
