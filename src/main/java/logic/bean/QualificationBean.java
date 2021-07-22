package logic.bean;

import java.io.Serializable;

public final class QualificationBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9103951860044466560L;
	private String qualify;

	public void setQualify(String qualify) {
		this.qualify = qualify;
	}
	
	public String getQualify() {
		return qualify;
	}
}
