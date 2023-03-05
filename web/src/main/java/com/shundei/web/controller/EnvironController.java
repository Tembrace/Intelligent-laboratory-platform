package com.shundei.web.controller;


import com.shundei.web.exception.BusinessException;
import com.shundei.web.utils.ResponseUtils;
import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.ResponseCode;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import com.tang.backendcommon.inner.InnerEnvironmentParamsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 环境表接口
 *
 * @author huoyouri
 */
@RestController
@RequestMapping("/environ")
public class EnvironController {
    @Resource
    private InnerEnvironmentParamsService service;

    @PostMapping("/insert")
    public BaseResponse<Boolean> insertEnvironParams(@RequestBody List<EnvironmentParamsRequest> requests) {
        if (requests == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "参数为空");
        }
        boolean b = true;
        for (EnvironmentParamsRequest request : requests) {
            if (request == null) {
                throw new BusinessException(ResponseCode.NULL_ERROR, "参数为空");
            }
            service.insertEnvironParams(request);
        }
        return ResponseUtils.success(ResponseCode.SUCCESS, true);
    }
}
