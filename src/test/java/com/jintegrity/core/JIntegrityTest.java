package com.jintegrity.core;

import static com.jintegrity.util.Utils.createAll;
import static com.jintegrity.util.Utils.dropAll;
import static com.jintegrity.util.Utils.insertAll;
import static com.jintegrity.util.Utils.loadAllContacts;
import static com.jintegrity.util.Utils.loadAllUsers;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.model.Contact;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.model.User;
import com.jintegrity.vendor.Db2DbUnitManager;
import com.jintegrity.vendor.HsqldbDbUnitManager;
import com.jintegrity.vendor.MsSqlServerDbUnitManager;
import com.jintegrity.vendor.MySqlDbUnitManager;
import com.jintegrity.vendor.Oracle10gDbUnitManager;
import com.jintegrity.vendor.PostgresqlDbUnitManager;

public class JIntegrityTest {

	@Before
	public void setup() throws Exception {
		createAll();
	}

	@After
	public void tearDown() throws Exception {
		dropAll();
	}

	@Test
	public void shouldHaveJustOneXmlOnProperties() throws Exception {
		// given
		JIntegrity helper = new JIntegrity();

		// when
		int count = helper.getXmls().length;

		// then
		assertEquals("should have 2 xml on properties", 2, count);
	}

	@Test
	public void shouldHaveTheFixedRootPathOnProperties() throws Exception { 
		// given
		JIntegrity helper = new JIntegrity();

		// when
		String path = helper.getPath();

		// then
		assertEquals("should have the fixed root path", "/dataset/", path);
	}

	@Test
	public void shouldGetFixedPathAndXmlFromProperties() throws Exception {
		// given
		JIntegrity helper = new JIntegrity();

		// when
		helper.insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should be the path from properties", "/dataset/", helper.getPath());
		assertEquals("should be the xml from properties", "User", helper.getXmls()[0]);
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
	}

	@Test
	public void shouldUseGlobalPathAndXmlFromProperties() throws Exception {
		// given
		String path = "dataset/other";

		// when
		JIntegrity helper = new JIntegrity();
		helper.path(path).insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);

