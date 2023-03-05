package com.shundei.web.controller;

import com.shundei.web.exception.BusinessException;
import com.shundei.web.model.domain.User;
import com.shundei.web.model.dto.request.OutputDataRequest;
import com.shundei.web.model.dto.request.user.ApiUserLoginRequest;
import com.shundei.web.model.dto.request.user.ApiUserRegisterRequest;
import com.shundei.web.model.dto.request.user.ApiUserUpdateRequest;
import com.shundei.web.model.dto.response.user.ApiSafeUser;
import com.shundei.web.service.UserService;
import com.shundei.web.utils.ResponseUtils;
import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.shundei.web.constant.UserConstant.ADMIN_ROLE;
import static com.shundei.web.constant.UserConstant.LOGIN_STATE;

/**
 * 网页访问用户接口
 *
 * @author huoyouri
 */
@RestController
@RequestMapping("/api/user")
public class ApiUserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody ApiUserRegisterRequest apiUserRegisterRequest) {
        if (apiUserRegisterRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = apiUserRegisterRequest.getUserAccount();
        String userPassword = apiUserRegisterRequest.getUserPassword();
        String checkPass = apiUserRegisterRequest.getCheckPass();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPass)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "请求参数为空");
        }
        long res = userService.userRegister(userAccount, userPassword, checkPass);
        return ResponseUtils.success(ResponseCode.SUCCESS, res, "注册成功");
    }

    @PostMapping("/login")
    public BaseResponse<ApiSafeUser> login(@RequestBody ApiUserLoginRequest apiUserLoginRequest, HttpServletRequest request) {
        if (apiUserLoginRequest == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = apiUserLoginRequest.getUserAccount();
        String userPassword = apiUserLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "请求参数为空");
        }
        ApiSafeUser apiSafeUser = userService.userLogin(userAccount, userPassword, request);
        return ResponseUtils.success(ResponseCode.SUCCESS, apiSafeUser, "登录成功");
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求参数为空");
        }
        Boolean res = userService.userLogout(request);
        return ResponseUtils.success(ResponseCode.SUCCESS, res, "注销成功");
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ResponseCode.NO_AUTH, "无权限");
        }
        if (id <= 0) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "删除ID错误");
        }
        // 加了逻辑删除，那么这里删除也就是逻辑删除
        Boolean res = userService.removeById(id);
        return ResponseUtils.success(ResponseCode.SUCCESS, res, "删除成功");
    }

    /**
     * 是否为管理员
     *
     * @param request 请求
     * @return 是/否
     */
    public boolean isAdmin(HttpServletRequest request) {
        ApiSafeUser apiSafeUser = (ApiSafeUser) request.getSession().getAttribute(LOGIN_STATE);
        return apiSafeUser != null && apiSafeUser.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 获取用户登录态   因为用户信息万年不变，所以可以直接查数据库，如果一直变，可以从缓存中拿，然后缓存和数据库进行更新即可
     *
     * @param request 请求的cookie
     * @return 脱敏后的用户信息
     */
    @GetMapping("/current")
    public BaseResponse<ApiSafeUser> getCurrentUser(HttpServletRequest request) {
        ApiSafeUser currentUser = (ApiSafeUser) request.getSession().getAttribute(LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "未登录");
        }
        long id = currentUser.getId();
        User user = userService.getById(id);
        ApiSafeUser res = userService.getCleanUser(user);
        return ResponseUtils.success(ResponseCode.SUCCESS, res, "获取用户信息成功");
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody ApiUserUpdateRequest apiUserUpdateRequest, HttpServletRequest request) {
        ApiSafeUser currentUser = (ApiSafeUser) request.getSession().getAttribute(LOGIN_STATE);
        if (currentUser == null || apiUserUpdateRequest == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "未登录");
        }
        if (!currentUser.getId().equals(apiUserUpdateRequest.getId())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "无法修改信息");
        }
        boolean res = userService.userUpdate(apiUserUpdateRequest);
        return res ? ResponseUtils.success(ResponseCode.SUCCESS, res, "修改用户信息成功") :
                ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "修改用户信息失败");
    }

    @PostMapping("/upload")
    public BaseResponse<Boolean> upload(String file, String filename, HttpServletRequest request) {
        ApiSafeUser currentUser = (ApiSafeUser) request.getSession().getAttribute(LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN, "未登录");
        }
        if (StringUtils.isAnyBlank(file, filename)) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        boolean res = userService.userUpload(filename, file);
        return res ? ResponseUtils.success(ResponseCode.SUCCESS, res, "上传成功") :
                ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "上传失败");
    }

    @PostMapping("/download")
    public BaseResponse<List> download(@RequestBody OutputDataRequest request) {
        if (request == null) {
            throw new BusinessException(ResponseCode.NULL_ERROR, "请求为空");
        }
        List res = userService.userDownload(request);
        return res != null ? ResponseUtils.success(ResponseCode.SUCCESS, res, "导出成功") :
                ResponseUtils.error(ResponseCode.SYSTEM_ERROR, "导出失败");
    }
}
