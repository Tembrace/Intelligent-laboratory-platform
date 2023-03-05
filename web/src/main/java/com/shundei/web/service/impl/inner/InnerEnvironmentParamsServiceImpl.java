package com.shundei.web.service.impl.inner;

import com.shundei.web.common.DateToSqldatetime;
import com.shundei.web.exception.BusinessException;
import com.shundei.web.mapper.EnvironmentParamsMapper;
import com.shundei.web.model.domain.EnvironmentParams;
import com.tang.backendcommon.ResponseCode;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import com.tang.backendcommon.inner.InnerEnvironmentParamsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 环境表实现类
 *
 * @author huoyouri
 */
@Slf4j
@DubboService
public class InnerEnvironmentParamsServiceImpl implements InnerEnvironmentParamsService {

    @Resource
    private EnvironmentParamsMapper mapper;

    @Override
    public boolean insertEnvironParams(EnvironmentParamsRequest request) {
        // 判断请求体
        if (request == null) {
            log.info("insert environment params failed, request cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        Integer environId = request.getId();
        Date time = request.getCurrentTime();
        Boolean isFF = request.getIsFF();
        if (ObjectUtils.anyNull(environId, isFF, time)) {
            log.info("insert environment params failed, params cannot be null");
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "参数为空");
        }
        if (Boolean.TRUE.equals(isFF)) {
            // 空值，插入最大时刻的值，如果没有就不插入
            EnvironmentParams environ = null;
            synchronized (EnvironmentParamsRequest.class) {
                environ = mapper.selectCurTimeById(environId);
            }
            if (environ == null) {
                log.info("select currentTime environment params failed, the environId params not exist");
                return true;
            }
            EnvironmentParams param = new EnvironmentParams();
            param.setEnvironmentID(environId);
            param.setTemperature(environ.getTemperature());
            param.setHumidity(environ.getHumidity());
            param.setPm2_5(environ.getPm2_5());
            param.setCo2(environ.getCo2());
            param.setCurrentTime(DateToSqldatetime.dateTodatetime(time));
            int res = 0;
            synchronized (EnvironmentParamsRequest.class) {
                res = mapper.insert(param);
            }
            if (res == 0) {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR, "系统内部错误: 插入环境表表数据失败:" + param);
            }
            return true;
        }
        BigDecimal temperature = request.getTemperature();
        BigDecimal humidity = request.getHumidity();
        BigDecimal pm2_5 = request.getPm2_5();
        Integer co2 = request.getCo2();
        if (ObjectUtils.anyNull(temperature, humidity, pm2_5, co2)) {
            log.info("insert environment params failed, params cannot be null");
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "参数为空");
        }
        EnvironmentParams params = new EnvironmentParams();
        params.setEnvironmentID(environId);
        params.setTemperature(temperature);
        params.setHumidity(humidity);
        params.setPm2_5(pm2_5);
        params.setCo2(co2);
        // 注意在数据库业务这里进行Date转datetime类型的数据
        params.setCurrentTime(DateToSqldatetime.dateTodatetime(time));
        int res = 0;
        synchronized (EnvironmentParamsRequest.class) {
            res = mapper.insert(params);
        }
        if (res == 0) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "系统内部错误: 插入环境表表数据失败:" + params);
        }
        return true;
    }
}
