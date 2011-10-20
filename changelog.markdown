### 0.1.2
+ Now String "null" on XML is converted to null Object;
+ Fixed the function to capture the custom PropertiesKey from constructor.

### 0.1.0
+ Capture the XML path and the XMLs names from jintegrity.properties;
+ Do the following operations: clean_insert, delete, delete_all, insert, refresh, truncate_table and update;
+ You can let jintegrity read the path and XMLs files from jintegrity.properties as default like .insert();
+ You can especify the path of XML on methods like .xml("User");
+ You can especify the name of XML on methods like .insert("User");
+ You can especify the fully qualified XML on methods like .insert("/dataset/User");
+ You can especify a global path to override properties like .path("dataset");
+ You can especify one or more global XMLs to override properties like .xml("User", "Contact");
+ You can use a custom key Hibernate properties like new PropertiesKey().url("javax.persistence.jdbc.url");
+ You can do chaining like .clean("User").insert("User", "Contact");
+ You can get the path and XMLs setted to jIntegrity using helper.getPath() and helper.getXmls();
+ You can instantiate, get current and close JPA connection like JPAHelper.entityManagerFactory("default");
+ You can instantiate, get current and close Hibernate connection like HibernateHelper.currentSession();
+ You can export the schema based on .cfg using HibernateHelper.exportSchema();
+ You can especify the following drivers: DB2, HSQL, MsSQL, MySQL, Oracle 10g and Postgre.