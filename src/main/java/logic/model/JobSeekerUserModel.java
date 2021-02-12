package logic.model;

import java.util.Date;

public class JobSeekerUserModel extends UserModel {
	private Date birthday;
	private String cv;
	private String homeAddress;
	private String biography;
	private ComuneModel comune;
	private EmploymentStatusModel employmentStatus;

	public Date getBirthday() {
		return birthday;
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
