/**
 * 
 */
package com.nj.nfhy.util.ftpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.http.HttpServletResponse;

import com.nj.nfhy.pojo.Ftp;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * @author 88386726 2017年12月28日
 *
 */
public class FtpUtils {
	private static Logger logger = Logger.getLogger(FtpUtil.class);
	private static FTPClient ftp;

	public static boolean connectFtp(Ftp f) throws Exception {
		logger.info("===============>进入ftp 连接");
		ftp = new FTPClient();
		boolean flag = false;
		int reply;
		if (f.getPort() == null) {
			ftp.connect(f.getIpAddr(), 21);
		} else {
			ftp.connect(f.getIpAddr(), f.getPort());
		}
		flag = ftp.login(f.getUserName(), f.getPwd());
		System.out.println(flag);
		if (!flag) {
			return false;
		}
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			closeFtp();
			return flag;
		}
		flag = ftp.changeWorkingDirectory(f.getPath());
		return flag;
	}

	/*
	 * 关闭ftp连接
	 */
	public static void closeFtp() {
		if (ftp != null && ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void startDown(Ftp f, String remoteBaseDir, HttpServletResponse response) throws Exception {
		if (connectFtp(f)) {
			FTPFile[] files = null;
			boolean changedir = ftp.changeWorkingDirectory(remoteBaseDir);
			if (changedir) {
				ftp.setControlEncoding("GBK");
				files = ftp.listFiles();
				for (int i = 0; i < files.length; i++) {
					downloadFile(files[i], response);
				}
			}

		} else {
			logger.error("链接失败！");
		}
	}

	/**
	 * 
	 * 下载FTP文件 当你需要下载FTP文件的时候，调用此方法 根据<b>获取的文件名，本地地址，远程地址</b>进行下载
	 * 
	 * @param ftpFile
	 * @param response
	 */
	private static void downloadFile(FTPFile ftpFile, HttpServletResponse response) {
		if (ftpFile.isFile()) {
			if (ftpFile.getName().indexOf("?") == -1) {
				OutputStream outputStream = null;
				try {
					outputStream = response.getOutputStream();
					;
					ftp.retrieveFile(ftpFile.getName(), outputStream);
					outputStream.flush();
					outputStream.close();
				} catch (Exception e) {
					logger.error(e);
				} finally {
					try {
						if (outputStream != null) {
							outputStream.close();
						}
					} catch (IOException e) {
						logger.error("输出文件流异常");
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		Ftp f = new Ftp();
		f.setPort(21);
		f.setIpAddr("192.168.0.248");
		f.setUserName("ftpuser");
		f.setPwd("123123");
		
		try {
			connectFtp(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
