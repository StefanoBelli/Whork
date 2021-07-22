package logic.bean;

import java.io.Serializable;

public final class JobPositionBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2549482060959492118L;
	private String position;

	public void setPosition(String position) {
		this.position = position;
	}
	
	public String getPosition() {
		return position;
	}
}
