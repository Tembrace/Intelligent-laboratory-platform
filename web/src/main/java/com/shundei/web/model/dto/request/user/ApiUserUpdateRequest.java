package com.shundei.web.model.dto.request.user;


import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户请求
 *
 * @author huoyouri
 */
@Data
public class ApiUserUpdateRequest implements Serializable {

    private static final long serialVersionUID = -4459882801375654746L;
    /**
     * id
     */
    private Long id;
    
    /**
     * 昵称
     */
    private String username;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatarUrl;
}
