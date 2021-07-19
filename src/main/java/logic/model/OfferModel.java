package logic.model;

import java.util.Date;

public final class OfferModel {
	private int id;
	private String offerName;
	private String description;
	private String jobPhysicalLocationFullAddress;
	private String companyVat;
	private int salaryEUR;
	private String photo;
	private String workShift;
	private String jobPosition;
	private String qualification;
	private String typeOfContract;
	private Date publishDate;
	private int clickStats;
	private String note;
	private boolean verifiedByWhork;
	private String jobCategory;
	private String employeeCF;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}


	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


	public String getCompanyVat() {
		return companyVat;
	}

	public void setCompanyVat(String companyVat) {
		this.companyVat = companyVat;
	}

	public String getWorkShit() {
		return workShift;
	}

	public void setWorkShit(String workShift) {
		this.workShift = workShift;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getTypeOfContract() {
		return typeOfContract;
	}

	public void setTypeOfContract(String typeOfContract) {
		this.typeOfContract = typeOfContract;
	}

	public String getJobPhysicalLocationFullAddress() {
		return jobPhysicalLocationFullAddress;
	}

	public void setJobPhysicalLocationFullAddress(String jobPhysicalLocationFullAddress) {
		this.jobPhysicalLocationFullAddress = jobPhysicalLocationFullAddress;
	}

	public int getSalaryEUR() {
		return salaryEUR;
	}

	public void setSalaryEUR(int salaryEUR) {
		this.salaryEUR = salaryEUR;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getClickStats() {
		return clickStats;
	}

	public void setClickStats(int clickStats) {
		this.clickStats = clickStats;
	}

	public boolean isVerifiedByWhork() {
		return verifiedByWhork;
	}

	public void setVerifiedByWhork(boolean verifiedByWhork) {
		this.verifiedByWhork = verifiedByWhork;
	}

	public String getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	public String getEmployeeCF() {
		return employeeCF;
	}

	public void setEmployeeCF(String employeeCF) {
		this.employeeCF = employeeCF;
	}
	
	
		
}
