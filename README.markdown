# jIntegrity - http://jintegrity.com

jIntegrity is a toolbox to help you test with a good integration with DbUnit.

## License

The jIntegrity is licensed under [The MIT License](http://www.opensource.org/licenses/mit-license.php)

## Version

	@version         0.1.0
	@since           2011.09.30
	@author          Washington Botelho dos Santos
	@documentation   jintegrity.com/doc

## Usage

### hibernate.properties default keys
	hibernate.connection.driver_class=
	hibernate.connection.url=
	hibernate.connection.username=
	hibernate.connection.password=

### jintegrity.properties default body
	path= 
	xml=

+ You don't need starts with slash on path; Ex.: my/package/dataset
+ The XMLs is comma separated. Ex.: User,Product,Payment

### Use path and XML from jintegrity.properties
	JIntegrity helper = new JIntegrity();
	helper.insert();

### Use global path and XML from jintegrity.properties
	JIntegrity helper = new JIntegrity();
	helper.path("my/package/dataset").insert();

### Use global XML and path from jintegrity.properties
	JIntegrity helper = new JIntegrity();
	helper.xml("User").insert();

### Use global path and global XML
	JIntegrity helper = new JIntegrity();
	helper.path("my/package/dataset").xml("User").insert();

### Overrides global path and global xml with a fully qualified XML path
	JIntegrity helper = new JIntegrity();
	helper.path("my/package/dataset").xml("User").insert("/my/package/dataset/Payment");

+ The first slash on action insert actives the fully qualified path and it ignores the path configuration.

### Using two diferent XMLs on method
	JIntegrity helper = new JIntegrity();
	helper.insert("User", "Payment");

### Using two diferent XMLs on method with one using fully qualified path 
	JIntegrity helper = new JIntegrity();
	helper.insert("User", "/my/package/dataset/Payment");

### Getting the DbUnitManager to use other methods 
	JIntegrity helper = new JIntegrity();
	DbUnitManager dbUnitManger = helper.getDbUnitManager();
	dbUnitManger.refresh("User");

+ If you don't need anything specific from JIntegrity object, you can instantiate the ```DbUnitManager``` directly.

### Using JPA

	EntityManager manager = JPAHelper.currentEntityManager();
	Query query = manager.createQuery("from User");
	List<User> userList = query.getResultList(); 

### Using Hibernate

	Session session = HibernateHelper.currentSession();
	Criteria criteria = session.createCriteria(User.class);
	List<User> userList = criteria.list();

### Exporting schema from models

	HibernateHelper.exportSchema();

+ You need especify the mappings on hibernate.cfg.xml. Ex.: ```<mapping class="com.jintegrity.model.User" />```

## JIntegrity Methods

	String helper.getPath();

	String[] helper.getXmls();

	DbUnitManager helper.getDbUnitManager();

	JIntegrity helper.xml(String xml);
	JIntegrity helper.path(String path);
	JIntegrity helper.vendor(String vendor); // * Vendors
	JIntegrity helper.keys(PropertiesKey propertiesKey);

	JIntegrity helper.clean(String... xml);
	JIntegrity helper.insert(String... xml);
	JIntegrity helper.cleanAndInsert(String... xml);

	JIntegrity helper.useDB2();
	JIntegrity helper.useHSQL();
	JIntegrity helper.useMSSQL();
	JIntegrity helper.useMySQL();
	JIntegrity helper.useOracle10g();

	// * Vendors
	JIntegrity.DB2;
	JIntegrity.HSQL;
	JIntegrity.MSSQL;
	JIntegrity.MYSQL;
	JIntegrity.ORACLE10G;
	JIntegrity.POSTGRE;

## DbUnitManager Methods

	void dbUnitManager.cleanAndInsert(String xml);
	void dbUnitManager.delete(String xml);
	void dbUnitManager.deleteAll(String xml);
	void dbUnitManager.insert(String xml);
	void dbUnitManager.refresh(String xml);
	void dbUnitManager.truncate(String xml);
	void dbUnitManager.update(String xml);

	Connection dbUnitManager.getConnection();

	void dbUnitManager.execute(DatabaseOperation operation, String xml);

## JPA Helpers Methods

	EntityManagerFactory entityManagerFactory(String name);
	EntityManager currentEntityManager();
	void close();

## Hibernate Helpers Methods

	SessionFactory sessionFactory();
	Session currentSession();
	void close();
	void exportSchema();

## More

+ Priority: properties < global methods { path() and xml() } < action methos { clean(), insert() and cleanAndInsert() }.

## Buy me a coffee

You can do it by [PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=X8HEP2878NDEG&item_name=jIntegrity). Thanks! (: