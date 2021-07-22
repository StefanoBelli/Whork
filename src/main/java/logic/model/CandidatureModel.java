package logic.model;

import java.util.Date;

public final class CandidatureModel {
	
	private int offerId;
	private String jobSeekerCF;
	private Date candidatureDate;
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
	
	
}
