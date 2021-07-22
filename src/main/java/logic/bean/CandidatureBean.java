package logic.bean;

import java.io.Serializable;

public final class CandidatureBean implements Serializable {
	private static final long serialVersionUID = -4315333658093380322L;
	
	private String SocialReason;
	private String CandidatureDate;
	private String TypeOfContract;
	private String JobOccupation;
	private String Email;
	
	public String getSocialReason() {
		return SocialReason;
	}
	public void setSocialReason(String socialReason) {
		SocialReason = socialReason;
	}
	public String getCandidatureDate() {
		return CandidatureDate;
	}
	public void setCandidatureDate(String candidatureDate) {
		CandidatureDate = candidatureDate;
	}
	public String getTypeOfContract() {
		return TypeOfContract;
	}
	public void setTypeOfContract(String typeOfContract) {
		TypeOfContract = typeOfContract;
	}
	public String getJobOccupation() {
		return JobOccupation;
	}
	public void setJobOccupation(String jobOccupation) {
		JobOccupation = jobOccupation;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
}
