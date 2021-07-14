package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.RegisterJobSeekerViewController;
import logic.util.GraphicsUtil;

public final class RegisterJobSeekerView implements ControllableView {
	private static final String WIN_TITLE = "Register as a job seeker - Whork";
	private static final String AUTH_MESSAGE = "Authentication";
	private static final String EMAIL_MESSAGE = "Email:";
	private static final String PASSWORD_MESSAGE = "Password:";
	private static final String RETYPE_PASSWORD_MESSAGE = "Retype password:";
	private static final String ABOUT_YOU_MESSAGE = "About you";
	private static final String NAME_MESSAGE = "Name:";
	private static final String SURNAME_MESSAGE = "Surname:";
	private static final String FISCAL_CODE_MESSAGE = "Fiscal code:";
	private static final String PHONE_NUMBER_MESSAGE = "Phone number:";
	private static final String PROFILE_PHOTO_MESSAGE = "Profile photo:";
	private static final String CHOOSE_FILE_BTN = "Choose...";
	public static final String SELECT_FILE_MESSAGE = "(Select a file)";
	private static final String TOWN_MESSAGE = "Town:";
	private static final String ADDRESS_MESSAGE = "Address:";
	private static final String EMPLOYMENT_STATUS_MESSAGE = "Employment status";
	private static final String ATTACH_YOUR_CV_MESSAGE = "Attach your cv:";
	private static final String PRIVACY_POLICY_MESSAGE = "Privacy policy";
	private static final String PRIVACY_POLICY_DISCLAIMER = 
		"I have read the Customer Privacy Policy and consent to the processing\n" +
		" of my personal data for the purposes related to the management of the\n" +
		" contractual relationship and the provision of services ";
	private static final String CONFIRM_BTN = "Confirm";
	private static final String TYPE_EMAIL_HINT = "Type your email here...";
	private static final String TYPE_PASSWORD_HINT = "Type your password here...";
	private static final String RETYPE_PASSWORD_HINT = "Retype your password here...";
	private static final String TYPE_NAME_HINT = "Type your name here...";
	private static final String TYPE_SURNAME_HINT = "Type your surname here...";
	private static final String TYPE_FISCAL_CODE_HINT = "Type your fiscal code here...";
	private static final String TYPE_PHONE_NUMBER_HINT = "Type your phone number here...";
	private static final String TYPE_TOWN_HINT = "Type your town here...";
	private static final String TYPE_ADDRESS_HINT = "Type your address here...";
	private static final double CONFIG_WIN_WIDTH = 490;
	private static final double CONFIG_WIN_HEIGHT = 530;
	private static final double WEBVIEW_MAX_HEIGHT = 200;
	private static final double WEBVIEW_MAX_WIDTH = 200;

	private Scene scene;

	private Text authMessage;
	private Label emailMessage;
	private TextField emailField;
	private Label passwordMessage;
	private PasswordField passwordField;
	private Label retypePasswordMessage;
	private PasswordField retypePasswordField;
	private Text aboutYouMessage;
	private Label nameMessage;
	private TextField nameField;
	private Label surnameMessage;
	private TextField surnameField;
	private Label fiscalCodeMessage;
	private TextField fiscalCodeField;
	private Label phoneNumberMessage;
	private TextField phoneNumberField;
	private Label profilePhotoMessage;
	private Button profilePhotoButton;
	private Label profilePhotoFileLabel;
	private Label townMessage;
	private TextField townField;
	private Label addressMessage;
	private TextField addressField;
	private WebView mapWebView;
	private Text employmentStatusMessage;
	private ChoiceBox<String> employmentStatusChoiceBox;
	private Label attachYourCvMessage;
	private Button attachYourCvButton;
	private Label cvFileLabel;
	private Text privacyPolicyMessage;
	private Label privacyPolicyDisclaimer;
	private CheckBox privacyPolicyCheckBox;
	private Button confirmButton;
	
	private GraphicsController controller;

