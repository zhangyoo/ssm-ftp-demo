package com.nj.nfhy.util.mialUtils;

import com.nj.nfhy.util.basicUtils.PropertyUtil;
import com.nj.nfhy.util.basicUtils.ValidateUtil;
import org.apache.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class JavaMail {
	
	// logger日志
		private final static Logger logger = Logger.getLogger(JavaMail.class);

	/**
	 * 
	 * 
	 * 
	 * 以文本格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 * @return 发送成功返回true；失败返回false
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		try {
			// 判断是否需要身份认证
			MyAuthenticator authenticator = null;
			PropertyUtil propertyUtil = new PropertyUtil();
			Properties pro = propertyUtil.getProperties();
			if ("true".equals(pro.getProperty("mail.smtp.auth"))) {
				// 如果需要身份认证，则创建一个密码验证器
				authenticator = new MyAuthenticator(pro.getProperty("mail.sender.username"),
						pro.getProperty("mail.sender.password"));
			}
			// 根据邮件会话属性和密码验证器构造一个发送邮件的session
			Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			// String nick = MimeUtility.encodeText("易卡爱途
			// ip:"+InetAddress.getLocalHost().getHostAddress());
			// 创建邮件发送者地址
			Address from = new InternetAddress("苏交科档案管理系统" + " <" + pro.getProperty("mail.sender.address") + "> ");
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @param mailInfo
	 *            待发送的邮件的信息
	 * @return 发送成功返回true；失败返回false
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		PropertyUtil propertyUtil = new PropertyUtil();
		Properties pro = propertyUtil.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if ("true".trim().equals(pro.getProperty("mail.smtp.auth"))) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(pro.getProperty("mail.sender.username"),
					pro.getProperty("mail.sender.password"));
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			String title = String.format("%1$s <%2$s>", MimeUtility.encodeText("苏交科档案管理系统", "gb2312", "B"), "dangan@jsti.com");
			// 创建邮件发送者地址
			InternetAddress from = new InternetAddress(pro.getProperty("mail.sender.address"),title);
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 添加附件的内容
			if (ValidateUtil.isNotEmpty(mailInfo.getAttachFileNames())) {
				for (String filePath : mailInfo.getAttachFileNames()) {
					MimeBodyPart filePart = new MimeBodyPart();
					DataSource source = new FileDataSource(filePath);
					filePart.setDataHandler(new DataHandler(source));
					// 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
					try {
						// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
						// BASE64Encoder enc = new BASE64Encoder();
						// filePart.setFileName("=?GBK?B?" +
						// enc.encode(filePath.getBytes()) + "?=");
						// MimeUtility.encodeWord可以避免文件名乱码
						filePart.setFileName(MimeUtility.encodeWord(source.getName()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mainPart.addBodyPart(filePart);
				}

			}
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			mailMessage.saveChanges();
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	
	public static void sendMailHtml( String mail,  String title,  String content) {
		try {
			MailSenderInfo mailInfo = new MailSenderInfo();
			mailInfo.setSubject(title);
			mailInfo.setToAddress(mail);
			mailInfo.setContent(content);
			boolean isMail = JavaMail.sendHtmlMail(mailInfo); 
			if(isMail){
				logger.info("==================>邮件发送成功");
			}else{
				logger.info("==================>邮件发送失败");
			}
		} catch (Exception e) {
			
		}
		
	}

}
