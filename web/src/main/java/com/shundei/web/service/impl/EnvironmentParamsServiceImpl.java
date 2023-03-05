package com.shundei.web.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shundei.web.common.PageParams;
import com.shundei.web.exception.BusinessException;
import com.shundei.web.mapper.EnvironmentParamsMapper;
import com.shundei.web.model.domain.EnvironmentParams;
import com.shundei.web.model.dto.request.environ.ApiEnvironPageRequest;
import com.shundei.web.model.dto.request.environ.ApiEnvironRequest;
import com.shundei.web.model.dto.response.PageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironHalfDay;
import com.shundei.web.model.dto.response.environ.ApiEnvironHalfTime;
import com.shundei.web.model.dto.response.environ.ApiEnvironPageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironResult;
import com.shundei.web.service.EnvironmentParamsService;
import com.tang.backendcommon.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 针对表【environment_params(环境表)】的数据库操作Service实现
 *
 * @author huoyouri
 */
@Service
@Slf4j
public class EnvironmentParamsServiceImpl extends ServiceImpl<EnvironmentParamsMapper, EnvironmentParams>
        implements EnvironmentParamsService {

    @Resource
    private EnvironmentParamsMapper mapper;

    
    @Override
    public PageResult<List<ApiEnvironPageResult>> selectEnvironPagesById(ApiEnvironPageRequest request) {
        // 判断请求体
        if (request == null) {
            log.info("page select environment params failed, request cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        // 查询环境表数据
        Integer environId = request.getEnvironId();
        if (environId == null || environId > 5 || environId < 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "环境表ID错误");
        }
        String currentTime = request.getCurrentTime();
        Integer pageSize = request.getPageSize();
        String order = request.getOrder();
        String orderKey = request.getOrderKey();
        Integer pageNum = request.getPageNum();
        if (ObjectUtils.anyNull(pageSize, order, orderKey, pageNum)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "分页查询参数为空");
        }
        QueryWrapper<EnvironmentParams> query = new QueryWrapper<>();
        query.eq("environmentID", environId);
        if (!StringUtils.isBlank(currentTime)) {
            // 这里比较数据库里面的datetime类型数据直接用string即可
            query.likeRight("currentTime", currentTime);
        }
        int count = this.count(query);
        if (count == 0) {
            // 查询出条数为空就返回
            log.info("page select res is 0");
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "分页查询结果为空");
        }
        if (PageParams.ORDER_ASC.equals(order)) {
            // 递增
            // condition是执行条件，满足orderKey是createTime执行，否则不执行
            query.orderByAsc("createTime".equals(orderKey), "currentTime");
        } else if (PageParams.ORDER_DESC.equals(order)) {
            query.orderByDesc("createTime".equals(orderKey), "currentTime");
        } else {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "分页查询排序方式出错");
        }
        Page<EnvironmentParams> page = new Page(pageNum, pageSize);
        Page<EnvironmentParams> res = this.page(page, query);
        return transformToPageResult(res);
    }

    @Override
    public ApiEnvironResult selectEnvironById(ApiEnvironRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "首页环境表参数请求为空");
        }
        Integer environId = request.getEnvironId();
        Integer halfHourPoint = request.getHalfHourPoint();
        Integer halfDayPoint = request.getHalfDayPoint();
        String currentTime = request.getCurrentTime();
        if (ObjectUtils.anyNull(environId, halfHourPoint, halfDayPoint, currentTime)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "首页环境表参数为空");
        }
        QueryWrapper<EnvironmentParams> query1 = new QueryWrapper<>();
        QueryWrapper<EnvironmentParams> query2 = new QueryWrapper<>();
        query1.eq("environmentID", environId);
        query2.eq("environmentID", environId);
        query1.orderByDesc("currentTime");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        try {
            Date time = ft.parse(currentTime);
            cal.setTime(time);
            cal.add(Calendar.DATE, -1 * (halfDayPoint / 2));
            String pre = ft.format(cal.getTime());
            log.info("transform to string date: {}", pre);
            cal.setTime(time);
            cal.add(Calendar.DATE, 1);
            String next = ft.format(cal.getTime());
            log.info("transform to string date: {}", next);
            query2.between("currentTime", pre, next);
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("String parse to Date type failed, reason: {}", e.getMessage());
        }
        query2.orderByAsc("currentTime");
        // 下面空格很重要
        query1.last("limit " + halfHourPoint);
        List<EnvironmentParams> res1 = this.list(query1);
        // 直接进行结果的封装
        List<ApiEnvironHalfTime> totalRes1 = transform(res1);
        log.info("1:{}", totalRes1.size());
        List<EnvironmentParams> res2 = this.list(query2);
        log.info("2:{}", res2.size());
        List<ApiEnvironHalfDay> totalRes2 = transformAver(res2);
        return new ApiEnvironResult(totalRes1, totalRes2);
    }

    /**
     * 将结果转换为半小时的结果
     *
     * @param params 请求
     * @return 结果
     */
    private List<ApiEnvironHalfTime> transform(List<EnvironmentParams> params) {
        if (params == null) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "环境表查询结果为空");
        }
        List<ApiEnvironHalfTime> res = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = params.size() - 1; i >= 0; i--) {
            EnvironmentParams param = params.get(i);
            String time = ft.format(param.getCurrentTime());
            res.add(new ApiEnvironHalfTime(
                    time,
                    param.getTemperature().setScale(2, BigDecimal.ROUND_HALF_UP),
                    param.getHumidity().setScale(2, BigDecimal.ROUND_HALF_UP),
                    param.getCo2(),
                    param.getPm2_5().setScale(2, BigDecimal.ROUND_HALF_UP)
            ));
        }
        return res;
    }

    /**
     * 将结果转换为半天均值的结果
     *
     * @param params 请求
     * @return 均值结果
     */
    private List<ApiEnvironHalfDay> transformAver(List<EnvironmentParams> params) {
        if (params == null) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "环境表查询结果为空");
        }
        // 半天12个小时，24个值,升序的时间结果
        List<ApiEnvironHalfDay> res = new ArrayList<>();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        // 这里传进来的长度可能为0，所以这里需要单独处理
        if (params.isEmpty()) {
            // 这里不抛异常是因为本来就可以返回空的
            return new ArrayList<>();
        }
        String preTime = ft.format(params.get(0).getCurrentTime());
        BigDecimal preSumTA = new BigDecimal("0");
        BigDecimal preSumTP = new BigDecimal("0");
        BigDecimal preSumHA = new BigDecimal("0");
        BigDecimal preSumHP = new BigDecimal("0");
        int preSumCA = 0;
        int preSumCP = 0;
        BigDecimal preSumPA = new BigDecimal("0");
        BigDecimal preSumPP = new BigDecimal("0");
        int preCountA = 0;
        int preCountP = 0;
        for (int i = 0; i < params.size(); i++) {
            EnvironmentParams param = params.get(i);
            Date time = param.getCurrentTime();
            String curTime = ft.format(time);
            if (preTime.equals(curTime)) {
                if (DateUtil.isAM(time)) {
                    // 上午
                    preSumTA = preSumTA.add(param.getTemperature());
                    preSumHA = preSumHA.add(param.getHumidity());
                    preSumCA += param.getCo2();
                    preSumPA = preSumPA.add(param.getPm2_5());
                    preCountA++;
                } else {
                    // 下午
                    preSumTP = preSumTP.add(param.getTemperature());
                    preSumHP = preSumHP.add(param.getHumidity());
                    preSumCP += param.getCo2();
                    preSumPP = preSumPP.add(param.getPm2_5());
                    preCountP++;
                }
            } else {
                // 新的一天,结算前一天，清零
                if (preCountA != 0) {
                    ApiEnvironHalfDay halfDayA = new ApiEnvironHalfDay();
                    halfDayA.setIsAm(true);
                    halfDayA.setTime(preTime);
                    halfDayA.setAverTemp(preSumTA.divide(BigDecimal.valueOf(preCountA)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    halfDayA.setAverHum(preSumHA.divide(BigDecimal.valueOf(preCountA)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    halfDayA.setAverCo2(preSumCA / preCountA);
                    halfDayA.setAverPm25(preSumPA.divide(BigDecimal.valueOf(preCountA)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    res.add(halfDayA);
                }
                if (preCountP != 0) {
                    ApiEnvironHalfDay halfDayP = new ApiEnvironHalfDay();
                    halfDayP.setIsAm(false);
                    halfDayP.setTime(preTime);
                    halfDayP.setAverTemp(preSumTP.divide(BigDecimal.valueOf(preCountP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    halfDayP.setAverHum(preSumHP.divide(BigDecimal.valueOf(preCountP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    halfDayP.setAverCo2(preSumCP / preCountP);
                    halfDayP.setAverPm25(preSumPP.divide(BigDecimal.valueOf(preCountP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                    res.add(halfDayP);
                }
                preSumTA = new BigDecimal("0");
                preSumTP = new BigDecimal("0");
                preSumHA = new BigDecimal("0");
                preSumHP = new BigDecimal("0");
                preSumCA = 0;
                preSumCP = 0;
                preSumPA = new BigDecimal("0");
                preSumPP = new BigDecimal("0");
                preCountA = 0;
                preCountP = 0;
            }
            preTime = curTime;
        }
        // 整理剩下的部分
        if (preCountA != 0) {
            // 先看上午
            ApiEnvironHalfDay halfDayA = new ApiEnvironHalfDay();
            halfDayA.setIsAm(true);
            halfDayA.setTime(preTime);
            halfDayA.setAverTemp(preSumTA.divide(BigDecimal.valueOf(preCountA)).setScale(2, BigDecimal.ROUND_HALF_UP));
            halfDayA.setAverHum(preSumHA.divide(BigDecimal.valueOf(preCountA)).setScale(2, BigDecimal.ROUND_HALF_UP));
            halfDayA.setAverCo2(preSumCA / preCountA);
            halfDayA.setAverPm25(preSumPA.divide(BigDecimal.valueOf(preCountA)).setScale(2, BigDecimal.ROUND_HALF_UP));
            res.add(halfDayA);
        }
        if (preCountP != 0) {
            // 下午
            ApiEnvironHalfDay halfDayP = new ApiEnvironHalfDay();
            halfDayP.setIsAm(false);
            halfDayP.setTime(preTime);
            halfDayP.setAverTemp(preSumTP.divide(BigDecimal.valueOf(preCountP)).setScale(2, BigDecimal.ROUND_HALF_UP));
            halfDayP.setAverHum(preSumHP.divide(BigDecimal.valueOf(preCountP)).setScale(2, BigDecimal.ROUND_HALF_UP));
            halfDayP.setAverCo2(preSumCP / preCountP);
            halfDayP.setAverPm25(preSumPP.divide(BigDecimal.valueOf(preCountP)).setScale(2, BigDecimal.ROUND_HALF_UP));
            res.add(halfDayP);
        }
        return res;
    }

    /**
     * 将Page对象转换为需要的返回体
     *
     * @param params
     * @return
     */
    private PageResult<List<ApiEnvironPageResult>> transformToPageResult(Page<EnvironmentParams> params) {
        if (params == null) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "分页查询结果为空");
        }
        long total = params.getTotal();
        List<EnvironmentParams> records = params.getRecords();
        List<ApiEnvironPageResult> res = new ArrayList<>();
        for (EnvironmentParams record : records) {
            res.add(new ApiEnvironPageResult(record.getEnvironmentID(),
                    record.getTemperature(), record.getHumidity(),
                    record.getCo2(), record.getPm2_5(),
                    record.getCurrentTime()));
        }
        return new PageResult(res, total);
    }
}




