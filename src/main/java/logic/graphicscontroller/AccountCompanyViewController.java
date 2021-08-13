package logic.graphicscontroller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import logic.bean.ChatLogEntryBean;
import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.controller.CandidatureController;
import logic.controller.RegisterController;
import logic.exception.AlreadyExistantUserException;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.factory.DialogFactory;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.util.tuple.Pair;
import logic.view.AccountCompanyView;
import logic.view.ChatItem;
import logic.view.ControllableView;
import logic.view.PostOfferView;
import logic.view.RecruiterItem;
import logic.view.ViewStack;

public final class AccountCompanyViewController extends GraphicsController {
	
	private static final double MAX_WIDTH = 200;

	private static final String JAN = "Jan";
	private static final String FEB = "Feb";
	private static final String MAR = "Mar";
	private static final String APR = "Apr";
	private static final String MAY = "May";
	private static final String JUN = "Jun";
	private static final String JUL = "Jul";
	private static final String AUG = "Aug";
	private static final String SEP = "Sep";
	private static final String OCT = "Oct";
	private static final String NOV = "Nov";
	private static final String DEC = "Dec";

	private Button homeBtn;
	private Button postOfferBtn;
	private Button logOutBtn;
	private Text nameAdminText;
	private Text nameCompanyText;
	private ImageView imgAdminView;
	private Text numberEmployeeText;
	private Text numberOfferText;
	private Text totalClickText;

	private CategoryAxis yAxis;
	private StackedBarChart<Number, String> candidateBarChart;
	private XYChart.Series<Number, String> series;

	private ListView<ChatLogEntryBean> listChatView;
	private ListView<UserBean> listRecruiterView;
	private Button addRecruiterBtn;
	private TextField nameField;
	private TextField surnameField;
	private TextField emailField;
	private PasswordField passwordField;
	private TextField fiscalCodeField;
	private TextField phoneNumberField;
	private Button profilePhotoButton;
	private Label profilePhotoFileLabel;
	private Button submitBtn;
	private Button cancelBtn;
	private VBox vboxAddRecr;

	private Text textFX;

	private File photo;
	private UserBean user;
	private String email;
	private Map<String, UserBean> mapRecruiter;

	public AccountCompanyViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setup() {
		Node[] n = view.getNodes();
		homeBtn = (Button) n[0];
		postOfferBtn = (Button) n[1];
		logOutBtn = (Button) n[2];
		nameAdminText = (Text) n[3];
		nameCompanyText = (Text) n[4];
		imgAdminView = (ImageView) n[5];
		numberEmployeeText = (Text) n[6];
		numberOfferText = (Text) n[7];
		totalClickText = (Text) n[8];
		yAxis = (CategoryAxis) n[9];
		candidateBarChart = (StackedBarChart<Number, String>) n[10];
		listChatView = (ListView<ChatLogEntryBean>) n[11];
		listRecruiterView = (ListView<UserBean>) n[12];
		addRecruiterBtn = (Button) n[13];
		nameField = (TextField) n[14];
		surnameField = (TextField) n[15];
		emailField = (TextField) n[16];
		passwordField = (PasswordField) n[17];
		fiscalCodeField = (TextField) n[18];
		phoneNumberField = (TextField) n[19];
		profilePhotoButton = (Button) n[20];
		profilePhotoFileLabel = (Label) n[21];
		submitBtn = (Button) n[22];
		cancelBtn = (Button) n[23];
		vboxAddRecr = (VBox) n[24];
		textFX = (Text) n[25];

		user = LoginHandler.getSessionUser();

		n[26].setVisible(user.isAdmin());

		try {
			email = AccountController.getEmailEmployeeByCF(user);
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}

		setTextProperties();
		setPostOfferButton();
		setHeader();
		setNumber();
		setNumberCandidateChart();
		setChat();
		if(user.isAdmin()) {
			setRecruiter();
			setAddRecruiter(false);
		}
		setTextAnimation();

		setListeners();
	}
	
	private void setListeners() {
		homeBtn.setOnMouseClicked(new HandleHomeRequest());
		postOfferBtn.setOnMouseClicked(new HandlePostOfferRequest());
		logOutBtn.setOnMouseClicked(new GraphicsUtil.HandleLogoutRequest(viewStack));
		addRecruiterBtn.setOnMouseClicked(new HandleAddRecruiterRequest());
		submitBtn.setOnMouseClicked(new HandleSubmitRecruiterRequest());
		cancelBtn.setOnMouseClicked(new HandleCancelRecruiterRequest());
		profilePhotoButton.setOnMouseClicked(new HandleProfilePhotoButtonClicked());
	}

