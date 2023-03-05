package com.shundei.netty.clients;

import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.dto.ElectParamsRequest;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 和另外web微服务的通信实现
 *
 * @author huoyouri
 */
@Service
@Slf4j
public class NettyClientsService {

    @Autowired
    private NettyClients nettyClients;

    public boolean insertEnviron(List<EnvironmentParamsRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            log.info("the requests has no data");
            throw new RuntimeException("传递的环境表参数为空");
        }
        BaseResponse<Boolean> response = nettyClients.insertEnvironParams(requests);
        // 其余微服务之前通信应该有自己的协议，这里直接就借用了和前端的通信协议
        // TODO 插入失败后应该返回重新需要插入的帧，这里没做
        return response.getCode() == 20000;
    }

    public boolean insertElect(List<ElectParamsRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            log.info("the requests has no data");
            throw new RuntimeException("传递的电表参数为空");
        }
        BaseResponse<Boolean> response = nettyClients.insertElectParams(requests);
        // 其余微服务之前通信应该有自己的协议，这里直接就借用了和前端的通信协议
        // TODO 插入失败后应该返回重新需要插入的帧，这里没做
        return response.getCode() == 20000;
    }
}
