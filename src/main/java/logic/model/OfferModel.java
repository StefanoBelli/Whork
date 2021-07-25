package logic.model;

import java.util.Date;

public final class OfferModel {
	
	private int id;
	private String offerName;
	private String description;
	private String jobPhysicalLocationFullAddress;
	private CompanyModel company;
	private int salaryEUR;
	private String photo;
	private String workShift;
	private JobPositionModel jobPosition;
	private QualificationModel qualification;
	private TypeOfContractModel typeOfContract;
	private Date publishDate;
	private int clickStats;
	private String note;
	private boolean verifiedByWhork;
	private JobCategoryModel jobCategory;
	private EmployeeUserModel employee;
	
	public String getOfferName() {
		return offerName;
	}
	
	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	

	
	public String getPhoto() {
		return photo;
	}

	public boolean isVerifiedByWhork() {
		return verifiedByWhork;
	}

	public String getNote() {
		return note;
	}

	

	public void setNote(String note) {
		this.note = note;
	}


	public String getWorkShit() {
		return workShift;
	}



	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	public void setWorkShit(String workShift) {
		this.workShift = workShift;
	}

	public int getSalaryEUR() {
		return salaryEUR;
	}


	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public String getJobPhysicalLocationFullAddress() {
		return jobPhysicalLocationFullAddress;
	}


	public void setSalaryEUR(int salaryEUR) {
		this.salaryEUR = salaryEUR;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public int getClickStats() {
		return clickStats;
	}

	public void setClickStats(int clickStats) {
		this.clickStats = clickStats;
	}


	public void setJobPhysicalLocationFullAddress(String jobPhysicalLocationFullAddress) {
		this.jobPhysicalLocationFullAddress = jobPhysicalLocationFullAddress;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	
	public void setVerifiedByWhork(boolean verifiedByWhork) {
		this.verifiedByWhork = verifiedByWhork;
	}

	
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public int getId() {
		return id;
	}

	public EmployeeUserModel getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeUserModel employee) {
		this.employee = employee;
	}

	public CompanyModel getCompany() {
		return company;
	}

	public void setCompany(CompanyModel company) {
		this.company = company;
	}

	public TypeOfContractModel getTypeOfContract() {
		return typeOfContract;
	}

	public void setTypeOfContract(TypeOfContractModel typeOfContract) {
		this.typeOfContract = typeOfContract;
	}

	public JobPositionModel getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(JobPositionModel jobPosition) {
		this.jobPosition = jobPosition;
	}

	public JobCategoryModel getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(JobCategoryModel jobCategory) {
		this.jobCategory = jobCategory;
	}

	public QualificationModel getQualification() {
		return qualification;
	}

	public void setQualification(QualificationModel qualification) {
		this.qualification = qualification;
	}
	
}
