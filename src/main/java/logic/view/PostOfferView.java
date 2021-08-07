package logic.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.graphicscontroller.PostOfferViewController;
import logic.util.GraphicsUtil;

public final class PostOfferView implements ControllableView {
	
	private static final String WIN_TITLE = "Whork - Post offer";
	private static final String CATEGORY_MSG = "Category: ";
	private static final String DESCRIPTION_MSG = "Offer Description: ";
	private static final String OFFER_NAME_MSG = "Offer Name: ";
	private static final String OFFER_ADDRESS_MSG = "Address: ";
	private static final String OFFER_WORK_SHIFT_MSG = "Work Shift: ";
	private static final String OFFER_SALARY_MSG = "Offer salary (€): ";
	private static final String POST_OFFER_BTN_MSG = "Post Offer";
	private static final String PHOTO_BTN_MSG = "Sfoglia...";
	private static final String OFFER_PHOTO_MSG = "Offer photo: ";
	private static final String POSITION_MSG = "Position: ";
	private static final String QUALIFICATION_MSG = "Qualify: ";
	private static final String TYPE_OF_CONTRACT_MSG = "Type of Contract: ";
	private static final int CONFIG_WIN_WIDTH = 480;
	private static final int CONFIG_WIN_HEIGHT = 530;
	private static final int TXT_WIDTH = 290;
	private static final String OFFER_NOTE_MSG = "Offer Note: ";
	private static final String POST_OFFER_MSG = "Post an offer by filling out this form";
	
	private Text postOfferText;
	private Button postOfferBtn;
	private Label offerDescriptionMessage;
	private TextArea offerDescriptionTxt;
	private Label offerNameMessage;
	private TextField offerSalaryTxt;
	private Label offerSalaryMessage;
	private Label offerPhotoMessage;
	private Button offerPhotoBtn;
	private TextField offerNameTxt;
	private Label categoryMessage;
	private ChoiceBox<String> jobCategoryCB;
	private Label positionMessage;
	private ChoiceBox<String> jobPositionCB;
	private Label qualificationMessage;
	private ChoiceBox<String> qualificationCB;
	private Label typeOfContractMessage;
	private ChoiceBox<String> typeOfContractCB;
	private Label offerNoteMessage;
	private TextField offerNoteTxt;
	private TextField offerAddressTxt;
	private Label offerAddressMessage;
	private Label workShiftMessage;
	private TextField workShiftTxt;
	private Scene scene;
	private Label photoDetailLbl;

	private PostOfferViewController controller;
	
	public PostOfferView(ViewStack viewStack) {
		controller = new PostOfferViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}
	
	private void init() {
		postOfferText = new Text(POST_OFFER_MSG);
		offerNameMessage= new Label(OFFER_NAME_MSG);
		offerNameTxt=new TextField();
		offerSalaryMessage=new Label(OFFER_SALARY_MSG);
		offerSalaryTxt=new TextField();
		categoryMessage = new Label(CATEGORY_MSG);
		jobCategoryCB=new ChoiceBox<>();
		positionMessage = new Label(POSITION_MSG);
		jobPositionCB= new ChoiceBox<>();
		qualificationMessage = new Label(QUALIFICATION_MSG);
		qualificationCB= new ChoiceBox<>();
		typeOfContractMessage = new Label(TYPE_OF_CONTRACT_MSG);
		typeOfContractCB= new ChoiceBox<>();
		offerAddressMessage=new Label(OFFER_ADDRESS_MSG);
		offerAddressTxt = new TextField();
		workShiftMessage=new Label(OFFER_WORK_SHIFT_MSG);
		workShiftTxt= new TextField();
		offerNoteMessage= new Label(OFFER_NOTE_MSG);
		offerNoteTxt=new TextField();
		offerDescriptionMessage=new Label(DESCRIPTION_MSG);
		offerDescriptionTxt=new TextArea();
		postOfferBtn=new Button(POST_OFFER_BTN_MSG);
		offerPhotoMessage=new Label(OFFER_PHOTO_MSG);
		offerPhotoBtn=new Button(PHOTO_BTN_MSG);
		photoDetailLbl= new Label(RegisterJobSeekerView.SELECT_FILE_MESSAGE);
		controller.setup();
	}

