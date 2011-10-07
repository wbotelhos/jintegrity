package com.jintegrity.vendor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.mssql.MsSqlConnection;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.PropertiesKey;

public class MsSqlServerDbUnitManager extends DbUnitManager {

	private static final Logger LOG = Logger.getLogger(MsSqlServerDbUnitManager.class);

	public MsSqlServerDbUnitManager() { }

	public MsSqlServerDbUnitManager(PropertiesKey propertiesKey) {
		super(propertiesKey);
	}

	@Override
	protected IDatabaseConnection getDbUnitConnection() throws SQLException, DatabaseUnitException {
		LOG.info("MSSQL driver selected");

		Connection conn = getConnection();

		DatabaseMetaData databaseMetaData = conn.getMetaData();

		String url = databaseMetaData.getURL();
		int start = url.lastIndexOf("/") + 1;
		int end = url.length();
		String schema = url.substring(start, end);

		IDatabaseConnection iConn = new MsSqlConnection(conn, schema);

		iConn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MsSqlDataTypeFactory());

		return iConn;
	}

}
