package com.nj.nfhy.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nj.nfhy.controller.base.BaseController;
import com.nj.nfhy.util.basicUtils.*;
import com.nj.nfhy.util.ftpUtils.ImgCompress;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

/**
 * 通过流上传文件
 *
 * @author 88386726
 *
 */
@Controller
@RequestMapping("/fileuploadByIo")
public class FileuploadByIoController extends BaseController {
	Logger logger = Logger.getLogger(FileuploadByIoController.class);
	// 取配置文件参数
	@Value("#{configProperties['uploadPath']}") //上传盘符路径
	private String uploadPath;
	@Value("#{configProperties['ImgUrl']}") //服务器地址
	private String ImgUrl;
	@Value("#{configProperties['ThumbImgWidth']}")
	private int ThumbImgWidth;
	@Value("#{configProperties['ThumbImgHeight']}")
	private int ThumbImgHeight;

	@PostConstruct
	public void print(){
		System.out.println(1111);
	}
	
	/**
	 * 多文件上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/moreUploadFile.htm")
	@ResponseBody
	public void moreUploadFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
			HttpServletRequest request, HttpServletResponse response) {
		ModelBase model = new ModelBase();
		String callback = "";
		String path = "";
		String today = DateUtil.getToday();
		String uploadFileUrl = uploadPath + "\\" + today + "\\";
		Map<String, Object> map = new HashMap<String, Object>();

		// 原图
		List<String> listImagePath = new ArrayList<String>();
		// 缩略图
		List<String> thumblistImagePath = new ArrayList<String>();
		try {
			if (files != null && files.length > 0) {
				for (MultipartFile mf : files) {
					if (!mf.isEmpty()) {
						File dir = new File(uploadFileUrl);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						// 生成uuid作为文件名称
						String uuid = UUID.randomUUID().toString().replaceAll("-", "");
						// 获得文件类型（可以判断如果不是图片，禁止上传）
						String contentType = mf.getContentType();
						// 获得文件后缀名称
						String imageName = contentType.substring(contentType.indexOf("/") + 1);
						path = uuid + "." + imageName;
						File targetFile = new File(uploadFileUrl + path);
						if (!targetFile.exists()) {
							targetFile.createNewFile();
						}
						mf.transferTo(targetFile);
						String pathOne = "thumb" + uuid + ".jpg";
						ImgCompress imgComOne = new ImgCompress(uploadFileUrl + path);
						imgComOne.resizeFix(ThumbImgWidth, ThumbImgHeight, uploadFileUrl + pathOne);
						listImagePath.add(today + "/" + path);
						thumblistImagePath.add(today + "/" + pathOne);
					}
				}
				map.put("listImagePath", listImagePath);
				map.put("thumblistImagePath", thumblistImagePath);
				model.setCode(MsgInfo.a_suc_code);
				model.setData(map);
				model.setImgUrlPrefix(ImgUrl);
				model.setMessage(LetterInfo.a_ImgUpload_SucMsg);
				model.printJson(model, response, callback);
			}

		} catch (Exception e) {
			logger.info("异常-------------" + e);
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_ImgUpload_ErrorMsg);
			model.printJson(model, response, callback);
		}
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
		try{
			String img=request.getParameter("Img");
			//没有图片
			if(ValidateUtil.isEmpty(img)){
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
				model.printJson(model, response, "");
				return;
			}
			map =GenerateImage(img,request);
			//图片上传失败
			if(ValidateUtil.isEmpty(map)){
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_error_codeMsg);
				model.printJson(model, response, "");
				return;
			}
			model.setCode(MsgInfo.a_suc_code);
			model.setData(map);
			model.setMessage(LetterInfo.a_ImgUpload_SucMsg);
			model.printJson(model, response, "");
			return;
		}catch(Exception e){
			e.printStackTrace();
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_error_codeMsg);
			model.printJson(model, response, "");
			return;
		}

	}

	public Map GenerateImage(String imgStr, HttpServletRequest request) {
		// 对字节数组字符串进行Base64解码并生成图片
		Map<String, Object> imgmap = new HashMap<String, Object>();
		// 原图
		List<String> listImagePath = new ArrayList<String>();
		// 缩略图
		List<String> thumblistImagePath = new ArrayList<String>();
		String today = DateUtil.getToday();
		String uploadFileUrl = uploadPath + today + "\\";
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String filepath = uuid +  ".jpg";
		filepath = uploadFileUrl + filepath;
		File dir = new File(uploadFileUrl);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (imgStr == null) // 图像数据为空
			return null;
		try {
			// Base64解码
			byte[] b = Base64.decode(imgStr.getBytes("utf-8"));
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 重命名上传后的文件名
			String fileName = String.valueOf(System.currentTimeMillis());
			// 定义上传路径
			// 图片生成
			OutputStream out = new FileOutputStream(filepath);
			out.write(b);
			out.flush();
			out.close();
			String pathOne = "thumb" + uuid + ".jpg";
			ImgCompress imgComOne = new ImgCompress(filepath);
			String comfilepath = uploadFileUrl + pathOne;
			imgComOne.resizeFix(ThumbImgWidth, ThumbImgHeight, comfilepath);
			
			listImagePath.add(ImgUrl+"/"+today +"/"+uuid +  ".jpg");
			thumblistImagePath.add(ImgUrl+"/"+today + "/thumb"+uuid +  ".jpg");
			imgmap.put("listImagePath", listImagePath);
			imgmap.put("thumblistImagePath", thumblistImagePath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return imgmap;
	}
	
	

	/**
	 * 将输入流读成byte[] 再进行base64加密
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public String tranfer(InputStream inputStream) throws IOException {
		BASE64Encoder encoder = new BASE64Encoder();
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inputStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return encoder.encodeBuffer(in2b);
	}
	
	
	public static void main(String[] args) throws IOException  {
	      BufferedInputStream in = new BufferedInputStream(new FileInputStream("D://111.png"));        
	        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);        
	       
	        System.out.println("Available bytes:" + in.available());        
	       
	        byte[] temp = new byte[1024];        
	        int size = 0;        
	        while ((size = in.read(temp)) != -1) {        
	            out.write(temp, 0, size);        
	        }        
	        in.close();        
	       
	        byte[] content = out.toByteArray();        
	        System.out.println("Readed bytes count:" + content.length);   
	        System.out.println(new String(Base64.encode(content),"utf-8"));
	}
}