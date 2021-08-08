package logic.graphicscontroller;

import java.text.SimpleDateFormat;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.bean.ChatLogEntryBean;
import logic.bean.UserBean;
import logic.controller.ChatController;
import logic.controller.OfflineChatController;
import logic.exception.InternalException;
import logic.util.GraphicsUtil;
import logic.view.ControllableView;
import logic.view.ViewStack;

/**
 * @author Stefano Belli
 */
public final class ChatViewController extends GraphicsController {

	private Text chattingWithText;
	private TextArea chatTextArea;
	private TextArea sndTextArea;
	private Button sndButton;

	private String fromEmail;
	private String toEmail;

	public ChatViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		if(ChatController.getInstance().isOnlineService()) {
			GraphicsUtil.showExceptionStage(new IllegalArgumentException());
		}

		Node[] n = view.getNodes();
		
		chattingWithText = ((Text) n[0]);
		chatTextArea = ((TextArea) n[1]);
		sndTextArea = ((TextArea) n[2]);
		sndButton = ((Button) n[3]);

		sndButton.setOnMouseClicked(new HandleSendRequest());
	}

	@Override
	public void update() {
		//future implementation
	}

	public void startPullingPushingChatLogs(String remoteEmail) {
		toEmail = remoteEmail;
		chattingWithText.setText(
			new StringBuilder(chattingWithText.getText())
				.append(remoteEmail).toString());
		chatTextArea.setDisable(false);
		sndTextArea.setDisable(false);
		sndButton.setDisable(false);
		
		UserBean currentUser = LoginHandler.getSessionUser();
		try {
			if (currentUser.isEmployee()) {
				fromEmail = OfflineChatController.getEmployeeEmail(currentUser);
			} else {
				fromEmail = OfflineChatController.getJobSeekerEmail(currentUser);
			}
		} catch (InternalException e) {
			GraphicsUtil.showExceptionStage(e);
		}

		pullMessages();
	}

	private void appendEntryToChatLog(ChatLogEntryBean entry) {
		StringBuilder messageBuilder = new StringBuilder();
		if (entry.getSenderEmail().equals(fromEmail)) {
			messageBuilder.append("Me");
		} else {
			messageBuilder.append("Remote");
		}

		messageBuilder.append(", [on: ").append(fmtDate(entry.getDeliveryRequestTime()))
			.append("]: ").append(entry.getText()).append("\n");

		chatTextArea.appendText(messageBuilder.toString());
	}

	private void pullMessages() {
		try {
			for(final ChatLogEntryBean entry : OfflineChatController.retrieveLastMessages(fromEmail, toEmail)) {
				appendEntryToChatLog(entry);
			}
		} catch(InternalException e) {
			GraphicsUtil.showExceptionStage(e);
		}
	}

	private static String fmtDate(long ts) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return simpleDateFormat.format(ts);
	}

	private final class HandleSendRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			final String text = sndTextArea.getText();
			if(!text.isBlank()) {
				try {
					appendEntryToChatLog(
						OfflineChatController.pushMessage(fromEmail, toEmail, text));
				} catch (InternalException e) {
					GraphicsUtil.showExceptionStage(e);
				}
				sndTextArea.clear();
			}
		}
	}
}
