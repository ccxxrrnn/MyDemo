package com.xw.ftpFile;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.Properties;

/**
 * @author xiongwei
 * @description
 */
public class InitFTP {

    /**
     * 自动连接Ftp请求
     * @return 动作
     */
    public static FtpAction autoInitFtpClient() throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("ftpProperties.properties"));
        String host = properties.getProperty("ftp.host");
        String username = properties.getProperty("ftp.username");
        String password = properties.getProperty("ftp.password");
        int port = Integer.parseInt(properties.getProperty("ftp.port"));
        return handInitFtpClient(host , username , password , port);
    }

    /**
     * 手动连接Ftp请求
     * @param host IP
     * @param username 用户名
     * @param password 密码
     * @param port 短裤
     * @return 动作
     */
    public static FtpAction handInitFtpClient(String host , String username , String password , int port) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        System.out.println("正在连接FTP服务器:" + host + ":" + port);
        ftpClient.connect(host , port);
        ftpClient.login(username , password);
        int replyCode = ftpClient.getReplyCode();
        if(!FTPReply.isPositiveCompletion(replyCode)){
            System.out.println("FTP服务器连接失败:" + host + ":" + port);
        }
        System.out.println("FTP服务器连接成功:" + host + ":" + port);
        return new FtpAction(ftpClient);
    }


}
