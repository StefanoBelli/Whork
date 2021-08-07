package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.RegisterCompanyViewController;
import logic.util.GraphicsUtil;

public final class RegisterCompanyView implements ControllableView {
	private static final String TYPE_VAT_HINT = "Type VAT here...";
	private static final String TYPE_COMPANY_FC_HINT = "Type company fiscal code here...";
	private static final String AUTH_MESSAGE = "Authentication";
	private static final String EMAIL_MESSAGE = "Email:";
	private static final String PASSWORD_MESSAGE = "Password:";
	private static final String RETYPE_PASSWORD_MESSAGE = "Retype password:";
	private static final String ABOUT_YOU_MESSAGE = "About you";
	private static final String WIN_TITLE = "Register as a company - Whork";
	private static final String TYPE_EMAIL_HINT = "Type your email here...";
	private static final String TYPE_PASSWORD_HINT = "Type your password here...";
	private static final String RETYPE_PASSWORD_HINT = "Retype your password here...";
	private static final String TYPE_NAME_HINT = "Type your name here...";
	private static final String TYPE_SURNAME_HINT = "Type your surname here...";
	private static final String TYPE_FISCAL_CODE_HINT = "Type your fiscal code here...";
	private static final String TYPE_PHONE_NUMBER_HINT = "Type your phone number here...";
	private static final String TYPE_BUSINESS_NAME_HINT = "Type business name here...";
	private static final String NAME_MESSAGE = "Name:";
	private static final String SURNAME_MESSAGE = "Surname:";
	private static final String FISCAL_CODE_MESSAGE = "Fiscal code:";
	private static final String PHONE_NUMBER_MESSAGE = "Phone number:";
	private static final String PROFILE_PHOTO_MESSAGE = "Profile photo:";
	private static final String CHOOSE_FILE_BTN = "Choose...";
	public static final String SELECT_FILE_MESSAGE = "(Select a file)";
	private static final String ABOUT_COMPANY_MESSAGE = "About the company";
	private static final String BUSINESS_NAME_MESSAGE = "Business name:";
	private static final String VAT_MESSAGE = "VAT:";
	private static final String COMPANY_FC_MESSAGE = "Company fiscal code:";
	private static final String COMPANY_LOGO_MESSAGE = "Company logo:";
	private static final String PRIVACY_POLICY_MESSAGE = "Privacy policy";
	private static final String PRIVACY_POLICY_DISCLAIMER = 
		"I have read the Customer Privacy Policy and consent to the processing\n" +
		" of my personal data for the purposes related to the management of the\n" +
		" contractual relationship and the provision of services ";
	private static final String CONFIRM_BTN = "Confirm";
	private static final String ARE_YOU_RECRUITER_MESSAGE = "Are you a recruiter for your company?";
	private static final double CONFIG_WIN_WIDTH = 560;
	private static final double CONFIG_WIN_HEIGTH = 480;

	private Scene scene;
	
	private TextField surnameField;
	private Label fiscalCodeMessage;
	private TextField fiscalCodeField;
	private Label phoneNumberMessage;
	private TextField phoneNumberField;
	private Label profilePhotoMessage;
	private Button profilePhotoButton;
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
	private Label profilePhotoFileLabel;
	private Label areYouRecruiterMessage;
	private CheckBox areYouRecruiterCheckBox;
	private Text aboutCompanyMessage;
	private Label businessNameMessage;
	private TextField businessNameField;
	private Label vatMessage;
	private TextField vatField;
	private Label companyFcMessage;
	private TextField companyFcField;
	private Label companyLogoMessage;
	private Button companyLogoButton;
	private Label companyLogoFileLabel;
	private Text privacyPolicyMessage;
	private Label privacyPolicyDisclaimer;
	private CheckBox privacyPolicyCheckBox;
	private Button confirmButton;

	private GraphicsController controller;

	public RegisterCompanyView(ViewStack viewStack) {
		controller = new RegisterCompanyViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void populateScene() {
		emailField.setPromptText(TYPE_EMAIL_HINT);
		passwordField.setPromptText(TYPE_PASSWORD_HINT);
		retypePasswordField.setPromptText(RETYPE_PASSWORD_HINT);
		nameField.setPromptText(TYPE_NAME_HINT);
		surnameField.setPromptText(TYPE_SURNAME_HINT);
		fiscalCodeField.setPromptText(TYPE_FISCAL_CODE_HINT);
		phoneNumberField.setPromptText(TYPE_PHONE_NUMBER_HINT);
		businessNameField.setPromptText(TYPE_BUSINESS_NAME_HINT);
		vatField.setPromptText(TYPE_VAT_HINT);
		companyFcField.setPromptText(TYPE_COMPANY_FC_HINT);
		confirmButton.setDisable(true);

		setTextProperties();

		Font boldFont = GraphicsUtil.getBoldFont();
		authMessage.setFont(boldFont);
		aboutYouMessage.setFont(boldFont);
		aboutCompanyMessage.setFont(boldFont);
		privacyPolicyMessage.setFont(boldFont);
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

		companyFcField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(companyFcField, 16));

		businessNameField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(businessNameField, 45));

