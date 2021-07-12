package logic.bean;

import java.io.Serializable;

public final class CompanyBean implements Serializable {
	private static final long serialVersionUID = 143762902979557477L;
	
	private String vat;
	private String socialReason;
	private String cf;
	private String logo;

	public String getVat() {
		return vat;
	}

	public String getSocialReason() {
		return socialReason;
	}

	public String getCf() {
		return cf;
	}

	public String getLogo() {
		return logo;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public void setSocialReason(String socialReason) {
		this.socialReason = socialReason;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
