package logic.graphicscontroller;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.web.WebView;
import logic.bean.ComuneBean;
import logic.bean.ProvinciaBean;
import logic.pool.ComuniPool;
import logic.pool.EmploymentsStatusPool;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.ViewStack;
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
	private Text errorMessages;
	private List<String> itTowns;
	
	public RegisterJobSeekerViewController(ControllableView view, ViewStack viewStack) {
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
		townField = (TextField) n[9];
		addressField = (TextField) n[10];
		mapWebView = (WebView) n[11];
		employmentStatusChoiceBox = (ChoiceBox) n[12];
		attachYourCvButton = (Button) n[13];
		cvFileLabel = (Label) n[14];
		privacyPolicyCheckBox = (CheckBox) n[15];
		confirmButton = (Button) n[16];
		errorMessages = (Text) n[17];

		loadItTownsAutoCompletion();
		loadMap("Italy");
		loadEmploymentStatuses();
	}

	@Override
	public void update() {
		
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
		mapWebView.getEngine().loadContent(getMapsIframe(query));
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
}
