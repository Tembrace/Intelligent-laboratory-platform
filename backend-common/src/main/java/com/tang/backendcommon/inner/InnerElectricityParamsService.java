package com.tang.backendcommon.inner;


import com.tang.backendcommon.dto.ElectParamsRequest;

/**
 * 电表服务
 * 
 * @author huoyouri
 */
public interface InnerElectricityParamsService {
    /**
     * 插入电表数据
     *
     * @param request 电表参数请求体
     * @return 是否成功
     */
    boolean insertElectParams(ElectParamsRequest request);
}
