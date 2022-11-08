package com.xw.ftpFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author xiongwei
 * @description
 */
public class FtpAction {

    FTPClient ftpClient;

    FtpAction(FTPClient ftpClient){
        this.ftpClient = ftpClient;
    }

    /**
     *
     * @param pathName     要删除文件/目录所在ftp路径
     * @param fileName     要删除文件所在ftp的文件名
     */
    void deleteFtp(String pathName, String fileName ) throws IOException {
        // 跳转到文件目录
        ftpClient.changeWorkingDirectory(pathName);
        processBeforeOperation(FtpActionCode.DELETE);
        if (Safes.of(fileName)) {
            //文件名不为空，删除指定文件
            ftpClient.deleteFile(pathName + File.separator + fileName);
            System.out.println("删除成功");
        } else {
            //文件名为空，删除路径下所有文件
            System.out.println("正在删除");
            //删除文件
            //获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                if (file.isFile()) {
                    //判断为文件，直接删除
                    ftpClient.deleteFile(pathName + File.separator + file.getName());
                    System.out.println(file + "：已完成删除操作");
                }
                if (file.isDirectory()) {
                    deleteFtp(pathName + File.separator + file.getName(), null );
                }
            }
            //删除文件夹
            ftpClient.removeDirectory(pathName);
            System.out.println(pathName + "删除操作完成");
        }
        processAfterOperation(FtpActionCode.DELETE);
    }

    /**
     * @param pathName     要下载文件所在ftp路径
     * @param fileName     要下载文件所在ftp的文件名
     * @param downloadPath 文件下载后保存的路径
     */
    public void downLoadFTP(String pathName, String fileName, String downloadPath) throws IOException {
        OutputStream outputStream = null;
        // 跳转到文件目录
        ftpClient.changeWorkingDirectory(pathName);
        processBeforeOperation(FtpActionCode.DOWN);
        if (Safes.of(fileName)) {
            //文件名不为空，下载指定文件
            File filePath = new File(downloadPath);
            if (!filePath.exists()) {
                //目录不存在，创建目录
                filePath.mkdir();
            }
            outputStream = Files.newOutputStream(new File(downloadPath + File.separator + fileName).toPath());
            ftpClient.retrieveFile(fileName, outputStream);
            System.out.println("下载操作完成");
        } else {
            //获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            //文件名为空，下载路径下所有文件（不包含文件夹）
            for (FTPFile file : files) {
                File filePath = new File(downloadPath);
                if (!filePath.exists()) {
                    //目录不存在，创建目录
                    filePath.mkdir();
                }
                File downloadFile = new File(downloadPath + File.separator + file.getName());
                outputStream = Files.newOutputStream(downloadFile.toPath());
                ftpClient.retrieveFile(file.getName(), outputStream);

                System.out.println("下载操作完成：" + downloadFile);
            }
        }
        processAfterOperation(FtpActionCode.DOWN);
        close( outputStream );
    }

    /**
     * @param pathName     要移动文件所在ftp路径
     * @param fileName     要移动文件所在ftp的文件名
     * @param movePath     文件移动后的路径
     * @param moveName     文件移动后的文件名（与源文件一致时实现只移动不重命名，不一致则实现了移动+重命名）
     */
    public void moveFTP(String pathName, String fileName, String movePath, String moveName) throws IOException {
        processBeforeOperation(FtpActionCode.MOVE);
        if (!ftpClient.changeWorkingDirectory(movePath)) {
            //跳转到目标路径失败时创建目标目录
            ftpClient.makeDirectory(movePath);
        }
        if (Safes.of(moveName)) {
            // 跳转回需要进行操作的目录
            ftpClient.changeWorkingDirectory(pathName);
            //移动后的文件名不为空，移动目标文件
            //ftpClient.rename(旧文件名, 新路径)
            ftpClient.rename(fileName, movePath + File.separator + moveName);
            System.out.println("文件移动操作已完成：" + movePath + File.separator + moveName);
        } else {
            //移动后的文件名为空，移动目标路径所有文件
            // 跳转回需要进行操作的目录
            ftpClient.changeWorkingDirectory(pathName);
            //获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                ftpClient.rename(file.getName(), movePath + File.separator + file.getName());
                System.out.println("移动操作完成：" + file.getName());
            }
        }
        processAfterOperation(FtpActionCode.MOVE);
    }

    /**
     *
     * @param pathName     要读取文件所在ftp路径
     * @param fileName     要读取文件所在ftp的文件名
     */
    public void readFTP(String pathName, String fileName) throws IOException {

        InputStream inputStream = null;
        BufferedReader reader = null;
        // 跳转到文件目录
        ftpClient.changeWorkingDirectory(pathName);
        processBeforeOperation(FtpActionCode.READ);
        if (Safes.of(fileName)) {
            //文件名不为空，读取指定文件
            inputStream = ftpClient.retrieveFileStream(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String fileL;
            StringBuilder buffer = new StringBuilder();
            while(((fileL=reader.readLine()) != null)){
                buffer.append(fileL);
            }
            System.out.println(fileName);
            System.out.println(buffer);

        } else {
            //获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            //文件名为空，读取路径下所有文件（不包含文件夹）
            System.out.println(Arrays.toString(files));
            for (FTPFile file : files) {
                System.out.println(file.getName());
                inputStream = ftpClient.retrieveFileStream(file.getName());
                System.out.println(inputStream);
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String fileL;
                StringBuilder buffer = new StringBuilder();

                while(((fileL=reader.readLine()) != null)){
                    buffer.append(fileL).append("\n");
                }
                System.out.println(fileName);
                System.out.println(buffer);
                //retrieveFileStream使用了流，需要释放一下，不然会返回空指针
                ftpClient.completePendingCommand();
            }
        }
        processAfterOperation(FtpActionCode.READ);
        close(inputStream , reader);
    }

    /**
     * @param pathName 文件上传到ftp服务器的路径
     * @param fileName 文件上传到ftp服务器的名称
     * @param originPath 要上传文件所在的路径（绝对路径）
     **/
    public void uploadFile(String pathName, String fileName, String originPath) throws IOException {
        //将文本数据转换为输入流
        InputStream inputStream = Files.newInputStream(new File(originPath).toPath());
        processBeforeOperation(FtpActionCode.UPLOAD);
        //在ftp服务器创建目标路径
        ftpClient.makeDirectory(pathName);
        //切换到目标路径
        ftpClient.changeWorkingDirectory(pathName);
        //开始上传，inputStream表示数据源。
        ftpClient.storeFile(fileName, inputStream);
        processAfterOperation(FtpActionCode.UPLOAD);
        close( inputStream );
    }

    /**
     * @param pathName 文本写入到ftp服务器的路径
     * @param fileName 文本写入到ftp服务器的名称
     * @param contentText 要写入的文本数据
     **/
    public void writeFile(String pathName, String fileName, String contentText) throws IOException {
        InputStream inputStream = null;
        //将文本数据转换为输入流，通过getBytes()方法避免中文乱码
        inputStream = new ByteArrayInputStream(contentText.getBytes());
        processBeforeOperation(FtpActionCode.WRITE);
        //在ftp服务器创建目标路径
        ftpClient.makeDirectory(pathName);
        //切换到目标路径
        ftpClient.changeWorkingDirectory(pathName);
        //开始写入，inputStream表示数据源。
        ftpClient.storeFile(fileName, inputStream);
        processAfterOperation(FtpActionCode.WRITE);
        close( inputStream);
    }

    void processAfterOperation(FtpActionCode code){
        System.out.println(code.chinese + "结束");
    }

    /**
     * 关闭
     * @param objects 对象
     */
    void close(Object... objects){
        for (Object o : objects){
            if (Objects.isNull(o)){
                continue;
            }
            try{
                if (o instanceof FTPClient){
                    FTPClient ftpClient = (FTPClient) o;
                    ftpClient.disconnect();
                }
                if (o instanceof InputStream){
                    InputStream inputStream = (InputStream) o;
                    inputStream.close();
                }
                if (o instanceof BufferedReader){
                    BufferedReader reader = (BufferedReader) o;
                    reader.close();
                }
                if (o instanceof OutputStream){
                    OutputStream outputStream = (OutputStream) o;
                    outputStream.close();
                }
            }catch(IOException e){
                System.out.println(o.getClass() + " 关闭失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 在进行操作之前需要的动作
     */
    void processBeforeOperation(FtpActionCode code) throws IOException {
        System.out.println(code.chinese + "开始");
        switch (code){
            case WRITE:
                //以二进制文件形式输入
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            case DELETE:
            case DOWN:
            case READ:
            case UPLOAD:
            case MOVE:
                //设置被动模式传输
                ftpClient.enterLocalPassiveMode();
                break;
            default:
        }
    }
}
