package com.victor2022.seckill.service;

import com.victor2022.seckill.entity.bo.GoodsBo;

import java.util.List;

/**
 * My Blog : www.hfbin.cn
 * github: https://github.com/hfbin
 * Created by: HuangFuBin
 * Date: 2018/7/12
 * Time: 19:46
 * Such description:
 */
public interface SeckillGoodsService {

    List<GoodsBo> getSeckillGoodsList();

    GoodsBo getseckillGoodsBoByGoodsId(long goodsId);

    int reduceStock(long goodsId);
}
