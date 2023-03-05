package com.shundei.netty.controller;

import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import com.tang.backendcommon.inner.InnerEnvironmentParamsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * 测试
 *
 * @author huoyouri
 */
@RestController
@EnableDubbo
@RequestMapping("/")
public class TestController {

    EnvironmentParamsRequest res = new EnvironmentParamsRequest(
            1,
            new BigDecimal("23.2"),
            new BigDecimal("35.9"),
            new BigDecimal("200.2"),
            2000,
            new Date(),
            false
    );
    
    @DubboReference
    private InnerEnvironmentParamsService innerEnvironmentParamsService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/test")
    public void test() {
        ArrayList<EnvironmentParamsRequest> request = new ArrayList<>();
        request.add(res);
        // restTemplate.postForObject("http://localhost:8081/environ/insert", request, BaseResponse.class);
        innerEnvironmentParamsService.insertEnvironParams(res);
    }
}
