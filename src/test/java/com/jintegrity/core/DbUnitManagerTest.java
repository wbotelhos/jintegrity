package com.jintegrity.core;

import static com.jintegrity.util.Utils.createAll;
import static com.jintegrity.util.Utils.dropAll;
import static com.jintegrity.util.Utils.insertAll;
import static com.jintegrity.util.Utils.loadAllUsers;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.util.List;

import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.model.User;

public class DbUnitManagerTest {

	@Before
	public void setup() throws Exception {
		createAll();
	}

	@After
	public void tearDown() throws Exception {
		dropAll();
	}

	@Test
	public void shouldIgnoreTheXmlExtensionIfTheFileHasIt() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
	}

	@Test
	public void shouldAppendXmlExtensionIfTheFileNameHasNot() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
	}

	@Test
	public void shouldIgnoreTheFirstSlashIfTheFileHasIt() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
	}

	@Test
	public void shouldPrependTheFirstSlashIfTheFileHasNot() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
	}

	@Test
	public void shouldGetConnectionWithDefaultPropertiesKey() throws Exception {
		// given

		// when
		DbUnitManager dbUnitManager = new DbUnitManager();

		Connection conn = dbUnitManager.getConnection();

		DatabaseMetaData metaData = conn.getMetaData();

		// then
		assertNotNull("should exist a connection", conn);
		assertEquals("should found the default driver value", "HSQL Database Engine Driver", metaData.getDriverName());
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:test", metaData.getURL());
		assertEquals("should found the default user value", "SA", metaData.getUserName());
	}

	@Test
	public void shouldGetConnectionWithDefaultPropertiesKeyWithNullPropertiesParam() throws Exception {
		// given
		PropertiesKey propertiesKey = null;

		// when
		DbUnitManager dbUnitManager = new DbUnitManager(propertiesKey);

		Connection conn = dbUnitManager.getConnection();

		DatabaseMetaData metaData = conn.getMetaData();

		// then
		assertNotNull("should exist a connection", conn);
		assertEquals("should found the default driver value", "HSQL Database Engine Driver", metaData.getDriverName());
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:test", metaData.getURL());
		assertEquals("should found the default user value", "SA", metaData.getUserName());
	}

	@Test
	public void shouldGetConnectionWithCustomPropertiesKey() throws Exception {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("javax.persistence.jdbc.driver").password("javax.persistence.jdbc.password").url("javax.persistence.jdbc.url").user("javax.persistence.jdbc.user");

		// when
		DbUnitManager dbUnitManager = new DbUnitManager(propertiesKey);

		Connection conn = dbUnitManager.getConnection();

		DatabaseMetaData metaData = conn.getMetaData();

		// then
		assertNotNull("should exist a connection", conn);
		assertEquals("should found the default driver value", "HSQL Database Engine Driver", metaData.getDriverName());
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:test", metaData.getURL());
		assertEquals("should found the default user value", "SA", metaData.getUserName());
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenXmlWasFileNotFound() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();
		String xml = "MISSING";

		try {
			// when
			dbUnitManager.execute(DatabaseOperation.CLEAN_INSERT, xml);
		} catch (JIntegrityException e) {
			// then
			assertEquals("the file '/MISSING.xml' was not found!", e.getMessage());
		}
	}

	@Test
	public void shouldWorksWithNoStartSlashOnXMLPath() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.execute(DatabaseOperation.CLEAN_INSERT, xml);

		// then
	}

	@Test
	public void shouldInsert() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAllUsers(dbUnitManager);
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
	}

	@Test
	public void shouldDeleteAll() throws Exception {
		insertAll();

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String contactXML = "dataset/Contact";
		String userXML = "dataset/User";

		// when
		dbUnitManager.deleteAll(contactXML);
		dbUnitManager.deleteAll(userXML);

		List<User> userList = loadAllUsers(dbUnitManager);

		// then
		assertEquals("should have 0 registers", 0, userList.size());
	}

	@Test
	public void shouldCleanAndInsert() throws Exception {
		insertAll();

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String userXML = "dataset/Contact";

		// when
		dbUnitManager.cleanAndInsert(userXML);

		List<User> userList = loadAllUsers(dbUnitManager);

		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
	}

	@Test
	public void shouldRefresh() throws Exception {
		insertAll();

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/refresh";

		// when
		dbUnitManager.refresh(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 4 registers", 4, userList.size());

		assertEquals("should keep the first register", 1, first.getId().intValue());
		assertEquals("should keep the second register", 2, second.getId().intValue());
		assertEquals("should keep the third register", 3, third.getId().intValue());

		assertEquals("should keep the email of the first register", "email-1@mail.com", first.getEmail());
		assertEquals("should change the name of the first register", "name-11", first.getName());

		assertEquals("should keep the email of the second register", "email-2@mail.com", second.getEmail());
		assertEquals("should keep the name of the second register", "name-2", second.getName());

		assertEquals("should keep the email of the third register", "email-3@mail.com", third.getEmail());
		assertEquals("should keep the name of the third register", "name-3", third.getName());

		assertNotNull("should insert the fourth register", fourth);
		assertEquals("should be the fourth register", 4, fourth.getId().intValue());
	}

	@Test
	public void shouldUpdate() throws Exception {
		insertAll();

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/update";

		// when
		dbUnitManager.refresh(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 4 registers", 4, userList.size());

		assertEquals("should keep the first register", 1, first.getId().intValue());
		assertEquals("should keep the second register", 2, second.getId().intValue());
		assertEquals("should keep the third register", 3, third.getId().intValue());

		assertEquals("should keep the email of the first register", "email-1@mail.com", first.getEmail());
		assertEquals("should change the name of the first register", "name-11", first.getName());

		assertEquals("should keep the email of the second register", "email-2@mail.com", second.getEmail());
		assertEquals("should keep the name of the second register", "name-2", second.getName());

		assertEquals("should keep the email of the third register", "email-3@mail.com", third.getEmail());
		assertEquals("should keep the name of the third register", "name-3", third.getName());

		assertNotNull("should insert the fourth register", fourth);
		assertEquals("should be the fourth register", 4, fourth.getId().intValue());
	}

	@Test
	public void shouldDelete() throws Exception {
		insertAll();

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/delete";

		// when
		dbUnitManager.delete(xml);

		List<User> userList = loadAllUsers(dbUnitManager);

		User first = userList.get(0);
		User third = userList.get(1);

		// then
		assertEquals("should have 2 registers", 2, userList.size());

		assertEquals("should keep the first register", 1, first.getId().intValue());
		assertEquals("should keep the third register", 3, third.getId().intValue());
	}

	@Test
	@Ignore // TODO check why trucante don't reset the primary key to zero. Although MySQL do!
	public void shouldTruncate() throws Exception {
		insertAll();

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "dataset/User";

		// when
		dbUnitManager.truncate(xml);

		Connection conn = dbUnitManager.getConnection();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO User (email, name) VALUES ('email-4@mail.com', 'name-4')");

		try {
			ps.executeUpdate();
		} finally {
			conn.close();
			ps.close();
		}

		List<User> userList = loadAllUsers(dbUnitManager);

		User first = userList.get(0);

		// then
		assertEquals("should have 1 registers", 1, userList.size());
		assertEquals("should be the first primary key", 1, first.getId().intValue());
	}

}