package com.victor2022.seckill.dao;

import com.victor2022.seckill.entity.bo.GoodsBo;
import com.victor2022.seckill.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKeyWithBLOBs(Goods record);

    int updateByPrimaryKey(Goods record);

    List<GoodsBo> selectAllGoodes ();

    GoodsBo getseckillGoodsBoByGoodsId(long goodsId);

    int updateStock(long goodsId);
}