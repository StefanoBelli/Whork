package logic.bean;

import java.io.Serializable;
import java.util.Date;

public final class CandidatureBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6778756927236912607L;
	
	private Date candidatureDate;
	
	private int offerId;
	
	private String jobSeekerCF;
	
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
	
}
