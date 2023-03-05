package com.shundei.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shundei.web.exception.BusinessException;
import com.shundei.web.mapper.ElectricityParamsMapper;
import com.shundei.web.mapper.EnvironmentParamsMapper;
import com.shundei.web.mapper.UserMapper;
import com.shundei.web.model.domain.ElectricityParams;
import com.shundei.web.model.domain.EnvironmentParams;
import com.shundei.web.model.domain.User;
import com.shundei.web.model.dto.request.OutputDataRequest;
import com.shundei.web.model.dto.request.user.ApiUserUpdateRequest;
import com.shundei.web.model.dto.response.elect.ApiElectPageResult;
import com.shundei.web.model.dto.response.environ.ApiEnvironPageResult;
import com.shundei.web.model.dto.response.user.ApiSafeUser;
import com.shundei.web.service.UserService;
import com.tang.backendcommon.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shundei.web.constant.UserConstant.LOGIN_STATE;

/**
 * 用户业务
 *
 * @author huoyouri
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 加密，后续要随机盐
     */
    private static final String SALT = "huoyouri";


    /**
     * 最短账号长度
     */
    @Value("${user.account.minlen}")
    private int minUserAccountLen;

    /**
     * 最长账号长度
     */
    @Value("${user.account.maxlen}")
    private int maxUserAccountLen;

    /**
     * 最短密码长度
     */
    @Value("${user.password.minlen}")
    private int minPasswordLen;

    /**
     * 最长密码长度
     */
    @Value("${user.password.maxlen}")
    private int maxPasswordLen;

    /**
     * 密码是否一定包含特殊字符
     */
    @Value("${user.password.contain-special-characters}")
    private boolean isContainSpecialCharacter;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ElectricityParamsMapper electMapper;

    @Resource
    private EnvironmentParamsMapper environMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPass) {
        // 非空校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPass)) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "数据为空");
        }
        // 长度校验
        if (userAccount.length() < minUserAccountLen || userAccount.length() > maxUserAccountLen
                || userPassword.length() < minPasswordLen || userPassword.length() > maxPasswordLen
                || checkPass.length() < minPasswordLen || checkPass.length() > maxPasswordLen) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "长度错误");
        }
        // 账户不包含特殊字符,正则表达式,密码也需要校验，字母数字特殊字符
        final String REG_ACCOUNT = "^[a-zA-Z][-_a-zA-Z0-9]{" + minUserAccountLen + "," + maxUserAccountLen + "}$";
        Matcher matcher = Pattern.compile(REG_ACCOUNT).matcher(userAccount);
        if (!matcher.find()) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "账户不包含特殊字符");
        }
        // 密码必须包含字母数字，可以选择性开关是否必须包含特殊字符
        final String REG_PASSWORD = isContainSpecialCharacter ?
                "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\\\W]+$)(?![a-zA-Z\\\\W]+$)[0-9A-Za-z\\\\W]{" + minPasswordLen + "," + maxPasswordLen + "}$" :
                "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{" + minPasswordLen + "," + maxPasswordLen + "}$";
        matcher = Pattern.compile(REG_PASSWORD).matcher(userPassword);
        if (!matcher.find()) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, isContainSpecialCharacter ? "密码必须包含字母数字特殊字符" : "密码必须包含字母数字");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPass)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "两次密码不同");
        }
        // 账户不能重复
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("userAccount", userAccount);
        long count = this.count(query);
        if (count > 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "账户不能重复");
        }
        // md5加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        // 插入失败会回滚吗？
        boolean res = this.save(user);
        if (!res) {
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "系统内部异常");
        }
        return user.getId();
    }

    @Override
    public ApiSafeUser userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // // 非空校验
        // if (StringUtils.isAnyBlank(userAccount, userPassword)) {
        //     throw new BusinessException(ResponseCode.NULL_ERROR, "数据为空");
        // }
        // // 长度校验
        // if (userAccount.length() < minUserAccountLen || userAccount.length() > maxUserAccountLen
        //         || userPassword.length() < minPasswordLen || userPassword.length() > maxPasswordLen) {
        //     throw new BusinessException(ResponseCode.PARAMS_ERROR, "长度错误");
        // }
        // // 账户不包含特殊字符,正则表达式,密码也需要校验，字母数字特殊字符
        // final String REG_ACCOUNT = "^[a-zA-Z][-_a-zA-Z0-9]{" + minUserAccountLen + "," + maxUserAccountLen + "}$";
        // Matcher matcher = Pattern.compile(REG_ACCOUNT).matcher(userAccount);
        // if (!matcher.find()) {
        //     throw new BusinessException(ResponseCode.PARAMS_ERROR, "账户不对");
        // }
        // // 密码必须包含字母数字，可以选择性开关是否必须包含特殊字符
        // final String REG_PASSWORD = isContainSpecialCharacter ?
        //         "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\\\W]+$)(?![a-zA-Z\\\\W]+$)[0-9A-Za-z\\\\W]{" + minPasswordLen + "," + maxPasswordLen + "}$" :
        //         "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{" + minPasswordLen + "," + maxPasswordLen + "}$";
        // matcher = Pattern.compile(REG_PASSWORD).matcher(userPassword);
        // if (!matcher.find()) {
        //     throw new BusinessException(ResponseCode.PARAMS_ERROR, "密码不对");
        // }
        // // 加密
        // String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("userAccount", userAccount);
        query.eq("userPassword", userPassword);
        User user = userMapper.selectOne(query);
        if (user == null) {
            // 添加日志
            log.info("user login failed, userAccount cannot match password");
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "账户密码不对");
        }
        // 脱敏，选择信息返回
        ApiSafeUser apiSafeUser = getCleanUser(user);
        // 记录用户登录态
        request.getSession().setAttribute(LOGIN_STATE, apiSafeUser);
        return apiSafeUser;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request == null) {
            log.info("user logout failed, request cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        request.getSession().removeAttribute(LOGIN_STATE);
        return true;
    }

    @Override
    public boolean userUpdate(ApiUserUpdateRequest updateUser) {
        if (updateUser == null) {
            log.info("user update failed, updateUser cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        // updateUser需要判断，先取出值
        long id = updateUser.getId();
        String username = updateUser.getUsername();
        int gender = updateUser.getGender();
        String phone = updateUser.getPhone();
        String email = updateUser.getEmail();
        String avatarUrl = updateUser.getAvatarUrl();
        log.info("url:{}", avatarUrl);
        if (StringUtils.isAnyBlank(username, phone, email, avatarUrl)) {
            log.info("user update failed, updateUser cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        // TODO 用户更新,注意校验参数的合法性
        return false;
    }

    @Override
    public boolean userUpload(String filename, String file) {
        if (StringUtils.isAnyBlank(file, filename)) {
            log.info("user upload failed, file cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        // TODO 用户上传
        return false;
    }

    /**
     * 下载报表，还是分开下载
     *
     * @param request 请求
     * @return
     */
    @Override
    public List userDownload(OutputDataRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        String endTime = request.getEndTime();
        String startTime = request.getStartTime();
        Boolean isElect = request.getIsElect();
        Boolean isEnviron = request.getIsEnviron();
        if (ObjectUtils.anyNull(endTime, startTime, isEnviron, isElect)) {
            log.info("user download failed, request cannot be null");
            throw new BusinessException(ResponseCode.NULL_ERROR, "下载参数为空");
        }
        if (isElect.equals(isEnviron)) {
            log.info("user download failed, meter types must be choose");
            throw new BusinessException(ResponseCode.NULL_ERROR, "未选择导出的数据表类型");
        }
        // 按时间排序
        QueryWrapper<ElectricityParams> electQuery = null;
        QueryWrapper<EnvironmentParams> environQuery = null;
        List<ElectricityParams> electRes = null;
        List<EnvironmentParams> environRes = null;
        if (isElect) {
            electQuery = new QueryWrapper<>();
            electQuery.between("currentTime", startTime, endTime);
            electQuery.orderByDesc("currentTime");
            electRes = electMapper.selectList(electQuery);
            List<ApiElectPageResult> res = new ArrayList<>();
            for (int i = 0; i < electRes.size(); i += 2) {
                ApiElectPageResult tmp = new ApiElectPageResult();
                ElectricityParams params1 = electRes.get(i);
                ElectricityParams params2 = electRes.get(i + 1);
                if (params1.getCurrentTime() != params2.getCurrentTime()) {
                    log.info("elect data lack, download elect data failed");
                    throw new BusinessException(ResponseCode.SYSTEM_ERROR, "电表数据缺失");
                }
                tmp.setCreateTime(params1.getCurrentTime());
                if (params1.getElectricityID() == 1 && params2.getElectricityID() == 2) {
                    tmp.setElect1(params1.getCapacity());
                    tmp.setElect2(params2.getCapacity());
                } else if (params1.getElectricityID() == 2 && params2.getElectricityID() == 1) {
                    tmp.setElect1(params2.getCapacity());
                    tmp.setElect2(params1.getCapacity());
                } else {
                    log.info("elect data lack, download elect data failed");
                    throw new BusinessException(ResponseCode.SYSTEM_ERROR, "电表数据缺失");
                }
                res.add(tmp);
            }
            return res;
        }
        if (isEnviron) {
            environQuery = new QueryWrapper<>();
            environQuery.between("currentTime", startTime, endTime);
            environQuery.orderByDesc("currentTime");
            environRes = environMapper.selectList(environQuery);
            List<ApiEnvironPageResult> res = new ArrayList<>();
            for (int i = 0; i < environRes.size(); i++) {
                EnvironmentParams params = environRes.get(i);
                res.add(new ApiEnvironPageResult(
                        params.getEnvironmentID(),
                        params.getTemperature(),
                        params.getHumidity(),
                        params.getCo2(),
                        params.getPm2_5(),
                        params.getCurrentTime()
                ));
            }
            return res;
        }
        return null;
    }


    @Override
    public ApiSafeUser getCleanUser(User user) {
        if (user == null) {
            log.info("get safe user failed, user cannot be null");
            throw new BusinessException(ResponseCode.SYSTEM_ERROR);
        }
        ApiSafeUser apiSafeUser = new ApiSafeUser();
        apiSafeUser.setId(user.getId());
        apiSafeUser.setUsername(user.getUsername());
        apiSafeUser.setUserAccount(user.getUserAccount());
        apiSafeUser.setGender(user.getGender());
        apiSafeUser.setPhone(user.getPhone());
        apiSafeUser.setEmail(user.getEmail());
        apiSafeUser.setAvatarUrl(user.getAvatarUrl());
        apiSafeUser.setUserStatus(user.getUserStatus());
        apiSafeUser.setCreateTime(user.getCreateTime());
        apiSafeUser.setUserRole(user.getUserRole());
        return apiSafeUser;
    }
}




