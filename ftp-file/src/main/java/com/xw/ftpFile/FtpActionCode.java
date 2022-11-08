package com.xw.ftpFile;

/**
 * @author xiongwei
 * @description
 */
public enum FtpActionCode {
    DELETE("删除"),
    DOWN("下载"),
    MOVE("移动"),
    READ("读取"),
    WRITE("写入"),
    UPLOAD("上传");

    String chinese;

    FtpActionCode(String chinese){
        this.chinese = chinese;
    }
}
