package logic.bean;

import java.io.Serializable;
import java.util.Date;

public final class OfferBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 145847504079594489L;
	
	private int id;
	private String offerName;
	private String description;
	private String jobPhysicalLocationFullAddress;
	private CompanyBean company;
	private int salaryEUR;
	private String photo;
	private String workShift;
	private JobPositionBean jobPosition;
	private QualificationBean qualification;
	private TypeOfContractBean typeOfContract;
	private Date publishDate;
	private int clickStats;
	private String note;
	private boolean verifiedByWhork;
	private JobCategoryBean jobCategory;
	private UserBean employee;
		

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

	public String getWorkShit() {
		return workShift;
	}
	
	public void setWorkShit(String workShift) {
		this.workShift = workShift;
	}

	public JobPositionBean getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(JobPositionBean jobPosition) {
		this.jobPosition = jobPosition;
	}

	public QualificationBean getQualification() {
		return qualification;
	}

	public void setQualification(QualificationBean qualification) {
		this.qualification = qualification;
	}

	public TypeOfContractBean getTypeOfContract() {
		return typeOfContract;
	}

	public void setTypeOfContract(TypeOfContractBean typeOfContract) {
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

	public JobCategoryBean getJobCategory() {
		return jobCategory;
	}

	public void setJobCategory(JobCategoryBean jobCategory) {
		this.jobCategory = jobCategory;
	}

	
	public CompanyBean getCompany() {
		return company;
	}
	public void setCompany(CompanyBean company) {
		this.company = company;
	}
	public UserBean getEmployee() {
		return employee;
	}
	public void setEmployee(UserBean employee) {
		this.employee = employee;
	}
		
}
