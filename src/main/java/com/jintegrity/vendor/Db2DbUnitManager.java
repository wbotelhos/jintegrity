package com.jintegrity.vendor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.db2.Db2Connection;
import org.dbunit.ext.db2.Db2DataTypeFactory;

import com.jintegrity.core.DbUnitManager;
import com.jintegrity.model.PropertiesKey;

public class Db2DbUnitManager extends DbUnitManager {

	private static final Logger LOG = Logger.getLogger(Db2DbUnitManager.class);

	public Db2DbUnitManager() { }

	public Db2DbUnitManager(PropertiesKey propertiesKey) {
		super(propertiesKey);
	}

	@Override
	protected IDatabaseConnection getDbUnitConnection() throws SQLException, DatabaseUnitException {
		LOG.info("DB2 driver selected");

		Connection conn = getConnection();

		DatabaseMetaData databaseMetaData = conn.getMetaData();

		String url = databaseMetaData.getURL();
		int start = url.lastIndexOf("/") + 1;
		int end = url.length();
		String schema = url.substring(start, end);

		IDatabaseConnection iConn = new Db2Connection(conn, schema);

		iConn.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new Db2DataTypeFactory());

		return iConn;
	}

}
