package com.shundei.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shundei.web.model.domain.User;
import com.shundei.web.model.dto.request.OutputDataRequest;
import com.shundei.web.model.dto.request.user.ApiUserUpdateRequest;
import com.shundei.web.model.dto.response.user.ApiSafeUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author huoyouri
 */
public interface UserService extends IService<User> {

    /**
     * 注册
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param checkPass    校验密码
     * @return 插入表的行数
     */
    long userRegister(String userAccount, String userPassword, String checkPass);

    /**
     * 登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @return 脱敏后的用户信息
     */
    ApiSafeUser userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 注销
     *
     * @param request 请求参数
     * @return 是否成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 更新
     *
     * @param updateUser 更新用户
     * @return 是否成功
     */
    boolean userUpdate(ApiUserUpdateRequest updateUser);

    /**
     * 上传文件
     *
     * @param filename 文件名
     * @param file     文件
     * @return 是否成功
     */
    boolean userUpload(String filename, String file);

    /**
     * 报表下载
     *
     * @param request 请求
     * @return 字节数组
     */
    List userDownload(OutputDataRequest request);

    /**
     * 用户信息脱敏
     *
     * @param user 原始user
     * @return 脱敏后的user
     */
    ApiSafeUser getCleanUser(User user);
}
