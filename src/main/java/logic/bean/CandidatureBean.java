package logic.bean;

import java.io.Serializable;
import java.util.Date;

public final class CandidatureBean implements Serializable {	
	private static final long serialVersionUID = -6778756927236912607L;
	
	private Date candidatureDate;	
	private int offerId;	
	private String jobSeekerCF;
	
	private String jobOccupation;
	private String socialReason;
	private String typeOfContract;
	private String email;
	
	public void setCandidatureDate(Date candidatureDate) {
		this.candidatureDate = candidatureDate;
	}
	
	public void setOfferId(int offerId) {
		this.offerId = offerId;
	}
	
	public String getJobSeekerCF() {
		return jobSeekerCF;
	}
	
	public int getOfferId() {
		return offerId;
	}
	
	public Date getCandidatureDate() {
		return candidatureDate;
	}
	
	public void setJobSeekerCF(String jobSeekerCF) {
		this.jobSeekerCF = jobSeekerCF;
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