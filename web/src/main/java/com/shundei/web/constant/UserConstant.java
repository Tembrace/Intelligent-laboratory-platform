package com.shundei.web.constant;

/**
 * 用户常量，接口内属性默认是public static
 *
 * @author huoyouri
 */
public interface UserConstant {
    /**
     * 登录状态
     */
    final String LOGIN_STATE = "userLoginState";

    /**
     * 权限
     * 0 -> 普通用户
     * 1 -> 管理员
     */
    final int DEFAULT_ROLE = 0;
    final int ADMIN_ROLE = 1;
}
