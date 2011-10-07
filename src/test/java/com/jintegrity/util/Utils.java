package com.jintegrity.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.User;

public class Utils {

	public static List<User> loadAll(DbUnitManager dbUnitManager) throws SQLException {
		Connection conn = dbUnitManager.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT id, email, name FROM User");
		List<User> userList = new ArrayList<User>();

		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				userList.add(new User(rs.getLong("id"), rs.getString("email"), rs.getString("name")));
			}
		} finally {
			conn.close();
			ps.close();
		}

		return userList;
	}

}
