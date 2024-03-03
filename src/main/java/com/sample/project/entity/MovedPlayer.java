package com.sample.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MOVEDPLAYER")
public class MovedPlayer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column(name = "CATEGORY")
	public String category;

	@Column(name = "NAME")
	public String name;

	@Column(name = "OLDDEPARTMENT")
	public String oldDepartment;

	@Column(name = "OLDDEPARTMENTCODE")
	public String oldDepartmentCode;

	@Column(name = "NEWDEPARTMENT")
	public String newDepartment;

	@Column(name = "NEWDEPARTMENTCODE")
	public String newDepartmentCode;

	@Column(name = "MAIL")
	public String mail;

	@Column(name = "DATE")
	public String date;

	public MovedPlayer() {

	}

	public MovedPlayer(Long id, String category, String name, String oldDepartment,
			String oldDepartmentCode, String newDepartment, String newDepartmentCode, String mail,
			String date) {
		super();
		this.id = id;
		this.category = category;
		this.name = name;
		this.oldDepartment = oldDepartment;
		this.oldDepartmentCode = oldDepartmentCode;
		this.newDepartment = newDepartment;
		this.newDepartmentCode = newDepartmentCode;
		this.mail = mail;
		this.date = date;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the oldDepartment
	 */
	public String getOldDepartment() {
		return oldDepartment;
	}

	/**
	 * @param oldDepartment the oldDepartment to set
	 */
	public void setOldDepartment(String oldDepartment) {
		this.oldDepartment = oldDepartment;
	}

	/**
	 * @return the oldDepartmentCode
	 */
	public String getOldDepartmentCode() {
		return oldDepartmentCode;
	}

	/**
	 * @param oldDepartmentCode the oldDepartmentCode to set
	 */
	public void setOldDepartmentCode(String oldDepartmentCode) {
		this.oldDepartmentCode = oldDepartmentCode;
	}

	/**
	 * @return the newDepartment
	 */
	public String getNewDepartment() {
		return newDepartment;
	}

	/**
	 * @param newDepartment the newDepartment to set
	 */
	public void setNewDepartment(String newDepartment) {
		this.newDepartment = newDepartment;
	}

	/**
	 * @return the newDepartmentCode
	 */
	public String getNewDepartmentCode() {
		return newDepartmentCode;
	}

	/**
	 * @param newDepartmentCode the newDepartmentCode to set
	 */
	public void setNewDepartmentCode(String newDepartmentCode) {
		this.newDepartmentCode = newDepartmentCode;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

}