	private void setNodesProps() {
		postOfferText.setFont(GraphicsUtil.getBoldFont());
		offerNameTxt.setMinWidth(TXT_WIDTH);
		offerSalaryTxt.setMinWidth(TXT_WIDTH);
		offerAddressTxt.setMinWidth(TXT_WIDTH);
		workShiftTxt.setMinWidth(TXT_WIDTH);
		offerNoteTxt.setMinWidth(TXT_WIDTH);
		jobCategoryCB.setMinWidth(TXT_WIDTH);
		jobPositionCB.setMinWidth(TXT_WIDTH);
		qualificationCB.setMinWidth(TXT_WIDTH);
		typeOfContractCB.setMinWidth(TXT_WIDTH);
		offerPhotoMessage.setPadding(new Insets(5));
		photoDetailLbl.setFont(Font.font(9));
		photoDetailLbl.setPadding(new Insets(10));
		photoDetailLbl.setAlignment(Pos.BOTTOM_LEFT);
		offerDescriptionTxt.setMaxWidth(455);
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10,10,0,10));
		vbox.setSpacing(10);
		
		HBox hboxdetails= new HBox();
		hboxdetails.setSpacing(60);
		VBox vboxlbl = new VBox();

		vboxlbl.setSpacing(10);
		VBox vboxtxt = new VBox();
		vboxlbl.getChildren().add(offerNameMessage);
		vboxtxt.getChildren().add(offerNameTxt);
		
		vboxlbl.getChildren().add(offerSalaryMessage);
		vboxtxt.getChildren().add(offerSalaryTxt);
		
		vboxlbl.getChildren().add(offerAddressMessage);
		vboxtxt.getChildren().add(offerAddressTxt);
		
		vboxlbl.getChildren().add(workShiftMessage);
		vboxtxt.getChildren().add(workShiftTxt);
		
		vboxlbl.getChildren().add(offerNoteMessage);
		vboxtxt.getChildren().add(offerNoteTxt);
		
		hboxdetails.getChildren().add(vboxlbl);
		hboxdetails.getChildren().add(vboxtxt);
		
		HBox hboxflt=new HBox();
		hboxflt.setSpacing(50);
		VBox vboxfltname=new VBox();

		vboxfltname.setSpacing(10);
		VBox vboxfltdetails=new VBox();
		vboxfltname.getChildren().add(categoryMessage);
		vboxfltdetails.getChildren().add(jobCategoryCB);
		
		vboxfltname.getChildren().add(positionMessage);
		vboxfltdetails.getChildren().add(jobPositionCB);
		
		vboxfltname.getChildren().add(qualificationMessage);
		vboxfltdetails.getChildren().add(qualificationCB);
	
		vboxfltname.getChildren().add(typeOfContractMessage);
		vboxfltdetails.getChildren().add(typeOfContractCB);
		
		hboxflt.getChildren().add(vboxfltname);
		hboxflt.getChildren().add(vboxfltdetails);
		
		HBox hboxphoto=new HBox();
		hboxphoto.getChildren().add(offerPhotoMessage);
		hboxphoto.getChildren().add(offerPhotoBtn);
		hboxphoto.getChildren().add(photoDetailLbl);
		
		vbox.getChildren().add(postOfferText);
		vbox.getChildren().add(hboxdetails);
		vbox.getChildren().add(hboxflt);
		vbox.getChildren().add(offerDescriptionMessage);
		vbox.getChildren().add(offerDescriptionTxt);
		vbox.getChildren().add(hboxphoto);
		vbox.getChildren().add(postOfferBtn);

		HBox hboxpostbtn= new HBox();
		hboxpostbtn.getChildren().add(postOfferBtn);
		hboxpostbtn.setPadding(new Insets(0,0,10,200));

		vbox.getChildren().add(hboxpostbtn);
		vbox.setAlignment(Pos.CENTER);

		scene = new Scene(vbox, CONFIG_WIN_WIDTH, CONFIG_WIN_HEIGHT);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WIN_TITLE);
		stage.setResizable(false);
	}

	@Override
	public void visible() {
		controller.update();
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
				postOfferBtn,
				offerDescriptionTxt,
				offerSalaryTxt,
				offerPhotoBtn,
				offerNameTxt,
				jobCategoryCB,
				jobPositionCB,
				qualificationCB,
				typeOfContractCB,
				offerNoteTxt,
				offerAddressTxt,
				workShiftTxt,
				photoDetailLbl
		};
	}
}
