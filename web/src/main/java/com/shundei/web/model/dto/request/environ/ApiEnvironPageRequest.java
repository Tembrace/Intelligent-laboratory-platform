package com.shundei.web.model.dto.request.environ;

import lombok.Data;

import java.io.Serializable;

/**
 * 环境表报表参数请求体
 *
 * @author huoyouri
 */
@Data
public class ApiEnvironPageRequest implements Serializable {

    private static final long serialVersionUID = -5340484892123156231L;

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
     * 环境表ID
     */
    private Integer environId;

    /**
     * 每页显示多少条
     */
    private Integer pageSize;

    /**
     * 第几页
     */
    private Integer pageNum;
}
