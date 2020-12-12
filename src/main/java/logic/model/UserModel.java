package logic.model;

import java.util.Date;

public final class UserModel {
	private String countryId;
	private String mail;
	private String name;
	private String surname;
	private Date birthday;
	private String phoneNumber;
	private String homeAddress;
	private String biography;
	private boolean employed;
	private CityModel city;
	
	public String getCountryId() {
		return countryId;
	}
	
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getHomeAddress() {
		return homeAddress;
	}
	
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	public String getBiography() {
		return biography;
	}
	
	public void setBiography(String biography) {
		this.biography = biography;
	}
	
	public boolean isEmployed() {
		return employed;
	}
	
	public void setEmployed(boolean employed) {
		this.employed = employed;
	}
	
	public CityModel getCity() {
		return city;
	}
	
	public void setCity(CityModel city) {
		this.city = city;
	}
}
