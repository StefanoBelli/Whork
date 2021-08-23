package logic.view;

import java.util.Date;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import logic.bean.ChatLogEntryBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.graphicscontroller.AccountCompanyViewController;
import logic.util.GraphicsUtil;
import logic.util.Util;

/**
 * @author Magliari Elio
 */

public final class ChatItem {
	private static final int MAX_WIDTH = 50;

	private HBox itemBox;
	private ImageView imgView;
	private Text emailText;
	private Text messageText;
	private Text dateText;
	private Button replyBtn;

	private String remoteEmail;

	private void init() {
		itemBox = new HBox();
		imgView = new ImageView();
		emailText = new Text();
		messageText = new Text();
		dateText = new Text();
		replyBtn = new Button("Reply");
	}

	public void setInfo(ChatLogEntryBean chat, String email) {
		final String dflRoot = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_DFL_ROOT);
		final String usrData = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA);

		String nameChat = (!chat.getSenderEmail().equals(email)) ? chat.getSenderEmail() : chat.getReceiverEmail();
	 	String date = new Date(chat.getDeliveryRequestTime()).toString();
	 	UserBean userPicture = null;
		 
		remoteEmail = nameChat;
		
	 	try {
			userPicture = AccountController.getPictureForMessage(nameChat);
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}	                           	 		
 		
	 	String picture = (userPicture != null) ? userPicture.getPhoto() : null;

	 	init();
		
		imgView.setFitWidth(MAX_WIDTH);
		imgView.setPreserveRatio(true);

		StringBuilder pathBuilder = new StringBuilder("file:");
		
		if(picture == null)
			imgView.setImage(new Image(pathBuilder.append(dflRoot).append("/avatar2.png").toString()));
		else
			imgView.setImage(
					new Image(pathBuilder.append(usrData).append("/").append(picture).toString()));
		
		emailText.setText(nameChat);
		emailText.setFont(GraphicsUtil.getBoldFont());
		
		messageText.setText(chat.getText());

		dateText.setText(date.substring(0, date.length()-13));

		setListeners();
	}

	private void setListeners() {
		replyBtn.setOnMouseClicked(
				new AccountCompanyViewController.HandleChatRequest(remoteEmail));
	}

	public Node getBox() {
		itemBox.getChildren().add(imgView);
		itemBox.getChildren().add(emailText);
		itemBox.getChildren().add(messageText);
		itemBox.getChildren().add(dateText);
		itemBox.getChildren().add(replyBtn);

		itemBox.setSpacing(20);		
		itemBox.setAlignment(Pos.CENTER);

		return itemBox;
	}
}
