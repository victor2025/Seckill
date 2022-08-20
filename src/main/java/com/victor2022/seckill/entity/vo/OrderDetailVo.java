package com.victor2022.seckill.entity.vo;

import com.victor2022.seckill.entity.bo.GoodsBo;
import com.victor2022.seckill.entity.OrderInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/19
 * Time: 0:40
 * Such description:
 */
@Setter
@Getter
public class OrderDetailVo {
    private GoodsBo goods;
    private OrderInfo order;
}