	private void setTextProperties() {
		nameField.textProperty().addListener(
				new GraphicsUtil.LimitLengthChangeListener(nameField, 45));

		surnameField.textProperty().addListener(
				new GraphicsUtil.LimitLengthChangeListener(surnameField, 45));

		emailField.textProperty().addListener(
				new GraphicsUtil.LimitLengthChangeListener(emailField, 255));

		passwordField.textProperty().addListener(
				new GraphicsUtil.LimitLengthChangeListener(passwordField, 255));

		fiscalCodeField.textProperty().addListener(
				new GraphicsUtil.LimitLengthChangeListener(fiscalCodeField, 16));

		phoneNumberField.textProperty().addListener(
				new GraphicsUtil.LimitLengthChangeListener(phoneNumberField, 10));
	}

	private void setTextAnimation() {
		textFX.setTextOrigin(VPos.TOP);
		textFX.setFont(Font.font(24));

	    double sceneWidth = 400;
	    double msgWidth = textFX.getLayoutBounds().getWidth();
	    KeyValue initKeyValue = new KeyValue(textFX.translateXProperty(), sceneWidth);
	    KeyFrame initFrame = new KeyFrame(Duration.ZERO, initKeyValue);
	    KeyValue endKeyValue = new KeyValue(textFX.translateXProperty(), msgWidth);
	    KeyFrame endFrame = new KeyFrame(Duration.seconds(5), endKeyValue);
	    Timeline timeline = new Timeline(initFrame, endFrame);
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.play();
	}

	private void setPostOfferButton() {
		postOfferBtn.setDisable(!user.isRecruiter());
	}

