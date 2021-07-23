package logic.model;

public class UserModel {
	protected UserModel() {}
	
	private String name;
	private String surname;
	private String phoneNumber;
	private String cf;
	private String photo;
	private boolean employee;
	private String website;
	private String twitter;
	private String facebook;
	private String instagram;

	public String getCf() {
		return cf;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPhoto() {
		return photo;
	}

	public boolean isEmployee() {
		return employee;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	protected void setEmployee(boolean employee) {
		this.employee = employee;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}
}