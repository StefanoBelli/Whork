package logic.model;

import java.util.Date;

public final class JobSeekerUserModel extends UserModel {
	private Date birthday;
	private String cv;
	private String homeAddress;
	private String biography;
	private ComuneModel comune;
	private EmploymentStatusModel employmentStatus;
	private String website;
	private String twitter;
	private String facebook;
	private String instagram;

	public JobSeekerUserModel() {
		setEmployee(false);
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getWebsite() {
		return website;
	}

	public String getFacebook() {
		return facebook;
	}

	

	public String getTwitter() {
		return twitter;
	}
	
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getInstagram() {
		return instagram;
	}

	
	public String getCv() {
		return cv;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public String getBiography() {
		return biography;
	}

	public ComuneModel getComune() {
		return comune;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}

	public EmploymentStatusModel getEmploymentStatus() {
		return employmentStatus;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public void setComune(ComuneModel comune) {
		this.comune = comune;
	}

	public void setEmploymentStatus(EmploymentStatusModel employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
}