		// then
		assertEquals("should be the path from method", "/" + path + "/", helper.getPath());
		assertEquals("should have 1 register", 1, userList.size());
		assertEquals("should be the xml from properties", "User", helper.getXmls()[0]);
		assertEquals("should be the first register", 11, first.getId().intValue());
	}

	@Test
	public void shouldUseGlobalPathAndGlobalXml() throws Exception {
		// given
		String path = "dataset/other";
		String xml = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.path(path).xml(xml).insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);

		// then
		assertEquals("should have 1 xml", 1, helper.getXmls().length);
		assertEquals("should be the xml from method", "UserPlus", helper.getXmls()[0]);
		assertEquals("should have 1 register", 1, userList.size());
		assertEquals("should be the first register", 22, first.getId().intValue());
	}

	@Test
	public void shouldUseGlobalXmlAndPathFromProperties() throws Exception {
		// given
		String xml = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.xml(xml).insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);

		// then
		assertEquals("should have 1 xml", 1, helper.getXmls().length);
		assertEquals("should be the xml from method", "UserPlus", helper.getXmls()[0]);
		assertEquals("should have 1 register", 1, userList.size());
		assertEquals("should be the first register", 4, first.getId().intValue());
	}

	@Test
	public void shouldOverrideTheXmlFromPropertiesWithXmlFromAction() throws Exception {
		// given
		String xml = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.insert(xml);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);

		// then
		assertEquals("should have 1 xml", 1, helper.getXmls().length);
		assertEquals("should be the xml from method", "UserPlus", helper.getXmls()[0]);
		assertEquals("should have 1 register", 1, userList.size());
		assertEquals("should be the first register", 4, first.getId().intValue());
	}

	@Test
	public void shouldOverrideTheGlobalXmlWithXmlFromAction() throws Exception {
		// given
		String global = "Global";
		String xml = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.xml(global).insert(xml);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);

		// then
		assertEquals("should have 1 xml", 1, helper.getXmls().length);
		assertEquals("should be the xml from method", "UserPlus", helper.getXmls()[0]);
		assertEquals("should have 1 register", 1, userList.size());
		assertEquals("should be the first register", 4, first.getId().intValue());
	}

	@Test
	public void shouldOverrideTheGlobalPathAndGlobalXmlWithPathAndXmlFromAction() throws Exception {
		// given
		String globalPath = "global";
		String globalXml = "Global";
		String fullPath = "/dataset/other/UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.path(globalPath).xml(globalXml).insert(fullPath);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);

		// then
		assertEquals("should have 1 xml", 1, helper.getXmls().length);
		 // "/" is a hack, because getPath always return a fixedPath.
		assertEquals("should be the xml from method", "/" + fullPath, helper.getPath() + helper.getXmls()[0]);
		assertEquals("should have 1 register", 1, userList.size());
		assertEquals("should be the first register", 22, first.getId().intValue());
	}

	@Test
	public void shouldInserTwoXmlFromAction() throws Exception {
		// given
		String firstXML = "User";
		String secondXML = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.insert(firstXML, secondXML);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 2 xml", 2, helper.getXmls().length);
		assertEquals("should have 4 registers", 4, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
		assertEquals("should be the third register", 4, fourth.getId().intValue());
	}

	@Test
	public void shouldUseTwoDiferenteXmlOnMethodWithOneUsingFullyQualifiedPath() throws Exception {
		// given
		String firstXML = "User";
		String secondXML = "/dataset/other/UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.insert(firstXML, secondXML);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 2 xml", 2, helper.getXmls().length);
		assertEquals("should have 4 registers", 4, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
		assertEquals("should be the third register", 22, fourth.getId().intValue());
	}

	@Test
	public void shouldUseTwoXmlOnGlobalMethod() throws Exception {
		// given
		String firstXML = "User";
		String secondXML = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.xml(firstXML, secondXML).insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 2 xml", 2, helper.getXmls().length);
		assertEquals("should have 4 registers", 4, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
		assertEquals("should be the third register", 4, fourth.getId().intValue());
	}

	@Test
	public void shouldUseTwoXmlOnGlobalWithOneUsingFullyQualifiedPathMethod() throws Exception {
		// given
		String firstXML = "User";
		String secondXML = "/dataset/other/UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.xml(firstXML, secondXML).insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 2 xml", 2, helper.getXmls().length);
		assertEquals("should have 4 registers", 4, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
		assertEquals("should be the third register", 22, fourth.getId().intValue());
	}

	@Test
	public void shouldUseXmlFromPropertiesAndOneXmlFromMethod() throws Exception {
		// given
		String xml = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity();
		helper.insert().insert(xml);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 1 xml on last method", 1, helper.getXmls().length);
		assertEquals("should have 4 registers", 4, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
		assertEquals("should be the third register", 4, fourth.getId().intValue());
	}

	@Test
	public void shouldUseCustomKeys() throws Exception {
		// given
		PropertiesKey propertiesKey = new PropertiesKey();
		propertiesKey.driver("javax.persistence.jdbc.driver").password("javax.persistence.jdbc.password").url("javax.persistence.jdbc.url").user("javax.persistence.jdbc.user");

		// when
		JIntegrity helper = new JIntegrity(propertiesKey);
		helper.insert();

		List<User> userList = loadAllUsers(helper.getDbUnitManager());
		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should have 2 xml", 2, helper.getXmls().length);
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
	}

	@Test
	public void shouldDeleteAllByHelper() throws Exception {
		insertAll();

		// given
		String xml = "Contact";

		// when
		JIntegrity helper = new JIntegrity().clean(xml);

		List<Contact> contactList = loadAllContacts(helper.getDbUnitManager());

		// then
		assertEquals("should have 0 registers", 0, contactList.size());
	}

	@Test
	public void shouldCleanAndInsertByHelper() throws Exception {
		insertAll();

		// given
		String xml = "Contact";

		// when
		JIntegrity helper = new JIntegrity().cleanAndInsert(xml);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());

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
	public void shouldInsertByHelper() throws Exception {
		// given
		String xml = "User";

		// when
		JIntegrity helper = new JIntegrity().insert(xml);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());

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
	public void shouldInsertTwoRegisterByHelper() throws Exception {
		// given
		String user = "User";
		String other = "UserPlus";

		// when
		JIntegrity helper = new JIntegrity().insert(user, other);

		List<User> userList = loadAllUsers(helper.getDbUnitManager());

		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);
		User fourth = userList.get(3);

		// then
		assertEquals("should have 4 registers", 4, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());
		assertEquals("should be the fourth register", 4, fourth.getId().intValue());
	}

	@Test
	public void shouldGetDefaultDbUnitConnectionByJIntegrityHelper() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity();
		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of DbUnitManager", dbUnitManager instanceof DbUnitManager);
	}

	@Test
	public void shouldGetDb2Connection() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity().vendor(JIntegrity.DB2);

		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of DB2 connection", dbUnitManager instanceof Db2DbUnitManager);
	}

	@Test
	public void shouldGetHsqlConnection() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity().vendor(JIntegrity.HSQL);

		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of HSQL connection", dbUnitManager instanceof HsqldbDbUnitManager);
	}

	@Test
	public void shouldGetMssqlConnection() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity().vendor(JIntegrity.MSSQL);

		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of MS SQL connection", dbUnitManager instanceof MsSqlServerDbUnitManager);
	}

	@Test
	public void shouldGetMysqlConnection() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity().vendor(JIntegrity.MYSQL);

		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of MySQL connection", dbUnitManager instanceof MySqlDbUnitManager);
	}

	@Test
	public void shouldGetOracle10gConnection() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity().vendor(JIntegrity.ORACLE10G);

		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of Oracle 10g connection", dbUnitManager instanceof Oracle10gDbUnitManager);
	}

	@Test
	public void shouldGetPostgreConnection() throws Exception {
		// given

		// when
		JIntegrity helper = new JIntegrity().vendor(JIntegrity.POSTGRE);

		DbUnitManager dbUnitManager = helper.getDbUnitManager();

		// then
		assertTrue("should be instance of Postegre connection", dbUnitManager instanceof PostgresqlDbUnitManager);
	}

	@Test
	public void shouldThrowJIntegrityExceptionWhenNotFoundDriverFactoryType() throws Exception {
		// given

		try {
			// when
			new JIntegrity().vendor("MISSING");
			fail("exception expected but not thrown!");
		} catch (JIntegrityException e) {
			// then
			assertEquals("the 'MISSING' driver factory is not supported!", e.getMessage());
		}
	}

}