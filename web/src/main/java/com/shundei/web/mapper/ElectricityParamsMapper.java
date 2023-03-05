package com.shundei.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shundei.web.model.domain.ElectricityParams;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 针对表【electricity_params(电表)】的数据库操作Mapper
 *
 * @author huoyouri
 */
public interface ElectricityParamsMapper extends BaseMapper<ElectricityParams> {
    /**
     * 查询今天的电表电量和
     *
     * @param electId 电表ID
     * @param curTime 时间
     * @return 和
     */
    @Select("select sum(capacity) from shundei.electricity_params " +
            "where electricityID = ${electId} and currentTime like '${curTime}%'")
    BigDecimal selectCurDayElect(@Param("electId") int electId, @Param("curTime") String curTime);

    /**
     * 查找当前电表最大时间的记录
     *
     * @param electId 电表ID
     * @return 记录
     */
    @Select("select * from shundei.electricity_params where electricityID = ${electId}" +
            " and currentTime = (select max(currentTime) from shundei.electricity_params " +
            "where electricityID = ${electId})")
    ElectricityParams selectCurTimeById(@Param("electId") int electId);
}




