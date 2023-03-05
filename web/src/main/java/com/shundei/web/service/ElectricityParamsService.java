package com.shundei.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shundei.web.model.domain.ElectricityParams;
import com.shundei.web.model.dto.request.elect.ApiElectPageRequest;
import com.shundei.web.model.dto.response.PageResult;
import com.shundei.web.model.dto.response.elect.ApiElectPageResult;
import com.shundei.web.model.dto.response.elect.ApiElectResult;

import java.util.List;

/**
 * 针对表【electricity_params(电表)】的数据库操作Service
 *
 * @author huoyouri
 */
public interface ElectricityParamsService extends IService<ElectricityParams> {

    /**
     * 分页查询电表数据
     *
     * @param request 电表参数请求体
     * @return 分页查询结果
     */
    PageResult<List<ApiElectPageResult>> selectElectPagesById(ApiElectPageRequest request);

    /**
     * 查询今天的电表总量
     *
     * @return 结果
     */
    ApiElectResult selectCurDayElects();
}
