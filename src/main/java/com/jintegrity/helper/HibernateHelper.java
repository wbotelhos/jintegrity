package com.jintegrity.helper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class HibernateHelper {

	private static Session session;
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public static void exportSchema() {
		SchemaExport schemaExport = new SchemaExport(getConfiguratin());
		schemaExport.create(true, true);
	}

	public static SessionFactory sessionFactory() {
		if (sessionFactory == null) {
			
			Configuration configuratin = getConfiguratin();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuratin.getProperties()).buildServiceRegistry();
			
			sessionFactory = configuratin.buildSessionFactory(serviceRegistry);
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
