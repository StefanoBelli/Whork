package logic.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import logic.bean.OfferBean;
import logic.graphicscontroller.HomeViewController;
import logic.graphicscontroller.LoginHandler;
import logic.graphicscontroller.state.Context;
import logic.util.GraphicsUtil;
import logic.util.Util;

public final class OfferItem {
	private static final double WEBVIEW_MAX_HEIGHT = 200;
	private static final double MAX_WIDTH = 200;

	public OfferItem() {
	}

	private HBox itemBox;
	private ImageView offerImg;
	private Text offerNameTxt;
	private Label offerDescriptionLbl;
	private Label socialReasonLbl;
	private Label salaryLbl;
	private Label workShiftLbl;
	private Label jobPositionLbl;
	private Label jobCategoryLbl;
	private Label qualificationLbl;
	private Label typeOfContractLbl;
	private Label publishDateLbl;
	private WebView mapWebView;
	private Button chatBtn;
	private Button candidateBtn;
	private Context context;
	
	private void init() {
		itemBox=new HBox();
		offerImg = new ImageView();
		offerNameTxt = new Text();
		offerDescriptionLbl= new Label();
		socialReasonLbl= new Label();
		salaryLbl= new Label();
		workShiftLbl= new Label();
		jobPositionLbl= new Label();
		jobCategoryLbl= new Label();
		qualificationLbl= new Label();
		typeOfContractLbl= new Label();
		publishDateLbl= new Label();
		mapWebView= new WebView();
		chatBtn=new Button("Chat with Recruiter");
		candidateBtn=new Button("Candidate");
	}
	

	public void setInfo(OfferBean itemBean) {
		final String usrData = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA);
		final String dflRoot = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_DFL_ROOT);
		

		
		init();
		
		offerImg.setFitWidth(MAX_WIDTH);
		offerImg.setPreserveRatio(true);
		
		if(itemBean.getPhoto()!=null) {
			offerImg.setImage(
					new Image(new StringBuilder(usrData).append("/").append(itemBean.getPhoto()).toString()));
		}else {
			offerImg.setImage(
					new Image(new StringBuilder("file:").append(dflRoot).append("/offerPhoto.jpg").toString()));
		}
		
		offerNameTxt.setText("Offer Name: " + itemBean.getOfferName()+
				(itemBean.isVerifiedByWhork() ? "(Verified by Whork)" : ""));
		offerNameTxt.setFont(GraphicsUtil.getBoldFont());
		offerDescriptionLbl.setText("Description: "+itemBean.getDescription());
		
		socialReasonLbl.setText("Company: "+itemBean.getCompany().getSocialReason());
		
		salaryLbl.setText("Salary: "+Integer.toString(itemBean.getSalaryEUR())+" €");
		workShiftLbl.setText("Work Shift: "+itemBean.getWorkShit());
		jobPositionLbl.setText("Position: "+itemBean.getJobPosition().getPosition());
		jobCategoryLbl.setText("Category: "+itemBean.getJobCategory().getCategory());
		qualificationLbl.setText("Requirements qualification: "+itemBean.getQualification().getQualify());
		typeOfContractLbl.setText("Type Of Contract: "+itemBean.getTypeOfContract().getContract());
		publishDateLbl.setText("Publish Date: "+itemBean.getPublishDate().toString());
		mapWebView.getEngine().loadContent(getMapsIframe(itemBean.getJobPhysicalLocationFullAddress()));

		mapWebView.setMaxHeight(WEBVIEW_MAX_HEIGHT);
		mapWebView.setMaxWidth(MAX_WIDTH);
		mapWebView.setDisable(true);
		

		context=new Context(LoginHandler.getSessionUser(), itemBean, candidateBtn, chatBtn);
		
		//chatBtn.setDisable(!context.getValue().get(0));
		//candidateBtn.setDisable(!context.getValue().get(1));

		
		
		setListeners(itemBean);
	}

	private void setListeners(OfferBean itemBean) {
		chatBtn.setOnMouseClicked(new HomeViewController.HandleChatRequest(itemBean));
		candidateBtn.setOnMouseClicked(new HomeViewController.HandleCandidateRequest(itemBean, candidateBtn, context));
	}

	public Node getBox() {
		itemBox.getChildren().add(offerImg);
		
		itemBox.setSpacing(20);
		
		VBox offer= new VBox();
		offer.getChildren().add(offerNameTxt);
		offer.getChildren().add(offerDescriptionLbl);
		HBox offerDetails = new HBox();
		VBox vbox1 = new VBox();
		vbox1.getChildren().add(socialReasonLbl);
		vbox1.getChildren().add(salaryLbl);
		vbox1.getChildren().add(workShiftLbl);

		vbox1.getChildren().add(publishDateLbl);
		offerDetails.getChildren().add(vbox1);
		offerDetails.setSpacing(20);
		VBox vbox2=new VBox();
		vbox2.getChildren().add(jobCategoryLbl);
		vbox2.getChildren().add(jobPositionLbl);
		vbox2.getChildren().add(typeOfContractLbl);
		vbox2.getChildren().add(qualificationLbl);
		offerDetails.getChildren().add(vbox2);
		offer.getChildren().add(offerDetails);
		
		itemBox.getChildren().add(offer);
		itemBox.getChildren().add(mapWebView);
		
		VBox buttons = new VBox();
		buttons.getChildren().add(candidateBtn);
		buttons.setSpacing(20);
		buttons.getChildren().add(chatBtn);
		itemBox.getChildren().add(buttons);
		
		return itemBox;
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
}
