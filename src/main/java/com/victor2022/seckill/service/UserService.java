package com.victor2022.seckill.service;

import com.victor2022.seckill.entity.User;
import com.victor2022.seckill.common.param.LoginParam;
import com.victor2022.seckill.common.result.Result;

/**
 * My Blog : www.hfbin.cn
 * github: https://github.com/hfbin
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 12:00
 * Such description:
 */
public interface UserService {
    Result<User> login(LoginParam loginParam);
}
