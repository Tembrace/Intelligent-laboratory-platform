package com.shundei.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shundei.web.model.domain.EnvironmentParams;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 针对表【environment_params(环境表)】的数据库操作Mapper
 *
 * @author huoyouri
 */
public interface EnvironmentParamsMapper extends BaseMapper<EnvironmentParams> {

    /**
     * 查找当前环境表最大时间的记录
     * BUG记录，这里因为没固定标号，导致先插入了一些比较大的时间在配上表号就找不到了，
     * 必须在子查询中加入表号的限制
     *
     * @param environId 环境表ID
     * @return 记录
     */
    @Select("select * from shundei.environment_params where environmentID = ${environId}" +
            " and currentTime = (select max(currentTime) from shundei.environment_params " +
            "where environmentID = ${environId})")
    EnvironmentParams selectCurTimeById(@Param("environId") int environId);
}




