package com.shundei.netty;

import com.shundei.netty.clients.NettyClientsService;
import com.shundei.netty.common.TimeStringToBytes;
import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import com.tang.backendcommon.inner.InnerEnvironmentParamsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@EnableDubbo
@Service
class NettyApplicationTests {

    @Autowired
    private NettyClientsService nettyClientsService;

    @Autowired
    private RestTemplate restTemplate;

    @DubboReference
    private InnerEnvironmentParamsService innerEnvironmentParamsService;

    @Test
    void contextLoads() {
        List<EnvironmentParamsRequest> request = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            EnvironmentParamsRequest res = new EnvironmentParamsRequest();
            res.setId(1);
            res.setTemperature(new BigDecimal("23.2"));
            res.setHumidity(new BigDecimal("35.9"));
            res.setPm2_5(new BigDecimal("200.2"));
            res.setCo2(2000);
            res.setIsFF(false);
            res.setCurrentTime(new Date());
            request.add(res);
        }
        long feignStart = System.currentTimeMillis();
        // feign调用
        BaseResponse object = restTemplate.postForObject("http://localhost:8081/environ/insert", request, BaseResponse.class);
        long feignEnd = System.currentTimeMillis();
        // rpc调用
        long rpcStart = System.currentTimeMillis();
        innerEnvironmentParamsService.insertEnvironParams(request.get(0));
        long rpcEnd = System.currentTimeMillis();
        System.out.println("feign: " + (feignEnd - feignStart));
        System.out.println("rpc: " + (rpcEnd - rpcStart));
    }


    @Test
    void testDate() {
        TimeStringToBytes tb = new TimeStringToBytes();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.OCTOBER, 13, 14, 59, 43);
        byte[] bytes = tb.curTransformToBytes();
        System.out.println(Arrays.toString(bytes));
    }
}
