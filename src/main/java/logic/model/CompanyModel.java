package logic.model;

public final class CompanyModel {
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
