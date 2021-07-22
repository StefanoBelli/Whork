package logic.model;

import java.util.Date;

public final class OfferModel {
	private int id;
	
	private String description;
	private String jobPhysicalLocationFullAddress;
	private String jobCategory;
	private String companyVat;
	private String photo;
	private String note;
	private String typeOfContract;
	private String workShift;
	private String qualification;
	private Date publishDate;
	private String jobPosition;
	private int clickStats;
	private boolean verifiedByWhork;
	private int salaryEUR;
	private String offerName;
	private String employeeCF;
	



	public String getEmployeeCF() {
		return employeeCF;
	}
	
	public String getOfferName() {
		return offerName;
	}
	
	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	

	public String getJobCategory() {
		return jobCategory;
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



	public String getCompanyVat() {
		return companyVat;
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

	public String getJobPosition() {
		return jobPosition;
	}
	
	
	public void setWorkShit(String workShift) {
		this.workShift = workShift;
	}



	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getQualification() {
		return qualification;
	}



	public String getTypeOfContract() {
		return typeOfContract;
	}

	


	public int getSalaryEUR() {
		return salaryEUR;
	}


	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	
	public void setTypeOfContract(String typeOfContract) {
		this.typeOfContract = typeOfContract;
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

	public void setCompanyVat(String companyVat) {
		this.companyVat = companyVat;
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

	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}



	public void setEmployeeCF(String employeeCF) {
		this.employeeCF = employeeCF;
	}
	

	public int getId() {
		return id;
	}
	
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	
}
