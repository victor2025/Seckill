package com.victor2022.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.victor2022.seckill.service.impl.RedisService;
import com.victor2022.seckill.entity.bo.GoodsBo;
import com.victor2022.seckill.common.param.Const;
import com.victor2022.seckill.entity.User;
import com.victor2022.seckill.config.redis.GoodsKey;
import com.victor2022.seckill.config.redis.UserKey;
import com.victor2022.seckill.common.result.CodeMsg;
import com.victor2022.seckill.common.result.Result;
import com.victor2022.seckill.service.SeckillGoodsService;
import com.victor2022.seckill.common.util.CookieUtil;
import com.victor2022.seckill.entity.vo.GoodsDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by: HuangFuBin
 * Date: 2018/7/11
 * Time: 20:52
 * Such description:
 */

@Controller
@RequestMapping("/goods")
@ResponseBody
public class GoodsController {

    @Autowired
    RedisService redisService;

    @Autowired
    SeckillGoodsService seckillGoodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
        //修改前
       /* List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
         model.addAttribute("goodsList", goodsList);
    	 return "goods_list";*/
        //修改后
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        List<GoodsBo> goodsList = seckillGoodsService.getSeckillGoodsList();
        model.addAttribute("goodsList", goodsList);
//        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
//        SpringWebContext ctx = new SpringWebContext(request, response,
//                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
//        //手动渲染
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        html = (String) JSON.toJSON(goodsList);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html, Const.RedisCacheExtime.GOODS_LIST);
        }
        return html;
    }

    @GetMapping("/to_detail2/{goodsId}")
    public String detail2(Model model,
                          @PathVariable("goodsId") long goodsId, HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        if (goods == null) {
            return "没有找到该页面";
        } else {
            model.addAttribute("goods", goods);
            long startAt = goods.getStartDate().getTime();
            long endAt = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();

            int miaoshaStatus = 0;
            int remainSeconds = 0;
            if (now < startAt) {//秒杀还没开始，倒计时
                miaoshaStatus = 0;
                remainSeconds = (int) ((startAt - now) / 1000);
            } else if (now > endAt) {//秒杀已经结束
                miaoshaStatus = 2;
                remainSeconds = -1;
            } else {//秒杀进行中
                miaoshaStatus = 1;
                remainSeconds = 0;
            }
            model.addAttribute("seckillStatus", miaoshaStatus);
            model.addAttribute("remainSeconds", remainSeconds);

//            SpringWebContext ctx = new SpringWebContext(request, response,
//                    request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
            WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
            html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
            if (!StringUtils.isEmpty(html)) {
                redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html, Const.RedisCacheExtime.GOODS_INFO);
            }
            return html;
        }
    }

    @GetMapping("/detail/{goodsId}")
    public Result<GoodsDetailVo> detail(Model model,
                                        @PathVariable("goodsId") long goodsId, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        User user = redisService.get(UserKey.getByName, loginToken, User.class);

        GoodsBo goods = seckillGoodsService.getseckillGoodsBoByGoodsId(goodsId);
        if (goods == null) {
            return Result.error(CodeMsg.NO_GOODS);
        } else {
            model.addAttribute("goods", goods);
            long startAt = goods.getStartDate().getTime();
            long endAt = goods.getEndDate().getTime();
            long now = System.currentTimeMillis();

            int miaoshaStatus = 0;
            int remainSeconds = 0;
            if (now < startAt) {//秒杀还没开始，倒计时
                miaoshaStatus = 0;
                remainSeconds = (int) ((startAt - now) / 1000);
            } else if (now > endAt) {//秒杀已经结束
                miaoshaStatus = 2;
                remainSeconds = -1;
            } else {//秒杀进行中
                miaoshaStatus = 1;
                remainSeconds = 0;
            }
            GoodsDetailVo vo = new GoodsDetailVo();
            vo.setGoods(goods);
            vo.setUser(user);
            vo.setRemainSeconds(remainSeconds);
            vo.setMiaoshaStatus(miaoshaStatus);
            return Result.success(vo);
        }
    }
}

