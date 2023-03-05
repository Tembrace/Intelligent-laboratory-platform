package com.shundei.web.controller;

import com.shundei.web.exception.BusinessException;
import com.shundei.web.utils.ResponseUtils;
import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.ResponseCode;
import com.tang.backendcommon.dto.ElectParamsRequest;
import com.tang.backendcommon.inner.InnerElectricityParamsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 电表接口
 *
 * @author huoyouri
 */
@RestController
@RequestMapping("/elect")
public class ElectController {

    @Resource
    private InnerElectricityParamsService service;

    @PostMapping("/insert")
    public BaseResponse<Boolean> insertElectParams(@RequestBody List<ElectParamsRequest> requests) {
        if (requests == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "参数为空");
        }
        boolean b = true;
        for (ElectParamsRequest request : requests) {
            if (request == null) {
                throw new BusinessException(ResponseCode.NULL_ERROR, "参数为空");
            }
            service.insertElectParams(request);
        }
        return ResponseUtils.success(ResponseCode.SUCCESS, true);
    }
}
