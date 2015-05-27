package com.hash.core;

import com.thoughtworks.xstream.XStream;

//App user class
public class User{
	private String name;
	private String username;
	private String password;
	private String smtpServer;
	private String smtpPort;
	private String socketPort;
	
	
	//Initializes user
	public User(String n, String u, String p, String ss, String sp, String s) {
		name = n;
		username = u;
		password = p;
		smtpPort = sp;
		smtpServer = ss;
		socketPort = s;
	}
	
	//Default initialization. Uses Gmail host settings.
	public User()
	{
		name = "user";
		username = "";
		password = "";
		smtpServer = "smtp.gmail.com"; // default smtp server 
		smtpPort = "465"; // default smtp port 
		socketPort = "465"; // default socketfactory port 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(String socketPort) {
		this.socketPort = socketPort;
	}
	
	public boolean isComplete()
	{
		if (!username.equals("") && !password.equals("") && !name.equals("") && !smtpServer.equals("")
				&& !smtpPort.equals("") && !socketPort.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void load(String userXml)
	{
		XStream xstream = new XStream();
		this.name = ((User)xstream.fromXML(userXml)).name;
		this.username = ((User)xstream.fromXML(userXml)).username;
		this.password = ((User)xstream.fromXML(userXml)).password;
		this.smtpServer = ((User)xstream.fromXML(userXml)).smtpServer;
		this.smtpPort = ((User)xstream.fromXML(userXml)).smtpPort;
		this.socketPort = ((User)xstream.fromXML(userXml)).socketPort;
	}

	
}
