package com.shundei.web.model.dto.response.environ;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 半天环境表返回结果(均值)
 *
 * @author huoyouri
 */
@Data
public class ApiEnvironHalfDay implements Serializable {

    private static final long serialVersionUID = -2277450566121741096L;

    private String time;

    private BigDecimal averTemp;

    private BigDecimal averHum;

    private Integer averCo2;

    private BigDecimal averPm25;

    private Boolean isAm;

    public ApiEnvironHalfDay() {
    }

    public ApiEnvironHalfDay(String time, BigDecimal averTemp, BigDecimal averHum, Integer averCo2, BigDecimal averPm25, Boolean isAm) {
        this.time = time;
        this.averTemp = averTemp;
        this.averHum = averHum;
        this.averCo2 = averCo2;
        this.averPm25 = averPm25;
        this.isAm = isAm;
    }
}
