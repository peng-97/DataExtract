package com.example.shape.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value
     * 如果第一个list为空或者不存在则返回null
     *
     * @param srckey  key
     * @param dstkey  value
     * @param indexdb Redis库的序号
     * @param time    超时时间
     * @param t       时间单位
     * @return
     */
    public String add(String srckey, Object dstkey, int indexdb, int time, TimeUnit t) {

        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate
                .getConnectionFactory();
        jedisConnectionFactory.setDatabase(indexdb);
        //需要将链接刷新
        jedisConnectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        //存储数据，key，value，过期时间，过期时间类型
        redisTemplate.opsForValue().set(srckey, dstkey, time, t);
//		jedisConnectionFactory.resetConnection();
        return 1 + "";
    }

    /**
     * 根据key获取value
     *
     * @param srckey key
     * @return value
     */
    public Object get(String srckey) {
        return redisTemplate.opsForValue().get(srckey);
    }

    /**
     * 根据key从指定Redis数据库中获取value
     *
     * @param srckey  key
     * @param indexDb Redis库的序号
     * @return value
     */
    public Object getDataByIndex(String srckey, int indexDb) {

        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate
                .getConnectionFactory();
        jedisConnectionFactory.setDatabase(indexDb);
        //需要将链接刷新
        jedisConnectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//		jedisConnectionFactory.resetConnection();
        return redisTemplate.opsForValue().get(srckey);
    }

    /**
     * 向Redis指定库中添加数据
     *
     * @param srckey  key
     * @param dstkey  value
     * @param indexdb Redis库的序号
     * @param t       时间单位
     * @return
     */
    public String add1(String srckey, Object dstkey, int indexdb, TimeUnit t) {
        redisTemplate.opsForValue().set(srckey, dstkey, indexdb, t);
        return 1 + "";
    }

    /**
     * 从Redis指定库中删除key
     *
     * @param key     key
     * @param indexDb Redis库的序号
     * @return 0/1
     */
    public Object delete(String key, int indexDb) {
        LettuceConnectionFactory jedisConnectionFactory = (LettuceConnectionFactory) redisTemplate
                .getConnectionFactory();
        jedisConnectionFactory.setDatabase(indexDb);
        jedisConnectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//		jedisConnectionFactory.resetConnection();
        return redisTemplate.delete(key);
    }
}