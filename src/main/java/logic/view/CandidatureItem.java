package logic.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.controller.CandidatureController;
import logic.exception.InternalException;
import logic.graphicscontroller.LoginHandler;
import logic.util.GraphicsUtil;
import logic.util.Util;

public class CandidatureItem {
	private final double MAX_WIDTH = 50;
	private HBox itemBox;
	private ImageView imgView;
	private Text nameCompanyField;
	private Text applicationDateField;
	private Text contractField;
	private Text jobPositionField;
	private Text emailField;
	private Button deleteBtn;
	
	private CandidatureBean candidatureBean;
	private UserBean user;

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
		candidatureBean = candidature;
		user = LoginHandler.getSessionUser();
		
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
		
		setListeners();
	}

	private void setListeners() {
		deleteBtn.setOnMouseClicked(new HandleDeleteRequest());
	}

	public Node getBox() {
		deleteBtn.setFont(GraphicsUtil.getBoldFont());

		itemBox.getChildren().add(imgView);
		itemBox.getChildren().add(nameCompanyField);
		itemBox.getChildren().add(applicationDateField);
		itemBox.getChildren().add(contractField);
		itemBox.getChildren().add(jobPositionField);
		itemBox.getChildren().add(emailField);
		itemBox.getChildren().add(deleteBtn);

		itemBox.setSpacing(20);		
		itemBox.setAlignment(Pos.CENTER);
		itemBox.setPadding(new Insets(10, 10, 10, 10));

		return itemBox;
	}
	
	private final class HandleDeleteRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			try {
				CandidatureController.deleteCandidature(user, candidatureBean);
			} catch (InternalException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			}
		}
	}
}

