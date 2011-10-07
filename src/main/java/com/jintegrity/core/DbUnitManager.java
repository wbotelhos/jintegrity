package com.jintegrity.core;

import static org.dbunit.operation.DatabaseOperation.CLEAN_INSERT;
import static org.dbunit.operation.DatabaseOperation.DELETE;
import static org.dbunit.operation.DatabaseOperation.DELETE_ALL;
import static org.dbunit.operation.DatabaseOperation.INSERT;
import static org.dbunit.operation.DatabaseOperation.REFRESH;
import static org.dbunit.operation.DatabaseOperation.TRUNCATE_TABLE;
import static org.dbunit.operation.DatabaseOperation.UPDATE;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.NoSuchTableException;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.model.PropertiesValue;

public class DbUnitManager {

	private static final Logger LOG = Logger.getLogger(DbUnitManager.class);

	private PropertiesKey propertiesKey;

	public DbUnitManager() { }

	public DbUnitManager(PropertiesKey propertiesKey) {
		if (propertiesKey == null) {
			LOG.info("using the default properties keys");
		} else {
			LOG.info("using custom properties keys: " + propertiesKey.toString());
		}

		this.propertiesKey = propertiesKey;
	}

	public Connection getConnection() {
		PropertiesReader propertiesReader = new PropertiesReader();
		PropertiesValue propertiesValue = propertiesReader.getPropertie(propertiesKey);

		final String driver		= propertiesValue.getDriver();
		final String url		= propertiesValue.getUrl();
		final String username	= propertiesValue.getUsername();
		final String password	= propertiesValue.getPassword();

		LOG.info("using connection configuration as: " + driver + "[\"" + url + "\", \"" + username + "\", \"" + password + "\"]");

		Connection connection = null;

		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			throw new JIntegrityException("the driver \"" + driver + "\" was not found!", e);
		} catch (InstantiationException e) {
			throw new JIntegrityException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new JIntegrityException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new JIntegrityException("Could not establish connection on \"" + url + "\" to \"" + username + "\"!", e);
		}

		return connection;
	}

	// to be overrided to other vendors.
	protected IDatabaseConnection getDbUnitConnection() throws DatabaseUnitException, SQLException {
		LOG.info("Default driver selected");

		DatabaseConnection iConn = new DatabaseConnection(getConnection());

		return iConn;
	}

	public void execute(DatabaseOperation operation, String xml) {
		try {
			if (!xml.startsWith("/")) {
				xml = "/" + xml;
			}

			InputStream XMLStream = getClass().getResourceAsStream(xml);

			if (XMLStream == null) {
				throw new JIntegrityException("the file \"" + xml + "\" was not found!");
			}

			IDatabaseConnection iConn = getDbUnitConnection();

			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();

			LOG.info("building \"" + xml + "\"");
			IDataSet dataSet = builder.build(XMLStream);

			operation.execute(iConn, dataSet);

			iConn.close();
		} catch (JIntegrityException e) {
			throw new JIntegrityException(e.getMessage(), e);
		} catch (NoSuchTableException e) {
			throw new JIntegrityException("the table \"" + e.getMessage() + "\" was not found!", e);
		} catch (DatabaseUnitException e) {
			throw new JIntegrityException(e.toString(), e);
		} catch (SQLException e) {
			throw new JIntegrityException(e.getMessage(), e);
		}
	}

	public void cleanAndInsert(String xml) {
		execute(CLEAN_INSERT, xml);
		LOG.info("clean_insert operation finished!");
	}

	public void delete(String xml) {
		execute(DELETE, xml);
		LOG.info("delete operation finished!\n");
	}

	public void deleteAll(String xml) {
		execute(DELETE_ALL, xml);
		LOG.info("delete_all operation finished!\n");
	}

	public void insert(String xml) {
		execute(INSERT, xml);
		LOG.info("insert operation finished!\n");
	}

	public void refresh(String xml) {
		execute(REFRESH, xml);
		LOG.info("refresh operation finished!");
	}

	public void truncate(String xml) {
		execute(TRUNCATE_TABLE, xml);
		LOG.info("truncate_table operation finished!");
	}

	public void update(String xml) {
		execute(UPDATE, xml);
		LOG.info("update operation finished!\n");
	}

}