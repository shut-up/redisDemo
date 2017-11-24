package com.example.demo.utils;
import com.example.demo.config.DataSourceConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;  
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;


/**  
 * Created by Administrator on 2017/5/9.  
 */  
@Component("redisClient")  
public class RedisClient extends DataSourceConfig{
    private JedisPool jedisPool = getJedisPool();

    //增加
    public void set(String key, double score,  List<String> addValues) throws Exception {
        Jedis jedis = null;
        try {  
            jedis = jedisPool.getResource();
            for(String s : addValues) {
                jedis.zadd(key, score, s);
            }
        } finally {
            //返还到连接池
            if(jedis != null)
                jedis.close();
        }  
    }  

    //获取
    public Set<String> get(String key, int begin, int end) throws Exception  {
        Jedis jedis = null;
        try {  
            jedis = jedisPool.getResource();
            return jedis.zrange(key, begin, end);
        } finally {  
            //返还到连接池
            if(jedis != null)
                jedis.close();
        }  
    }

    //获取长度
    public long getLength(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        }finally {
            if (jedis != null)
                jedis.close();
        }
    }

    //获取交集
    public long getZinterstore(String destKey, String...key1){
        Jedis jedis = null;
        long length = 0L;
        try {
            jedis = jedisPool.getResource();
            length = jedis.zinterstore(destKey, key1);
        }finally {
            if (jedis != null)
                jedis.close();
        }
        return length;
    }

    //删除
    public void remove(String key, String...value){
        Jedis jedis = null;
        long length = 0L;
        try {
            jedis = jedisPool.getResource();
            jedis.zrem(key,value);
        }finally {
            if (jedis != null)
                jedis.close();
        }
    }

    //查找
    public Set<String> search(String key, String min, String max){
        Jedis jedis = null;
        long length = 0L;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrangeByLex(key, min, max);
        }finally {
            if (jedis != null)
                jedis.close();
        }
    }

}