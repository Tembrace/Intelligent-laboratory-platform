package com.shundei.web.model.dto.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author huoyouri
 */
@Data
public class ApiUserLoginRequest implements Serializable {

    private static final long serialVersionUID = -1501197313583953631L;

    private String userAccount;

    private String userPassword;

    private boolean autoLogin;
}
