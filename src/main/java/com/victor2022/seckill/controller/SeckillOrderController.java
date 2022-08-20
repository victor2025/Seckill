package com.victor2022.seckill.controller;

import com.victor2022.seckill.entity.bo.GoodsBo;
import com.victor2022.seckill.entity.OrderInfo;
import com.victor2022.seckill.entity.User;
import com.victor2022.seckill.config.redis.UserKey;
import com.victor2022.seckill.common.result.CodeMsg;
import com.victor2022.seckill.common.result.Result;
import com.victor2022.seckill.service.SeckillGoodsService;
import com.victor2022.seckill.service.SeckillOrderService;
import com.victor2022.seckill.common.util.CookieUtil;
import com.victor2022.seckill.entity.vo.OrderDetailVo;
import com.victor2022.seckill.service.impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/19
 * Time: 0:56
 * Such description:
 */
@Controller
@RequestMapping("/order")
@ResponseBody
public class SeckillOrderController {
    @Autowired
    RedisService redisService;
    @Autowired
    SeckillOrderService seckillOrderService;
    @Autowired
    SeckillGoodsService seckillGoodsService;

    @GetMapping("/detail")
    public Result<OrderDetailVo> info(Model model,
                                      @RequestParam("orderId") long orderId , HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        if(user == null) {
            return Result.error(CodeMsg.USER_NO_LOGIN);
        }
        OrderInfo order = seckillOrderService.getOrderInfo(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
