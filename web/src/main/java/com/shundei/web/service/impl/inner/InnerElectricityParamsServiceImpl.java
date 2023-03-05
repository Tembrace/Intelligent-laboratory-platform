package com.shundei.web.service.impl.inner;

import com.shundei.web.common.DateToSqldatetime;
import com.shundei.web.exception.BusinessException;
import com.shundei.web.mapper.ElectricityParamsMapper;
import com.shundei.web.model.domain.ElectricityParams;
import com.tang.backendcommon.ResponseCode;
import com.tang.backendcommon.dto.ElectParamsRequest;
import com.tang.backendcommon.inner.InnerElectricityParamsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 电表服务实现
 *
 * @author huoyouri
 */
@DubboService
@Slf4j
public class InnerElectricityParamsServiceImpl implements InnerElectricityParamsService {

    @Resource
    private ElectricityParamsMapper mapper;

    @Override
    public boolean insertElectParams(ElectParamsRequest request) {
        // 判断请求体
        if (request == null) {
            log.info("insert elect params failed, request cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        Integer electId = request.getId();
        Date time = request.getCurrentTime();
        Boolean isFF = request.getIsFF();
        if (ObjectUtils.anyNull(electId, isFF, time)) {
            log.info("insert elect params failed, params cannot be null");
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "参数为空");
        }
        if (Boolean.TRUE.equals(isFF)) {
            // 空值，插入最大时刻的值，如果没有就不插入
            ElectricityParams elect = null;
            synchronized (ElectParamsRequest.class) {
                elect = mapper.selectCurTimeById(electId);
            }
            if (elect == null) {
                log.info("select currentTime elect params failed, the electId params not exist");
                return true;
            }
            ElectricityParams param = new ElectricityParams();
            param.setElectricityID(electId);
            param.setCapacity(elect.getCapacity());
            param.setCurrentTime(DateToSqldatetime.dateTodatetime(time));
            int res = 0;
            synchronized (ElectParamsRequest.class) {
                res = mapper.insert(param);
            }
            if (res == 0) {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR, "系统内部错误: 插入电表数据失败:" + param);
            }
            return true;
        }
        BigDecimal capacity = request.getCapacity();
        if (capacity == null) {
            log.info("insert elect params failed, params cannot be null");
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "参数为空");
        }
        ElectricityParams params = new ElectricityParams();
        params.setElectricityID(electId);
        params.setCapacity(capacity);
        // 注意在数据库业务这里进行Date转datetime类型的数据
        params.setCurrentTime(DateToSqldatetime.dateTodatetime(time));
        int res = 0;
        synchronized (ElectParamsRequest.class) {
            res = mapper.insert(params);
        }
        if (res == 0) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "系统内部错误: 插入电表数据失败:" + params);
        }
        return true;
    }
}
