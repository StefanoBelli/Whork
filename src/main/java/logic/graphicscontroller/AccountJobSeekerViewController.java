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
	private TextField nameField;
	private TextField surnameField;
	private TextField emailField;
	private TextField phoneField;
	private TextField fiscalCodeField;
	private TextField addressField;
	private ImageView imgView;
	private Label nameLabel;
	private Label statusLabel;
	private Label locationLabel;
	private TextField websiteField;
	private TextField twitterField;
	private TextField instaField;
	private TextField facebookField;
	private TextField bioField;
	private Button editSocialBtn;
	private Button submitSocialBtn;
	private Button cancelSocialBtn;
	private Button editPersonalBtn;
	private Label oldPasswordLabel;
	private Label newPasswordLabel;
	private Label confirmPasswordLabel;
	private TextField oldPasswordField;
	private TextField newPasswordField;
	private TextField confirmPasswordField;
	private Button changePasswordBtn;
	private Button submitPersonalBtn;
	private Button cancelPersonalBtn;
	private Button editBioBtn;
	private Button submitBioBtn;
	private Button cancelBioBtn;
	private ListView<CandidatureBean> listCandidatureView;
	
	private List<CandidatureBean> list;
	
	private Edit socialInstance;
	private Personal personalInstance;
	private Edit bioInstance;

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
		oldPasswordField = (TextField) n[26];
		newPasswordField = (TextField) n[27];
		confirmPasswordField = (TextField) n[28];
		oldPasswordLabel = (Label) n[29];
		newPasswordLabel = (Label) n[30];
		confirmPasswordLabel = (Label) n[31];
		editBioBtn = (Button) n[32];
		submitBioBtn = (Button) n[33];
		cancelBioBtn = (Button) n[34];

		user = LoginHandler.getSessionUser();
		try {
			email = AccountController.getEmailJobSeekerByCF(user);
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}

		Factory factory = new Factory();

		socialInstance = factory.createElement("Social");
		personalInstance = (Personal) factory.createElement("Personal");
		bioInstance = factory.createElement("Bio");

		setDescription();
		setSocial();
		setPersonal();
		settingTextField();
		settingButton();
		setCandidature();
		setBio();		

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
		changePasswordBtn.setOnMouseClicked(new HandleChangePasswordRequest());
		submitPersonalBtn.setOnMouseClicked(new HandleSubmitPersonalRequest());
		cancelPersonalBtn.setOnMouseClicked(new HandleCancelPersonalRequest());
		
		editBioBtn.setOnMouseClicked(new HandleEditBioRequest());
		submitBioBtn.setOnMouseClicked(new HandleSubmitBioRequest());
		cancelBioBtn.setOnMouseClicked(new HandleCancelBioRequest());
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
		oldPasswordField.setText("");
		newPasswordField.setText("");
		confirmPasswordField.setText("");
	}

	private void setBio() {
		if(user.getBiography() == null) bioField.setText("Insert here your bio");
		else bioField.setText(user.getBiography());
	}
	private void settingTextField() {
		personalInstance.text();
		fiscalCodeField.setEditable(false);
		socialInstance.text(false);
		bioInstance.text(false);
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
		socialInstance.button(false);
		personalInstance.button(false);
		bioInstance.button(false);
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
			socialInstance.button(true);
			socialInstance.text(true);
		}
	}

	private final class HandleSubmitSocialRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			
			if(websiteField.getText() == "" ||
				twitterField.getText() == "" ||
				facebookField.getText() == "" ||
				instaField.getText() == "") {
				
				showErrorDialogEmptyField("Social data");
				socialInstance.text(false);
				socialInstance.button(false);
				return;
			}
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
			socialInstance.text(false);
			socialInstance.button(false);
		}
	}

	private final class HandleCancelSocialRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			socialInstance.button(false);
			socialInstance.text(false);
			setSocial();			
		}
	}
	
	private final class HandleEditPersonalRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			personalInstance.button(true);
			personalInstance.text(true);
		}
	}
	
	private final class HandleChangePasswordRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			personalInstance.button(true);
			personalInstance.text(false);
		}
	}
	
	private final class HandleSubmitPersonalRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			personalInstance.button(false);
			
			if(!oldPasswordField.isVisible()) {
				if(nameField.getText() == "" ||
					surnameField.getText() == "" ||
					phoneField.getText() == "" ||
					addressField.getText() == "") {
						showErrorDialogEmptyField("Personal data");
						personalInstance.text();
						return;
				}
				
				user.setName(nameField.getText());
				user.setSurname(surnameField.getText());
				user.setPhoneNumber(phoneField.getText());
				user.setHomeAddress(addressField.getText());
				personalInstance.text();

				try {
					AccountController.editAccountController("JobSeekerInfoAccount", user, BeanFactory.buildUserAuthBean(emailField.getText(), ""), null);
					LoginHandler.setSessionUser(user);
				} catch (DataAccessException | InternalException | InvalidPasswordException | DataLogicException e) {
					Util.exceptionLog(e);
					GraphicsUtil.showExceptionStage(e);
				}
			} else {
				personalInstance.text();
				if(oldPasswordField.getText().isBlank()||
					newPasswordField.getText().isBlank() ||
					confirmPasswordField.getText().isBlank()) {

						showErrorDialogEmptyField("Password data");
							
						oldPasswordField.setText("");
						newPasswordField.setText("");
						confirmPasswordField.setText("");
						return;
				}
				
				if(newPasswordField.getText().equals(confirmPasswordField.getText())) {
					try {
						AccountController.editAccountController("ChangePasswordAccount", user, BeanFactory.buildUserAuthBean(email, oldPasswordField.getText()),
								newPasswordField.getText());
					} catch (DataAccessException | InternalException | DataLogicException e) {
						Util.exceptionLog(e);
						GraphicsUtil.showExceptionStage(e);
					} catch (InvalidPasswordException e) {
						showNonEqualityPasswordErrorDialog("Try to use the old password");
					}
				} else {
					showNonEqualityPasswordErrorDialog("Try writing the same password in both fields");
				}
				
				oldPasswordField.setText("");
				newPasswordField.setText("");
				confirmPasswordField.setText("");
			}
		}
		
		private void showNonEqualityPasswordErrorDialog(String msg) {
			DialogFactory.error(
					"Passwords are not the same!", 
					"Unable to change password", 
					msg).showAndWait();
		}
	}
	
	private void showErrorDialogEmptyField(String argWhat) {
		DialogFactory.error(
				argWhat, 
				"Unable to change data", 
				"You cannot leave empty field").showAndWait();
	}

	private final class HandleCancelPersonalRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			personalInstance.button(false);
			personalInstance.text();
			setPersonal();
		}
	}

	private final class HandleEditBioRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			bioInstance.button(true);
			bioInstance.text(true);
		}
	}

	private final class HandleSubmitBioRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			bioInstance.button(false);
			bioInstance.text(false);
			
			if(bioField.getText() == "") {
				showErrorDialogEmptyField("Bio data");
				return;
			}
			
			user.setBiography(bioField.getText());
			
			try {
				AccountController.editAccountController("JobSeekerBiography", user, BeanFactory.buildUserAuthBean(email, ""), null);
			} catch (DataAccessException | InternalException | InvalidPasswordException | DataLogicException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			}
		}
	}

	private final class HandleCancelBioRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			bioInstance.button(false);
			bioInstance.text(false);
			setBio();
		}
	}

	private static final class HandleChatRequest implements EventHandler<MouseEvent> {

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

	private interface Edit {
		void button(boolean variable);
		void text(boolean variable);
	}

	private final class Social implements Edit {
		
		@Override
		public void button(boolean variable) {
			editSocialBtn.setVisible(!variable);
			submitSocialBtn.setVisible(variable);
			cancelSocialBtn.setVisible(variable);
		}
		
		@Override
		public void text(boolean variable) {
			websiteField.setEditable(variable);
			twitterField.setEditable(variable);
			instaField.setEditable(variable);
			facebookField.setEditable(variable);
		}
		
	}

	private final class Personal implements Edit {

		@Override
		public void button(boolean variable) {
			if(variable) {
				editPersonalBtn.setVisible(false);
				changePasswordBtn.setVisible(false);
			} else {
				editPersonalBtn.setVisible(true);
				changePasswordBtn.setVisible(true);
			}
			submitPersonalBtn.setVisible(variable);
			cancelPersonalBtn.setVisible(variable);
		}
		
		@Override
		public void text(boolean variable) {
			nameField.setEditable(variable);
			surnameField.setEditable(variable);
			emailField.setEditable(variable);
			phoneField.setEditable(variable);
			addressField.setEditable(variable);
			if(variable) {
				oldPasswordField.setVisible(false);
				newPasswordField.setVisible(false);
				confirmPasswordField.setVisible(false);
				oldPasswordLabel.setVisible(false);
				newPasswordLabel.setVisible(false);
				confirmPasswordLabel.setVisible(false);
			} else {
				oldPasswordField.setVisible(true);
				newPasswordField.setVisible(true);
				confirmPasswordField.setVisible(true);
				oldPasswordLabel.setVisible(true);
				newPasswordLabel.setVisible(true);
				confirmPasswordLabel.setVisible(true);
			}
		}
		
		public void text() {
			nameField.setEditable(false);
			surnameField.setEditable(false);
			emailField.setEditable(false);
			phoneField.setEditable(false);
			addressField.setEditable(false);
			oldPasswordField.setVisible(false);
			newPasswordField.setVisible(false);
			confirmPasswordField.setVisible(false);
			oldPasswordLabel.setVisible(false);
			newPasswordLabel.setVisible(false);
			confirmPasswordLabel.setVisible(false);
		}

	}

	private final class Bio implements Edit {
		
		@Override
		public void button(boolean variable) {
			editBioBtn.setVisible(!variable);
			submitBioBtn.setVisible(variable);
			cancelBioBtn.setVisible(variable);
		}
		
		@Override
		public void text(boolean variable) {
			bioField.setEditable(variable);
		}

	}

	private final class Factory {

		private Edit createElement(String element) {
			switch(element) {
				case("Social"): 
					return createNewConcreteProductSocial();
				case("Personal"):
					return createNewConcreteProductPersonal();
				case("Bio"):
					return createNewConcreteProductBio();
				default: return null;
			}
		}

		private Edit createNewConcreteProductSocial() {
			return new Social();
		}

		private Edit createNewConcreteProductPersonal() {
			return new Personal();
		}

		private Edit createNewConcreteProductBio() {
			return new Bio();
		}
	}
}
