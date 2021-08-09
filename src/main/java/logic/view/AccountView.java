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
import logic.graphicscontroller.AccountViewController;
import logic.graphicscontroller.GraphicsController;

public final class AccountView implements ControllableView {

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

	public AccountView(ViewStack viewStack) {
		controller = new AccountViewController(this, viewStack);
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
		
		controller.setup();
	}

	private void setNodesProps() {
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
		vboxPers.setPadding(new Insets(10,500,500,10));
		
		VBox vboxSocial = new VBox();
		vboxPers.getChildren().add(websiteField);
		vboxPers.getChildren().add(twitterField);
		vboxPers.getChildren().add(instaField);
		vboxPers.getChildren().add(facebookField);
		vboxPers.setPadding(new Insets(10,500,500,10));
		
		VBox vboxData = new VBox();
		vboxPers.getChildren().add(nameField);
		vboxPers.getChildren().add(surnameField);
		vboxPers.getChildren().add(emailField);
		vboxPers.getChildren().add(phoneField);
		vboxPers.getChildren().add(fiscalCodeField);
		vboxPers.getChildren().add(addressField);
		vboxPers.setPadding(new Insets(10,500,500,10));
		
		VBox vboxBio = new VBox();
		vboxBio.getChildren().add(bioField);
		
		vbox.getChildren().add(hboxHeader);
		vbox.getChildren().add(vboxPers);
		vbox.getChildren().add(vboxSocial);
		vbox.getChildren().add(vboxData);
		vbox.getChildren().add(vboxBio);

		scene = new Scene(vbox, DefaultWindowSize.WIDTH, DefaultWindowSize.HEIGHT);
	}

	private void populateScene() {
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
