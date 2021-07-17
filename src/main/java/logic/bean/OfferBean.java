package logic.bean;

import java.io.Serializable;
import java.util.Date;

public final class OfferBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 145847504079594489L;
	private Integer id;
	private String description;
	private String companyHeadQuarterAddress;
	private String employeeMail;
	private String companyVat;
	private String salary;
	private String photo;
	private Boolean otherTime;
	private String workShift;
	private String jobPosition;
	private String qualification;
	private String typeOfContract;
	private Boolean yearSalary;
	private String offerName;
	private Integer numberOfCandidatures;
	private Date data;
	private Integer clickNumber;
	private String note;
	private String numberSaved;
	private Boolean checked;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Boolean getYearSalary() {
		return yearSalary;
	}

	public void setYearSalary(Boolean yearSalary) {
		this.yearSalary = yearSalary;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date date) {
		this.data = date;
	}

	public String getEmployeeMail() {
		return employeeMail;
	}

	public void setEmployeeMail(String employeeMail) {
		this.employeeMail = employeeMail;
	}

	public Integer getClickNumber() {
		return clickNumber;
	}

	public void setClickNumber(Integer clickNumber) {
		this.clickNumber = clickNumber;
	}

	public String getNumberSaved() {
		return numberSaved;
	}

	public void setNumberSaved(String numberSaved) {
		this.numberSaved = numberSaved;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getNumberOfCandidatures() {
		return numberOfCandidatures;
	}

	public void setNumberOfCandidatures(Integer numberOfCandidatures) {
		this.numberOfCandidatures = numberOfCandidatures;
	}

	public String getCompanyHeadQuarterAddress() {
		return companyHeadQuarterAddress;
	}

	public void setCompanyHeadQuarterAddress(String companyHeadQuarterAddress) {
		this.companyHeadQuarterAddress = companyHeadQuarterAddress;
	}

	public String getCompanyVat() {
		return companyVat;
	}

	public void setCompanyVat(String companyVat) {
		this.companyVat = companyVat;
	}

	public Boolean getOtherTime() {
		return otherTime;
	}

	public void setOtherTime(Boolean otherTime) {
		this.otherTime = otherTime;
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

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	public void setTypeOfContract(String typeOfContract) {
		this.typeOfContract = typeOfContract;
	}
	
	public String getWorkShit() {
		return workShift;
	}

	public void setWorkShit(String workShift) {
		this.workShift = workShift;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	
	
	
		
}
