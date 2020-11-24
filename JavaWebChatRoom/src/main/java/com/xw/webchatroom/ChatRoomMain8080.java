package com.xw.webchatroom;

import com.xw.webchatroom.NettyServer.MyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-17 18:12
 */

@SpringBootApplication
public class ChatRoomMain8080 {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ChatRoomMain8080.class,args);
        MyServer myServer = new MyServer(8090);
        myServer.run();
    }
}
