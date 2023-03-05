package com.shundei.web.model.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 报表下载请求
 *
 * @author huoyouri
 */
@Data
public class OutputDataRequest implements Serializable {

    private static final long serialVersionUID = 652058428023645861L;

    private Boolean isEnviron;

    private Boolean isElect;
    
    private String startTime;

    private String endTime;
}
