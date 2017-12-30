package com.nj.nfhy.util.ftpUtils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import com.nj.nfhy.util.basicUtils.DateUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpUtil {   
	@Value("#{configProperties['ftp.hostname']}") 
	private String hostname;
	@Value("#{configProperties['ftp.hosturl']}") 
	private String hosturl;
	@Value("#{configProperties['ftp.port']}") 
	private Integer port;
	@Value("#{configProperties['ftp.username']}") 
	private String username;
	@Value("#{configProperties['ftp.password']}") 
	private String password;
	@Value("#{configProperties['ftp.mp4path']}")
	private String mp4path;
	@Value("#{configProperties['ftp.flvpath']}")
	private String flvpath;
	@Value("#{configProperties['ftp.imgpath']}")
	private String imgpath;
	@Value("#{configProperties['ftp.basepath']}")
	private String basepath;
	
	public static final String FTPIMG="img";
	public static final String FTPMP4="mp4";
	public static final String FTPFLV="flv";
	
	
	
	
	public static byte[] compressPicByte(byte[] imageByte, int width, int height, boolean gp) {  
	    	byte[] inByte = null;  
		try {   
		ByteArrayInputStream byteInput = new ByteArrayInputStream(imageByte);  
		Image img = ImageIO.read(byteInput);  
		// 判断图片格式是否正确   
		if (img.getWidth(null) == -1) {  
		return inByte;  
		} else {   
		int newWidth; int newHeight;   
		// 判断是否是等比缩放   
		if (gp == true) {   
		// 为等比缩放计算输出的图片宽度及高度   
		double rate1 = ((double) img.getWidth(null)) / (double) width + 0.1;   
		double rate2 = ((double) img.getHeight(null)) / (double) height + 0.1;  
		// 根据缩放比率大的进行缩放控制   
		double rate = rate1 > rate2 ? rate1 : rate2;   
		newWidth = (int) (((double) img.getWidth(null)) / rate);   
		newHeight = (int) (((double) img.getHeight(null)) / rate);   
		} else {   
		newWidth = width; // 输出的图片宽度   
		newHeight = height; // 输出的图片高度   
		}   
		BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);   
		img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);  
		/* 
		* Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 
		* 优先级比速度高 生成的图片质量比较好 但速度慢 
		*/   
		tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);  
		  
		ImageWriter imgWrier;  
		ImageWriteParam imgWriteParams;  
		// 指定写图片的方式为 jpg  
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();  
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);  
//		       // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT  
//		       imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);  
//		       // 这里指定压缩的程度，参数qality是取值0~1范围内，  
//		       imgWriteParams.setCompressionQuality((float)45217/imageByte.length);  
//		                          
//		       imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);  
//		       ColorModel colorModel = ColorModel.getRGBdefault();  
//		       // 指定压缩时使用的色彩模式  
//		       imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel  
//		               .createCompatibleSampleModel(100, 100)));  
		//将压缩后的图片返回字节流  
		ByteArrayOutputStream out = new ByteArrayOutputStream(imageByte.length);  
		imgWrier.reset();  
		// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何 OutputStream构造  
		imgWrier.setOutput(ImageIO.createImageOutputStream(out));  
		// 调用write方法，就可以向输入流写图片  
		imgWrier.write(null, new IIOImage(tag, null, null), imgWriteParams);  
		out.flush();  
		out.close();  
		byteInput.close();  
		inByte = out.toByteArray();  
		  
		}   
		} catch (IOException ex) {   
		ex.printStackTrace();  
		}   
		return inByte;  
		}  
	
    public static BufferedImage getBufferedImage(String imgUrl,int w,int h) { 
        URL url = null; 
        InputStream is = null; 
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        try { 
            url = new URL(imgUrl); 
            is = url.openStream(); 
            img = ImageIO.read(is); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
             
            try { 
                is.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return img; 
    }  
	
	
	 public String FavFTPInit(InputStream inputStream,String suffix,String type){
		 String filename =UUID.randomUUID().toString();
		 String today = DateUtil.getToday();
		 String path = "";
		 String urlpath = "";
		 if(type.equals(FtpUtil.FTPIMG)){
			 path = basepath+imgpath+"/"+today+"/";
			 urlpath = imgpath+"/"+today+"/";
		 }else if(type.equals(FtpUtil.FTPMP4)){
			 path = basepath+mp4path+"/"+today+"/";
			 urlpath = mp4path+"/"+today+"/";
		 }else if(type.equals(FtpUtil.FTPFLV)){
			 path = basepath+flvpath+"/"+today+"/";
			 urlpath = flvpath+"/"+today+"/";
		 }
		 boolean flag =uploadFileFromProduction(hostname, port, username, password, path, filename+suffix, inputStream);
		 if(flag){
			 return hosturl+urlpath+filename+suffix;
		 }else{
			 return null;
		 }
	 }
	 
	 
	 public static void main(String[] args) throws FileNotFoundException {
		 String s = "http://192.168.0.248/img/2016-10-13/279e0c64-0d7d-4d59-bed0-c33b88a63118.png";
			URL url= null;
			try {
				url = new URL(s);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(url.getHost());
			System.out.println(url.getPath());
			System.out.println(url.getProtocol());
		    
		//   String localpath = "D:/";
		      
		//   FavFTPUtil.downloadFile(hostname, port, username, password, pathname, filename, localpath);
	}
       
	 /**
	   * 上传文件（可供Action/Controller层使用）
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器保存目录
	   * @param fileName  上传到FTP服务器后的文件名称
	   * @param inputStream 输入文件流
	   * @return
	   */
	  public boolean uploadFile(String hostname, int port, String username, String password, String pathname, String fileName, InputStream inputStream){
	    boolean flag = false;
	    FTPClient ftpClient = new FTPClient();
	    ftpClient.setControlEncoding("UTF-8");
	    try {
	      //连接FTP服务器
	    	System.out.println("ftp conn to : >>>>"+hostname+"   "+ port);
	    	System.out.println("ftp conn to : >>>>"+hostname+"   "+ port);
	    	System.out.println("ftp conn to : >>>>"+hostname+"   "+ port);
	      ftpClient.connect(hostname, port);
	      //登录FTP服务器
	      ftpClient.login(username, password);
	      //是否成功登录FTP服务器
	      int replyCode = ftpClient.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(replyCode)){
	        return flag;
	      }
	        
	      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	      ftpClient.makeDirectory(pathname);
	      ftpClient.changeWorkingDirectory(pathname);
	      ftpClient.storeFile(fileName, inputStream);
	      inputStream.close();
	      ftpClient.logout();
	      flag = true;
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally{
	      if(ftpClient.isConnected()){
	        try {
	          ftpClient.disconnect();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }
	    return flag;
	  }
	    
	    
	  /**
	   * 上传文件（可对文件进行重命名）
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器保存目录
	   * @param filename  上传到FTP服务器后的文件名称
	   * @param originfilename 待上传文件的名称（绝对地址）
	   * @return
	   */
	  public boolean uploadFileFromProduction(String hostname, int port, String username, String password, String pathname, String filename, InputStream inputStream){
	    boolean flag = false;
	    try {
	      flag = uploadFile(hostname, port, username, password, pathname, filename, inputStream);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return flag;
	  }
	    
	  /**
	   * 上传文件（不可以进行文件的重命名操作）
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器保存目录
	   * @param originfilename 待上传文件的名称（绝对地址）
	   * @return
	   */
	  public boolean uploadFileFromProduction(String hostname, int port, String username, String password, String pathname,  InputStream inputStream,String fileName){
	    boolean flag = false;
	    try {
	      flag = uploadFile(hostname, port, username, password, pathname, fileName, inputStream);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return flag;
	  }
	  
	  
	  /**
	   * 下载文件
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器保存目录
	   * @param filename  要删除的文件名称
	   * @return
	 * @throws IOException 
	   */
	  public boolean webdownloadFile(String hostname, int port, String username, String password, String pathname, String filename, FTPClient ftpClient,OutputStream os) throws IOException{
	      boolean flag = false;
	      //验证FTP服务器是否登录成功
	      int replyCode = ftpClient.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(replyCode)){
	        return flag;
	      }
	      //切换FTP目录
	      ftpClient.changeWorkingDirectory(pathname);
	      //ftpClient.setControlEncoding("GB2312");
	      //ftpClient.dele(filename);
	      //flag = ftpClient.deleteFile(filename);
          //ftpClient.enterRemotePassiveMode();
	      ftpClient.enterLocalPassiveMode(); 
	      flag =ftpClient.retrieveFile(filename, os);
	      System.out.println(filename+" download file is "+flag);
	      //flag = true;
	      return flag;
	  }  
	  
	  
	  /**
	   * 删除文件
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器保存目录
	   * @param filename  要删除的文件名称
	   * @return
	 * @throws IOException 
	   */
	  public boolean deleteMultiFile(String hostname, int port, String username, String password, String pathname, String filename, FTPClient ftpClient) throws IOException{
	    boolean flag = false;
	      //验证FTP服务器是否登录成功
	      int replyCode = ftpClient.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(replyCode)){
	        return flag;
	      }
	      //切换FTP目录
	      ftpClient.changeWorkingDirectory(pathname);
	      //ftpClient.dele(filename);
	      flag = ftpClient.deleteFile(filename);
	      System.out.println(filename+" delete file is "+flag);
	      flag = true;
	    return flag;
	  }  
	  
	  
	  /**
	   * 删除文件
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器保存目录
	   * @param filename  要删除的文件名称
	   * @return
	   */
	  public boolean deleteFile(String hostname, int port, String username, String password, String pathname, String filename){
	    boolean flag = false;
	    FTPClient ftpClient = new FTPClient();
	    try {
	      //连接FTP服务器
	      ftpClient.connect(hostname, port);
	      //登录FTP服务器
	      ftpClient.login(username, password);
	      //验证FTP服务器是否登录成功
	      int replyCode = ftpClient.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(replyCode)){
	        return flag;
	      }
	      //切换FTP目录
	      ftpClient.changeWorkingDirectory(pathname);
	      ftpClient.dele(filename);
	      ftpClient.logout();
	      flag = true;
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally{
	      if(ftpClient.isConnected()){
	        try {
	          ftpClient.logout();
	        } catch (IOException e) {
	          
	        }
	      }
	    }
	    return flag;
	  }
	    
	  /**
	   * 下载文件
	   * @param hostname FTP服务器地址
	   * @param port  FTP服务器端口号
	   * @param username  FTP登录帐号
	   * @param password  FTP登录密码
	   * @param pathname  FTP服务器文件目录
	   * @param filename  文件名称
	   * @param localpath 下载后的文件路径
	   * @return
	   */
	  public boolean downloadFile(String hostname, int port, String username, String password, String pathname, String filename, String localpath){
	    boolean flag = false;
	    FTPClient ftpClient = new FTPClient();
	    try {
	      //连接FTP服务器
	      ftpClient.connect(hostname, port);
	      //登录FTP服务器
	      ftpClient.login(username, password);
	      //验证FTP服务器是否登录成功
	      int replyCode = ftpClient.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(replyCode)){
	        return flag;
	      }
	      //切换FTP目录
	      ftpClient.changeWorkingDirectory(pathname);
	      FTPFile[] ftpFiles = ftpClient.listFiles();
	      for(FTPFile file : ftpFiles){
	        if(filename.equalsIgnoreCase(file.getName())){
	          File localFile = new File(localpath + "/" + file.getName());
	          OutputStream os = new FileOutputStream(localFile);
	          ftpClient.retrieveFile(file.getName(), os);
	          os.close();
	        }
	      }
	      ftpClient.logout();
	      flag = true;
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally{
	      if(ftpClient.isConnected()){
	        try {
	          ftpClient.logout();
	        } catch (IOException e) {
	          
	        }
	      }
	    }
	    return flag;
	  }

	public String FavMultyFTPInit(InputStream inputStream, String suffix, String type, FTPClient ftpClient) {
		 String filename =UUID.randomUUID().toString();
		 String today = DateUtil.getToday();
		 String path = "";
		 String urlpath = "";
		 if(type.equals(FtpUtil.FTPIMG)){
			 path = basepath+imgpath+"/"+today+"/";
			 urlpath = imgpath+"/"+today+"/";
		 }else if(type.equals(FtpUtil.FTPMP4)){
			 path = basepath+mp4path+"/"+today+"/";
			 urlpath = mp4path+"/"+today+"/";
		 }else if(type.equals(FtpUtil.FTPFLV)){
			 path = basepath+flvpath+"/"+today+"/";
			 urlpath = flvpath+"/"+today+"/";
		 }
		 boolean flag =multyUploadFileFromProduction(hostname, port, username, password, path, filename+suffix, inputStream,ftpClient);
		 if(flag){
			 return hosturl+urlpath+filename+suffix;
		 }else{
			 return null;
		 }
	}

	private boolean multyUploadFileFromProduction(String hostname, int port, String username, String password, String pathname, String filename, InputStream inputStream, FTPClient ftpClient) {
		 boolean flag = false;
		    try {
		      flag = MultyUploadFile(hostname, port, username, password, pathname, filename, inputStream,ftpClient);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return flag;
	}

	private boolean MultyUploadFile(String hostname, int port, String username, String password, String pathname, String fileName, InputStream inputStream, FTPClient ftpClient) {
		 boolean flag = false;
		    try{
		        
		      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		      ftpClient.makeDirectory(pathname);
		      ftpClient.changeWorkingDirectory(pathname);
		      ftpClient.storeFile(fileName, inputStream);
		      inputStream.close();
		     // ftpClient.logout();
		      flag = true;
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
//		    finally{
//		      if(ftpClient.isConnected()){
//		        try {
//		          ftpClient.disconnect();
//		        } catch (IOException e) {
//		          e.printStackTrace();
//		        }
//		      }
//		    }
		    return flag;
	}
}  