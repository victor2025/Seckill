package com.victor2022.seckill.config;

import com.victor2022.order.MQConfigs;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: victor2022
 * @date: 2022/8/21 下午6:24
 * @description:
 */
@Configuration
public class RabbitMQConfig {

    /**
     * @return: org.springframework.amqp.core.Exchange
     * @author: victor2022
     * @date: 2022/8/21 下午6:29
     * @description: 创建Exchange的bean
     */
    @Bean("orderExchange")
    public Exchange initTopicExchange(){
        return ExchangeBuilder.topicExchange(MQConfigs.ORDER_EXCHANGE)
                .durable(true)
                .build();
    }

    /**
     * @return: org.springframework.amqp.core.Queue
     * @author: victor2022
     * @date: 2022/8/21 下午6:34
     * @description: 创建Queue
     */
    @Bean("orderQueue")
    public Queue initQueue(){
        return QueueBuilder.durable(MQConfigs.ORDER_QUEUE).build();
    }

    /**
     * @param exchange:
     * @param queue:
     * @return: org.springframework.amqp.core.Binding
     * @author: victor2022
     * @date: 2022/8/21 下午6:34
     * @description: 绑定Queue和Exchange
     */
    @Bean
    public Binding itemQueueExchange(@Qualifier("orderExchange") Exchange exchange,
                                     @Qualifier("orderQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("order.#").noargs();
    }

}
