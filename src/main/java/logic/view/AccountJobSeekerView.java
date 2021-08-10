package logic.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.AccountJobSeekerViewController;
import logic.graphicscontroller.GraphicsController;

public final class AccountJobSeekerView implements ControllableView {
	private static final String HOME = "Home";
	private static final String CHAT = "Chat";
	private static final String LOGOUT = "Logout";
	private static final String WEB = "Website";
	private static final String TWITTER = "Twitter";
	private static final String FACEBOOK = "Facebook";
	private static final String INSTAGRAM = "Instagram";
	private static final String NAME = "Name";
	private static final String SURNAME = "Surname";
	private static final String EMAIL = "Email";
	private static final String PHONE = "Phone";
	private static final String FISCALCODE = "Fiscal Code";
	private static final String ADDRESS = "Address";
	private static final String BIO = "My Biography";
	private static final String EDIT = "Edit";
	private static final String SUBMIT = "Submit";
	private static final String CANCEL = "Cancel";
	
	private static final double WIDTHWINDOW = DefaultWindowSize.WIDTH+400;
	private static final double HEIGHTWINDOW = DefaultWindowSize.HEIGHT+200;

	private static final String COMMON_STYLING =
		"-fx-border-style: solid;-fx-border-width: 1;-fx-border-color: black";

	private Scene scene;

	private Button homeBtn;
	private Button logoutBtn;
	private Button chatBtn;
	private Label nameLabelText;
	private TextField nameField;
	private Label surnameLabelText;
	private TextField surnameField;
	private Label emailLabelText;
	private TextField emailField;
	private Label phoneLabelText;
	private TextField phoneField;
	private Label fiscalCodeLabelText;
	private TextField fiscalCodeField;
	private Label addressLabelText;
	private TextField addressField;
	private ImageView imgView;
	private Label nameLabel;
	private Label statusLabel;
	private Label locationLabel;
	private Label websiteLabel;
	private Label twitterLabel;
	private Label facebookLabel;
	private Label instagramLabel;
	private TextField websiteField;
	private TextField twitterField;
	private TextField instaField;
	private TextField facebookField;
	private Button editSocialBtn;
	private Button submitSocialBtn;
	private Button cancelSocialBtn;
	private Label bioLabelText;
	private TextField bioField;
	private ListView<Object> listCandidatureView;
	
	private GraphicsController controller;

	public AccountJobSeekerView(ViewStack viewStack) {
		controller = new AccountJobSeekerViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		homeBtn = new Button(HOME);
		logoutBtn = new Button(LOGOUT);
		chatBtn = new Button(CHAT);
		
		imgView = new ImageView();
		nameLabel = new Label();
		statusLabel = new Label();
		locationLabel = new Label();
		
		websiteLabel = new Label(WEB);
		twitterLabel = new Label(TWITTER);
		facebookLabel = new Label(FACEBOOK);
		instagramLabel = new Label(INSTAGRAM);
		websiteField = new TextField();
		twitterField = new TextField();
		instaField = new TextField();
		facebookField = new TextField();
		editSocialBtn = new Button(EDIT);
		submitSocialBtn = new Button(SUBMIT);
		cancelSocialBtn = new Button(CANCEL);

		nameLabelText = new Label(NAME);
		surnameLabelText = new Label(SURNAME);
		emailLabelText = new Label(EMAIL);
		phoneLabelText = new Label(PHONE);
		fiscalCodeLabelText = new Label(FISCALCODE);
		addressLabelText = new Label(ADDRESS);
		nameField = new TextField();
		surnameField = new TextField();
		emailField = new TextField();
		phoneField = new TextField();
		fiscalCodeField = new TextField();
		addressField = new TextField();
		
		bioLabelText = new Label(BIO);
		bioField = new TextField();
		
		listCandidatureView = new ListView<>();
		
		controller.setup();
	}

	private void setNodesProps() {
		nameField.setPrefWidth(300);
		nameField.setAlignment(Pos.CENTER);
		surnameField.setPrefWidth(600);
		surnameField.setAlignment(Pos.CENTER);
		emailField.setPrefWidth(600);
		emailField.setAlignment(Pos.CENTER);
		phoneField.setPrefWidth(600);
		phoneField.setAlignment(Pos.CENTER);
		fiscalCodeField.setPrefWidth(600);
		fiscalCodeField.setAlignment(Pos.CENTER);
		addressField.setPrefWidth(600);
		addressField.setAlignment(Pos.CENTER);
		websiteField.setPrefWidth(450);
		websiteField.setAlignment(Pos.CENTER);
		twitterField.setPrefWidth(450);
		twitterField.setAlignment(Pos.CENTER);
		instaField.setPrefWidth(450);
		instaField.setAlignment(Pos.CENTER);
		facebookField.setPrefWidth(450);
		facebookField.setAlignment(Pos.CENTER);
		bioField.setPrefWidth(500);
	}
	
