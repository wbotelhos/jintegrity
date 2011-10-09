package com.jintegrity.helper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateHelper {

	private static Session session;
	private static SessionFactory sessionFactory;

	public static void exportSchema() {
		SchemaExport schemaExport = new SchemaExport(getConfiguratin());
		schemaExport.create(true, true);
	}

	public static SessionFactory sessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = getConfiguratin().buildSessionFactory();
		}

		return sessionFactory;
	}

	public static Session currentSession() {
		if (session == null) {
			session = sessionFactory().openSession();
			session.beginTransaction();
		}

		return session;
	}

	public static void close() {
		session.getTransaction().rollback();
		session.close();
		session = null;
	}

	private static Configuration getConfiguratin() {
		Configuration configurantion = new Configuration();
		configurantion.configure();

		return configurantion;
	}

}