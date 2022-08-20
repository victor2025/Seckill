package com.victor2022.seckill.service.impl;

import com.victor2022.seckill.common.util.JsonUtil;
import com.victor2022.seckill.config.redis.KeyPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author victor2022
 * @creat 2022/4/15 10:28
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取当个对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        //生成真正的key
        String realKey = prefix.getPrefix() + key;
        String str = (String) redisTemplate.opsForValue().get(realKey);
        T t = JsonUtil.stringToBean(str, clazz);
        return t;
    }

    public boolean expire(KeyPrefix prefix, String key, int exTime) {

        Boolean isSuccess = redisTemplate.expire(prefix.getPrefix() + key, exTime, TimeUnit.SECONDS);
        return isSuccess;
    }

    /**
     * 设置对象
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value, int exTime) {
        String str = JsonUtil.beanToString(value);
        if (str == null || str.length() <= 0) {
            return false;
        }
        //生成真正的key
        String realKey = prefix.getPrefix() + key;
        if (exTime == 0) {
            //直接保存
            redisTemplate.opsForValue().set(realKey, str);
        } else {
            //设置过期时间
            redisTemplate.opsForValue().set(realKey, str, exTime, TimeUnit.SECONDS);
        }
        return true;
    }

    public boolean del(KeyPrefix prefix, String key) {
        redisTemplate.delete(prefix.getPrefix() + key);
        return true;
    }

    /**
     * 判断key是否存在
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {

        Boolean hasKey = redisTemplate.hasKey(prefix.getPrefix() + key);
        return hasKey;
    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {

        Long result = redisTemplate.opsForValue().increment(prefix.getPrefix() + key,1);
        return result;

    }

    /**
     * 减少值
     */
    public <T> Long decr(KeyPrefix prefix, String key) {

        Long result = redisTemplate.opsForValue().increment(prefix.getPrefix() + key,-1);
        return result;

    }

}
