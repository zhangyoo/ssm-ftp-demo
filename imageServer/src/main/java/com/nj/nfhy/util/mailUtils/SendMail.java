package com.nj.nfhy.util.mailUtils;

public class SendMail {
	public static void main(String[] args) {
        SendMail.send_163();
    }
    
    // 163邮箱
    public static void send_163() 
    {
        //MailSenderInfo用于输入发送人的信息，方便以后如果用多个，或者不同的邮箱进行发送
        MailSenderInfo mailInfo = new MailSenderInfo();
//        mailInfo.setMailServerHost("smtp.163.com");                //163的规定地址
//        mailInfo.setMailServerPort("25");                        //邮件协议规定端口号
//        mailInfo.setValidate(true);
//        MimeUtility.encodeWord("wocao");
        //需要身份认证
        /*下面的信息需要根据实际进行修改*/
//        mailInfo.setToAddress("qtt48@jsti.com");             // 设置接受者邮箱地址
        mailInfo.setToAddress("732839131@qq.com");
        mailInfo.setSubject("标题");                        
        mailInfo.setContent("内容<h1>http://www.baidu.com</h1>");
        
        
//        JavaMail.sendTextMail(mailInfo); // 发送文体格式
        String[] files = null;
        JavaMail.sendHtmlMail(mailInfo); // 发送html格式
        
    }
}
