package com.jintegrity.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.jintegrity.core.DbUnitManager;

public class SQLHelper {

	private final DbUnitManager dbUnitManager = new DbUnitManager();

	public SQLHelper run(String... sqls) throws IOException, SQLException {
		for (String sql : sqls) {
			Connection conn = dbUnitManager.getConnection();
	
			if (!sql.startsWith("/")) {
				sql = "/" + sql;
			}

			InputStream stream = getClass().getResourceAsStream(sql + ".sql");
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(stream));

			String line = null;
			StringBuilder query = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("--")) {
					query.append(line);
				}
			}

			PreparedStatement ps = conn.prepareStatement(query.toString());

			try {
				ps.executeUpdate();
			} finally {
				conn.close();
				ps.close();
			}
		}

		return this;
	}

}