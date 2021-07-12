package logic.bean;

import java.util.Date;
import java.io.Serializable;

public final class UserBean implements Serializable {
	private static final long serialVersionUID = -3927240997879942530L;

	private String cf;
	private String name;
	private String surname;
	private String phoneNumber;
	private boolean employee;
	private CompanyBean company;
	private boolean admin;
	private boolean recruiter;
	private String note;
	private String photo;
	private Date birthday;
	private String cv;
	private String homeAddress;
	private String biography;
	private ComuneBean comune;
	private EmploymentStatusBean employmentStatus;

	public String getCf() {
		return this.cf;
	}

	public String getName() {
		return this.name;
	}

	public String getSurname() {
		return this.surname;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public boolean isEmployee() {
		return this.employee;
	}

	public CompanyBean getCompany() {
		return company;
	}

	public boolean isAdmin() {
		return admin;
	}

	public boolean isRecruiter() {
		return recruiter;
	}

	public String getNote() {
		return note;
	}

	public String getPhoto() {
		return photo;
	}

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

	public ComuneBean getComune() {
		return comune;
	}

	public EmploymentStatusBean getEmploymentStatus() {
		return employmentStatus;
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

	public void setEmployee(boolean employee) {
		this.employee = employee;
	}

	public void setCompany(CompanyBean company) {
		this.company = company;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void setRecruiter(boolean recruiter) {
		this.recruiter = recruiter;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
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
	
	public void setComune(ComuneBean comune) {
		this.comune = comune;
	}

	public void setEmploymentStatus(EmploymentStatusBean employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
}
