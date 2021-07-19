package logic.bean;

import java.io.Serializable;

public final class JobCategoryBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8182575710064139514L;
	private String category;

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
}
