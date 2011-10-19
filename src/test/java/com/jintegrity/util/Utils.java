package com.jintegrity.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.helper.SQLHelper;
import com.jintegrity.model.Contact;
import com.jintegrity.model.User;

public class Utils {

	private static final SQLHelper sqlHelper = new SQLHelper();

	public static void insertAll() throws SQLException, IOException {
		sqlHelper.run("sql/drop-contact", "sql/drop-user", "sql/create-user", "sql/create-contact", "sql/insert-user", "sql/insert-contact");
	}

	public static void createAll() throws SQLException, IOException {
		sqlHelper.run("sql/create-user", "sql/create-contact");
	}

	public static void dropAll() throws SQLException, IOException {
		sqlHelper.run("sql/drop-contact", "sql/drop-user");
	}

	public static List<User> loadAllUsers(DbUnitManager dbUnitManager) throws SQLException {
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

	public static List<Contact> loadAllContacts(DbUnitManager dbUnitManager) throws SQLException {
		Connection conn = dbUnitManager.getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT id, phone FROM Contact");
		List<Contact> contactList = new ArrayList<Contact>();

		try {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				contactList.add(new Contact(rs.getLong("id"), rs.getString("phone")));
			}
		} finally {
			conn.close();
			ps.close();
		}

		return contactList;
	}

}
