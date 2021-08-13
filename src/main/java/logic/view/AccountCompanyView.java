package logic.view;

import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.graphicscontroller.AccountCompanyViewController;
import logic.graphicscontroller.GraphicsController;
import logic.util.GraphicsUtil;

public final class AccountCompanyView implements ControllableView {
	private static final int WIDTHWINDOW = DefaultWindowSize.WIDTH + 360;
	private static final int HEIGHTWINDOW = DefaultWindowSize.HEIGHT + 400;

	private static final String PROFILE_PHOTO_MESSAGE = "Profile photo:";
	private static final String CHOOSE_FILE_BTN = "Choose...";
	public static final String SELECT_FILE_MESSAGE = "(Select a file)";
	private static final String COMMON_STYLING =
		"-fx-border-style: solid;-fx-border-width: 1;-fx-border-color: black";

	private Button homeBtn;
	private Button postOfferBtn;
	private Button logOutBtn;
	private Text nameAdminText;
	private Text nameCompanyText;
	private ImageView imgAdminView;
	private Text dateText;
	private Text numberEmployeeText;
	private Label numberEmployeeLabel;
	private Text numberOfferText;
	private Label numberOfferLabel;
	private Text totalClickText;
	private Label totalClickLabel;

	private NumberAxis xAxis;
	private CategoryAxis yAxis;
	private StackedBarChart<Number, String> candidateBarChart;
	private PieChart pieChart;

	private Label messageLabel;
	private ListView<Object> listChatView;
	private HBox hboxRecruiter;
	private Label recruiterLabel;
	private Button addRecruiterBtn;
	private ListView<Object> listRecruiterView;
	private Label nameLabel;
	private Label surnameLabel;
	private Label emailLabel;
	private Label passwordLabel;
	private Label fiscalCodeLabel;
	private Label phoneNumberLabel;
	private TextField nameField;
	private TextField surnameField;
	private TextField emailField;
	private PasswordField passwordField;
	private TextField fiscalCodeField;
	private TextField phoneNumberField;
	private Label profilePhotoMessage;
	private Button profilePhotoButton;
	private Label profilePhotoFileLabel;
	private Button submitBtn;
	private Button cancelBtn;
	private VBox vboxAddRecr;

	private Text imageRecruiterLabel;
	private Text emailRecruiterLabel;
	private Text nameRecruiterLabel;
	private Text fiscalCodeRecruiterLabel;
	private Text numberPostLabel;
	private Text numberPhoneRecruiterLabel;

	private Text textFX;

	private Scene scene;

	private GraphicsController controller;

	public AccountCompanyView(ViewStack viewStack) {
		controller = new AccountCompanyViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		homeBtn = new Button("Home");
		postOfferBtn = new Button("Post Offer");
		logOutBtn = new Button("Log Out");
		nameAdminText = new Text();
		nameCompanyText = new Text();
		imgAdminView = new ImageView();
		dateText = new Text(new Date().toString().substring(0, 10));
		numberEmployeeText = new Text();
		numberEmployeeLabel = new Label("Number of employees of the company on this site");
		numberOfferText = new Text();
		numberOfferLabel = new Label("Number of Offers posted");
		totalClickText = new Text();
		totalClickLabel = new Label("Total number of clicks");

		xAxis = new NumberAxis();
		yAxis = new CategoryAxis();
		candidateBarChart = new StackedBarChart<>(xAxis, yAxis);

		pieChart = new PieChart(AccountCompanyViewController.setPieChart());

		messageLabel = new Label("Messages");
		listChatView = new ListView<>();
		recruiterLabel = new Label("Recuiters");
		addRecruiterBtn = new Button("Add Recruiter");
		listRecruiterView = new ListView<>();
		nameField = new TextField();
		surnameField = new TextField();
		emailField = new TextField();
		passwordField = new PasswordField();
		fiscalCodeField = new TextField();
		phoneNumberField = new TextField();
		nameLabel = new Label("Name");
		surnameLabel = new Label("Surname");
		emailLabel = new Label("Email");
		passwordLabel = new Label("Password");
		fiscalCodeLabel = new Label("Fiscal Code");
		phoneNumberLabel = new Label("Phone Number");
		profilePhotoMessage = new Label(PROFILE_PHOTO_MESSAGE);
		profilePhotoButton = new Button(CHOOSE_FILE_BTN);
		profilePhotoFileLabel = new Label(SELECT_FILE_MESSAGE);
		submitBtn = new Button("Submit");
		cancelBtn = new Button("Cancel");
		imageRecruiterLabel = new Text("Image");
		emailRecruiterLabel = new Text("Email");
		nameRecruiterLabel = new Text("Name");
		fiscalCodeRecruiterLabel = new Text("Fiscal Code");
		numberPostLabel = new Text("Number of post");
		numberPhoneRecruiterLabel = new Text("Phone Number");
		vboxAddRecr = new VBox(5);
		hboxRecruiter = new HBox(10);
		textFX = new Text("Dashboard");

		controller.setup();

	}

