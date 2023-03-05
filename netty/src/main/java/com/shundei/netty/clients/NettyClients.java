package com.shundei.netty.clients;


import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.dto.ElectParamsRequest;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 请求另外一个微服务的接口，这里的是服务名，在application.yml里面是spring.application.name
 *
 * @author huoyouri
 */
@FeignClient("webservice")
public interface NettyClients {

    @PostMapping("/environ/insert")
    BaseResponse<Boolean> insertEnvironParams(@RequestBody List<EnvironmentParamsRequest> requests);

    @PostMapping("/elect/insert")
    BaseResponse<Boolean> insertElectParams(@RequestBody List<ElectParamsRequest> requests);
}
