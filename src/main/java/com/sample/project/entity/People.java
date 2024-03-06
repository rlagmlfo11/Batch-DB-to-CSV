package com.sample.project.entity;

public class People {

	private String personalName;

	private String personalAge;

	private String personalGender;

	private String personalMail;

	private String personalWage;

	private String boss1;

	private String boss2;

	private String boss3;

	private String codeName;

	public People() {

	}

	public People(String personalName, String personalAge, String personalGender,
			String personalMail, String personalWage, String boss1, String boss2, String boss3,
			String codeName) {
		super();
		this.personalName = personalName;
		this.personalAge = personalAge;
		this.personalGender = personalGender;
		this.personalMail = personalMail;
		this.personalWage = personalWage;
		this.boss1 = boss1;
		this.boss2 = boss2;
		this.boss3 = boss3;
		this.codeName = codeName;
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

	/**
	 * @return the boss1
	 */
	public String getBoss1() {
		return boss1;
	}

	/**
	 * @param boss1 the boss1 to set
	 */
	public void setBoss1(String boss1) {
		this.boss1 = boss1;
	}

	/**
	 * @return the boss2
	 */
	public String getBoss2() {
		return boss2;
	}

	/**
	 * @param boss2 the boss2 to set
	 */
	public void setBoss2(String boss2) {
		this.boss2 = boss2;
	}

	/**
	 * @return the boss3
	 */
	public String getBoss3() {
		return boss3;
	}

	/**
	 * @param boss3 the boss3 to set
	 */
	public void setBoss3(String boss3) {
		this.boss3 = boss3;
	}

	/**
	 * @return the codeName
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * @param codeName the codeName to set
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}
