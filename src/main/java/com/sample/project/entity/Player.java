package com.sample.project.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYER")
public class Player {

	@Column(name = "NAME")
	private String name;

	@Column(name = "AGE")
	private String age;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "MAIL")
	private String mail;

	@Column(name = "WAGE")
	private String wage;

	@Column(name = "BOSS")
	private String boss;

	@Column(name = "DEPARTMENT")
	private String department;

	@Column(name = "RECEIVEDDATE")
	private String receivedDate;

	@Column(name = "DEPARTMENTCODE")
	private String departmentCode;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public Player() {

	}

	public Player(String name, String age, String gender, String mail, String wage, String boss,
			String department, String receivedDate, String departmentCode, Long id) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mail = mail;
		this.wage = wage;
		this.boss = boss;
		this.department = department;
		this.receivedDate = receivedDate;
		this.departmentCode = departmentCode;
		this.id = id;
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
	 * @return the age
	 */
	public String getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
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
	 * @return the wage
	 */
	public String getWage() {
		return wage;
	}

	/**
	 * @param wage the wage to set
	 */
	public void setWage(String wage) {
		this.wage = wage;
	}

	/**
	 * @return the boss
	 */
	public String getBoss() {
		return boss;
	}

	/**
	 * @param boss the boss to set
	 */
	public void setBoss(String boss) {
		this.boss = boss;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the receivedDate
	 */
	public String getReceivedDate() {
		return receivedDate;
	}

	/**
	 * @param receivedDate the receivedDate to set
	 */
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}

	/**
	 * @return the departmentCode
	 */
	public String getDepartmentCode() {
		return departmentCode;
	}

	/**
	 * @param departmentCode the departmentCode to set
	 */
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
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

}
