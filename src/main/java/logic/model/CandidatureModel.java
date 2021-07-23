package logic.model;

import java.util.Date;

public final class CandidatureModel {
	
	private int offerId;
	private String jobSeekerCF;
	private Date candidatureDate;
	
	private String jobOccupation;
	private String socialReason;
	private String typeOfContract;
	private String email;
	
	public int getOfferId() {
		return offerId;
	}
	
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	
	public String getJobSeekerCF() {
		return jobSeekerCF;
	}
	
	public void setJobSeekerCF(String jobSeekerCF) {
		this.jobSeekerCF = jobSeekerCF;
	}
	
	public Date getCandidatureDate() {
		return candidatureDate;
	}
	
	public void setCandidatureDate(Date candidatureDate) {
		this.candidatureDate = candidatureDate;
	}

	public String getJobOccupation() {
		return jobOccupation;
	}

	public void setJobOccupation(String jobOccupation) {
		this.jobOccupation = jobOccupation;
	}

	public String getSocialReason() {
		return socialReason;
	}

	public void setSocialReason(String socialReason) {
		this.socialReason = socialReason;
	}

	public String getTypeOfContract() {
		return typeOfContract;
	}

	public void setTypeOfContract(String typeOfContract) {
		this.typeOfContract = typeOfContract;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}