package com.shundei.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shundei.web.model.domain.EnvironmentParams;
import com.shundei.web.model.dto.request.environ.ApiEnvironPageRequest;
import com.shundei.web.model.dto.request.environ.ApiEnvironRequest;
import com.shundei.web.model.dto.response.PageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironPageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironResult;

import java.util.List;

/**
 * 针对表【environment_params(环境表)】的数据库操作Service
 *
 * @author huoyouri
 */
public interface EnvironmentParamsService extends IService<EnvironmentParams> {
    

    /**
     * 分页查询环境表数据
     *
     * @param request 环境表参数请求体
     * @return 分页查询结果
     */
    PageResult<List<ApiEnvironPageResult>> selectEnvironPagesById(ApiEnvironPageRequest request);

    /**
     * 根据Id查询环境表参数
     *
     * @param request 请求
     * @return 返回以每半小时的情况和每半天的情况
     */
    ApiEnvironResult selectEnvironById(ApiEnvironRequest request);
}
