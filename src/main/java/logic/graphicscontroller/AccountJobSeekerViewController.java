package logic.graphicscontroller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.InvalidPasswordException;
import logic.factory.BeanFactory;
import logic.factory.DialogFactory;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.CandidatureItem;
import logic.view.ChatView;
import logic.view.ControllableView;
import logic.view.ViewStack;

public final class AccountJobSeekerViewController extends GraphicsController {
	
	private static final double MAX_WIDTH = 200;
	private static final String WHORK = "whork";

	private Button homeBtn;
	private Button chatBtn;
	private Button logoutBtn;
	private static TextField nameField;
	private static TextField surnameField;
	private static TextField emailField;
	private static TextField phoneField;
	private static TextField fiscalCodeField;
	private static TextField addressField;
	private ImageView imgView;
	private Label nameLabel;
	private Label statusLabel;
	private Label locationLabel;
	private static TextField websiteField;
	private static TextField twitterField;
	private static TextField instaField;
	private static TextField facebookField;
	private TextField bioField;
	private static Button editSocialBtn;
	private static Button submitSocialBtn;
	private static Button cancelSocialBtn;
	private static Button editPersonalBtn;
	private static Button changePasswordBtn;
	private static Button submitPersonalBtn;
	private static Button cancelPersonalBtn;
	private ListView<CandidatureBean> listCandidatureView;
	
	private List<CandidatureBean> list;
	
	private UserBean user;
	private String email;
	
	public AccountJobSeekerViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setup() {
		Node[] n = view.getNodes();
		homeBtn = (Button) n[0];
		chatBtn = (Button) n[1];
		logoutBtn = (Button) n[2];
		nameField = (TextField) n[3];
		surnameField = (TextField) n[4];
		emailField = (TextField) n[5];
		phoneField = (TextField) n[6];
		fiscalCodeField = (TextField) n[7];
		addressField = (TextField) n[8];
		nameLabel = (Label) n[9];
		statusLabel = (Label) n[10];
		locationLabel = (Label) n[11];
		websiteField = (TextField) n[12];
		twitterField = (TextField) n[13];
		instaField = (TextField) n[14];
		facebookField = (TextField) n[15];
		bioField = (TextField) n[16];
		imgView = (ImageView) n[17];
		listCandidatureView = (ListView<CandidatureBean>) n[18];
		editSocialBtn = (Button) n[19];
		submitSocialBtn = (Button) n[20];
		cancelSocialBtn = (Button) n[21];
		editPersonalBtn = (Button) n[22];
		changePasswordBtn = (Button) n[23];
		submitPersonalBtn = (Button) n[24];
		cancelPersonalBtn = (Button) n[25];

		user = LoginHandler.getSessionUser();
		try {
			email = AccountController.getEmailJobSeekerByCF(user);
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}
		
		setDescription();
		setSocial();
		setPersonal();
		settingTextField();
		settingButton();
		setCandidature();
		
		if(user.getBiography() == null) bioField.setText("Insert here your bio");
		else bioField.setText(user.getBiography());

		setListeners();
	}
	
	private void setListeners() {
		homeBtn.setOnMouseClicked(new HandleHomeRequest());
		chatBtn.setOnMouseClicked(new HandleChatRequest());
		logoutBtn.setOnMouseClicked(new HandleLogoutRequest());
		editSocialBtn.setOnMouseClicked(new HandleEditSocialRequest());
		submitSocialBtn.setOnMouseClicked(new HandleSubmitSocialRequest());
		cancelSocialBtn.setOnMouseClicked(new HandleCancelSocialRequest());
		
		editPersonalBtn.setOnMouseClicked(new HandleEditPersonalRequest());
		changePasswordBtn.setOnMouseClicked(new HandleEditPersonalRequest()); // change password
		submitPersonalBtn.setOnMouseClicked(new HandleSubmitPersonalRequest());
		cancelPersonalBtn.setOnMouseClicked(new HandleCancelPersonalRequest());
	}
	
