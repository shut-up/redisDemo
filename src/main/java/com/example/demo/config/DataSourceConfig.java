package com.example.demo.config;

import com.example.demo.utils.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * @Author: walton
 * @Description:
 * @Createtime: 2017/11/22
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public JedisPool getJedisPool(){
        return new JedisPool();
    }
    @Bean
    public RedisClient getRedisClient(){
        return new RedisClient();
    }
}
