package com.jintegrity.core;

import static com.jintegrity.util.Utils.loadAll;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.util.List;

import org.dbunit.operation.DatabaseOperation;
import org.junit.Ignore;
import org.junit.Test;

import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.helper.SQLHelper;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.model.User;

public class DbUnitManagerTest {

	private final SQLHelper sqlHelper = new SQLHelper();

	@Test
	public void shouldIgonreTheFirstSlashIfTheFileHasIt() throws Exception {
		sqlHelper.run("drop", "create");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "/User";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAll(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());

		sqlHelper.run("drop");
	}

	@Test
	public void shouldPrependTheFirstSlashIfTheFileHasNot() throws Exception {
		sqlHelper.run("drop", "create");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "User.xml";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAll(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());

		sqlHelper.run("drop");
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
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:jintegrity", metaData.getURL());
		assertEquals("should found the default user value", "root", metaData.getUserName());
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
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:jintegrity", metaData.getURL());
		assertEquals("should found the default user value", "root", metaData.getUserName());
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
		assertEquals("should found the default url value", "jdbc:hsqldb:mem:jintegrity", metaData.getURL());
		assertEquals("should found the default user value", "root", metaData.getUserName());
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenXmlWasFileNotFound() throws Exception {
		sqlHelper.run("drop", "create");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();
		String xml = "MISSING.xml";

		try {
			// when
			dbUnitManager.execute(DatabaseOperation.CLEAN_INSERT, xml);
		} catch (JIntegrityException e) {
			// then
			assertEquals("the file \"/MISSING.xml\" was not found!", e.getMessage());
		}

		sqlHelper.run("drop");
	}

	@Test
	public void shouldWorksWithNoStartSlashOnXMLPath() throws Exception {
		sqlHelper.run("drop", "create");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "User.xml";

		// when
		dbUnitManager.execute(DatabaseOperation.CLEAN_INSERT, xml);

		// then

		sqlHelper.run("drop");
	}

	@Test
	public void shouldInsert() throws Exception {
		sqlHelper.run("drop", "create");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "User.xml";

		// when
		dbUnitManager.insert(xml);

		List<User> userList = loadAll(dbUnitManager);
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());

		sqlHelper.run("drop");
	}

	@Test
	public void shouldDeleteAll() throws Exception {
		sqlHelper.run("drop", "create", "insert");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "User.xml";

		// when
		dbUnitManager.deleteAll(xml);

		List<User> userList = loadAll(dbUnitManager);

		// then
		assertEquals("should have 0 registers", 0, userList.size());

		sqlHelper.run("drop");
	}

	@Test
	public void shouldCleanAndInsert() throws Exception {
		sqlHelper.run("drop", "create", "insert");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "User.xml";

		// when
		dbUnitManager.cleanAndInsert(xml);

		List<User> userList = loadAll(dbUnitManager);

		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());

		sqlHelper.run("drop");
	}

	@Test
	public void shouldRefresh() throws Exception {
		sqlHelper.run("drop", "create", "insert");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "refresh.xml";

		// when
		dbUnitManager.refresh(xml);

		List<User> userList = loadAll(dbUnitManager);

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

		sqlHelper.run("drop");
	}

	@Test
	public void shouldUpdate() throws Exception {
		sqlHelper.run("drop", "create", "insert");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "update.xml";

		// when
		dbUnitManager.refresh(xml);

		List<User> userList = loadAll(dbUnitManager);

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

		sqlHelper.run("drop");
	}

	@Test
	public void shouldDelete() throws Exception {
		sqlHelper.run("drop", "create", "insert");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "delete.xml";

		// when
		dbUnitManager.delete(xml);

		List<User> userList = loadAll(dbUnitManager);

		User first = userList.get(0);
		User third = userList.get(1);

		// then
		assertEquals("should have 2 registers", 2, userList.size());

		assertEquals("should keep the first register", 1, first.getId().intValue());
		assertEquals("should keep the third register", 3, third.getId().intValue());

		sqlHelper.run("drop");
	}

	@Test
	@Ignore // TODO check why trucante don't reset the primary key to zero. Although MySQL do!
	public void shouldTruncate() throws Exception {
		sqlHelper.run("drop", "create", "insert");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		String xml = "User.xml";

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

		List<User> userList = loadAll(dbUnitManager);

		User first = userList.get(0);

		// then
		assertEquals("should have 1 registers", 1, userList.size());
		assertEquals("should be the first primary key", 1, first.getId().intValue());

		sqlHelper.run("drop");
	}

}