package com.victor2022.seckill.service;

import com.victor2022.seckill.entity.OrderInfo;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/17
 * Time: 10:49
 * Such description:
 */
public interface OrderService {

    long addOrder(OrderInfo orderInfo);

    OrderInfo getOrderInfo(long rderId);
}
