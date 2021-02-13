package logic.bean;

import java.util.Date;
import java.io.Serializable;
import java.io.File;
import logic.exception.SyntaxException;

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
	private File photo;
	private Date birthday;
	private File cv;
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

	public File getPhoto() {
		return photo;
	}

	public Date getBirthday() {
		return birthday;
	}

	public File getCv() {
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

	public void setCf(String cf) 
			throws SyntaxException {
		if(cf.length() != 16) {
			throw new SyntaxException("CF length must be equal to 16");
		}
		this.cf = cf;
	}

	public void setName(String name) 
			throws SyntaxException {
		if(name.length() > 45) {
			throw new SyntaxException("Name length must be max. 45 chars");
		}

		this.name = name;
	}

	public void setSurname(String surname) 
			throws SyntaxException {
		if(surname.length() > 45) {
			throw new SyntaxException("Surname length must be max. 45 chars");
		}

		this.surname = surname;
	}

	public void setPhoneNumber(String phoneNumber) 
			throws SyntaxException {
		
		if(phoneNumber.length() < 9 || phoneNumber.length() > 10) {
			throw new SyntaxException("Valid italian phone number format is 9-10 chars");
		}

		for(int i = 0; i < phoneNumber.length(); ++i) {
			if(!Character.isDigit(phoneNumber.charAt(i))) {
				throw new SyntaxException("Only digits are allowed");
			}
		}
		
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

	public void setNote(String note) 
			throws SyntaxException {
		if(note.length() > 45) {
			throw new SyntaxException("Note length cannot be greater than 45");
		}

		this.note = note;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCv(File cv) {
		this.cv = cv;
	}

	public void setHomeAddress(String homeAddress) 
			throws SyntaxException {
		if(homeAddress.length() > 45) {
			throw new SyntaxException("Home address length cannot be greater than 45");
		}

		this.homeAddress = homeAddress;
	}

	public void setBiography(String biography) 
			throws SyntaxException {
		if(homeAddress.length() > 250) {
			throw new SyntaxException("Biography length cannot be greater than 250");
		}
		this.biography = biography;
	}
	
	public void setComune(ComuneBean comune) {
		this.comune = comune;
	}

	public void setEmploymentStatus(EmploymentStatusBean employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
}