		vatField.textProperty().addListener(
			new GraphicsUtil.OnlyNumbersChangeListener(vatField));
		vatField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(vatField, 11));
	
		phoneNumberField.textProperty().addListener(
			new GraphicsUtil.OnlyNumbersChangeListener(phoneNumberField));
		phoneNumberField.textProperty().addListener(
			new GraphicsUtil.LimitLengthChangeListener(phoneNumberField, 10));
	}

	private void setNodesProps() {
		VBox authVbox = new VBox(5);
		authVbox.getChildren().add(authMessage);
		authVbox.getChildren().add(getHBox(emailMessage, emailField));
		authVbox.getChildren().add(getHBox(passwordMessage, passwordField));
		authVbox.getChildren().add(getHBox(retypePasswordMessage, retypePasswordField));

		VBox aboutYouVbox = new VBox(5);
		aboutYouVbox.getChildren().add(aboutYouMessage);
		aboutYouVbox.getChildren().add(getHBox(nameMessage, nameField));
		aboutYouVbox.getChildren().add(getHBox(surnameMessage, surnameField));
		aboutYouVbox.getChildren().add(getHBox(fiscalCodeMessage, fiscalCodeField));
		aboutYouVbox.getChildren().add(getHBox(phoneNumberMessage, phoneNumberField));
		aboutYouVbox.getChildren().add(getHBox(profilePhotoMessage, profilePhotoButton, profilePhotoFileLabel));
		aboutYouVbox.getChildren().add(getHBox(areYouRecruiterCheckBox, areYouRecruiterMessage));

		VBox aboutCompanyVbox = new VBox(5);
		aboutCompanyVbox.getChildren().add(aboutCompanyMessage);
		aboutCompanyVbox.getChildren().add(getHBox(businessNameMessage, businessNameField));
		aboutCompanyVbox.getChildren().add(getHBox(vatMessage, vatField));
		aboutCompanyVbox.getChildren().add(getHBox(companyFcMessage, companyFcField));
		aboutCompanyVbox.getChildren().add(getHBox(companyLogoMessage, companyLogoButton, companyLogoFileLabel));

		VBox privacyPolicyVbox = new VBox(3);
		privacyPolicyVbox.getChildren().add(privacyPolicyMessage);
		privacyPolicyVbox.getChildren().add(getHBox(privacyPolicyCheckBox, privacyPolicyDisclaimer));
		privacyPolicyVbox.getChildren().add(confirmButton);

		HBox pairedAuthAboutYouHbox = new HBox(10);
		pairedAuthAboutYouHbox.getChildren().add(authVbox);
		pairedAuthAboutYouHbox.getChildren().add(aboutYouVbox);

		VBox rootVbox = new VBox(10);
		rootVbox.getChildren().add(pairedAuthAboutYouHbox);
		rootVbox.getChildren().add(aboutCompanyVbox);
		rootVbox.getChildren().add(privacyPolicyVbox);

		scene = new Scene(rootVbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGTH);
	}

	private void init() {
		nameMessage = new Label(NAME_MESSAGE);
		nameField = new TextField();
		surnameMessage = new Label(SURNAME_MESSAGE);
		surnameField = new TextField();
		fiscalCodeMessage = new Label(FISCAL_CODE_MESSAGE);
		fiscalCodeField = new TextField();
		authMessage = new Text(AUTH_MESSAGE);
		emailMessage = new Label(EMAIL_MESSAGE);
		emailField = new TextField();
		passwordMessage = new Label(PASSWORD_MESSAGE);
		passwordField = new PasswordField();
		retypePasswordMessage = new Label(RETYPE_PASSWORD_MESSAGE);
		retypePasswordField = new PasswordField();
		aboutYouMessage = new Text(ABOUT_YOU_MESSAGE);
		phoneNumberMessage = new Label(PHONE_NUMBER_MESSAGE);
		phoneNumberField = new TextField();
		profilePhotoMessage = new Label(PROFILE_PHOTO_MESSAGE);
		profilePhotoButton = new Button(CHOOSE_FILE_BTN);
		profilePhotoFileLabel = new Label(SELECT_FILE_MESSAGE);
		areYouRecruiterCheckBox = new CheckBox();
		aboutCompanyMessage = new Text(ABOUT_COMPANY_MESSAGE);
		businessNameMessage = new Label(BUSINESS_NAME_MESSAGE);
		businessNameField = new TextField();
		vatMessage = new Label(VAT_MESSAGE);
		vatField = new TextField();
		companyFcMessage = new Label(COMPANY_FC_MESSAGE);
		companyFcField = new TextField();
		companyLogoMessage = new Label(COMPANY_LOGO_MESSAGE);
		companyLogoButton = new Button(CHOOSE_FILE_BTN);
		companyLogoFileLabel = new Label(SELECT_FILE_MESSAGE);
		privacyPolicyMessage = new Text(PRIVACY_POLICY_MESSAGE);
		privacyPolicyDisclaimer = new Label(PRIVACY_POLICY_DISCLAIMER);
		privacyPolicyCheckBox = new CheckBox();
		confirmButton = new Button(CONFIRM_BTN);
		areYouRecruiterMessage = new Label(ARE_YOU_RECRUITER_MESSAGE);
		controller.setup();
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WIN_TITLE);
		stage.setResizable(false);
	}

	
	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void visible() {
		//nothing to do
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			emailField,
			passwordField,
			retypePasswordField,
			nameField,
			surnameField,
			fiscalCodeField,
			phoneNumberField,
			profilePhotoButton,
			profilePhotoFileLabel,
			areYouRecruiterCheckBox,
			businessNameField,
			vatField,
			companyFcField,
			companyLogoButton,
			companyLogoFileLabel,
			privacyPolicyCheckBox,
			confirmButton
		};
	}

	private static HBox getHBox(Node... nodes) {
		return GraphicsUtil.getHBox(10, nodes);
	}
}
