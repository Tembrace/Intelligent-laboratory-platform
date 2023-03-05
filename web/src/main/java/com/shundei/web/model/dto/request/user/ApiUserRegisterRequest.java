package com.shundei.web.model.dto.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author huoyouri
 */
@Data
public class ApiUserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -8953283841037934714L;

    private String userAccount;

    private String userPassword;
    
    private String checkPass;
}
