package com.nj.nfhy.controller.base;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BaseController {

	public JSONObject initJsonParam(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		byte buffer[];
		try {
    buffer = getRequestPostBytes(request);
    String charEncoding = request.getCharacterEncoding();
    if (charEncoding == null)
    {
        charEncoding = "UTF-8";
    }
    String param = new String(buffer, charEncoding);
    if(null == param || "".equals(param))
    {
    param = "{}";
    }
    param = param.replaceAll("\\\\t", " ");
    jsonObject = JSONObject.parseObject(param);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 描述:获取 post 请求的 byte[] 数组
	 * 
	 * <pre>
	 * 举例：
	 * </pre>
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte buffer[] = new byte[contentLength];
		for (int i = 0; i < contentLength;) {

			int readlen = request.getInputStream().read(buffer, i, contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

}
