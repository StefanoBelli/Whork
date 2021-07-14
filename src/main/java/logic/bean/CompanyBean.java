package logic.bean;

import java.io.Serializable;

public final class CompanyBean implements Serializable {
	private static final long serialVersionUID = 143762902979557477L;
	
	private String cf;
	private String logo;
	private String vat;
	private String socialReason;

	public String getCf() {
		return cf;
	}

	public String getLogo() {
		return logo;
	}

	public String getVat() {
		return vat;
	}

	public String getSocialReason() {
		return socialReason;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public void setVat(String vat) {
		this.vat = vat;
	}

	public void setSocialReason(String socialReason) {
		this.socialReason = socialReason;
	}
}
