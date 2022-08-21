package com.victor2022.gateway.services;

import com.victor2022.user.UserInfo;
import org.springframework.stereotype.Service;

/**
 * @author: victor2022
 * @date: 2022/8/21 下午3:13
 * @description:
 */
@Service
public class UserStatusService {

    /**
     * @param info:
     * @return: boolean
     * @author: victor2022
     * @date: 2022/8/21 下午3:21
     * @description: 获取登录状态
     */
    public boolean isLogged(UserInfo info){
        System.out.println("查看当前用户是否已经登录...");
        // 远程调用获取用户状态
        return UserInfo.LOGGED.equals(info.getStatus());
    }


}
