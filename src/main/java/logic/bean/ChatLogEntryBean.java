package logic.bean;

import java.io.Serializable;

public final class ChatLogEntryBean implements Serializable {	
	private static final long serialVersionUID = -7974130019846853561L;
	
	private long logEntryId;
	private String senderEmail;
	private String receiverEmail;
	private String text;
	private long deliveryRequestTime;

	public long getLogEntryId() {
		return logEntryId;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public long getDeliveryRequestTime() {
		return deliveryRequestTime;
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

	public void setDeliveryRequestTime(long deliveryRequestTime) {
		this.deliveryRequestTime = deliveryRequestTime;
	}

	public void setText(String text) {
		this.text = text;
	}	
}
