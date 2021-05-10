package logic.model;

import java.util.Date;

public final class ChatLogEntryModel {
	private int scopeId;
	private String message;
	private Date dateSent;
	private Date dateDelivered;
	private UserModel sender;
	private UserModel receiver;

	public int getScopeId() {
		return scopeId;
	}

	public String getMessage() {
		return message;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public Date getDateDelivered() {
		return dateDelivered;
	}

	public UserModel getSender() {
		return sender;
	}

	public UserModel getReceiver() {
		return receiver;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public void setDateDelivered(Date dateDelivered) {
		this.dateDelivered = dateDelivered;
	}

	public void setSender(UserModel sender) {
		this.sender = sender;
	}

	public void setReceiver(UserModel receiver) {
		this.receiver = receiver;
	}
}
