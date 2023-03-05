package com.shundei.web.model.dto.request.elect;

import lombok.Data;

import java.io.Serializable;

/**
 * 电表报表参数请求体
 *
 * @author huoyouri
 */
@Data
public class ApiElectPageRequest implements Serializable {

    private static final long serialVersionUID = 5782899629513060569L;
    /**
     * 是否查已删除的数据，false是不查
     */
    private Boolean isDelete;

    /**
     * 查询结果按照该key排序
     */
    private String orderKey;

    /**
     * 升序还是降序，desc/asc
     */
    private String order;

    /**
     * 当前时间的筛选，如果没有就全部
     */
    private String currentTime;

    /**
     * 每页显示多少条
     */
    private Integer pageSize;

    /**
     * 第几页
     */
    private Integer pageNum;
}
