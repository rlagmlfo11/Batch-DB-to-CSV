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

	@Id
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

	public Player() {

	}

	public Player(String name, String age, String gender, String mail, String wage, String boss) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.mail = mail;
		this.wage = wage;
		this.boss = boss;
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

}
