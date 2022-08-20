package com.victor2022.seckill.dao;

import com.victor2022.seckill.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdeInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}