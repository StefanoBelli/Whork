package logic.model;

public final class ChatLogEntryModel {
	private long logEntryId;
	private String senderEmail;
	private String receiverEmail;
	private String text;
	private long deliveryRequestTime;

	public String getText() {
		return text;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getLogEntryId() {
		return logEntryId;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public void setDeliveryRequestTime(long deliveryRequestTime) {
		this.deliveryRequestTime = deliveryRequestTime;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setLogEntryId(long logEntryId) {
		this.logEntryId = logEntryId;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public long getDeliveryRequestTime() {
		return deliveryRequestTime;
	}
}
