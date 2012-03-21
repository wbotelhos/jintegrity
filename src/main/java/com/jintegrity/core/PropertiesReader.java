package com.jintegrity.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.model.PropertiesValue;

public class PropertiesReader {

	private static final String DRIVER_KEY = "hibernate.connection.driver_class";
	private static final String PASSWORD_KEY = "hibernate.connection.password";
	private static final String USER_KEY = "hibernate.connection.username";
	private static final String URL_KEY = "hibernate.connection.url";

	public PropertiesValue getPropertie(PropertiesKey propertiesKey) throws JIntegrityException {
		try {
			if (propertiesKey == null) {
				propertiesKey = new PropertiesKey().driver(DRIVER_KEY).password(PASSWORD_KEY).url(URL_KEY).user(USER_KEY);
			}

			String fileName = propertiesKey.getPropertiesFileName();

			InputStream stream = getClass().getResourceAsStream("/" + fileName);

			if (stream == null) {
				throw new JIntegrityException("the file " + fileName + " was not found!");
			}

			Properties properties = new Properties();
			properties.load(stream);

			if (!properties.containsKey(propertiesKey.getDriver())) {
				throw new JIntegrityException("the driver key '" + propertiesKey.getDriver() + "' was not found on '" + fileName + "' file!");
			}

			if (!properties.containsKey(propertiesKey.getPassword())) {
				throw new JIntegrityException("the password key '" + propertiesKey.getPassword() + "' was not found on '" + fileName + "' file!");
			}

			if (!properties.containsKey(propertiesKey.getUrl())) {
				throw new JIntegrityException("the url key '" + propertiesKey.getUrl() + "' was not found on '" + fileName + "' file!");
			}

			if (!properties.containsKey(propertiesKey.getUsername())) {
				throw new JIntegrityException("the username key '" + propertiesKey.getUsername() + "' was not found on '" + fileName + "' file!");
			}

			PropertiesValue propertiesValue = new PropertiesValue();
			propertiesValue.read(properties, propertiesKey);

			String driver	= propertiesValue.getDriver();
			String url		= propertiesValue.getUrl();
			String username	= propertiesValue.getUsername();

			if (driver == null || driver.isEmpty()) {
				throw new JIntegrityException("the driver value '" + driver + "' was not found on '" + fileName + "' file!");
			}

			if (url == null || url.isEmpty()) {
				throw new JIntegrityException("the url value '" + url + "' was not found on '" + fileName + "' file!");
			}

			if (username == null || username.isEmpty()) {
				throw new JIntegrityException("the username value '" + username + "' was not found on '" + fileName + "' file!");
			}

			stream.close();

			return propertiesValue;
		} catch (IOException e) {
			throw new JIntegrityException("error to read the .properties file!", e);
		}
	}
	
}
