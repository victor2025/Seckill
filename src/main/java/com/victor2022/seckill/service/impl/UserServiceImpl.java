package com.victor2022.seckill.service.impl;

import com.victor2022.seckill.entity.User;
import com.victor2022.seckill.dao.UserMapper;
import com.victor2022.seckill.common.param.LoginParam;
import com.victor2022.seckill.common.result.CodeMsg;
import com.victor2022.seckill.common.result.Result;
import com.victor2022.seckill.service.UserService;
import com.victor2022.seckill.common.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/10
 * Time: 12:01
 * Such description:
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public Result<User> login(LoginParam loginParam) {

        User user = userMapper.checkPhone(loginParam.getMobile());
        if(user == null){
            return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPwd= user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(loginParam.getPassword(), saltDB);
        if(!StringUtils.equals(dbPwd , calcPass)){
            return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        user.setPassword(StringUtils.EMPTY);
        return Result.success(user);
    }
}