	private void setDescription() {
		final String usrData = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA);
		final String dflRoot = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_DFL_ROOT);

		imgView.setFitWidth(MAX_WIDTH);
		imgView.setPreserveRatio(true);

		StringBuilder pathBuilder = new StringBuilder("file:");
		
		if(user.getPhoto()!=null) {
			imgView.setImage(
					new Image(pathBuilder.append(usrData).append("/").append(user.getPhoto()).toString()));
		} else {
			imgView.setImage(
					new Image(pathBuilder.append(dflRoot).append("/avatar.png").toString()));
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(user.getName())
				.append(" ")
				.append(user.getSurname());
		nameLabel.setText(builder.toString());
		statusLabel.setText(user.getEmploymentStatus().getStatus());
		builder =  new StringBuilder();
		builder.append(user.getComune().getNome())
				.append(", ")
				.append(user.getComune().getProvincia().getSigla())
				.append(", ")
				.append(user.getComune().getCap());
		locationLabel.setText(builder.toString());
	}

	private void setSocial() {
		if(user.getWebsite() == null) user.setWebsite("https://whork.it");
		if(user.getTwitter() == null) user.setTwitter(WHORK);
		if(user.getFacebook() == null) user.setFacebook(WHORK);
		if(user.getInstagram() == null) user.setInstagram(WHORK);
		
		websiteField.setText(user.getWebsite());
		twitterField.setText(user.getTwitter());
		instaField.setText(user.getInstagram());
		facebookField.setText(user.getFacebook());
	}
	
	private void setPersonal() {
		nameField.setText(user.getName());
		surnameField.setText(user.getSurname());
		emailField.setText(email);
		phoneField.setText(user.getPhoneNumber());
		fiscalCodeField.setText(user.getCf());
		addressField.setText(user.getHomeAddress());
	}
	
	private void settingTextField() {
		Personal.text(false);
		fiscalCodeField.setEditable(false);
		Social.text(false);
		bioField.setEditable(false);
	}

	private void setCandidature() {
		try {
			list = AccountController.getSeekerCandidature(user);
		} catch (InternalException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}

		fillListView(FXCollections.observableArrayList(list));
	}
	
	private void settingButton() {
		Social.button(false);
		Personal.button(false);
	}
	
	@Override
	public void update() {
		//no need to update anything
	}
	
	private void fillListView(ObservableList<CandidatureBean> list) {
		listCandidatureView.setItems(list);
		listCandidatureView.setCellFactory((ListView<CandidatureBean> oUnused) -> new ListCell<CandidatureBean>() {
				@Override
				public void updateItem(CandidatureBean itemBean, boolean empty) {
					super.updateItem(itemBean, empty);
					if (itemBean != null) {
						CandidatureItem newItem = new CandidatureItem();
						try {
							newItem.setInfo(itemBean);
						} catch (InternalException e) {
							Util.exceptionLog(e);
							GraphicsUtil.showExceptionStage(e);
						}
						setGraphic(newItem.getBox());
					}
				}
			}
		);
	}
	
	private final class HandleHomeRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			viewStack.pop();
		}
	}

	private final class HandleEditSocialRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			Social.button(true);
			Social.text(true);
		}
	}

	private final class HandleSubmitSocialRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			Social.text(false);
			user.setWebsite(websiteField.getText());
			user.setTwitter(twitterField.getText());
			user.setFacebook(facebookField.getText());
			user.setInstagram(instaField.getText());
			try {
				AccountController.editAccountController("SocialAccounts", user, null, null);
				LoginHandler.setSessionUser(user);
			} catch (DataAccessException | InternalException | InvalidPasswordException | DataLogicException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			}
			Social.button(false);
		}
	}

	private final class HandleCancelSocialRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			Social.button(false);
			Social.text(false);
			setSocial();			
		}
	}
	
	private final class HandleEditPersonalRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			Personal.button(true);
			Personal.text(true);
		}
	}
	
	private final class HandleSubmitPersonalRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			Personal.text(false);
			user.setName(nameField.getText());
			user.setSurname(surnameField.getText());
			user.setPhoneNumber(phoneField.getText());
			user.setHomeAddress(addressField.getText());

			try {
				AccountController.editAccountController("JobSeekerInfoAccount", user, BeanFactory.buildUserAuthBean(emailField.getText(), ""), null);
				LoginHandler.setSessionUser(user);
			} catch (DataAccessException | InternalException | InvalidPasswordException | DataLogicException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			}

			Personal.button(false);
		}
	}
	
	private final class HandleCancelPersonalRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			Personal.button(false);
			Personal.text(false);
			setPersonal();			
		}
	}

	public static final class HandleChatRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			Optional<String> remoteEmailOpt = 
				DialogFactory
					.input("Chat", "Remote email address", "Type in remote's email address")
					.showAndWait();

			if(remoteEmailOpt.isPresent()) {
				GraphicsUtil.showAndWaitWindow(ChatView.class, "setRemoteEmail", remoteEmailOpt.get());
			}
		}
	}

	private final class HandleLogoutRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			LoginHandler.logout();
			
			try {
				Util.Files.overWriteJsonAuth(null, null);
			} catch(IOException e) {
				Util.exceptionLog(e);
				GraphicsUtil.closeStageByMouseEvent(event);
				GraphicsUtil.showExceptionStage(e);
			}

			viewStack.pop();
		}
	}

	
	private abstract static class Edit {
		static void button(boolean variable) {}
		static void text(boolean variable) {}
	}
	
	private static class Social extends Edit {

		protected static void button(boolean variable) {
			if(variable == true) editSocialBtn.setVisible(false);
			else editSocialBtn.setVisible(true);
			submitSocialBtn.setVisible(variable);
			cancelSocialBtn.setVisible(variable);
		}

		protected static void text(boolean variable) {
			websiteField.setEditable(variable);
			twitterField.setEditable(variable);
			instaField.setEditable(variable);
			facebookField.setEditable(variable);
		}
		
	}

	private static class Personal extends Edit {

		protected static void button(boolean variable) {
			if(variable == true) {
				editPersonalBtn.setVisible(false);
				changePasswordBtn.setVisible(false);
			} else {
				editPersonalBtn.setVisible(true);
				changePasswordBtn.setVisible(true);
			}
			submitPersonalBtn.setVisible(variable);
			cancelPersonalBtn.setVisible(variable);
		}

		protected static void text(boolean variable) {
			nameField.setEditable(variable);
			surnameField.setEditable(variable);
			emailField.setEditable(variable);
			phoneField.setEditable(variable);
			addressField.setEditable(variable);
		}
		
	}
}
