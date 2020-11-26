import org.junit.Test;
import redis.clients.jedis.Jedis;


/**
 * @Author xiongwei
 * @WriteTime 2020-11-26 15:42
 */

public class test {

    public static void main(String[] args) {

//        Jedis jedis = new Jedis("127.0.0.1",6379);
//        jedis.auth("123456");
        for (int i = 0 ; i < 10 ; i++) {
            new Thread(()->{
                Jedis jedis = new Jedis("127.0.0.1",6379);
                jedis.auth("123456");
                Long llen = jedis.llen("1");
                System.out.println(llen);
            }).start();
        }
    }

    @Test
    public void execption(){
        while (true) {
            try {
                System.out.println("执行");
                int i = 2 / 0;
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
//        System.out.println("异常发生会退出");
    }

    @Test
    public void sleep() throws InterruptedException {
        for (int i = 0 ; i < 10 ; i++){
            Thread.sleep((1));
            System.out.println(i);
        }
    }
}

