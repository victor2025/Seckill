package com.victor2022.seckill.config.mq;

import com.victor2022.seckill.common.util.JsonUtil;
import com.victor2022.seckill.entity.bo.GoodsBo;
import com.victor2022.seckill.entity.SeckillOrder;
import com.victor2022.seckill.entity.User;
import com.victor2022.seckill.service.OrderService;
import com.victor2022.seckill.service.SeckillGoodsService;
import com.victor2022.seckill.service.SeckillOrderService;
import com.victor2022.seckill.service.impl.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		
		@Autowired
        RedisService redisService;
		
		@Autowired
        SeckillGoodsService goodsService;
		
		@Autowired
        OrderService orderService;
		
		@Autowired
        SeckillOrderService seckillOrderService;
		
		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			SeckillMessage mm  = JsonUtil.stringToBean(message, SeckillMessage.class);
			User user = mm.getUser();
			long goodsId = mm.getGoodsId();
			
			GoodsBo goods = goodsService.getseckillGoodsBoByGoodsId(goodsId);
	    	int stock = goods.getStockCount();
	    	if(stock <= 0) {
	    		return;
	    	}
	    	//判断是否已经秒杀到了
	    	SeckillOrder order = seckillOrderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
	    	if(order != null) {
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
			seckillOrderService.insert(user, goods);
		}
}
