package com.xw.ftpFile;

import java.io.IOException;

/**
 * @author xiongwei
 * @description
 */
public class Test {

    public static void main(String[] args) throws IOException {
        FtpAction ftpAction = InitFTP.autoInitFtpClient();

//        ftpAction.readFTP("/" , "ftptest.txt");

//        ftpAction.downLoadFTP("/" , "ftptest.txt" , "D:\\文档");

//        ftpAction.moveFTP("/" , "ftptest.txt" , "/move/" , "moveFtp.txt");

//        ftpAction.writeFile("/" , "hello.txt" , "hello world");

//        ftpAction.deleteFtp("/delete" , "delete.txt");

//        ftpAction.uploadFile("/" , "writeDemo.xlsx" , "D:\\easyexcel\\writeDemo.xlsx");

        ftpAction.close(ftpAction.ftpClient);
    }
}
