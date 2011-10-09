package com.jintegrity.helper;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateHelper {

	public static void exportSchema() {
	    Configuration configurantion = new Configuration();
	    configurantion.configure();

	    SchemaExport schemaExport = new SchemaExport(configurantion);
	    schemaExport.create(true, true);
	}

}