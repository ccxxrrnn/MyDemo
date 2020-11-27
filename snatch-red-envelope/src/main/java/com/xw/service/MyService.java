package com.xw.service;

import com.xw.util.JedisUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Random;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-25 19:20
 */

@Service
public class MyService {



    /**
     * 红包分开存入redis
     * @param redId
     * @param amount
     * @param num
     */
    //红包存入到redis中，
    public void sendRedEnvelope(String redId, String amount, String num){
        sendRedEnvelope0(redId,amount,num);
    }

    /**
     *第一种分方式
     * @param redId
     * @param amount
     * @param num
     *     //用list set存储  //关键怎么分开这些红包 //不停地用随机数分离
     *     //第一种分离方法 ，可以用  amount / num 算出评价每人分得多少红包 ，规定一个浮动标准 例如 20% 上下随机
     *     //浮动可能全为上 或者 下 会导致一些远离数据  就需要使  之间差值 小于 一定值
     *     //最后一位拿走所有值
     */
    public void sendRedEnvelope0(String redId, String amount, String num){
        //初始化
        Random random = new Random();
        double floatNum = 0.3;
        int people = Integer.parseInt(num);
        int count = Integer.parseInt(amount);
        Jedis jedis = JedisUtil.getJedis();
        while (people != 1) {
            int average = count / people;  //获取平均值
            int floatLow = (int) (average * (1 - floatNum)); //浮动下
            int floatUp = (int) (average * (1 + floatNum)); //浮动上

            int sal = 0;
            //拿到随机金额
            sal = random.nextInt(floatUp - floatLow + 1) + floatLow;
            count = count - sal;
            jedis.lpush("list:"+redId, String.valueOf(sal));
            jedis.sadd("set:"+redId, String.valueOf(people));
            people--;
            //最后一个拿走所有的
            if (people == 1) {
                jedis.sadd("set:"+redId, String.valueOf(people));
                jedis.lpush("list:"+redId, String.valueOf(count));
            }
        }
        JedisUtil.closeJedis(jedis);
    }

    /**
     * 第二种分方式
     * @param redId
     * @param amount
     * @param num
     */
    public void sendRedEnvelope1(String redId, String amount, String num){

    }


    /**
     * 从redis中去取userId的红包
     *
     * @param redId
     * @return
     * 分布式锁
     */
    public  double getRedPaper(String redId,String userId) throws InterruptedException {
        Jedis jedis = JedisUtil.getJedis();
        int lenRed = jedis.smembers("set:" + redId).size();
        if ( lenRed == 0){
            //没有红包了
            throw new MyException("没红包");
        }
        MyRedisLock myRedisLock = new MyRedisLock();

        String[] nums = myRedisLock.acquire_lock(redId);
        if( nums != null){
            //释放锁
            String uuId = nums[2];

            jedis.srem("set:" + redId,nums[0]); // 删除掉值已经有锁了，其他人拿不到
//            jedis.lrem("list:" + redId,1,nums[1]); //liat删除index会变
            if(myRedisLock.release_lock(nums[0],uuId)){
                //释放锁成功
            }else{
                throw new MyException(userId + "释放锁失败");
            }
        }else{
            //获取失败
            throw new MyException(userId + "抢红包失败，请重试");
        }
        JedisUtil.closeJedis(jedis);
        return (double) Integer.parseInt(nums[1]) / 100;
    }

    //加锁解决并发问题
//    public synchronized double getRedPaper(String userId) {
//        if (jedis.llen(userId) == 0){
//            //没有红包了
//            throw new MyException("没红包");
//        }
//        return (double) Integer.parseInt(jedis.lpop(userId))/100;
//    }
}
