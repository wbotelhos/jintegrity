package com.jintegrity.core;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.jintegrity.core.PropertiesReader;
import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.model.PropertiesValue;

public class PropertiesReaderTest {

	private final PropertiesReader propertiesReader = new PropertiesReader();

	@Test
	public void shouldReadDefaultHibernateKeys() {
		// given
		PropertiesKey propertiesKey = null;

		// when
		PropertiesValue propertiesValue = propertiesReader.getPropertie(propertiesKey);

		// then
		assertEquals("should found the default driver value", "org.hsqldb.jdbcDriver", propertiesValue.getDriver());
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:jintegrity", propertiesValue.getUrl());
		assertEquals("should found the default user value", "root", propertiesValue.getUsername());
		assertEquals("should found the default password value", "root", propertiesValue.getPassword());
	}

	@Test
	public void shouldReadCustomHibernateKeys() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("password").url("url").user("username");

		// when
		PropertiesValue propertiesValue = propertiesReader.getPropertie(propertiesKey);

		// then
		assertEquals("should found the custom driver value", "driver", propertiesValue.getDriver());
		assertEquals("should found the custom url value", "url", propertiesValue.getUrl());
		assertEquals("should found the custom url value", "username", propertiesValue.getUsername());
		assertEquals("should found the custom url value", "password", propertiesValue.getPassword());
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenDriverKeyNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("MISSING").password("password").url("url").user("username");

		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the driver key 'MISSING' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenPasswordKeyNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("MISSING").url("url").user("username");

		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the password key 'MISSING' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenUrlKeyNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("password").url("MISSING").user("username");
		
		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the url key 'MISSING' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenUsernameKeyNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("password").url("url").user("MISSING");

		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the username key 'MISSING' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}
	
	@Test
	public void shouldThrowJIntegrityExceptionWhenDriverValueNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driverMISSING").password("password").url("url").user("username");

		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the driver value '' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}

	@Test
	public void shouldNotThrowJIntegrityExceptionWhenPasswordValueNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("passwordMISSING").url("url").user("username");

		try {
			// when
			PropertiesValue propertiesValue = propertiesReader.getPropertie(propertiesKey);

			// then
			assertEquals("should have a empty password", "", propertiesValue.getPassword());
		} catch (JIntegrityException e) {
			fail("success expected but not do!");
		}
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenUrlValueNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("password").url("urlMISSING").user("username");
		
		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the url value '' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenUsernameValueNotFound() {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("driver").password("password").url("url").user("usernameMISSING");

		try {
			// when
			propertiesReader.getPropertie(propertiesKey);
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the username value '' was not found on 'hibernate.properties' file!", e.getMessage());
		}
	}

}