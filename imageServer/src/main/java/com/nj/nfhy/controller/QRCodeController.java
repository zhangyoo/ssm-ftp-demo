package com.nj.nfhy.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nj.nfhy.controller.base.BaseController;
import com.nj.nfhy.util.basicUtils.LetterInfo;
import com.nj.nfhy.util.basicUtils.ModelBase;
import com.nj.nfhy.util.basicUtils.MsgInfo;
import com.nj.nfhy.util.ftpUtils.QRCodeUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * 二维码
 * 
 * @author 88386726
 *
 */
@Controller
@RequestMapping("/qrCode")
public class QRCodeController extends BaseController {
	Logger logger = Logger.getLogger(QRCodeController.class);
	// 取配置文件参数
	@Value("#{configProperties['uploadPath']}")
	private String uploadPath;
	@Value("#{configProperties['ImgUrl']}")
	private String ImgUrl;

	@RequestMapping(value = "/creatQRCode.htm", method = RequestMethod.POST, headers = { "content-type=application/json" })
	public void creatQRCode(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj)
			throws Exception {
		ModelBase model = new ModelBase();
		String callback = "";
		try {
			String text = obj.getString("params");
			if (text != null) {
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				String name = uuid + ".jpg";
				QRCodeUtil.encode(text, name, null, uploadPath + "QRCODE\\", true);
				model.setCode(MsgInfo.a_suc_code);
				model.setData("QRCODE\\" + name);
				model.setImgUrlPrefix(ImgUrl);
				model.setMessage(LetterInfo.a_QRCode_SUCCMsg);
			} else {
				model.setCode(MsgInfo.a_error_code);
				model.setMessage(LetterInfo.a_aid_codeMsg);
			}
		} catch (Exception e) {
			model.setCode(MsgInfo.a_error_code);
			model.setMessage(LetterInfo.a_QRCode_ERRORMsg);
		} finally {
			model.printJson(model, response, callback);
		}

	}

}