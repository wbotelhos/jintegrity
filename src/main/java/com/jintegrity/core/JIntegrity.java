package com.jintegrity.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jintegrity.exception.JIntegrityException;
import com.jintegrity.model.PropertiesKey;
import com.jintegrity.vendor.Db2DbUnitManager;
import com.jintegrity.vendor.HsqldbDbUnitManager;
import com.jintegrity.vendor.MsSqlServerDbUnitManager;
import com.jintegrity.vendor.MySqlDbUnitManager;
import com.jintegrity.vendor.Oracle10gDbUnitManager;
import com.jintegrity.vendor.PostgresqlDbUnitManager;

public class JIntegrity {

	private static final Logger LOG = Logger.getLogger(JIntegrity.class);

	private DbUnitManager dbUnitManager = new DbUnitManager();

	private String path;
	private String[] xmls;
	private PropertiesKey propertiesKey;

	private static final String JINTEGRITY_FILE_NAME = "/jintegrity.properties";

	private static String pathFile;
	private static String[] xmlsFile;

	private static final String PATH_KEY = "path";
	private static final String XML_KEY = "xml";

	public static final String DB2 = "db2";
	public static final String HSQL = "hsql";
	public static final String MSSQL = "mssql";
	public static final String MYSQL = "mysql";
	public static final String POSTGRE = "postgre";
	public static final String ORACLE10G = "oracle10g";

	static {
		loadProperties();
	}

	public JIntegrity() { }

	public JIntegrity(PropertiesKey propertiesKey) {
		this.propertiesKey = propertiesKey;
		LOG.info("custom properties key selected: " + propertiesKey.toString());
	}

	public JIntegrity keys(PropertiesKey propertiesKey) {
		this.propertiesKey = propertiesKey;
		LOG.info("custom properties key selected: " + propertiesKey.toString());

		return this;
	}

	public JIntegrity useDB2() {
		dbUnitManager = new Db2DbUnitManager(propertiesKey);
		return this;
	}

	public JIntegrity useHSQL() {
		dbUnitManager = new HsqldbDbUnitManager(propertiesKey);
		return this;
	}

	public JIntegrity useMSSQL() {
		dbUnitManager = new MsSqlServerDbUnitManager(propertiesKey);
		return this;
	}

	public JIntegrity useMySQL() {
		dbUnitManager = new MySqlDbUnitManager(propertiesKey);
		return this;
	}


	public JIntegrity useOracle10g() {
		dbUnitManager = new Oracle10gDbUnitManager(propertiesKey);
		return this;
	}
	
	public JIntegrity usePostgre() {
		dbUnitManager = new PostgresqlDbUnitManager(propertiesKey);
		return this;
	}

	public JIntegrity vendor(String vendor) {
		if (vendor.equals(HSQL)) {
			dbUnitManager = new HsqldbDbUnitManager(propertiesKey);
		} else if (vendor.equals(MYSQL)) {
			dbUnitManager = new MySqlDbUnitManager(propertiesKey);
		} else if (vendor.equals(DB2)) {
			dbUnitManager = new Db2DbUnitManager(propertiesKey);
		} else if (vendor.equals(ORACLE10G)) {
			dbUnitManager = new Oracle10gDbUnitManager(propertiesKey);
		} else if (vendor.equals(POSTGRE)) {
			dbUnitManager = new PostgresqlDbUnitManager(propertiesKey);
		} else if (vendor.equals(MSSQL)) {
			dbUnitManager = new MsSqlServerDbUnitManager(propertiesKey);
		} else {
			LOG.error("the \"" + vendor + "\" driver factory is not supported yet!");
			throw new JIntegrityException("the \"" + vendor + "\" driver factory is not supported!");
		}

		return this;
	}

	public JIntegrity path(String path) {
		LOG.info("global path selected: " + path);
		this.path = fixPath(path);
		return this;
	}

	public JIntegrity xml(String... xml) {
		LOG.info("global xmls selected: " + xml);
		this.xmls = xml;
		return this;
	}

	public JIntegrity cleanAndInsert(String... xml) {
		return clean(xml).insert(xml);
	}

	public JIntegrity clean(String... xml) {
		adjustPathAndXML(xml);

		for (int i = xmls.length - 1; i >= 0; i--) {
			if (xmls[i].startsWith("/")) { // fully qualified
				path = "";
			}

			dbUnitManager.deleteAll(path + xmls[i] + ".xml");
		}

		return this;
	}

	public JIntegrity insert(String... xml) {
		adjustPathAndXML(xml);

		for (int i = 0; i < xmls.length; i++) {
			if (xmls[i].startsWith("/")) { // fully qualified
				path = "";
			}

			dbUnitManager.insert(path + xmls[i] + ".xml");
		}

		return this;
	}

	public String getPath() {
		if (path == null) {
			if (pathFile == null) {
				String message = "the '" + PATH_KEY + "' key was not found on '" + JINTEGRITY_FILE_NAME + "' file and not informed on method!";
				LOG.error(message);
				throw new JIntegrityException(message);
			}

			path = fixPath(pathFile);
		}

		return fixPath(path);
	}

	public String[] getXmls() {
		if (xmls == null) {

			if (xmlsFile == null) {
				String message = "the '" + XML_KEY + "' value was not found on '" + JINTEGRITY_FILE_NAME + "' file and not informed on method!";
				LOG.error(message);
				throw new JIntegrityException(message);
			} else {
				LOG.warn("the '" + XML_KEY + "' key was not found on '" + JINTEGRITY_FILE_NAME + "' file!");
			}

			xmls = xmlsFile;
		}

		return xmls;
	}

	public DbUnitManager getDbUnitManager() {
		return dbUnitManager;
	}

	private void adjustPathAndXML(String... xml) {
		if (xml.length > 0) {
			xmls = xml;
		}

		path = getPath();
		xmls = getXmls();
	}

	private String fixPath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}

		if (!path.endsWith("/")) {
			path += "/";
		}

		return path;
	}

	private static void loadProperties() {
		try {
			InputStream stream = JIntegrity.class.getResourceAsStream(JINTEGRITY_FILE_NAME);

			if (stream == null) {
				LOG.warn(JINTEGRITY_FILE_NAME + " file was not found!");
			} else {
				LOG.info(JINTEGRITY_FILE_NAME + " loaded");
				Properties properties = new Properties();
				properties.load(stream);

				if (properties.containsKey(PATH_KEY)) {
					pathFile = properties.getProperty(PATH_KEY);
					LOG.info("loaded the following path: " + pathFile);
				} else {
					LOG.warn("the '" + PATH_KEY + "' key was not found on '" + JINTEGRITY_FILE_NAME + "' file!");
				}

				if (properties.containsKey(XML_KEY)) {
					String xmls = properties.getProperty(XML_KEY);

					if (xmls == null || xmls.isEmpty()) {
						xmlsFile = new String[] { };
						LOG.warn("the '" + XML_KEY + "' value was not found on '" + JINTEGRITY_FILE_NAME + "' file!");
					} else {
						xmlsFile = xmls.split(",");
						LOG.info("loaded the following xmls: " + xmls.replaceAll(",", ", "));	
					}
				} else {
					LOG.warn("the '" + XML_KEY + "' key was not found on '" + JINTEGRITY_FILE_NAME + "' file!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new JIntegrityException(e.getMessage());
		}
	}

}