	private void populateScene() {
		VBox vbox = new VBox();
		
		HBox hboxHeader = new HBox(10);
		hboxHeader.getChildren().add(homeBtn);
		hboxHeader.getChildren().add(chatBtn);
		hboxHeader.getChildren().add(logoutBtn);		
		hboxHeader.setPadding(new Insets(10, 0, 0, 10));

		VBox vboxPers = new VBox(5);
		vboxPers.getChildren().add(imgView);
		vboxPers.getChildren().add(nameLabel);
		vboxPers.getChildren().add(statusLabel);
		vboxPers.getChildren().add(locationLabel);
		vboxPers.setAlignment(Pos.CENTER);
		vboxPers.setPrefSize(300, 300);
		vboxPers.setPadding(new Insets(10, 10, 10, 10));
		vboxPers.setStyle(COMMON_STYLING);

		VBox vboxSocial = new VBox();
		vboxSocial.getChildren().add(websiteLabel);
		vboxSocial.getChildren().add(websiteField);
		vboxSocial.getChildren().add(twitterLabel);
		vboxSocial.getChildren().add(twitterField);
		vboxSocial.getChildren().add(facebookLabel);
		vboxSocial.getChildren().add(facebookField);
		vboxSocial.getChildren().add(instagramLabel);
		vboxSocial.getChildren().add(instaField);
		HBox hboxSocialBtn = new HBox(10);
		hboxSocialBtn.getChildren().add(editSocialBtn);
		hboxSocialBtn.getChildren().add(submitSocialBtn);
		hboxSocialBtn.getChildren().add(cancelSocialBtn);
		hboxSocialBtn.setPadding(new Insets(10, 10, 10, 10));
		vboxSocial.getChildren().add(hboxSocialBtn);
		vboxSocial.setPadding(new Insets(10, 10, 10, 10));
		vboxSocial.setStyle(COMMON_STYLING);
		
		vboxPers.getChildren().add(vboxSocial);

		VBox vboxData = new VBox();
		vboxData.getChildren().add(nameLabelText);
		vboxData.getChildren().add(nameField);
		vboxData.getChildren().add(surnameLabelText);
		vboxData.getChildren().add(surnameField);
		vboxData.getChildren().add(emailLabelText);
		vboxData.getChildren().add(emailField);
		vboxData.getChildren().add(phoneLabelText);
		vboxData.getChildren().add(phoneField);
		vboxData.getChildren().add(fiscalCodeLabelText);
		vboxData.getChildren().add(fiscalCodeField);
		vboxData.getChildren().add(addressLabelText);
		vboxData.getChildren().add(addressField);
		vboxData.setPrefSize(450, 300);
		vboxData.setPadding(new Insets(10, 10, 10, 10));
		vboxData.setStyle(COMMON_STYLING);
		
		VBox vboxCand = new VBox();
		listCandidatureView.setPrefSize(800, 400);
		vboxCand.getChildren().add(listCandidatureView);
		vboxCand.setPrefSize(800, 300);
		vboxCand.setStyle(COMMON_STYLING);
		
		HBox hboxLev = new HBox(10);
		hboxLev.getChildren().add(vboxPers);
		hboxLev.getChildren().add(vboxData);
		hboxLev.getChildren().add(vboxCand);
		hboxLev.setPadding(new Insets(10, 10, 10, 10));
		
		VBox vboxBio = new VBox();
		vboxBio.getChildren().add(bioLabelText);
		vboxBio.getChildren().add(bioField);
		vboxBio.setPadding(new Insets(10, 10, 10, 10));
		vboxBio.setStyle(COMMON_STYLING);

		vbox.getChildren().add(hboxHeader);
		vbox.getChildren().add(hboxLev);
		vbox.getChildren().add(vboxBio);
		vbox.setPadding(new Insets(0, 10, 0, 10));

		scene = new Scene(vbox, WIDTHWINDOW, HEIGHTWINDOW);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setResizable(false);
		stage.setTitle("My Account - Whork");
	}

	@Override
	public void visible() {
		//no need to call update
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			homeBtn,
			chatBtn,
			logoutBtn,
			nameField,
			surnameField,
			emailField,
			phoneField,
			fiscalCodeField,
			addressField,
			nameLabel,
			statusLabel,
			locationLabel,
			websiteField,
			twitterField,
			instaField,
			facebookField,
			bioField,
			imgView,
			listCandidatureView,
			editSocialBtn,
			submitSocialBtn,
			cancelSocialBtn
		};
	}
}
