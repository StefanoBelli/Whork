package logic.bean;

import java.io.Serializable;

public final class TypeOfContractBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5194410109944938480L;
	private String contract;

	public void setContract(String contract) {
		this.contract = contract;
	}
	
	public String getContract() {
		return contract;
	}
}
