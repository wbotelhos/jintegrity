package com.jintegrity.helper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAHelper {

	private static EntityManagerFactory emf;
	private static EntityManager manager;
	private static String name;

	public static EntityManagerFactory entityManagerFactory(String name) {
		if (emf == null) {
			JPAHelper.name = name;
			emf = Persistence.createEntityManagerFactory(name);
		}

		return emf;
	}

	public static EntityManager currentEntityManager() {
		if (manager == null) {
			manager = entityManagerFactory(name).createEntityManager();
			manager.getTransaction().begin();
		}

		return manager;
	}

	public static void close() {
		manager.getTransaction().rollback();
		manager.close();
		manager = null;
	}

}
