package logic.model;

import java.util.Date;

public final class ChatLogEntryModel {
	private long logEntryId;
	private String senderEmail;
	private String receiverEmail;
	private String text;
	private Date deliveryRequestDate;
	private Date deliveredDate;

	public long getLogEntryId() {
		return logEntryId;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public Date getDeliveryRequestDate() {
		return deliveryRequestDate;
	}

	public Date getDeliveredDate() {
		return deliveredDate;
	}

	public String getText() {
		return text;
	}

	public void setLogEntryId(long logEntryId) {
		this.logEntryId = logEntryId;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public void setDeliveryRequestDate(Date deliveryRequestDate) {
		this.deliveryRequestDate = deliveryRequestDate;
	}

	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public void setText(String text) {
		this.text = text;
	}
}
