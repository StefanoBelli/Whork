package logic.view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.AccountJobSeekerViewController;
import logic.graphicscontroller.GraphicsController;

public final class AccountJobSeekerView implements ControllableView {

	private Scene scene;

	private Button homeBtn;
	private Button logoutBtn;
	private Button chatBtn;
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
	
	private GraphicsController controller;

	public AccountJobSeekerView(ViewStack viewStack) {
		controller = new AccountJobSeekerViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		homeBtn = new Button("Home");
		logoutBtn = new Button("Logout");
		chatBtn = new Button("Chat");
		
		nameLabel = new Label();
		statusLabel = new Label();
		locationLabel = new Label();
		
		websiteField = new TextField();
		twitterField = new TextField();
		instaField = new TextField();
		facebookField = new TextField();
		
		nameField = new TextField();
		surnameField = new TextField();
		emailField = new TextField();
		phoneField = new TextField();
		fiscalCodeField = new TextField();
		addressField = new TextField();
		
		bioField = new TextField();
		
	}

	private void setNodesProps() {
		nameField.setMaxWidth(450);
		nameField.setAlignment(Pos.CENTER);
		surnameField.setMaxWidth(450);
		surnameField.setAlignment(Pos.CENTER);
		emailField.setMaxWidth(450);
		emailField.setAlignment(Pos.CENTER);
		phoneField.setMaxWidth(450);
		phoneField.setAlignment(Pos.CENTER);
		fiscalCodeField.setMaxWidth(450);
		fiscalCodeField.setAlignment(Pos.CENTER);
		addressField.setMaxWidth(450);
		addressField.setAlignment(Pos.CENTER);
		websiteField.setMaxWidth(450);
		websiteField.setAlignment(Pos.CENTER);
		twitterField.setMaxWidth(450);
		twitterField.setAlignment(Pos.CENTER);
		instaField.setMaxWidth(450);
		instaField.setAlignment(Pos.CENTER);
		facebookField.setMaxWidth(450);
		facebookField.setAlignment(Pos.CENTER);
		bioField.setMaxWidth(450);
		
		controller.setup();
	}

		
	
	private void populateScene() {
		VBox vbox = new VBox();
		
		HBox hboxHeader = new HBox(10);
		hboxHeader.getChildren().add(homeBtn);
		hboxHeader.getChildren().add(chatBtn);
		hboxHeader.getChildren().add(logoutBtn);		
		hboxHeader.setPadding(new Insets(10, 0, 0, 10));

		VBox vboxPers = new VBox();
		vboxPers.getChildren().add(nameLabel);
		vboxPers.getChildren().add(statusLabel);
		vboxPers.getChildren().add(locationLabel);
		vboxPers.setPadding(new Insets(10, 10, 10, 10));
		vboxPers.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");

		VBox vboxSocial = new VBox();
		vboxSocial.getChildren().add(websiteField);
		vboxSocial.getChildren().add(twitterField);
		vboxSocial.getChildren().add(instaField);
		vboxSocial.getChildren().add(facebookField);
		vboxSocial.setPadding(new Insets(10, 10, 10, 10));
		vboxSocial.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");
		
		vboxPers.getChildren().add(vboxSocial);
		

		VBox vboxData = new VBox();
		vboxData.getChildren().add(nameField);
		vboxData.getChildren().add(surnameField);
		vboxData.getChildren().add(emailField);
		vboxData.getChildren().add(phoneField);
		vboxData.getChildren().add(fiscalCodeField);
		vboxData.getChildren().add(addressField);
		vboxData.setPadding(new Insets(10, 10, 10, 10));
		vboxData.setStyle("-fx-border-style: solid;"
                + "-fx-border-width: 1;"
                + "-fx-border-color: black");
		
		HBox hboxLev = new HBox(10);
		hboxLev.setAlignment(Pos.CENTER);
		hboxLev.getChildren().add(vboxPers);
		hboxLev.getChildren().add(vboxData);

		VBox vboxBio = new VBox();
		vboxBio.getChildren().add(bioField);

		vbox.getChildren().add(hboxHeader);
		vbox.getChildren().add(hboxLev);
		vbox.getChildren().add(vboxBio);

		scene = new Scene(vbox, DefaultWindowSize.WIDTH, DefaultWindowSize.HEIGHT);
		
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
			bioField
		};
	}
	
}
