package com.jintegrity.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Contact implements Serializable {

	private static final long serialVersionUID = 2444655854495633756L;

	@Id
	@GeneratedValue
	private Long id;
	private String phone;

	@OneToOne
	private User user;

	public Contact() { }

	public Contact(Long id, String phone) {
		this.id = id;
		this.phone = phone;
	}

	public Contact(Long id, String phone, Long userId) {
		this.id = id;
		this.phone = phone;

		User user = new User();
		user.setId(userId);

		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}