package com.xw.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @Author xiongwei
 * @WriteTime 2020-11-26 9:52
 */

public class JedisUtil {
    //设置参数
    private static String host;
    private static int port;

    private static JedisPool jedisPool;

    private static int password;


    /**
     * 初始化连接池
     */
    static {
        int redisTimeout = 30000;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        //最大空闲连接数
        config.setMaxIdle(10);
        //获取Jedis连接的最大等待时间（50秒）
        config.setMaxWaitMillis(50 * 1000);
        //在获取Jedis连接时，自动检验连接是否可用
        config.setTestOnBorrow(true);
        //在将连接放回池中前，自动检验连接是否有效
        config.setTestOnReturn(true);
        //自动测试池中的空闲连接是否都是可用连接
        config.setTestWhileIdle(true);
        jedisPool = new JedisPool(config, "127.0.0.1", 6379, redisTimeout,"123456");
    }


    /**
     * 获取连接方法
     *
     * @return
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 释放资源
     *
     * @param jedis
     */
    public static void closeJedis(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
}