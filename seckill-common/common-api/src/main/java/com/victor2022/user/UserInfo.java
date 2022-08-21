package com.victor2022.user;

import lombok.Data;

/**
 * @author: victor2022
 * @date: 2022/8/21 下午3:18
 * @description:
 */
@Data
public class UserInfo {

    public static final String LOGGED = "1";
    public static final String NOT_LOGGED = "0";

    private String id;
    private String token;
    private String status;
}
