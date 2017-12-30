package com.nj.nfhy.util.ftpUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FTPClientUtil {
	 private static final Logger LOGGER = Logger.getLogger(FTPClientUtil.class);

	    /**
	     * 连接文件服务器
	     * @param addr 文件服务器地址
	     * @param port 端口
	     * @param username 用户名
	     * @param password 密码
	     * @throws Exception 
	     */
	    public static FTPClient connect(String addr, int port, String username, String password) {
	        LOGGER.info("【连接文件服务器】addr = " + addr + " , port : " + port + " , username = " + username + " , password = "
	                    + password);

	        FTPClient ftpClient = new FTPClient();
	        try {
	            // 连接
	            ftpClient.connect(addr, port);
	            // 登录
	            ftpClient.login(username, password);
	            // 被动模式：每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据（参考资料：FTP主动/被动模式的解释）
	            ftpClient.enterLocalPassiveMode();
	            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	        } catch (Exception e) {
	            LOGGER.error("【连接文件服务器失败】", e);
	            throw new RuntimeException("连接文件服务器失败");
	        }
	        // 判断文件服务器是否可用？？
	        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
	            closeConnection(ftpClient);
	        }
	        return ftpClient;
	    }

	    /**
	     * 连接文件服务器
	     * @param addr 文件服务器地址
	     * @param port 端口
	     * @param username 用户名
	     * @param password 密码
	     * @param workingDirectory 目标连接工作目录
	     * @throws Exception 
	     */
	    public static FTPClient connect(String addr, int port, String username, String password, String workingDirectory)
	                                                                                                                     throws Exception {
	        FTPClient ftpClient = connect(addr, port, username, password);
	        changeWorkingDirectory(workingDirectory, ftpClient);
	        return ftpClient;
	    }

	    /**
	     * 关闭连接，使用完连接之后，一定要关闭连接，否则服务器会抛出 Connection reset by peer的错误
	     * @throws IOException
	     */
	    public static void closeConnection(FTPClient ftpClient) {
	        LOGGER.info("【关闭文件服务器连接】");
	        if (ftpClient == null) {
	            return;
	        }

	        try {
	            ftpClient.disconnect();
	        } catch (IOException e) {
	            LOGGER.error("【关闭连接失败】", e);
	            throw new RuntimeException("关闭连接失败");
	        }
	    }

	    /**
	     * 切换工作目录
	     * @param directory 目标工作目录
	     * @param ftpClient 
	     * @throws IOException
	     */
	    public static void changeWorkingDirectory(String directory, FTPClient ftpClient) {
	        LOGGER.info("【切换工作目录】directory : " + directory);
	        baseValidate(ftpClient);
	        // 切换到目标工作目录
	        try {
	            if (!ftpClient.changeWorkingDirectory(directory)) {
	                ftpClient.makeDirectory(directory);
	                ftpClient.changeWorkingDirectory(directory);
	            }
	        } catch (Throwable e) {
	            LOGGER.error("【切换工作目录失败】", e);
	            throw new RuntimeException("切换工作目录失败");
	        }
	    }

	    /** 
	     * 在服务器上创建一个文件夹 
	     * 
	     * @param dir 
	     *            文件夹名称，不能含有特殊字符，如 \ 、/ 、: 、* 、?、 "、 <、>... 
	     */  
	    public static boolean makeDirectory(FTPClient ftpClient,String dir) {  
	        boolean flag = true;  
	        try {  
	            flag = ftpClient.makeDirectory(dir);  
	            if (flag) {  
	                System.out.println("make Directory " +dir +" succeed");  
	  
	            } else {  
	  
	                System.out.println("make Directory " +dir+ " false");  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return flag;  
	    }  
	    //复制文件       
	    //sourceFileName 目标名称sourceDir  目标文件夹 targetDir 复制到的文件夹
	    public static void copyFile(FTPClient ftpClient,String sourceFileName, String sourceDir, String targetDir) throws IOException {  
	    	ByteArrayInputStream in = null;  
	        ByteArrayOutputStream fos = new ByteArrayOutputStream();  
	    	try{
	            ftpClient.setBufferSize(1024 * 2);  
	            // 变更工作路径  
	            ftpClient.changeWorkingDirectory(sourceDir);  
	            // 设置以二进制流的方式传输  
	            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);  
	            // 将文件读到内存中  
	            
	            ftpClient.retrieveFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), fos);  
	            in = new ByteArrayInputStream(fos.toByteArray());  
	            if (in != null) {  
	                ftpClient.changeWorkingDirectory(targetDir);  
	                ftpClient.storeFile(new String(sourceFileName.getBytes("GBK"), "iso-8859-1"), in);  
	            }  
	        } finally {  
	            // 关闭流  
	            if (in != null) {  
	                in.close();  
	            }  
	            if (fos != null) {  
	                fos.close();  
	            }  
	        }  
	    }  

	    /**
	     * 基本校验
	     * @param ftpClient {@link FTPClient}
	     */
	    private static void baseValidate(FTPClient ftpClient) {
	        if (ftpClient == null) {
	            LOGGER.warn("【参数ftpClient为空】");
	            throw new NullArgumentException("ftpClient");
	        }
	    }
}