	private void setHeader() {
		final String usrData = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA);
		final String dflRoot = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_DFL_ROOT);

		imgAdminView.setFitWidth(MAX_WIDTH);
		imgAdminView.setPreserveRatio(true);

		StringBuilder pathBuilder = new StringBuilder("file:");

		if(user.getPhoto()!=null) {
			imgAdminView.setImage(
					new Image(pathBuilder.append(usrData).append("/").append(user.getPhoto()).toString()));
		} else {
			imgAdminView.setImage(
					new Image(pathBuilder.append(dflRoot).append("/avatar4.jpg").toString()));
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(user.getName())
				.append(" ")
				.append(user.getSurname());
		
		nameAdminText.setText(builder.toString());
		nameCompanyText.setText(user.getCompany().getSocialReason());
	}
	
	private void setNumber() {
		try {
			numberEmployeeText.setText(Integer.toString(AccountController.getNumberOfEmployees(user)));
			numberOfferText.setText(Integer.toString(AccountController.getNumberOfOffers(user)));
			totalClickText.setText(Integer.toString(AccountController.getNumberOfClick(user)));
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}
	}

	private void setNumberCandidateChart() {
		List<Integer> listCandidatureByVat = null;
		series = new XYChart.Series<Number, String>();

		candidateBarChart.setTitle("Number of Candidate");
		yAxis.setCategories(FXCollections.<String>observableArrayList(
		        Arrays.asList(JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC)));
		
		try {
			listCandidatureByVat = CandidatureController.getCandidatureByCompanyVat(user.getCompany());
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
			return; //already unreachable, suppress sonar smell
		}

		List<String> listMonth = new ArrayList<>();
		listMonth.add(JAN); listMonth.add(FEB); listMonth.add(MAR); listMonth.add(APR);
		listMonth.add(MAY); listMonth.add(JUN); listMonth.add(JUL); listMonth.add(AUG);
		listMonth.add(SEP); listMonth.add(OCT); listMonth.add(NOV); listMonth.add(DEC);

		for(int i=0; i<listCandidatureByVat.size(); i++) {		
			series.getData().add(new XYChart.Data<Number, String>(listCandidatureByVat.get(i), listMonth.get(i)));
		}

		candidateBarChart.getData().add(series);
	}

	public static ObservableList<PieChart.Data> setPieChart() {
		Map<String, Double> mapEmployment = null;

		try {
			mapEmployment = AccountController.getEmploymentStatusBtCompanyVAT(LoginHandler.getSessionUser().getCompany());
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
			return null; //already unreachable, suppress sonar smell
		}

		Iterator<String> keys = mapEmployment.keySet().iterator();
		String key = null;

		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
		while(keys.hasNext()) {
			key = keys.next();
			pieData.add(new PieChart.Data(key, mapEmployment.get(key)*100));
		}

		return pieData;
	}

	private void setChat() {
   	 	List<ChatLogEntryBean> listChat = null;
		try {
			listChat = AccountController.getLastMessage(email);
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}                           	    

		fillListViewChat(FXCollections.observableArrayList(listChat));
	}

	@SuppressWarnings({"squid:S110", "squid:S1854"})
	private void fillListViewChat(ObservableList<ChatLogEntryBean> list) {
		listChatView.setItems(list);
		listChatView.setCellFactory((ListView<ChatLogEntryBean> oUnused) -> new ListCell<ChatLogEntryBean>() {
				@Override
				public void updateItem(ChatLogEntryBean itemBean, boolean empty) {
					super.updateItem(itemBean, empty);
					if (itemBean != null) {
						ChatItem newItem = new ChatItem();
						try {
							newItem.setInfo(itemBean, email);
						} catch (InternalException e) {
							Util.exceptionLog(e);
							GraphicsUtil.showExceptionStage(e);
						}
						setGraphic(newItem.getBox());
					}
				}
			}
		);
	}

	private void setRecruiter() {
		mapRecruiter = null;
		List<UserBean> listRecruiter = new ArrayList<>();

		try {
			mapRecruiter = AccountController.getEmployeeByCompanyVAT(user.getCompany());                                    		
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
			return; //already unreachable, suppress sonar smell
		}

		Iterator<String> keys = mapRecruiter.keySet().iterator();
		String key = null;

		while(keys.hasNext()) {
			key = keys.next();
			listRecruiter.add(mapRecruiter.get(key));
		}
		
		fillListViewRecruiter(FXCollections.observableArrayList(listRecruiter));
	}

	@SuppressWarnings({"squid:S110", "squid:S1854"})
	private void fillListViewRecruiter(ObservableList<UserBean> list) {
		listRecruiterView.setItems(list);
		listRecruiterView.setCellFactory((ListView<UserBean> oUnused) -> new ListCell<UserBean>() {
				@Override
				public void updateItem(UserBean itemBean, boolean empty) {
					super.updateItem(itemBean, empty);
					if (itemBean != null) {
						RecruiterItem newItem = new RecruiterItem();
						try {
							newItem.setInfo(itemBean, GraphicsUtil.getKey(mapRecruiter, itemBean));
						} catch (InternalException e) {
							Util.exceptionLog(e);
							GraphicsUtil.showExceptionStage(e);
						}
						setGraphic(newItem.getBox());
					}
				}
			}
		);
	}

	private void setAddRecruiter(boolean variable) {
		vboxAddRecr.setVisible(variable);
		if(variable) {
			nameField.setText("");
			surnameField.setText("");
			emailField.setText("");
			passwordField.setText("");
			fiscalCodeField.setText("");
			phoneNumberField.setText("");
		}
	}
	
	@Override
	public void update() {
		//no need to update anything
	}
	
	
	
	private final class HandleHomeRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			viewStack.pop();
		}
	}
	
	private final class HandlePostOfferRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			GraphicsUtil.showAndWaitWindow(PostOfferView.class);
		}
	}

	private final class HandleAddRecruiterRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			setAddRecruiter(!vboxAddRecr.isVisible());
		}
	}

	private final class HandleSubmitRecruiterRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			if(nameField.getText() == "" ||
				surnameField.getText() == "" ||
				emailField.getText() == "" ||
				passwordField.getText() == "" ||
				fiscalCodeField.getText() == "" ||
				phoneNumberField.getText() == "") {

					DialogFactory.error(
							"Recruiter Data",
							"Unable to add recruiter", 
							"You cannot leave empty field").showAndWait();
					
					setAddRecruiter(false);
					return;
			}
			
			UserBean userRecruiter = new UserBean();
	    	UserAuthBean userAuthRecruiter = new UserAuthBean();
	    	
	    	userRecruiter.setName(nameField.getText());
	    	userRecruiter.setSurname(surnameField.getText());
	    	userAuthRecruiter.setEmail(emailField.getText());
	    	userAuthRecruiter.setPassword(passwordField.getText());
			userRecruiter.setCf(fiscalCodeField.getText());
			userRecruiter.setPhoneNumber(phoneNumberField.getText());

			try {
				userRecruiter.setPhoto(Util.Files.saveUserFile(fiscalCodeField.getText(), photo));
			} catch (IOException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			}

			userRecruiter.setAdmin(false);
			userRecruiter.setEmployee(true);
			userRecruiter.setRecruiter(true);
			userRecruiter.setCompany(user.getCompany());
			userRecruiter.setNote(null);
			
			try {
				RegisterController.registerEmployeeForExistingCompany(new Pair<>(userRecruiter, userAuthRecruiter));
			} catch (InternalException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			} catch (AlreadyExistantUserException e) {
				DialogFactory.error(
					"Already Existant User", 
					"Unable to add recruiter", 
					e.getMessage()).showAndWait();
			}
			setAddRecruiter(false);
		}
	}

	private final class HandleCancelRecruiterRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {			
			setAddRecruiter(false);
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
				profilePhotoFileLabel.setText(AccountCompanyView.SELECT_FILE_MESSAGE);
			}
		}
	}
}

