package com.jintegrity.vendor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.PropertiesKey;

public class HsqldbDbUnitManager extends DbUnitManager {

	private static final Logger LOG = Logger.getLogger(HsqldbDbUnitManager.class);

	public HsqldbDbUnitManager() { }

	public HsqldbDbUnitManager(PropertiesKey propertiesKey) {
		super(propertiesKey);
	}

	@Override
	protected IDatabaseConnection getDbUnitConnection() throws SQLException, DatabaseUnitException {
		LOG.info("HSQL driver selected");

		Connection conn = getConnection();

		DatabaseMetaData databaseMetaData = conn.getMetaData();

		String url = databaseMetaData.getURL();
		int start = url.lastIndexOf(":") + 1;
		int end = url.length();
		String schema = url.substring(start, end);

		IDatabaseConnection iConn = new HsqldbConnection(conn, schema);

		iConn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

		return iConn;
	}

}