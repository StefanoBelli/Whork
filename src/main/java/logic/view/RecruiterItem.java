package logic.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.exception.DataAccessException;
import logic.util.GraphicsUtil;
import logic.util.Util;

public final class RecruiterItem {
	private static final int MAX_WIDTH = 50;

	private HBox itemBox;
	private ImageView imgView;
	private Text nameText;
	private Text emailText;
	private Text fiscalCodeText;
	private Text numberPostText;
	private Text phoneNumberText;

	private void init() {
		itemBox = new HBox();
		imgView = new ImageView();
		nameText = new Text();
		emailText = new Text();
		fiscalCodeText = new Text();
		numberPostText = new Text();
		phoneNumberText = new Text();
	}

	public void setInfo(UserBean userRecruiter, String email) {
		final String dflRoot = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_DFL_ROOT);
		final String usrData = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA);

		init();

		imgView.setFitWidth(MAX_WIDTH);
		imgView.setPreserveRatio(true);

		StringBuilder pathBuilder = new StringBuilder("file:");

		if(userRecruiter.getPhoto() == null)
			imgView.setImage(
					new Image(pathBuilder.append(dflRoot).append("/avatar3.jpg").toString()));
		else
			imgView.setImage(
					new Image(pathBuilder.append(usrData).append("/").append(userRecruiter.getPhoto()).toString()));

		StringBuilder builder = new StringBuilder();
		builder.append(userRecruiter.getName())
				.append(" ")
				.append(userRecruiter.getSurname());

		nameText.setText(builder.toString());
		nameText.setFont(GraphicsUtil.getBoldFont());
		
		emailText.setText(email);
		
		fiscalCodeText.setText(userRecruiter.getCf());

		try {
			numberPostText.setText(Integer.toString(AccountController.getNumberOfferOfAnEmployee(userRecruiter)));
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}
		
		phoneNumberText.setText(userRecruiter.getPhoneNumber());
	}

	public Node getBox() {
		itemBox.getChildren().add(imgView);
		itemBox.getChildren().add(nameText);
		itemBox.getChildren().add(emailText);
		itemBox.getChildren().add(fiscalCodeText);
		itemBox.getChildren().add(numberPostText);
		itemBox.getChildren().add(phoneNumberText);

		itemBox.setSpacing(20);
		itemBox.setAlignment(Pos.CENTER);
		itemBox.setPadding(new Insets(10, 10, 10, 10));

		return itemBox;
	}
}