	public RegisterJobSeekerView(ViewStack viewStack) {
		controller = new RegisterJobSeekerViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void populateScene() {
		VBox vbox = new VBox(5);
		vbox.getChildren().add(authMessage);
		vbox.getChildren().add(getHBox(emailMessage, emailField));
		vbox.getChildren().add(getHBox(passwordMessage, passwordField));
		vbox.getChildren().add(getHBox(retypePasswordMessage, retypePasswordField));
		vbox.getChildren().add(aboutYouMessage);
		vbox.getChildren().add(getHBox(nameMessage, nameField));
		vbox.getChildren().add(getHBox(surnameMessage, surnameField));
		vbox.getChildren().add(getHBox(fiscalCodeMessage, fiscalCodeField));
		vbox.getChildren().add(getHBox(phoneNumberMessage, phoneNumberField));
		vbox.getChildren().add(getHBox(profilePhotoMessage, profilePhotoButton, profilePhotoFileLabel));
		vbox.getChildren().add(getHBox(townMessage, townField));
		vbox.getChildren().add(getHBox(addressMessage, addressField));

		VBox finalVbox = new VBox(5);
		finalVbox.getChildren().add(employmentStatusMessage);
		finalVbox.getChildren().add(employmentStatusChoiceBox);
		finalVbox.getChildren().add(getHBox(attachYourCvMessage, attachYourCvButton, cvFileLabel));
		finalVbox.getChildren().add(privacyPolicyMessage);
		finalVbox.getChildren().add(getHBox(privacyPolicyCheckBox, privacyPolicyDisclaimer));
		finalVbox.getChildren().add(confirmButton);

		HBox hbox = new HBox(30);
		hbox.getChildren().add(vbox);
		hbox.getChildren().add(mapWebView);

		VBox rootVbox = new VBox(5);
		rootVbox.getChildren().add(hbox);
		rootVbox.getChildren().add(finalVbox);

		scene = new Scene(rootVbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}

	private void setNodesProps() {
		emailField.setPromptText(TYPE_EMAIL_HINT);
		passwordField.setPromptText(TYPE_PASSWORD_HINT);
		retypePasswordField.setPromptText(RETYPE_PASSWORD_HINT);
		nameField.setPromptText(TYPE_NAME_HINT);
		surnameField.setPromptText(TYPE_SURNAME_HINT);
		fiscalCodeField.setPromptText(TYPE_FISCAL_CODE_HINT);
		phoneNumberField.setPromptText(TYPE_PHONE_NUMBER_HINT);
		townField.setPromptText(TYPE_TOWN_HINT);
		addressField.setPromptText(TYPE_ADDRESS_HINT);
		confirmButton.setDisable(true);
		mapWebView.setMaxHeight(WEBVIEW_MAX_HEIGHT);
		mapWebView.setMaxWidth(WEBVIEW_MAX_WIDTH);
		mapWebView.setDisable(true);

		setTextProperties();

		Font boldFont = GraphicsUtil.getBoldFont();
		authMessage.setFont(boldFont);
		aboutYouMessage.setFont(boldFont);
		employmentStatusMessage.setFont(boldFont);
		privacyPolicyMessage.setFont(boldFont);
	}

	private void init() {
		authMessage = new Text(AUTH_MESSAGE);
		emailMessage = new Label(EMAIL_MESSAGE);
		emailField = new TextField();
		passwordMessage = new Label(PASSWORD_MESSAGE);
		passwordField = new PasswordField();
		retypePasswordMessage = new Label(RETYPE_PASSWORD_MESSAGE);
		retypePasswordField = new PasswordField();
		aboutYouMessage = new Text(ABOUT_YOU_MESSAGE);
		nameMessage = new Label(NAME_MESSAGE);
		nameField = new TextField();
		surnameMessage = new Label(SURNAME_MESSAGE);
		surnameField = new TextField();
		fiscalCodeMessage = new Label(FISCAL_CODE_MESSAGE);
		fiscalCodeField = new TextField();
		phoneNumberMessage = new Label(PHONE_NUMBER_MESSAGE);
		phoneNumberField = new TextField();
		profilePhotoMessage = new Label(PROFILE_PHOTO_MESSAGE);
		profilePhotoButton = new Button(CHOOSE_FILE_BTN);
		profilePhotoFileLabel = new Label(SELECT_FILE_MESSAGE);
		townMessage = new Label(TOWN_MESSAGE);
		townField = new TextField();
		addressMessage = new Label(ADDRESS_MESSAGE);
		addressField = new TextField();
		mapWebView = new WebView();
		employmentStatusMessage = new Text(EMPLOYMENT_STATUS_MESSAGE);
		employmentStatusChoiceBox = new ChoiceBox<>();
		attachYourCvMessage = new Label(ATTACH_YOUR_CV_MESSAGE);
		attachYourCvButton = new Button(CHOOSE_FILE_BTN);
		cvFileLabel = new Label(SELECT_FILE_MESSAGE);
		privacyPolicyMessage = new Text(PRIVACY_POLICY_MESSAGE);
		privacyPolicyDisclaimer = new Label(PRIVACY_POLICY_DISCLAIMER);
		privacyPolicyCheckBox = new CheckBox();
		confirmButton = new Button(CONFIRM_BTN);
		controller.setup();
	}

	private void setTextProperties() {
		emailField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(emailField, 255));

		nameField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(nameField, 45));

		surnameField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(surnameField, 45));

		fiscalCodeField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(fiscalCodeField, 16));

		addressField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(addressField, 45));
	
		phoneNumberField.textProperty().addListener(
			new GraphicsUtil.OnlyNumbersChangeListener(phoneNumberField));
		phoneNumberField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(phoneNumberField, 10));
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WIN_TITLE);
		stage.setResizable(false);
	}

	@Override
	public void visible() {
		//nothing to do
	}

	@Override
	public Node[] getNodes() {
		return new Node[]{
			emailField,
			passwordField,
			retypePasswordField,
			nameField,
			surnameField,
			fiscalCodeField,
			phoneNumberField,
			profilePhotoButton,
			profilePhotoFileLabel,
			townField,
			addressField,
			mapWebView,
			employmentStatusChoiceBox,
			attachYourCvButton,
			cvFileLabel,
			privacyPolicyCheckBox,
			confirmButton,
		};
	}

	private static HBox getHBox(Node... nodes) {
		return GraphicsUtil.getHBox(10, nodes);
	}
}
