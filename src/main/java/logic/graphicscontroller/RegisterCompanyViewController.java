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
import logic.graphicscontroller.formchecker.RegistrationFormChecker;
import logic.graphicscontroller.formchecker.CompanyRegistrationFormCheckerDecorator;
import logic.graphicscontroller.formchecker.FormChecker;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.util.tuple.Pair;
import static logic.graphicscontroller.commons.RegisterCommons.HandlePrivacyPolicyCheckBoxClicked;
import logic.graphicscontroller.commons.RegisterCommons;

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
		privacyPolicyCheckBox.setOnMouseClicked(
			new HandlePrivacyPolicyCheckBoxClicked(confirmButton, privacyPolicyCheckBox));
		confirmButton.setOnMouseClicked(new HandleConfirmButtonClicked());
		companyLogoButton.setOnMouseClicked(new HandleCompanyLogoButtonClicked());
		profilePhotoButton.setOnMouseClicked(new HandleProfilePhotoButtonClicked());
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
					RegisterCommons.ShowAndWaitDialog.internalException(e);
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
					RegisterCommons.ShowAndWaitDialog.alreadyExistantUser();
					return;
				} catch (IOException e) {
					Util.exceptionLog(e);
					RegisterCommons.ShowAndWaitDialog.ioException();
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
			RegisterCommons.ShowAndWaitDialog.success(name, email, businessName);
			((Stage) view.getScene().getWindow()).close();
		}

		private boolean checksArePassing() {
			FormChecker checker = new CompanyRegistrationFormCheckerDecorator(new RegistrationFormChecker());
			String errorString = checker.doChecks(new Object[] {
				email, password, retypedPassword, name, surname, fiscalCode, phoneNumber, businessName,
				vatNumber, companyFc, companyLogo
			});

			if(!errorString.equals("")) {
				RegisterCommons.ShowAndWaitDialog.formDoesNotPassChecks(errorString);
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
