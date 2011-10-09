package com.jintegrity.helper;

import static com.jintegrity.util.Utils.loadAll;
import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.User;

public class HibernateHelperTest {

	private final SQLHelper sqlHelper = new SQLHelper();

	@Test
	public void shouldExportSchema() throws Exception {
		sqlHelper.run("drop");

		// given
		DbUnitManager dbUnitManager = new DbUnitManager();

		// when
		HibernateHelper.exportSchema();

		sqlHelper.run("insert");

		List<User> userList = loadAll(dbUnitManager);

		// then
		assertEquals("should have 3 registers", 3, userList.size());

		sqlHelper.run("drop");
	}

}