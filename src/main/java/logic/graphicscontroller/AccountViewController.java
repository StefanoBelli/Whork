package logic.graphicscontroller;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import logic.bean.OfferBean;
import logic.bean.UserBean;
import logic.controller.OfflineChatController;
import logic.exception.InternalException;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ChatView;
import logic.view.ControllableView;
import logic.view.HomeView;
import logic.view.ViewStack;

public final class AccountViewController extends GraphicsController {
	
	private Button homeBtn;
	private Button chatBtn;
	private Button logoutBtn;
	private TextField nameField;
	private TextField surnameField;
	private TextField emailField;
	private TextField phoneField;
	private TextField fiscalCodeField;
	private TextField addressField;
	private Label nameLabel;
	private Label statusLabel;
	private Label locationLabel;
	private TextField websiteField;
	private TextField twitterField;
	private TextField instaField;
	private TextField facebookField;
	private TextField bioField;
	
	private StringBuilder builder;
	private UserBean user;
	
	public AccountViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

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
		
		user = LoginHandler.getSessionUser();
		
		setDescription();
		setSocial();
		setPersonal();
		
		if(user.getBiography() == null) bioField.setText("Insert here your bio");
		else bioField.setText(user.getBiography());

		setListeners();
	}
	
	public void setListeners() {
		homeBtn.setOnMouseClicked(new HandleHomeRequest());
		chatBtn.setOnMouseClicked(new HandleHomeRequest());
		logoutBtn.setOnMouseClicked(new HandleLogoutRequest());
	}
	
	public void setDescription() {
		builder = new StringBuilder();
		builder.append(user.getName())
		.append(" ")
		.append(user.getSurname());
		nameLabel.setText(builder.toString());
		statusLabel.setText(user.getEmploymentStatus().getStatus());
		builder.append(user.getComune().getNome())
				.append(", ")
				.append(user.getComune().getProvincia().getSigla())
				.append(", ")
				.append(user.getComune().getCap());
		locationLabel.setText(builder.toString());
	}
	
	public void setSocial() {
		if(user.getWebsite() == null) user.setWebsite("https://whork.it");
		if(user.getTwitter() == null) user.setTwitter("whork");
		if(user.getFacebook() == null) user.setFacebook("whork");
		if(user.getInstagram() == null) user.setInstagram("whork");
		
		websiteField.setText(user.getWebsite());
		twitterField.setText(user.getTwitter());
		instaField.setText(user.getInstagram());
		facebookField.setText(user.getFacebook());
	}
	
	public void setPersonal() {
		builder = new StringBuilder();
		nameField.setText(user.getName());
		surnameField.setText(user.getSurname());
		emailField.setText(user.getSurname());  // insert email
		phoneField.setText(user.getPhoneNumber());
		fiscalCodeField.setText(user.getCf());
		addressField.setText(user.getHomeAddress());
	}
	
	@Override
	public void update() {
		//no need to update anything
	}
	
	private final class HandleHomeRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			viewStack.push(new HomeView(viewStack));
		}
	}
	
	public static final class HandleChatRequest implements EventHandler<MouseEvent> {
		private final String remoteEmail;

		public HandleChatRequest(OfferBean offerBean) {
			String tmpEmail = null;
			try {
				tmpEmail = OfflineChatController.getEmployeeEmail(offerBean.getEmployee());
			} catch(InternalException e) {
				GraphicsUtil.showExceptionStage(e);
			}

			this.remoteEmail = tmpEmail;
		}

		@Override
		public void handle(MouseEvent event) {
			GraphicsUtil.showAndWaitWindow(ChatView.class, "setRemoteEmail", remoteEmail);
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
}
