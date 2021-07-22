package logic.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import logic.bean.OfferBean;
import logic.controller.OfferController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.graphicscontroller.AccountViewController;
import logic.graphicscontroller.LoginHandler;
import logic.util.Util;

public class OfferItem {
	
	private HBox itemBox;
	
	private ImageView offerImg;
	
	private Label offerNameLbl;
	
	private Label offerDescriptionLbl;
	
	private Label socialReasonLbl;
	
	private Label salaryLbl;
	
	private Label workShiftLbl;
	
	private Label jobPositionLbl;
	
	private Label jobCategoryLbl;

	private Label qualificationLbl;
	
	private Label typeOfContractLbl;

	private Label publishDateLbl;
	
	private Label verifiedByWhorkLbl;

	private WebView mapWebView;
	
	private Button chatBtn;
	
	private Button candidateBtn;
	

	
	

	public void setInfo(OfferBean itemBean) {
		offerImg.setImage(new Image(Util.InstanceConfig.getString(Util.InstanceConfig.KEY_CTX_USR_DATA) + "/" + itemBean.getPhoto()));
		offerNameLbl.setText(itemBean.getOfferName());
		offerDescriptionLbl.setText(itemBean.getDescription());
		
		try {
			socialReasonLbl.setText(OfferController.getCompanyByVAT(itemBean).getSocialReason());
		} catch (DataAccessException | DataLogicException e) {
			e.addSuppressed(e);
		}
		
		salaryLbl.setText(Integer.toString(itemBean.getSalaryEUR()));
		
		workShiftLbl.setText(itemBean.getWorkShit());
		
		jobPositionLbl.setText(itemBean.getJobPosition());
		
		jobCategoryLbl.setText(itemBean.getJobCategory());
		
		qualificationLbl.setText(itemBean.getQualification());
		
		typeOfContractLbl.setText(itemBean.getTypeOfContract());

		publishDateLbl.setText(itemBean.getPublishDate().toString());
		
		verifiedByWhorkLbl.setText(itemBean.isVerifiedByWhork()?"Verified by Whork":"");
		
		mapWebView.getEngine().loadContent(getMapsIframe(itemBean.getJobPhysicalLocationFullAddress()));
		
		if(LoginHandler.getSessionUser() == null) {
			chatBtn.setDisable(true);
			candidateBtn.setDisable(true);
		}else {
			chatBtn.setDisable(false);
			candidateBtn.setDisable(false);
		}	
		
		setListeners(itemBean);
	}

	private void setListeners(OfferBean itemBean) {
		chatBtn.setOnMouseClicked(new AccountViewController.HandleChatRequest(itemBean));
		candidateBtn.setOnMouseClicked(new AccountViewController.HandleCandidateRequest(itemBean));
		
	}

	
	public Node getBox() {
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
