package logic.bean;

import java.io.Serializable;
import java.io.File;
import logic.exception.SyntaxException;

public class CompanyBean implements Serializable {
	private static final long serialVersionUID = 143762902979557477L;
	
	private String vat;
	private String socialReason;
	private String cf;
	private File logo;

	public String getVat() {
		return vat;
	}

	public String getSocialReason() {
		return socialReason;
	}

	public String getCf() {
		return cf;
	}

	public File getLogo() {
		return logo;
	}

	public void setVat(String vat) 
			throws SyntaxException {
		if(vat.length() != 11) {
			throw new SyntaxException("VAT number length must be 11 chars");
		}

		this.vat = vat;
	}

	public void setSocialReason(String socialReason) 
			throws SyntaxException {
		if(socialReason.length() > 45) {
			throw new SyntaxException("Social reason length cannot be greater than 45");
		}

		this.socialReason = socialReason;
	}

	public void setCf(String cf) 
			throws SyntaxException {
		if (cf.length() != 16) {
			throw new SyntaxException("CF code length must be 16 chars");
		}

		this.cf = cf;
	}

	public void setLogo(File logo) {
		this.logo = logo;
	}
}
