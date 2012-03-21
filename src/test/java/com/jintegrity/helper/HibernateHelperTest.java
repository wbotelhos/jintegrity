package com.jintegrity.helper;

import static com.jintegrity.util.Utils.createAll;
import static com.jintegrity.util.Utils.dropAll;
import static com.jintegrity.util.Utils.insertAll;
import static com.jintegrity.util.Utils.loadAllUsers;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.User;

public class HibernateHelperTest {

	@Before
	public void setup() throws Exception {
		createAll();
	}

	@After
	public void tearDown() throws Exception {
		dropAll();
	}

	@Test
	public void shouldGetTheSameSession() throws Exception {
		// given
		Session expected = HibernateHelper.currentSession();

		// when
		Session actual = HibernateHelper.currentSession();

		// then
		assertSame("should be the same session", expected, actual);
	}

	@Test
	public void shouldGetDifferentSession() throws Exception {
		// given
		Session unexpected = HibernateHelper.currentSession();

		// when
		HibernateHelper.close();

		Session actual = HibernateHelper.currentSession();

		// then
		assertNotSame("should not be the same session", unexpected, actual);
	}

	@Test
	public void shouldHaveActiveSession() throws Exception {
		// given
		 Session session = HibernateHelper.currentSession();

		// when
		Transaction transaction = session.getTransaction();

		// then
		assertTrue("should have a active transaction", transaction.isActive());
	}

	@Test
	public void shouldLoadAll() throws Exception {
		insertAll();

		// given
		Session session = HibernateHelper.currentSession();

		// when
		Criteria criteria = session.createCriteria(User.class);

		@SuppressWarnings("unchecked")
		List<User> userList = criteria.list();

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
	public void shouldExportSchema() throws Exception {
		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		// when
		HibernateHelper.exportSchema();

		insertAll();

		List<User> userList = loadAllUsers(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
	}

}
