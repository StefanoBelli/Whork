package logic.bean;

import java.io.Serializable;
import java.util.Date;

public final class CandidatureBean implements Serializable {	
	private static final long serialVersionUID = -6778756927236912607L;
	
	private Date candidatureDate;	
	private OfferBean offer;
	private UserBean jobSeeker;
	
	
	public void setCandidatureDate(Date candidatureDate) {
		this.candidatureDate = candidatureDate;
	}
	
	
	public Date getCandidatureDate() {
		return candidatureDate;
	}

	public OfferBean getOffer() {
		return offer;
	}


	public UserBean getJobSeeker() {
		return jobSeeker;
	}
	
	
	public void setOffer(OfferBean offer) {
		this.offer = offer;
	}


	public void setJobSeeker(UserBean jobSeeker) {
		this.jobSeeker = jobSeeker;
	}

	
	
	
}