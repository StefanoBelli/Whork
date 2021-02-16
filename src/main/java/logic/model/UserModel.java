package logic.model;

public abstract class UserModel {
	private String name;
	private String surname;
	private String phoneNumber;
	private String cf;
	private boolean employee;

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
}
