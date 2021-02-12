package logic.model;

public class EmployeeUserModel extends UserModel {
	private CompanyModel company;
	private boolean admin;
	private boolean recruiter;
	private String note;
	private String photo;

	public CompanyModel getCompany() {
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

	public void setCompany(CompanyModel company) {
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
}
