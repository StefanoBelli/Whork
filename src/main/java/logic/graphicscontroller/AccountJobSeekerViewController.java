package logic.graphicscontroller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import logic.bean.CandidatureBean;
import logic.bean.UserBean;
import logic.factory.DialogFactory;
import logic.util.GraphicsUtil;
import logic.util.Util;
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
	private ListView<CandidatureBean> listCandidature;
	
	private UserBean user;
	
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
		listCandidature = (ListView<CandidatureBean>) n[18];

		user = LoginHandler.getSessionUser();
		
		setDescription();
		setSocial();
		setPersonal();
		settingTextField();
		
		if(user.getBiography() == null) bioField.setText("Insert here your bio");
		else bioField.setText(user.getBiography());

		setListeners();
	}
	
	public void setListeners() {
		homeBtn.setOnMouseClicked(new HandleHomeRequest());
		chatBtn.setOnMouseClicked(new HandleChatRequest());
		logoutBtn.setOnMouseClicked(new HandleLogoutRequest());
	}
	
	public void setDescription() {
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
		builder.append(user.getComune().getNome())
				.append(", ")
				.append(user.getComune().getProvincia().getSigla())
				.append(", ")
				.append(user.getComune().getCap());
		locationLabel.setText(builder.toString());
	}
	
	public void setSocial() {
		if(user.getWebsite() == null) user.setWebsite("https://whork.it");
		if(user.getTwitter() == null) user.setTwitter(WHORK);
		if(user.getFacebook() == null) user.setFacebook(WHORK);
		if(user.getInstagram() == null) user.setInstagram(WHORK);
		
		websiteField.setText(user.getWebsite());
		twitterField.setText(user.getTwitter());
		instaField.setText(user.getInstagram());
		facebookField.setText(user.getFacebook());
	}
	
	public void setPersonal() {
		nameField.setText(user.getName());
		surnameField.setText(user.getSurname());
		emailField.setText(user.getSurname());  // insert email
		phoneField.setText(user.getPhoneNumber());
		fiscalCodeField.setText(user.getCf());
		addressField.setText(user.getHomeAddress());
	}
	
	public void settingTextField() {
		nameField.setEditable(false);
		surnameField.setEditable(false);
		emailField.setEditable(false);
		phoneField.setEditable(false);
		fiscalCodeField.setEditable(false);
		addressField.setEditable(false);
		websiteField.setEditable(false);
		twitterField.setEditable(false);
		instaField.setEditable(false);
		facebookField.setEditable(false);
		bioField.setEditable(true);
	}
	
	@Override
	public void update() {
		//no need to update anything
	}
	
	private final class HandleHomeRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			viewStack.pop();
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
}
