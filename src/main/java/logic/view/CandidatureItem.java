package logic.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import logic.bean.CandidatureBean;
import logic.controller.CandidatureController;
import logic.exception.InternalException;
import logic.util.GraphicsUtil;
import logic.util.Util;

public class CandidatureItem {
	private final double MAX_WIDTH = 50;
	HBox itemBox;
	ImageView imgView;
	Text nameCompanyField;
	Text applicationDateField;
	Text contractField;
	Text jobPositionField;
	Text emailField;
	Button deleteBtn;
	
	private void init() {
		itemBox = new HBox();
		imgView = new ImageView();
		nameCompanyField = new Text();
		applicationDateField = new Text();
		contractField = new Text();
		jobPositionField = new Text();
		emailField = new Text();
		deleteBtn = new Button("Delete");
	}

	public void setInfo(CandidatureBean candidature) throws InternalException {
		final String dflRoot = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_DFL_ROOT);
		
		init();
		
		imgView.setFitWidth(MAX_WIDTH);
		imgView.setPreserveRatio(true);

		StringBuilder pathBuilder = new StringBuilder("file:");
		
		imgView.setImage(
					new Image(pathBuilder.append(dflRoot).append("/avatar1.png").toString()));
		
		
		nameCompanyField.setText(candidature.getOffer().getCompany().getSocialReason());
		nameCompanyField.setFont(GraphicsUtil.getBoldFont());
		
		applicationDateField.setText(candidature.getCandidatureDate().toString());
		
		contractField.setText(candidature.getOffer().getTypeOfContract().getContract());
		
		jobPositionField.setText(candidature.getOffer().getJobPosition().getPosition());
		
		emailField.setText(CandidatureController.getEmployeeEmailByCf(candidature.getOffer().getEmployee()));
		
		setListeners(candidature);
	}

	private void setListeners(CandidatureBean candidature) {
		//deleteBtn.setOnMouseClicked(new AccountJobSeekerViewController.HandleHomeRequest());
	}

	public Node getBox() {
		itemBox.getChildren().add(imgView);
		itemBox.getChildren().add(nameCompanyField);
		itemBox.getChildren().add(applicationDateField);
		itemBox.getChildren().add(contractField);
		itemBox.getChildren().add(jobPositionField);
		itemBox.getChildren().add(emailField);
		itemBox.getChildren().add(deleteBtn);
		
		itemBox.setSpacing(20);		
		
		return itemBox;
	}
}

