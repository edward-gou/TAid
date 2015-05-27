package com.hash.core;

import java.util.Date; 
import java.util.Properties; 

import javax.activation.CommandMap; 
import javax.activation.DataHandler; 
import javax.activation.DataSource; 
import javax.activation.FileDataSource; 
import javax.activation.MailcapCommandMap; 
import javax.mail.BodyPart; 
import javax.mail.Multipart; 
import javax.mail.PasswordAuthentication; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeBodyPart; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart; 
 
 
public class MailManager extends javax.mail.Authenticator 
{ 
	
	private String username; 
	private String password; 
	private String to; 
	private String from; 
	private String smtpPort; 
	private String socketPort; 
	private String smtpServer; 
	private String mailSubject; 
	private String mailBody; 
	
	//contentMultiPart is used to construct the body and attachments of the mail
	private Multipart contentMultiPart; 
 
	public MailManager() 
	{ 
	    contentMultiPart = new MimeMultipart(); 
		 
		// Fix for MailCap. javamail cannot find a handler for the multipart/mixed part.
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		CommandMap.setDefaultCommandMap(mc); 
	} 
	
	public String getBody() 
	{ 
		return mailBody; 
	} 
	
	public void setBody(String mailBody) 
	{ 
		this.mailBody = mailBody; 
	} 
	  
	public void setTo(String t)
	{
		this.to = t;
	}
	  
	public void setSub(String sub)
	{
		this.mailSubject = sub;
	}
	  
	public void setFrom(String f)
	{
		from = f;
	}
	  
	public void setLogin(String u, String p)
	{
		username = u;
		password = p;
	}
 
	public String getSmtpPort() 
	{
		return smtpPort;
	}
	
	
	public void setSmtpPort(String smtpPort) 
	{
		this.smtpPort = smtpPort;
	}


	public String getSocketPort() 
	{
		return socketPort;
	}


	public void setSocketPort(String socketPort) 
	{
		this.socketPort = socketPort;
	}


	public String getSmtpServer() 
	{
		return smtpServer;
	}


	public void setSmtpServer(String smtpServer) 
	{
		this.smtpServer = smtpServer;
	}

	/**
	 * Send the email using mailBody as the email body. Attachments must be attached
	 * before sending email.
	 * @throws Exception
	 */
	public void send() throws Exception 
	{ 
		Properties props = setProperties(); 
		
		//Create the message
		Session session = Session.getInstance(props, this); 
		MimeMessage msg = new MimeMessage(session); 
		msg.setFrom(new InternetAddress(from)); 
		msg.setRecipients(MimeMessage.RecipientType.TO, to); 
		msg.setSubject(mailSubject); 
		msg.setSentDate(new Date()); 
		
		//Fill the message body
		BodyPart messageBodyPart = new MimeBodyPart(); 
		messageBodyPart.setText(mailBody); 
		contentMultiPart.addBodyPart(messageBodyPart); 
		msg.setContent(contentMultiPart); 
		
		//Send 
		Transport.send(msg); 
		
	} 
	
	/**
	 * Attach a file at filename to the email.
	 * @param filename
	 * @throws Exception
	 */
	public void addAttachment(String filename) throws Exception 
	{ 
		BodyPart messageBodyPart = new MimeBodyPart(); 
		
		//Load data
		DataSource source = new FileDataSource(filename); 
		messageBodyPart.setDataHandler(new DataHandler(source)); 
		messageBodyPart.setFileName(filename); 
		
		//Attach data
		contentMultiPart.addBodyPart(messageBodyPart); 
	} 
 
	@Override 
	public PasswordAuthentication getPasswordAuthentication() 
	{ 
		return new PasswordAuthentication(username, password); 
	} 
 
	/**
	 * Initializes the properties of mail server authentication
	 * @return
	 */
	private Properties setProperties() 
	{ 
		Properties props = new Properties(); 
	
		props.put("mail.smtp.host", smtpServer); 
		props.put("mail.smtp.auth", "true"); 
	 
		props.put("mail.smtp.port", smtpPort); 
		props.put("mail.smtp.socketFactory.port", socketPort); 
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
	  	props.put("mail.smtp.socketFactory.fallback", "false"); 
	 
	  	return props; 
	} 
 
} 