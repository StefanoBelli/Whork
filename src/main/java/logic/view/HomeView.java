package logic.view;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.GraphicsController;
import logic.graphicscontroller.HomeViewController;

public final class HomeView implements ControllableView {
	private static final String CONFIG_WIN_TITLE = "Whork - Find a job";
	private static final String CATEGORY_MSG = "Select a Category: ";
	private static final String SEARCH_BTN_MSG = "Search";
	private static final String POST_OFFER_BTN_MSG = "Post Offer";
	private static final String RESET_BTN_MSG = "Reset Filters";
	private static final String POSITION_MSG = "Select a Position: ";
	private static final String QUALIFICATION_MSG = "Select a qualify: ";
	private static final String TYPE_OF_CONTRACT_MSG = "Select a Type of Contract: ";
	private static final String HINT_SEARCH_MSG = "Type a search term...";
	private static final int LIST_VIEW_MIN_HEIGHT = 495;
	private static final int FLT_SPACING = 80;
	
	private Button postOfferBtn;
	private Button accountBtn;
	private TextField searchField;
	private Button searchBtn;
	private Button resetBtn;
	private ListView<Object> offersLst;
	private Label categoryMessage;
	private ChoiceBox<String> jobCategoryCB;
	private Label positionMessage;
	private ChoiceBox<String> jobPositionCB;
	private Label qualificationMessage;
	private ChoiceBox<String> qualificationCB;
	private Label typeOfContractMessage;
	private ChoiceBox<String> typeOfContractCB;
	private Scene scene;

	private GraphicsController controller;

	public HomeView(ViewStack viewStack) {
		controller = new HomeViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void init() {
		accountBtn = new Button();
		searchField= new TextField();
		searchBtn = new Button(SEARCH_BTN_MSG);
		postOfferBtn = new Button(POST_OFFER_BTN_MSG);
		resetBtn = new Button(RESET_BTN_MSG);
		offersLst = new ListView<>();
		categoryMessage = new Label(CATEGORY_MSG);
		jobCategoryCB=new ChoiceBox<>();
		positionMessage = new Label(POSITION_MSG);
		jobPositionCB= new ChoiceBox<>();
		qualificationMessage = new Label(QUALIFICATION_MSG);
		qualificationCB= new ChoiceBox<>();
		typeOfContractMessage = new Label(TYPE_OF_CONTRACT_MSG);
		typeOfContractCB= new ChoiceBox<>();
		controller.setup();
	}

	private void setNodesProps() {
		searchField.setPromptText(HINT_SEARCH_MSG);
	}

	private void populateScene() {
		VBox vbox = new VBox();
		
		HBox hboxsrc= new HBox();
		hboxsrc.getChildren().add(searchField);
		hboxsrc.getChildren().add(searchBtn);
		
		hboxsrc.getChildren().add(resetBtn);
		hboxsrc.getChildren().add(accountBtn);
		hboxsrc.getChildren().add(postOfferBtn);
		hboxsrc.setPadding(new Insets(10, 0, 0, 10));
		
		HBox hboxflt=new HBox();
		
		VBox vboxfltctg=new VBox();
		vboxfltctg.getChildren().add(categoryMessage);
		vboxfltctg.getChildren().add(jobCategoryCB);
		
		VBox vboxfltps=new VBox();
		vboxfltps.getChildren().add(positionMessage);
		vboxfltps.getChildren().add(jobPositionCB);
		
		VBox vboxfltqlf=new VBox();
		vboxfltqlf.getChildren().add(qualificationMessage);
		vboxfltqlf.getChildren().add(qualificationCB);
		
		VBox vboxfltcnt=new VBox();
		vboxfltcnt.getChildren().add(typeOfContractMessage);
		vboxfltcnt.getChildren().add(typeOfContractCB);
		
		hboxflt.getChildren().add(vboxfltctg);
		hboxflt.setSpacing(FLT_SPACING);
		hboxflt.getChildren().add(vboxfltps);
		hboxflt.getChildren().add(vboxfltqlf);
		hboxflt.getChildren().add(vboxfltcnt);
		
		hboxflt.setPadding(new Insets(10,0,0,10));
		
		VBox vboxol = new VBox();
		vboxol.setPadding(new Insets(10,10,10,10));
		vboxol.getChildren().add(offersLst);
		
		vbox.getChildren().add(hboxsrc);
		vbox.getChildren().add(hboxflt);
		vbox.getChildren().add(vboxol);
		
		scene = new Scene(vbox, DefaultWindowSize.WIDTH, DefaultWindowSize.HEIGHT);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setResizable(false);
		stage.setTitle(CONFIG_WIN_TITLE);
		offersLst.setMinHeight(LIST_VIEW_MIN_HEIGHT);
	}
	
	@Override
	public Node[] getNodes() {
		return new Node[] {
			accountBtn,
			searchField,
			searchBtn,
			offersLst,
			jobCategoryCB,
			jobPositionCB,
			qualificationCB,
			typeOfContractCB,
			resetBtn,
			postOfferBtn
		};
	}

	@Override
	public void visible() {
		controller.update();
	}
}
