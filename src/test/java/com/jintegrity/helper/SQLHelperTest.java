package com.jintegrity.helper;

import static com.jintegrity.util.Utils.loadAll;
import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.User;

public class SQLHelperTest {

	private final DbUnitManager dbUnitManager = new DbUnitManager();
	private final SQLHelper sqlHelper = new SQLHelper();

	@Test
	public void shouldInsert() throws Exception {
		// given
		String drop = "drop";
		String create = "create";
		String insert = "insert";

		// when
		sqlHelper.run(drop, create, insert);

		List<User> userList = loadAll(dbUnitManager);

		User first = userList.get(0);
		User second = userList.get(1);
		User third = userList.get(2);

		// then
		assertEquals("should have 3 registers", 3, userList.size());
		assertEquals("should be the first register", 1, first.getId().intValue());
		assertEquals("should be the second register", 2, second.getId().intValue());
		assertEquals("should be the third register", 3, third.getId().intValue());

		sqlHelper.run(drop);
	}

}