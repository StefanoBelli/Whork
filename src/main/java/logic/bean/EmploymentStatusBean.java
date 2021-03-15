package logic.bean;

import java.io.Serializable;

public final class EmploymentStatusBean implements Serializable {
	private static final long serialVersionUID = 8204914069484753112L;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
