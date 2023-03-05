package com.tang.backendcommon.inner;


import com.tang.backendcommon.dto.EnvironmentParamsRequest;

/**
 * 环境表服务
 *
 * @author huoyouri
 */
public interface InnerEnvironmentParamsService {

    /**
     * 插入环境表数据
     *
     * @param request 环境表参数请求体
     * @return 是否成功
     */
    boolean insertEnvironParams(EnvironmentParamsRequest request);
}
