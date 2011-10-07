package com.jintegrity.vendor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.oracle.Oracle10DataTypeFactory;
import org.dbunit.ext.oracle.OracleConnection;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.PropertiesKey;

public class Oracle10gDbUnitManager extends DbUnitManager {

	private static final Logger LOG = Logger.getLogger(Oracle10gDbUnitManager.class);

	public Oracle10gDbUnitManager() { }

	public Oracle10gDbUnitManager(PropertiesKey propertiesKey) {
		super(propertiesKey);
	}

	@Override
	protected IDatabaseConnection getDbUnitConnection() throws DatabaseUnitException, SQLException {
		LOG.info("Oracle 10g driver selected");

		Connection conn = getConnection();

		DatabaseMetaData databaseMetaData = conn.getMetaData();

		String url = databaseMetaData.getURL();
		int start = url.lastIndexOf("/") + 1;
		int end = url.length();
		String schema = url.substring(start, end);

		IDatabaseConnection iConn = new OracleConnection(conn, schema);

		iConn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Oracle10DataTypeFactory());

		// skip oracle 10g recycle bin system tables if enabled
		iConn.getConfig().setProperty(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, Boolean.TRUE);

		return iConn;
	}

}