package cn.hfbin.seckill.controller;

import cn.hfbin.seckill.common.param.Const;
import cn.hfbin.seckill.service.impl.RedisService;
import cn.hfbin.seckill.entity.User;
import cn.hfbin.seckill.common.param.LoginParam;
import cn.hfbin.seckill.config.redis.UserKey;
import cn.hfbin.seckill.common.result.Result;
import cn.hfbin.seckill.service.UserService;
import cn.hfbin.seckill.common.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/9
 * Time: 12:37
 * Such description:
 */
@Controller
@RequestMapping("/user")
@ResponseBody
public class LoginController {

    @Autowired
    RedisService redisService;
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Result<User> doLogin(HttpServletResponse response, HttpSession session , @Valid LoginParam loginParam) {
        Result<User> login = userService.login(loginParam);
        if (login.isSuccess()){
            CookieUtil.writeLoginToken(response,session.getId());
            redisService.set(UserKey.getByName , session.getId() ,login.getData(), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return login;
    }

    @PostMapping("/logout")
    public String doLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request , response);
        redisService.del(UserKey.getByName , token);
        return "login";
    }
}
