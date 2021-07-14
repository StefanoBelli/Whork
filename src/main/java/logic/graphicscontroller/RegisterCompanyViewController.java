package logic.graphicscontroller;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import logic.view.ControllableView;
import logic.view.RegisterCompanyView;
import logic.view.ViewStack;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.RegisterController;
import logic.exception.AlreadyExistantCompanyException;
import logic.exception.AlreadyExistantUserException;
import logic.exception.InternalException;
import logic.exception.InvalidVatCodeException;
import logic.factory.BeanFactory;
import logic.factory.DialogFactory;
import logic.graphicscontroller.formchecker.BasicFormChecker;
import logic.graphicscontroller.formchecker.CompanyFormCheckerDecorator;
import logic.graphicscontroller.formchecker.FormChecker;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.util.tuple.Pair;

public final class RegisterCompanyViewController extends GraphicsController {
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField retypePasswordField;
	private TextField nameField;
	private TextField surnameField;
	private TextField fiscalCodeField;
	private TextField phoneNumberField;
	private Button profilePhotoButton;
	private Label profilePhotoFileLabel;
	private CheckBox areYouRecruiterCheckBox;
	private TextField businessNameField;
	private TextField vatField;
	private TextField companyFcField;
	private Button companyLogoButton;
	private Label companyLogoFileLabel;
	private CheckBox privacyPolicyCheckBox;
	private Button confirmButton;

	private File profilePhoto;
	private File companyLogo;

	public RegisterCompanyViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		emailField = (TextField) n[0];
		passwordField = (PasswordField) n[1];
		retypePasswordField = (PasswordField) n[2];
		nameField = (TextField) n[3];
		surnameField = (TextField) n[4];
		fiscalCodeField = (TextField) n[5];
		phoneNumberField = (TextField) n[6];
		profilePhotoButton = (Button) n[7];
		profilePhotoFileLabel = (Label) n[8];
		areYouRecruiterCheckBox = (CheckBox) n[9];
		businessNameField = (TextField) n[10];
		vatField = (TextField) n[11];
		companyFcField = (TextField) n[12];
		companyLogoButton = (Button) n[13];
		companyLogoFileLabel = (Label) n[14];
		privacyPolicyCheckBox = (CheckBox) n[15];
		confirmButton = (Button) n[16];

