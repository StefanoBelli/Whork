package logic.graphicscontroller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.bean.ComuneBean;
import logic.bean.ProvinciaBean;
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
import logic.graphicscontroller.formchecker.FormChecker;
import logic.graphicscontroller.formchecker.JobSeekerFormCheckerDecorator;
import logic.pool.ComuniPool;
import logic.pool.EmploymentsStatusPool;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.RegisterJobSeekerView;
import logic.view.ViewStack;
import logic.util.tuple.Pair;
import java.util.stream.Collectors;

import org.controlsfx.control.textfield.TextFields;

public final class RegisterJobSeekerViewController extends GraphicsController {
	private TextField emailField;
	private PasswordField passwordField;
	private PasswordField retypePasswordField;
	private TextField nameField;
	private TextField surnameField;
	private TextField fiscalCodeField;
	private TextField phoneNumberField;
	private Button profilePhotoButton;
	private Label profilePhotoFileLabel;
	private TextField townField;
	private TextField addressField;
	private WebView mapWebView;
	private ChoiceBox<String> employmentStatusChoiceBox;
	private Button attachYourCvButton;
	private Label cvFileLabel;
	private CheckBox privacyPolicyCheckBox;
	private Button confirmButton;
	
	private List<String> itTowns;

	private File cv;
	private File photo;

	private static final String BASIC_SEARCH_TERM = "Italy";
	private String currentMapQuery = "";
	
	public RegisterJobSeekerViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@SuppressWarnings("unchecked")
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
		townField = (TextField) n[9];
		addressField = (TextField) n[10];
		mapWebView = (WebView) n[11];
		employmentStatusChoiceBox = (ChoiceBox<String>) n[12];
		attachYourCvButton = (Button) n[13];
		cvFileLabel = (Label) n[14];
		privacyPolicyCheckBox = (CheckBox) n[15];
		confirmButton = (Button) n[16];

