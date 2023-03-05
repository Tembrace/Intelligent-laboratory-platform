package com.shundei.web.controller;


import com.shundei.web.exception.BusinessException;
import com.shundei.web.model.dto.request.environ.ApiEnvironPageRequest;
import com.shundei.web.model.dto.request.environ.ApiEnvironRequest;
import com.shundei.web.model.dto.response.PageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironPageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironResult;
import com.shundei.web.service.EnvironmentParamsService;
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
 * 网页访问环境表接口
 *
 * @author huoyouri
 */
@RestController
@RequestMapping("/api/environ")
public class ApiEnvironController {
    @Resource
    private EnvironmentParamsService service;

    @PostMapping("/search")
    public BaseResponse<PageResult<List<ApiEnvironPageResult>>> selectEnvironsPage(@RequestBody ApiEnvironPageRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求参数为空");
        }
        PageResult<List<ApiEnvironPageResult>> res = service.selectEnvironPagesById(request);
        return res == null ? ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "无数据") :
                ResponseUtils.success(ResponseCode.SUCCESS, res);
    }

    @PostMapping("/index/search")
    public BaseResponse<ApiEnvironResult> selectIndexEnvirons(@RequestBody ApiEnvironRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求参数为空");
        }
        ApiEnvironResult res = service.selectEnvironById(request);
        return res == null ? ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "无数据") :
                ResponseUtils.success(ResponseCode.SUCCESS, res);
    }
}
