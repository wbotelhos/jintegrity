-- HSQL
CREATE TABLE Contact (id INTEGER IDENTITY PRIMARY KEY, phone varchar(255), user_id INTEGER,
	FOREIGN KEY (user_id) REFERENCES user(id)
);

-- MySQL
--CREATE TABLE if NOT EXISTS Contact (id bigint AUTO_INCREMENT, phone varchar(255), user_id int,
--	PRIMARY KEY (id),
--	FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
--);