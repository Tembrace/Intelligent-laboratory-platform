package com.shundei.web.model.dto.response.elect;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页电表返回结果
 *
 * @author huoyouri
 */
@Data
public class ApiElectResult implements Serializable {

    private static final long serialVersionUID = 7446864207046801939L;

    private BigDecimal elect1;

    private BigDecimal elect2;

    public ApiElectResult() {
    }

    public ApiElectResult(BigDecimal elect1, BigDecimal elect2) {
        this.elect1 = elect1;
        this.elect2 = elect2;
    }
}