	private void setNodesProps() {
		imgAdminView.prefWidth(20);
		numberEmployeeText.setFont(GraphicsUtil.getBoldFont());
		numberOfferText.setFont(GraphicsUtil.getBoldFont());
		totalClickText.setFont(GraphicsUtil.getBoldFont());
		nameAdminText.setFont(GraphicsUtil.getBoldFont());
		nameCompanyText.setFont(GraphicsUtil.getBoldFont());
		dateText.setFont(GraphicsUtil.getBoldFont());
		recruiterLabel.setFont(GraphicsUtil.getBoldFont());
		addRecruiterBtn.setFont(GraphicsUtil.getBoldFont());
		messageLabel.setFont(GraphicsUtil.getBoldFont());
		listRecruiterView.setPrefSize(550, 400);
		homeBtn.setFont(GraphicsUtil.getBoldFont());
		postOfferBtn.setFont(GraphicsUtil.getBoldFont());
		logOutBtn.setFont(GraphicsUtil.getBoldFont());
	}

	private void populateScene() {
		VBox vbox = new VBox(10);

		VBox vboxPannel = new VBox(15);
		vboxPannel.getChildren().add(homeBtn);
		vboxPannel.getChildren().add(postOfferBtn);
		vboxPannel.getChildren().add(logOutBtn);
		vboxPannel.setPrefWidth(200);
		vboxPannel.setPadding(new Insets(10, 10, 10, 10));
		vboxPannel.setAlignment(Pos.CENTER);
		vboxPannel.setStyle(COMMON_STYLING);

		HBox hboxHeader = new HBox(10);
		VBox vboxHeader = new VBox(10);
		vboxHeader.getChildren().add(nameAdminText);
		vboxHeader.getChildren().add(nameCompanyText);
		vboxHeader.getChildren().add(textFX);
		hboxHeader.getChildren().add(vboxHeader);
		hboxHeader.getChildren().add(imgAdminView);
		hboxHeader.getChildren().add(dateText);

		hboxHeader.setAlignment(Pos.CENTER);
		hboxHeader.setStyle(COMMON_STYLING);
		hboxHeader.setPadding(new Insets(10, 10, 10, 10));
		vboxHeader.setPadding(new Insets(10, 900, 10, 10));
		hboxHeader.setPrefSize(900, 200);

		HBox hboxHeaderPannel = new HBox(10);
		hboxHeaderPannel.getChildren().add(vboxPannel);
		hboxHeaderPannel.getChildren().add(hboxHeader);

		HBox hboxNumber = new HBox(10);
		VBox vboxNumberEmployee = new VBox(10);
		vboxNumberEmployee.getChildren().add(numberEmployeeLabel);
		vboxNumberEmployee.getChildren().add(numberEmployeeText);
		vboxNumberEmployee.setAlignment(Pos.CENTER);
		vboxNumberEmployee.setStyle(COMMON_STYLING);
		vboxNumberEmployee.setPadding(new Insets(10, 10, 10, 10));

		VBox vboxNumberOffer = new VBox(10);
		vboxNumberOffer.getChildren().add(numberOfferLabel);
		vboxNumberOffer.getChildren().add(numberOfferText);
		vboxNumberOffer.setAlignment(Pos.CENTER);
		vboxNumberOffer.setStyle(COMMON_STYLING);
		vboxNumberOffer.setPadding(new Insets(10, 10, 10, 10));
		
		VBox vboxTotalClick = new VBox(10);
		vboxTotalClick.getChildren().add(totalClickLabel);
		vboxTotalClick.getChildren().add(totalClickText);
		vboxTotalClick.setAlignment(Pos.CENTER);
		vboxTotalClick.setStyle(COMMON_STYLING);
		vboxTotalClick.setPadding(new Insets(10, 10, 10, 10));

		hboxNumber.getChildren().add(vboxNumberEmployee);
		hboxNumber.getChildren().add(vboxNumberOffer);
		hboxNumber.getChildren().add(vboxTotalClick);

		HBox hboxChart = new HBox(10);
		hboxChart.getChildren().add(pieChart);
		hboxChart.getChildren().add(candidateBarChart);
		listChatView.setPrefSize(550, 600);
		VBox vboxMessage = new VBox(5);
		vboxMessage.getChildren().add(messageLabel);
		vboxMessage.getChildren().add(listChatView);
		vboxMessage.setPadding(new Insets(10, 10, 10, 10));
		hboxChart.getChildren().add(vboxMessage);
		hboxChart.setStyle(COMMON_STYLING);
		hboxChart.setPrefSize(300, 300);

		VBox vboxRecruiter = new VBox(10);
		vboxRecruiter.getChildren().add(getHBox(10, recruiterLabel, addRecruiterBtn));
		vboxRecruiter.getChildren().add(getHBox(100, imageRecruiterLabel, nameRecruiterLabel, emailRecruiterLabel, fiscalCodeRecruiterLabel, numberPostLabel, numberPhoneRecruiterLabel));
		vboxRecruiter.getChildren().add(listRecruiterView);
		vboxRecruiter.setStyle(COMMON_STYLING);

		vboxAddRecr.getChildren().add(nameLabel);
		vboxAddRecr.getChildren().add(nameField);
		vboxAddRecr.getChildren().add(surnameLabel);
		vboxAddRecr.getChildren().add(surnameField);
		vboxAddRecr.getChildren().add(emailLabel);
		vboxAddRecr.getChildren().add(emailField);
		vboxAddRecr.getChildren().add(passwordLabel);
		vboxAddRecr.getChildren().add(passwordField);
		vboxAddRecr.getChildren().add(fiscalCodeLabel);
		vboxAddRecr.getChildren().add(fiscalCodeField);
		vboxAddRecr.getChildren().add(phoneNumberLabel);
		vboxAddRecr.getChildren().add(phoneNumberField);
		vboxAddRecr.getChildren().add(getHBox(10, profilePhotoMessage, profilePhotoButton, profilePhotoFileLabel));
		vboxAddRecr.setStyle(COMMON_STYLING);
		vboxAddRecr.setPadding(new Insets(10, 10, 10, 10));
		vboxAddRecr.setPrefSize(650, 400);

		vboxRecruiter.setPrefSize(900, 300);
		vboxRecruiter.setPadding(new Insets(10, 10, 10, 10));
		hboxRecruiter.getChildren().add(vboxRecruiter);
		vboxAddRecr.getChildren().add(getHBox(10, submitBtn, cancelBtn));
		hboxRecruiter.getChildren().add(vboxAddRecr);
		hboxRecruiter.setPrefSize(900, 300);

		vbox.getChildren().add(hboxHeaderPannel);
		vbox.getChildren().add(hboxNumber);
		vbox.getChildren().add(hboxChart);
		vbox.getChildren().add(hboxRecruiter);
		vbox.setPadding(new Insets(10, 10, 10, 10));

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
			postOfferBtn,
			logOutBtn,
			nameAdminText,
			nameCompanyText,
			imgAdminView,
			numberEmployeeText,
			numberOfferText,
			totalClickText,
			yAxis,
			candidateBarChart,
			listChatView,
			listRecruiterView,
			addRecruiterBtn,
			nameField,
			surnameField,
			emailField,
			passwordField,
			fiscalCodeField,
			phoneNumberField,
			profilePhotoButton,
			profilePhotoFileLabel,
			submitBtn,
			cancelBtn,
			vboxAddRecr,
			textFX,
			hboxRecruiter
		};
	}

	private static HBox getHBox(int space, Node... nodes) {
		HBox hbox = GraphicsUtil.getHBox(space, nodes);
		hbox.setPadding(new Insets(10, 10, 10, 10));
		return hbox;
	}
}