		loadItTownsAutoCompletion();
		loadMap(BASIC_SEARCH_TERM);
		loadEmploymentStatuses();
		setListeners();
	}

	@Override
	public void update() {
		//no need to update anything
	}

	private static String getMapsIframe(String query) {
		StringBuilder builder = new StringBuilder();
		
		try {
			builder
				.append("<html><body><iframe title='whereareyou' width='200' height='200'")
				.append(" style='border:0' loading='lazy' allowfullscreen src='https://www.google.com")
				.append("/maps/embed/v1/place?key=AIzaSyAp5hG3kGqNGj6Auxh4IhC0Y60hzgUyzKo&q=")
				.append(URLEncoder.encode(query, "UTF-8"))
				.append("'></iframe></body></html>");
		} catch (UnsupportedEncodingException e) {
			Util.exceptionLog(e);
			return e.getMessage();
		}

		return builder.toString();
	}

	private void loadMap(String query) {
		if(!query.equalsIgnoreCase(currentMapQuery)) {
			currentMapQuery = query;
			mapWebView.getEngine().loadContent(getMapsIframe(query));
		}
	}

	private void loadItTownsAutoCompletion() {
		List<ComuneBean> comuni = ComuniPool.getComuni();
		itTowns = new ArrayList<>();
		for(final ComuneBean comune : comuni) {
			StringBuilder builder = new StringBuilder();
			ProvinciaBean provincia = comune.getProvincia();
			builder
				.append(comune.getNome())
				.append(" ")
				.append(provincia.getSigla())
				.append(" - ")
				.append(comune.getCap())
				.append(", ")
				.append(provincia.getRegione().getNome());
			itTowns.add(builder.toString());
		}

		TextFields.bindAutoCompletion(townField,
			t -> itTowns.stream().filter(elem -> 
				elem.toLowerCase().startsWith(t.getUserText().toLowerCase()))
						.collect(Collectors.toList()));
	}

	private void loadEmploymentStatuses() {
		List<String> status = new ArrayList<>();
		EmploymentsStatusPool.getEmploymentsStatus().forEach(e -> status.add(e.getStatus()));
		employmentStatusChoiceBox.setItems(FXCollections.observableArrayList(status));
		employmentStatusChoiceBox.getSelectionModel().select(0);
	}

	private void setListeners() {
		privacyPolicyCheckBox.setOnMouseClicked(new HandlePrivacyPolicyCheckBoxClicked());
		confirmButton.setOnMouseClicked(new HandleConfirmButtonClicked());
		attachYourCvButton.setOnMouseClicked(new HandleAttachYourCvButtonClicked());
		profilePhotoButton.setOnMouseClicked(new HandleProfilePhotoButtonClicked());
		townField.textProperty().addListener(new HandleTownFieldTextChanged());
		addressField.textProperty().addListener(new HandleAddressFieldTextChanged());
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
		private String town;
		private String address;
		private String employmentStatus;

		@Override
		public void handle(MouseEvent event) {
			email = emailField.getText();
			password = passwordField.getText();
			retypedPassword = retypePasswordField.getText();
			name = nameField.getText();
			surname = surnameField.getText();
			fiscalCode = fiscalCodeField.getText();
			phoneNumber = phoneNumberField.getText();
			town = townField.getText();
			address = addressField.getText();
			employmentStatus = employmentStatusChoiceBox.getSelectionModel().getSelectedItem();
			
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
				} catch (InvalidVatCodeException | AlreadyExistantCompanyException e) { //should not happen here
					Util.exceptionLog(e);
					GraphicsUtil.showExceptionStage(e);
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

		private boolean checksArePassing() {
			FormChecker checker = new JobSeekerFormCheckerDecorator(new BasicFormChecker(), itTowns);
			String errorString = checker.doChecks(new Object[] {
				email, password, retypedPassword, name, surname, fiscalCode, phoneNumber, town, address,
				cv
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

		private void showSuccessDialogAndCloseStage() {
			DialogFactory.info(
				"Success",
				new StringBuilder("Yay! ").append(name).append(" you did it!").toString(),
				new StringBuilder("You successfully signed up for Whork. ")
					.append("Now it is time to confirm your request to join us by checking for a mail ")
					.append("we sent you at the address you just gave us: ").append(email)
					.append(", be sure to check spam also.\nThe Whork team.").toString()
			).showAndWait();
			((Stage) view.getScene().getWindow()).close();
		}

		private Pair<UserBean, UserAuthBean> createBeans() 
				throws IOException {
			UserBean user = new UserBean();
			user.setCf(fiscalCode);
			user.setName(name);
			user.setSurname(surname);
			user.setPhoto(Util.Files.saveUserFile(fiscalCode, photo));
			user.setPhoneNumber(phoneNumber);
			user.setBiography(null);
			user.setHomeAddress(address);
			user.setEmploymentStatus(BeanFactory.buildEmploymentStatusBean(employmentStatus));
			user.setCv(Util.Files.saveUserFile(fiscalCode, cv));
			user.setBirthday(Util.deriveBirthdayFromFiscalCode(fiscalCode));
			user.setComune(BeanFactory.buildComuneBean(town));

			UserAuthBean userAuth = 
				BeanFactory.buildUserAuthBean(email, password);

			return new Pair<>(user, userAuth);
		}
	}

	private final class HandleAttachYourCvButtonClicked implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			cv = GraphicsUtil.showFileChooser((Stage)view.getScene().getWindow(), 
				"Choose your CV", new ExtensionFilter("PDF Files", "*.pdf"));

			if(cv != null) {
				cvFileLabel.setText(new StringBuilder("Selected: ").append(cv.getName()).toString());
			} else {
				cvFileLabel.setText(RegisterJobSeekerView.SELECT_FILE_MESSAGE);
			}
		}
	}

	private final class HandleProfilePhotoButtonClicked implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			photo = GraphicsUtil.showFileChooser((Stage)view.getScene().getWindow(), 
				"Choose your profile photo", new ExtensionFilter("Image Files", "*.png", "*.jpg"));

			if(photo != null) {
				profilePhotoFileLabel.setText(new StringBuilder("Selected: ").append(photo.getName()).toString());
			} else {
				profilePhotoFileLabel.setText(RegisterJobSeekerView.SELECT_FILE_MESSAGE);
			}
		}
	}

	private final class HandleTownFieldTextChanged implements ChangeListener<String> {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(!newValue.isBlank()) {
				String addressText = addressField.getText();
				loadMap(new StringBuilder(newValue)
					.append(", ").append(addressText).toString());
			} else {
				loadMap(BASIC_SEARCH_TERM);
			}
		}
	}

	private final class HandleAddressFieldTextChanged implements ChangeListener<String> {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			String townText = townField.getText();
			if(!townText.isBlank()) {
				loadMap(new StringBuilder(townText)
					.append(", ").append(newValue).toString());
			} else {
				loadMap(BASIC_SEARCH_TERM);
			}
		}
	}
}
