package com.jintegrity.helper;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jintegrity.model.User;

public class JPAHelperTest {

	private final SQLHelper sqlHelper = new SQLHelper();

	@BeforeClass
	public static void beforeClass() {
		JPAHelper.entityManagerFactory("default");
	}

	@Test
	public void shouldGetTheSameManager() throws Exception {
		// given
		EntityManager expected = JPAHelper.currentEntityManager();

		// when
		EntityManager actual = JPAHelper.currentEntityManager();

		// then
		assertSame("should be the same manager", expected, actual);
	}

	@Test
	public void shouldGetDifferentManager() throws Exception {
		// given
		EntityManager unexpected = JPAHelper.currentEntityManager();

		// when
		JPAHelper.close();

		EntityManager actual = JPAHelper.currentEntityManager();

		// then
		assertNotSame("should not be the same manager", unexpected, actual);
	}

	@Test
	public void shouldHaveActiveTransaction() throws Exception {
		// given
		EntityManager manager = JPAHelper.currentEntityManager();

		// when
		EntityTransaction transaction = manager.getTransaction();

		// then
		assertTrue("should have a active transaction", transaction.isActive());
	}

	@Test
	public void shouldLoadAll() throws Exception {
		sqlHelper.run("drop", "create", "insert");
		JPAHelper.entityManagerFactory("default");

		// given
		EntityManager manager = JPAHelper.currentEntityManager();

		// when
		Query query = manager.createQuery("from " + User.class.getName());

		@SuppressWarnings("unchecked")
		List<User> userList = query.getResultList();

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

}