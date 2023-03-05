package com.shundei.web.model.dto.response;


import lombok.Data;

import java.io.Serializable;

/**
 * 基本页码返回体
 *
 * @author huoyouri
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -6898944285168362359L;

    /**
     * 返回体
     */
    private T data;

    /**
     * 查询总数
     */
    private Long total;

    public PageResult(T data, Long total) {
        this.data = data;
        this.total = total;
    }
}
