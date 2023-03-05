package com.shundei.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shundei.web.common.PageParams;
import com.shundei.web.exception.BusinessException;
import com.shundei.web.mapper.ElectricityParamsMapper;
import com.shundei.web.model.domain.ElectricityParams;
import com.shundei.web.model.dto.request.elect.ApiElectPageRequest;
import com.shundei.web.model.dto.response.PageResult;
import com.shundei.web.model.dto.response.elect.ApiElectPageResult;
import com.shundei.web.model.dto.response.elect.ApiElectResult;
import com.shundei.web.service.ElectricityParamsService;
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
import java.util.Date;
import java.util.List;

/**
 * 针对表【electricity_params(电表)】的数据库操作Service实现
 *
 * @author huoyouri
 */
@Service
@Slf4j
public class ElectricityParamsServiceImpl extends ServiceImpl<ElectricityParamsMapper, ElectricityParams>
        implements ElectricityParamsService {

    @Resource
    private ElectricityParamsMapper mapper;

    @Override
    public PageResult<List<ApiElectPageResult>> selectElectPagesById(ApiElectPageRequest request) {
        // 判断请求体
        if (request == null) {
            log.info("page select elect params failed, request cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        // 后续待处理，目前不管
        Boolean isDelete = request.getIsDelete();
        String currentTime = request.getCurrentTime();
        Integer pageSize = request.getPageSize();
        String order = request.getOrder();
        String orderKey = request.getOrderKey();
        Integer pageNum = request.getPageNum();
        if (ObjectUtils.anyNull(pageSize, order, orderKey, pageNum)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "分页查询参数为空");
        }
        // 这里的wrapper必须有两个，不能一起用
        QueryWrapper<ElectricityParams> query1 = new QueryWrapper<>();
        QueryWrapper<ElectricityParams> query2 = new QueryWrapper<>();
        if (!StringUtils.isBlank(currentTime)) {
            // 这里比较数据库里面的datetime类型数据直接用string即可
            query1.likeRight("currentTime", currentTime);
            query2.likeRight("currentTime", currentTime);
        }
        int count = this.count(query1);
        if (count == 0) {
            // 查询出条数为空就返回
            log.info("page select res is 0");
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "分页查询结果为空");
        }
        if (PageParams.ORDER_ASC.equals(order)) {
            // 递增
            // condition是执行条件，满足orderKey是createTime执行，否则不执行
            query1.orderByAsc("createTime".equals(orderKey), "currentTime");
            query2.orderByAsc("createTime".equals(orderKey), "currentTime");
        } else if (PageParams.ORDER_DESC.equals(order)) {
            query1.orderByDesc("createTime".equals(orderKey), "currentTime");
            query2.orderByDesc("createTime".equals(orderKey), "currentTime");
        } else {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "分页查询排序方式出错");
        }
        Page<ElectricityParams> page = new Page(pageNum, pageSize);
        query1.eq("electricityID", 1);
        // 这个分页查出来的对象是固定的，即使查询条件发生了改变，结果也不会变，所以只是让res1和res2指向了相同的查询结果
        Page<ElectricityParams> res1 = mapper.selectPage(page, query1);
        long total1 = res1.getTotal();
        List<ElectricityParams> records1 = res1.getRecords();
        query2.eq("electricityID", 2);
        Page<ElectricityParams> res2 = mapper.selectPage(page, query2);
        long total2 = res2.getTotal();
        List<ElectricityParams> records2 = res2.getRecords();
        return transformToPageResult(total1, total2, records1, records2);
    }

    @Override
    public ApiElectResult selectCurDayElects() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        Date myTime = null;
        try {
            myTime = ft.parse("2022-10-05");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        BigDecimal decimal1 = mapper.selectCurDayElect(1, "2022-10-05");
        BigDecimal decimal2 = mapper.selectCurDayElect(2, ft.format(myTime));
        return new ApiElectResult(decimal1, decimal2);
    }

    /**
     * 将Page对象转换为需要的返回体
     *
     * @param total1,total2     总数
     * @param records1,records2 结果
     * @return 分页结果
     */
    private PageResult<List<ApiElectPageResult>> transformToPageResult(long total1, long total2, List<ElectricityParams> records1, List<ElectricityParams> records2) {
        if (ObjectUtils.anyNull(total1, total2, records1, records2)) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "分页查询结果为空");
        }
        if (records1.isEmpty() || records2.isEmpty()) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "分页查询结果为空");
        }
        List<ApiElectPageResult> finalRes = new ArrayList<>();
        int size = Math.min(records1.size(), records2.size());
        int i = 0, j = 0;
        for (i = 0, j = 0; i < size && j < size; i++, j++) {
            // TODO Bug, 没有解决，当此时时间有3个或更多时就会出现
            ElectricityParams record1 = records1.get(i);
            ElectricityParams record2 = records2.get(j);
            ApiElectPageResult res = new ApiElectPageResult();
            if (record1.getCurrentTime().compareTo(record2.getCurrentTime()) == 0) {
                log.info("ok");
                // 当前时间相等的情况
                res.setElect1(record1.getCapacity());
                res.setElect2(record2.getCapacity());
                res.setCreateTime(record1.getCurrentTime());
            } else if (record1.getCurrentTime().compareTo(record2.getCurrentTime()) < 0) {
                // 缺失了数据
                res.setElect1(record1.getCapacity());
                res.setElect2(null);
                res.setCreateTime(record1.getCurrentTime());
                j--;
            } else {
                // 缺失了数据
                res.setElect1(null);
                res.setElect2(record2.getCapacity());
                res.setCreateTime(record2.getCurrentTime());
                i--;
            }
            finalRes.add(res);
        }
        // 此时如果出现i和j数量不对等的情况，那么就应该加上剩下的
        if (i == size) {
            // 补充j
            while (j < size) {
                ElectricityParams param = records2.get(j);
                finalRes.add(new ApiElectPageResult(
                        null,
                        param.getCapacity(),
                        param.getCurrentTime()
                ));
                j++;
            }
        }
        if (j == size) {
            // 补充i
            while (i < size) {
                ElectricityParams param = records1.get(i);
                finalRes.add(new ApiElectPageResult(
                        param.getCapacity(),
                        null,
                        param.getCurrentTime()
                ));
                i++;
            }
        }
        // 筛选查询出来的条数
        int max = Math.max(records1.size(), records2.size());
        while (finalRes.size() > max) {
            finalRes.remove(finalRes.size() - 1);
        }
        return new PageResult(finalRes, Math.max(total1, total2));
    }
}




