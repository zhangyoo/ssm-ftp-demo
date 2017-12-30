package com.nj.nfhy.util.mq;

import com.alibaba.fastjson.JSONObject;
import com.nj.nfhy.util.basicUtils.ConstantsKey;
import com.nj.nfhy.util.basicUtils.DateUtil;
import com.nj.nfhy.util.mailUtils.JavaMail;
import com.nj.nfhy.util.mailUtils.MailSenderInfo;
import jxl.common.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Date;

public class MsgReceiver implements MessageListener {

	Logger log = Logger.getLogger(MsgReceiver.class);

	@Autowired
	private MsgSender msgSender;

	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				String messageText = textMessage.getText();
				log.info(MsgReceiver.class + "====接收内容==>" + messageText);
				log.info(MsgReceiver.class + "====接收时间==>" + DateUtil.time2Str(new Date()));
				opMq(messageText);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMailText(String mail, String title, String content) {
		try {
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setSubject(title);
			mailInfo.setToAddress(mail);
			mailInfo.setContent(content);
			boolean isMail = JavaMail.sendHtmlMail(mailInfo);
			if (isMail) {
				log.info("==================>邮件发送成功");
			} else {
				log.info("==================>邮件发送失败");
			}
		} catch (Exception e) {

		}
 
	}

	public void opMq(String messageText) {
		JSONObject jsonObject = JSONObject.parseObject(messageText);
		String opName = jsonObject.getString(ConstantsKey.OP_MQ_NAME);

		log.info(MsgReceiver.class + "====发送分享邮件接口执行结束==>");
	}
}
