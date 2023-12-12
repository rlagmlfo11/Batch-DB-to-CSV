package com.sample.project.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class People {

	@Id
	@Column(name = "PersonalName")
	private String personalName;

	@Column(name = "PersonalAge")
	private String personalAge;

	@Column(name = "PersonalGender")
	private String personalGender;

	@Column(name = "PersonalMail")
	private String personalMail;

	@Column(name = "PersonalWage")
	private String personalWage;

	public People() {

	}

	public People(String personalName, String personalAge, String personalGender,
			String personalMail, String personalWage) {
		super();
		this.personalName = personalName;
		this.personalAge = personalAge;
		this.personalGender = personalGender;
		this.personalMail = personalMail;
		this.personalWage = personalWage;
	}

	/**
	 * @return the personalName
	 */
	public String getPersonalName() {
		return personalName;
	}

	/**
	 * @param personalName the personalName to set
	 */
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	/**
	 * @return the personalAge
	 */
	public String getPersonalAge() {
		return personalAge;
	}

	/**
	 * @param personalAge the personalAge to set
	 */
	public void setPersonalAge(String personalAge) {
		this.personalAge = personalAge;
	}

	/**
	 * @return the personalGender
	 */
	public String getPersonalGender() {
		return personalGender;
	}

	/**
	 * @param personalGender the personalGender to set
	 */
	public void setPersonalGender(String personalGender) {
		this.personalGender = personalGender;
	}

	/**
	 * @return the personalMail
	 */
	public String getPersonalMail() {
		return personalMail;
	}

	/**
	 * @param personalMail the personalMail to set
	 */
	public void setPersonalMail(String personalMail) {
		this.personalMail = personalMail;
	}

	/**
	 * @return the personalWage
	 */
	public String getPersonalWage() {
		return personalWage;
	}

	/**
	 * @param personalWage the personalWage to set
	 */
	public void setPersonalWage(String personalWage) {
		this.personalWage = personalWage;
	}

}
