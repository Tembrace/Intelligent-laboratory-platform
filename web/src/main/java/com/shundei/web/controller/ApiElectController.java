package com.shundei.web.controller;

import com.shundei.web.exception.BusinessException;
import com.shundei.web.model.dto.request.elect.ApiElectPageRequest;
import com.shundei.web.model.dto.response.PageResult;
import com.shundei.web.model.dto.response.elect.ApiElectPageResult;
import com.shundei.web.model.dto.response.elect.ApiElectResult;
import com.shundei.web.service.ElectricityParamsService;
import com.shundei.web.utils.ResponseUtils;
import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.ResponseCode;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网页访问电表接口
 *
 * @author huoyouri
 */
@RestController
@RequestMapping("/api/elect")
public class ApiElectController {

    @Resource
    private ElectricityParamsService service;

    @PostMapping("/search")
    public BaseResponse<PageResult<List<ApiElectPageResult>>> selectELectsPage(@RequestBody ApiElectPageRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求参数为空");
        }
        PageResult<List<ApiElectPageResult>> res = service.selectElectPagesById(request);
        return res == null ? ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "无数据") :
                ResponseUtils.success(ResponseCode.SUCCESS, res);
    }

    @PostMapping("/index/search")
    public BaseResponse<ApiElectResult> selectIndexElects() {
        ApiElectResult res = service.selectCurDayElects();
        return res == null ? ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "无数据") :
                ResponseUtils.success(ResponseCode.SUCCESS, res);
    }
}
