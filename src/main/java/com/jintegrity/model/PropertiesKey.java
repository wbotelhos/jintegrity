package com.jintegrity.model;

import java.util.Properties;

public class PropertiesKey {

	private String driver;
	private String password;
	private String url;
	private String username;

	public PropertiesKey() { }

	public PropertiesKey(String driver, String password, String url, String username) {
		this.driver = driver;
		this.password = password;
		this.url = url;
		this.username = username;
	}

	public String getPropertiesFileName() {
		return "hibernate.properties";
	}

	public void read(Properties properties, PropertiesKey propertiesKey) {
		driver		= properties.getProperty(propertiesKey.getDriver()).trim();
		password	= properties.getProperty(propertiesKey.getPassword()).trim();
		url			= properties.getProperty(propertiesKey.getUrl()).trim();
		username	= properties.getProperty(propertiesKey.getUsername()).trim();
	}

	public PropertiesKey driver(String driver) {
		this.driver = driver;
		return this;
	}

	public PropertiesKey password(String password) {
		this.password = password;
		return this;
	}

	public PropertiesKey url(String url) {
		this.url = url;
		return this;
	}

	public PropertiesKey user(String username) {
		this.username = username;
		return this;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "[driver=" + driver + ", password=" + password + ", url=" + url + ", username=" + username + "]";
	}

}