		setListeners();
	}

	@Override
	public void update() {
		//no need to update anything
	}
	
	private void setListeners() {
		privacyPolicyCheckBox.setOnMouseClicked(new HandlePrivacyPolicyCheckBoxClicked());
		confirmButton.setOnMouseClicked(new HandleConfirmButtonClicked());
		companyLogoButton.setOnMouseClicked(new HandleCompanyLogoButtonClicked());
		profilePhotoButton.setOnMouseClicked(new HandleProfilePhotoButtonClicked());
	}

	private final class HandlePrivacyPolicyCheckBoxClicked implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			confirmButton.setDisable(!privacyPolicyCheckBox.isSelected());
		}
	}

	private final class HandleConfirmButtonClicked implements EventHandler<MouseEvent> {
		private static final String ERROR = "Error";

		private String email;
		private String password;
		private String retypedPassword;
		private String name;
		private String surname;
		private String fiscalCode;
		private String phoneNumber;
		private boolean areYouRecruiter;
		private String businessName;
		private String vatNumber;
		private String companyFc;
		
		@Override
		public void handle(MouseEvent event) {
			email = emailField.getText();
			password = passwordField.getText();
			retypedPassword = retypePasswordField.getText();
			name = nameField.getText();
			surname = surnameField.getText();
			fiscalCode = fiscalCodeField.getText();
			phoneNumber = phoneNumberField.getText();
			areYouRecruiter = areYouRecruiterCheckBox.isSelected();
			businessName = businessNameField.getText();
			vatNumber = vatField.getText();
			companyFc = companyFcField.getText();

			if(checksArePassing()) {
				try {
					RegisterController.register(createBeans());
				} catch (InternalException e) {
					Util.exceptionLog(e);
					DialogFactory.error(
						"Internal exception", 
						"Something bad just happened, we don't know much about it",
						e.getMessage()).showAndWait();
					return;
				} catch (AlreadyExistantCompanyException e) {
					DialogFactory.error(
						ERROR, 
						"Already existant company", 
						" * Same business name\n * Same VAT code\n * Same fiscal code").showAndWait();
					return;
				} catch (InvalidVatCodeException e) {
					DialogFactory.error(
						ERROR, 
						"Invalid VAT code",
						"We tried to check your VAT code but it seems invalid, therefore, we are rejecting your signup request").showAndWait();
					return;
				} catch (AlreadyExistantUserException e) {
					DialogFactory.error(
						ERROR, 
						"Already existant user", 
						"Another user with same email and/or fiscal code already exists").showAndWait();
					return;
				} catch (IOException e) {
					Util.exceptionLog(e);
					DialogFactory.error(
						ERROR, 
						"Unable to copy one or more file", 
						"Check logs to get more infos").showAndWait();
					return;
				}

				showSuccessDialogAndCloseStage();
			}
		}

		private Pair<UserBean, UserAuthBean> createBeans() 
				throws IOException {
			UserBean user = new UserBean();
			user.setCf(fiscalCode);
			user.setName(name);
			user.setSurname(surname);
			user.setPhoto(Util.Files.saveUserFile(fiscalCode, profilePhoto));
			user.setPhoneNumber(phoneNumber);
			user.setAdmin(true);
			user.setEmployee(true);
			user.setRecruiter(areYouRecruiter);
			user.setCompany(BeanFactory.buildCompanyBean(companyFc, 
				Util.Files.saveUserFile(fiscalCode, companyLogo), businessName, vatNumber));
			user.setNote(null);

			UserAuthBean userAuth = BeanFactory.buildUserAuthBean(email, password);

			return new Pair<>(user, userAuth);
		}

		private void showSuccessDialogAndCloseStage() {
			DialogFactory.info(
				"Success",
				new StringBuilder("Yay! ").append(name).append(" you did it!").toString(),
				new StringBuilder("You successfully signed up for Whork. ")
					.append("Now it is time to confirm your request to join us by checking for a mail ")
					.append("we sent you at the address you just gave us: ").append(email)
					.append(", be sure to check spam also.\nThe Whork team.")
					.append("\n\nAbout your company\nJust to be sure, we registered your company: ")
					.append(businessName).toString()
			).showAndWait();
			((Stage) view.getScene().getWindow()).close();
		}

		private boolean checksArePassing() {
			FormChecker checker = new CompanyFormCheckerDecorator(new BasicFormChecker());
			String errorString = checker.doChecks(new Object[] {
				email, password, retypedPassword, name, surname, fiscalCode, phoneNumber, businessName,
				vatNumber, companyFc, companyLogo
			});

			if(!errorString.equals("")) {
				DialogFactory.error(
					"Form does not pass checks", 
					"Errors are following, fix them all", 
					errorString).showAndWait();
				return false;
			}

			return true;
		}
	}

	private final class HandleCompanyLogoButtonClicked implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			companyLogo = GraphicsUtil.showFileChooser((Stage)view.getScene().getWindow(), 
				"Choose your company logo", new ExtensionFilter("Image Files", "*.png", "*.jpg"));

			if(companyLogo != null) {
				companyLogoFileLabel.setText(new StringBuilder("Selected: ").append(companyLogo.getName()).toString());
			} else {
				companyLogoFileLabel.setText(RegisterCompanyView.SELECT_FILE_MESSAGE);
			}
		}
	}

	private final class HandleProfilePhotoButtonClicked implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			profilePhoto = GraphicsUtil.showFileChooser((Stage)view.getScene().getWindow(), 
				"Choose your profile photo", new ExtensionFilter("Image Files", "*.png", "*.jpg"));

			if(profilePhoto != null) {
				profilePhotoFileLabel.setText(new StringBuilder("Selected: ").append(profilePhoto.getName()).toString());
			} else {
				profilePhotoFileLabel.setText(RegisterCompanyView.SELECT_FILE_MESSAGE);
			}
		}
	}
}
