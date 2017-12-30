package com.nj.nfhy.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nj.nfhy.controller.base.BaseController;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nj.nfhy.util.basicUtils.LetterInfo;
import com.nj.nfhy.util.basicUtils.MsgInfo;
import com.nj.nfhy.util.ftpUtils.FtpUtil;
import com.nj.nfhy.util.basicUtils.ModelBase;
import com.nj.nfhy.util.ftpUtils.QRCodeUtil;
import com.nj.nfhy.util.basicUtils.ValidateUtil;

/**
 * 通过ftp上传文件
 * 
 * @author 88386726
 *
 */
@Controller
@RequestMapping("/fileUploadByFtp")
public class FileUploadByFtpController extends BaseController {
	Logger logger = Logger.getLogger(FileUploadByFtpController.class);
	// 取配置文件参数
	@Value("#{configProperties['uploadPath']}") // 上传盘符路径
	private String uploadPath;
	@Value("#{configProperties['ImgUrl']}") // 服务器地址
	private String imgUrl;
	@Value("#{configProperties['ThumbImgWidth']}")
	private int ThumbImgWidth;
	@Value("#{configProperties['ThumbImgHeight']}")
	private int ThumbImgHeight;
	@Autowired
	private FtpUtil ftputil;
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

	public static void main(String[] args) throws SocketException, IOException {
		FTPClient ftpClient = null;
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.connect("192.168.22.28", 21);
		// 登录FTP服务器
		// ftpClient.setControlEncoding("ISO-8859-1");
		// ftpClient.setControlEncoding("GBK");
		FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
		conf.setServerLanguageCode("zh");
		Boolean flag = ftpClient.login("root", "B.g.s.da!@#");
		// 是否成功登录FTP服务器
		int replyCode = ftpClient.getReplyCode();
		// String path = new String("/土地".getBytes(),"GBK");
		// String dictionaryname=new
		// String("土地证".getBytes("utf-8"),"ISO-8859-1");
		// String currentPwd = ftpClient.printWorkingDirectory();
		// System.out.println(">>>>>>"+currentPwd);
		Boolean flag3 = FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"));
		System.out.println("flag3 :" + flag3);
		// ftpClient.changeWorkingDirectory("/aaa");

		// Boolean flag1 = ftpClient.makeDirectory(name);
		// System.out.println("flag1 :"+flag1);
		FTPFile fsds[] = ftpClient.listDirectories();
		for (FTPFile f : fsds) {
			System.out.println(" dictionary is :" + f.getName());
			// System.out.println(ftpClient.changeWorkingDirectory(f.getLink()));
		}
		Boolean flag2 = ftpClient.changeWorkingDirectory(new String("/data/合同档案".getBytes(), "iso-8859-1"));
		System.out.println("flag2 :" + flag2);
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			System.out.println(111111);
		} else {
			System.out.println(222);
		}
		// FTPFile fsd[] = ftpClient.listDirectories();
		// for (FTPFile f : fsd) {
		// System.out.println(" dictionary is :" + f.getName());
		// // System.out.println(ftpClient.changeWorkingDirectory(f.getLink()));
		// }
		ftpClient.enterLocalPassiveMode();
		FTPFile fs[] = ftpClient.listFiles();
		for (FTPFile f : fs) {
			System.out.println(f.getName());
		}
	}

	/**
	 * 使用ftp下载文件
	 * @param request
	 * @param response
	 */
	@RequestMapping("/download.htm")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		ModelBase model = new ModelBase();
		FTPClient ftpClient = null;
		try {
			String fileName = request.getParameter("filename");
			// "http://192.168.0.248/mp4/2016-10-14/c6ea1936-d79c-4e70-bbc6-af19f380c86c.mp4";
			fileName = "http://" + hostname + fileName;
			String names[] = fileName.split("/");
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + names[names.length - 1]);
			os = response.getOutputStream();
			// String path = Thread.currentThread().getContextClassLoader()
			// .getResource("").getPath()
			// + "download";//这个download目录为啥建立在classes下的
			// InputStream inputStream = new FileInputStream(new File(path
			// + File.separator + fileName));

			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 是否成功登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
			}
			URL url = new URL(fileName);
			String filename = url.getPath();
			String s[] = filename.split("/");
			System.out.println(basepath);
			filename = s[3];
			String path = basepath + "/" + s[1] + "/" + s[2] + "/";
			try {
				ftputil.webdownloadFile(hostname, port, username, password, path, filename, ftpClient, os);
				model.setCode(MsgInfo.a_suc_code);
				model.setMessage(LetterInfo.a_suc_codeMsg);
			} catch (Exception e) {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
			}

			// byte[] b = new byte[2048];
			// int length;
			// while ((length = inputStream.read(b)) > 0) {
			// os.write(b, 0, length);
			// }

		} catch (FileNotFoundException e) {
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_error_codeMsg);
			e.printStackTrace();
		} catch (Exception e) {
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_error_codeMsg);
			e.printStackTrace();
		} finally {
			// 这里主要关闭。
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write("{\"success\":200000}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ftp删除
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/FtpMultyDelFile.htm")
	public void FtpMultyDelFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelBase model = new ModelBase();
		String[] fs = request.getParameterValues("fs");
		String name = request.getParameter("name");
		// 原图
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding("UTF-8");
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 是否成功登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
				model.printJson(model, response, "");
			}

			for (String img : fs) {
				URL url = new URL(img);
				String filename = url.getPath();
				String s[] = filename.split("/");
				filename = s[3];
				basepath = basepath + "/" + s[1] + "/" + s[2] + "/";
				ftputil.deleteMultiFile(hostname, port, username, password, basepath, filename, ftpClient);
			}
			model.setCode(MsgInfo.a_suc_code);
		} catch (Exception e) {
			e.printStackTrace();
			model.setCode(MsgInfo.a_error_code);
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		model.printJson(model, response, "");
		return;

	}

	/**
	 * 生成二维码并使用ftp上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/QRUploadFile.htm")
	public void QRUploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String text = request.getParameter("params");
		ModelBase model = new ModelBase();
		// 原图
		List<String> listImagePath = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		BufferedImage image = QRCodeUtil.createImage(text, "", false);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "JPG", out);
		byte[] b = out.toByteArray();
		InputStream input = new ByteArrayInputStream(b);
		String url = ftputil.FavFTPInit(input, ".jpg", FtpUtil.FTPIMG);
		listImagePath.add(url);
		map.put("listImagePath", listImagePath);
		model.setCode(MsgInfo.a_suc_code);
		model.setData(map);
		model.setMessage(LetterInfo.a_ImgUpload_SucMsg);
		model.printJson(model, response, "");
		return;
		// ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + name));

	}

	/**
	 * base64单个文件上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Base64UploadFile.htm", method = RequestMethod.POST)
	public void base64UploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelBase model = new ModelBase();
		Map<String, Object> map = new HashMap<String, Object>();
		// 原图
		List<String> listImagePath = new ArrayList<String>();
		// 缩略图
		List<String> thumblistImagePath = new ArrayList<String>();
		try {
			String img = request.getParameter("Img");
			// 没有图片
			if (ValidateUtil.isEmpty(img)) {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
				model.printJson(model, response, "");
				return;
			}
			byte[] b = Base64.decode(img.getBytes("utf-8"));
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}

			InputStream input = new ByteArrayInputStream(b);
			String url = ftputil.FavFTPInit(input, ".jpg", FtpUtil.FTPIMG);
			b = FtpUtil.compressPicByte(b, ThumbImgWidth, ThumbImgHeight, true);
			input = new ByteArrayInputStream(b);
			// String compressurl = ftputil.FavFTPInit(input, ".jpg",
			// FtpUtil.FTPIMG);

			listImagePath.add(url);
			// thumblistImagePath.add(compressurl);
			map.put("listImagePath", listImagePath);
			map.put("thumblistImagePath", thumblistImagePath);
			model.setCode(MsgInfo.a_suc_code);
			model.setData(map);
			model.setMessage(LetterInfo.a_ImgUpload_SucMsg);
			model.printJson(model, response, "");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_error_codeMsg);
			model.printJson(model, response, "");
			return;
		}

	}

	/**
	 * 多文件上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/multiUploadFile.htm")
	@ResponseBody
	public void moreUploadFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response) {
		ModelBase model = new ModelBase();
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		String callback = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 原图
		List<String> listImagePath = new ArrayList<String>();
		// 缩略图
		List<String> thumblistImagePath = new ArrayList<String>();
		try {
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 是否成功登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
				model.printJson(model, response, "");
			}
			System.out.println("ftp conn to : >>>>" + hostname + "   " + port);
			System.out.println("ftp conn to : >>>>" + hostname + "   " + port);
			System.out.println("ftp conn to : >>>>" + hostname + "   " + port);
			if (files != null && files.length > 0) {
				for (MultipartFile mf : files) {
					if (!mf.isEmpty()) {
						byte[] b = mf.getBytes();
						InputStream input = new ByteArrayInputStream(b);
						String url = ftputil.FavMultyFTPInit(input, ".png", FtpUtil.FTPIMG, ftpClient);
						// b = FtpUtil.compressPicByte(b, ThumbImgWidth,
						// ThumbImgHeight, true);
						// input = new ByteArrayInputStream(b);
						// String compressurl = ftputil.FavFTPInit(input,
						// ".jpg", FtpUtil.FTPIMG);

						listImagePath.add(url);
						// thumblistImagePath.add(compressurl);
					}
				}
				map.put("listImagePath", listImagePath);
				map.put("thumblistImagePath", thumblistImagePath);
				model.setCode(MsgInfo.a_suc_code);
				model.setData(map);
				model.setImgUrlPrefix(imgUrl);
				model.setMessage(LetterInfo.a_ImgUpload_SucMsg);
				model.printJson(model, response, callback);
			}

		} catch (Exception e) {
			logger.info("异常-------------" + e);
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_ImgUpload_ErrorMsg);
			model.printJson(model, response, callback);
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * base64多文件上传
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/Base64MultyUploadFile.htm", method = RequestMethod.POST)
	public void base64MultyUploadFile(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> picmap = new HashMap<String, String>();
		ModelBase model = new ModelBase();
		FTPClient ftpClient = new FTPClient();
		try {
			Enumeration<String> paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				if (request.getParameter(paramName) != null) {
					String paramValue = request.getParameter(paramName);
					if (ValidateUtil.isEmpty(paramValue)) {
						model.setCode(MsgInfo.a_error_code);
						model.setMessage(LetterInfo.a_error_codeMsg);
						model.printJson(model, response, "");
						return;
					}
					if (paramValue.length() != 0) {
						picmap.put(paramName, paramValue);
						System.out.println(
								"Base64MultyUploadFile  get parameter ==========>" + paramName + ":" + paramValue);
					}
				}
			}

			ftpClient.setControlEncoding("UTF-8");
			// 连接FTP服务器
			System.out.println("ftp conn to : >>>>" + hostname + "   " + port);
			System.out.println("ftp conn to : >>>>" + hostname + "   " + port);
			System.out.println("ftp conn to : >>>>" + hostname + "   " + port);
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 是否成功登录FTP服务器
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
				model.printJson(model, response, "");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			Set<String> names = picmap.keySet();
			for (String name : names) {
				String pic = picmap.get(name);
				byte[] b = Base64.decode(pic.getBytes("utf-8"));
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}

				InputStream input = new ByteArrayInputStream(b);
				String url = ftputil.FavMultyFTPInit(input, ".jpg", FtpUtil.FTPIMG, ftpClient);
				List<String> listImagePath = new ArrayList<String>();
				listImagePath.add(url);
				map.put(name, listImagePath);

			}

			model.setCode(MsgInfo.a_suc_code);
			model.setData(map);
			model.setMessage(LetterInfo.a_ImgUpload_SucMsg);
			model.printJson(model, response, "");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_error_codeMsg);
			model.printJson(model, response, "");
			return;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * mp4上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mp4UploadFile.htm")
	@ResponseBody
	public void mp4UploadFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response) {
		ModelBase model = new ModelBase();
		String callback = "";
		Map<String, Object> map = new HashMap<String, Object>();
		// 原图
		List<String> listImagePath = new ArrayList<String>();
		// 缩略图
		List<String> thumblistImagePath = new ArrayList<String>();
		try {
			if (files != null && files.length > 0) {
				for (MultipartFile mf : files) {
					if (!mf.isEmpty()) {
						byte[] b = mf.getBytes();
						InputStream input = new ByteArrayInputStream(b);
						String url = ftputil.FavFTPInit(input, ".mp4", FtpUtil.FTPMP4);
						listImagePath.add(url);
					}
				}
				map.put("map4", listImagePath);
				model.setCode(MsgInfo.a_suc_code);
				model.setData(map);
				model.setMessage("视频上传成功");
				model.printJson(model, response, callback);
			}

		} catch (Exception e) {
			logger.info("异常-------------" + e);
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_ImgUpload_ErrorMsg);
			model.printJson(model, response, callback);
		}
	}

}