package logic.model;

import java.util.Date;

public final class CandidatureModel {
	
	private UserModel jobSeeker;
	private OfferModel offer;
	private Date candidatureDate;
	
	public OfferModel getOffer() {
		return offer;
	}
	
	public void setOffer(OfferModel offer) {
		this.offer = offer;
	}
	
	public UserModel getJobSeeker() {
		return jobSeeker;
	}
	
	public void setJobSeeker(UserModel jobSeeker) {
		this.jobSeeker = jobSeeker;
	}
	
	public Date getCandidatureDate() {
		return candidatureDate;
	}
	
	public void setCandidatureDate(Date candidatureDate) {
		this.candidatureDate = candidatureDate;
	}

	
	
}