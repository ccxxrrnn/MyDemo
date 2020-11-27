package com.xw.service;


import com.xw.util.JedisUtil;
import org.apache.tomcat.jni.Time;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;

import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;


/**
 * @Author xiongwei
 * @WriteTime 2020-11-26 9:30
 */

/*
（1）获取锁的时候，使用setnx加锁，并使用expire命令为锁添加一个超时时间，超过该时间则自动释放锁，锁的value值为一个随机生成的UUID，通过此在释放锁的时候进行判断。

（2）获取锁的时候还设置一个获取的超时时间，若超过这个时间则放弃获取锁。

（3）释放锁的时候，通过UUID判断是不是该锁，若是该锁，则执行delete进行锁释放。
 */
public class MyRedisLock {


    //主方法   获得锁  和 释放锁


    /**
     * 通过锁试名来尝获取锁
     * @param redId
     * @return
     */
    public  String[] acquire_lock(String redId) throws InterruptedException {
        //创建一个唯一标识  使用当前线程的id
        String uuId = UUID.randomUUID().toString();
        LocalTime dateTime = LocalTime.now();
        //单位为秒 ，用户请求等待的时间
        long acquire_time = 10;
        LocalTime end = dateTime.plusSeconds(acquire_time);


        while (end.isAfter(LocalTime.now())){ //判断 end是否在now之后
            //尝试set值
            Jedis jedis = JedisUtil.getJedis();

            String index = jedis.srandmember("set:"+redId);
            String lockName = jedis.lindex("list:" + redId, Long.parseLong(index ) - 1);
            //单位为秒 ，定义锁到一定时间自动释放，避免死锁
            int time_out = 10;
            if( jedis.setnx("lock:" + index, uuId) == 1){
                //1 则表示插入成功，那么锁可以被获取,设置超时时间
                jedis.expire("lock:" + index, time_out);
                JedisUtil.closeJedis(jedis);
                return new String[]{index,lockName,uuId};
            }else if (jedis.ttl("lock:" + index) >= 0){
                //判断锁的剩余时间 小于0 则表示已经过期，那么延长时间
                jedis.expire("lock:" + index, time_out);
            }
            JedisUtil.closeJedis(jedis);

//            Thread.sleep(1);
        }
        return null;
        //多次重复获取不到 ，直接抛出异常，

    }


    /**
     * 释放锁
     * @param index
     * @param uuId
     * @return
     */
    public  boolean release_lock(String index,String uuId){
        Jedis jedis = JedisUtil.getJedis();
        index = "lock:" + index;
        while (true){
            try {
//              //监视数据
                jedis.watch(index);
                String lock_value = jedis.get(index);
                if (lock_value == null) {
                    JedisUtil.closeJedis(jedis);
                    return true;
                }
                if (lock_value.equals(uuId)) {
                    //开启事务
                    Transaction multi = jedis.multi();
                    multi.del(lock_value);
                    //可能报错 多线程
                    multi.exec();
                    multi.close();

                    JedisUtil.closeJedis(jedis);
                    return true;
                }
                jedis.unwatch();
                break;
            }catch (Exception e){

//                System.out.println(e.getMessage());
            }
        }

        JedisUtil.closeJedis(jedis);
        return false;
    }
}
