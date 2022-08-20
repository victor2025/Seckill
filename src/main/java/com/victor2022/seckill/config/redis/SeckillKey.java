package com.victor2022.seckill.config.redis;

public class SeckillKey extends BasePrefix{

	private SeckillKey(String prefix) {
		super(prefix);
	}
	public static SeckillKey isGoodsOver = new SeckillKey("go");
	public static SeckillKey getSeckillPath = new SeckillKey("mp");
}
