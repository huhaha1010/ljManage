package com.mr.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtils {
     public static String sendemail(String myEmailAccount,String myEmailPassword,Map<String,String> para){
    	 if(!para.containsKey("toEmail")||!para.containsKey("userName")||!para.containsKey("sub")||!para.containsKey("content"))
    	     return "发送邮件参数不正确";
    	 Properties props  = new Properties();
    	 props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）     
    	 props.setProperty("mail.smtp.host", "smtp.mxhichina.com");   // 发件人的邮箱的 SMTP 服务器地址    
         props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
         final String smtpPort = "465";	     
         props.setProperty("mail.smtp.port", smtpPort);	     
         props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");	       
         props.setProperty("mail.smtp.socketFactory.fallback", "false");

    	 Session session =  Session.getInstance(props,new SimpleAuthenticator(myEmailAccount, myEmailPassword));
    	 session.setDebug(true);
    	 MimeMessage message = new MimeMessage(session);
    	 try {
			message.setFrom(new InternetAddress(myEmailAccount,"帅气的人-周","UTF-8"));
			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(para.get("toEmail"), para.get("userName")+"用户", "UTF-8"));
			message.setSubject(para.get("sub"));
			message.setContent(para.get("content"), "text/html;charset=UTF-8");
			message.setSentDate(new Date());
			message.saveChanges();
			Transport transport = session.getTransport();
			transport.connect(myEmailAccount, myEmailPassword);
			System.out.println(myEmailPassword+"==="+myEmailAccount);
			Transport.send(message);
			transport.close();
			 
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		 return "success";
     }
}
class SimpleAuthenticator extends Authenticator {
    
    private String username;
    
    private String password;
    
    public SimpleAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PasswordAuthentication getPasswordAuthentication() {    
        return new PasswordAuthentication(this.username, this.password);

    }
    
}