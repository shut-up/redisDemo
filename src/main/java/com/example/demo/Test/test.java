package com.example.demo.Test;

import com.example.demo.config.DataSourceConfig;
import com.example.demo.utils.RedisClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author: walton
 * @Description:
 * @Createtime: 2017/11/22
 */

@RestController
public class test extends DataSourceConfig{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis()throws Exception{
        RedisClient redisClient = getRedisClient();

        List<String> list = new ArrayList<>();
        list.add("自行车人");
        add(redisClient, "machine1", 1.0, list);

        List matchValue = likeSearch(redisClient, "machine1", "xb");
        if(matchValue.size()==0){
            System.out.println("无匹配关键词！");
        }else {
            System.out.println("模糊查找词匹配的数据为：" + matchValue + "，个数：" + matchValue.size());
        }
    }

    //增加，可同时增加多个
    public void add(RedisClient redisClient, String key, double score, List<String> addValues)throws Exception{
        redisClient.set(key, score, addValues);   //有序集合插入数据
    }

    //删除，可同时删除多个
    public void delete(RedisClient redisClient, String key, String[] deleteValue)throws Exception{
        redisClient.remove(key, deleteValue);
    }

    //获取
    public Set<String> get(RedisClient redisClient, String key, int start, int end)throws Exception{
        Set<String> value = redisClient.get(key,start,end);
        return value;
    }

    //模糊查询
    public List<String> likeSearch(RedisClient redisClient, String key, String searchWorld)throws Exception {
        //查找到的所有数据
        String searchValue = null;
        //匹配模糊查询到的数据
        List<String> matchValue = new ArrayList<>();

        //查找全部数据
        Set<String> result = redisClient.get(key, 0 , -1);
        System.out.println("查找到全部的数据为" + result);
        //找出匹配的数据
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            searchValue = (String) iterator.next();
            //searchValue包含searchWorld的情况，将searchValue添加到matchValue，匹配成功
            if (searchValue.indexOf(searchWorld) != -1) {
                matchValue.add(searchValue);
            }
        }
        return matchValue;
    }

    //获取交集
    public Set<String> mixedValue(RedisClient redisClient, String[] s, String destKey)throws Exception{
        //追加数组元素
        //Arrays.copyOf(s,s.length+1);
        //s[s.length-1] = "ddd";
        redisClient.getZinterstore(destKey, s);  //计算有序集的交集
        Set<String> destValue = redisClient.get(destKey,0,-1);    //获取到交集后的所有集合
        System.out.println("交集的destValue:"+destValue);
        return destValue;
    }

}
