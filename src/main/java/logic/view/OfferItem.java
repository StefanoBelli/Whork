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
import logic.graphicscontroller.HomeViewController;
import logic.util.GraphicsUtil;
import logic.util.Util;

public final class OfferItem {
	private boolean isNoUserLoggedIn;

	public OfferItem(boolean isNoUserLoggedIn) {
		this.isNoUserLoggedIn = isNoUserLoggedIn;
	}
	
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
		final String usrData = Util.InstanceConfig.getString(Util.InstanceConfig.KEY_USR_DATA);

		offerImg.setImage(
			new Image(new StringBuilder(usrData).append("/").append(itemBean.getPhoto()).toString()));
		offerNameLbl.setText(itemBean.getOfferName());
		offerDescriptionLbl.setText(itemBean.getDescription());
		
		try {
			socialReasonLbl.setText(OfferController.getCompanyByVAT(itemBean).getSocialReason());
		} catch (DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}
		
		salaryLbl.setText(Integer.toString(itemBean.getSalaryEUR()));
		workShiftLbl.setText(itemBean.getWorkShit());
		jobPositionLbl.setText(itemBean.getJobPosition());
		jobCategoryLbl.setText(itemBean.getJobCategory());
		qualificationLbl.setText(itemBean.getQualification());
		typeOfContractLbl.setText(itemBean.getTypeOfContract());
		publishDateLbl.setText(itemBean.getPublishDate().toString());
		verifiedByWhorkLbl.setText(itemBean.isVerifiedByWhork() ? "Verified by Whork" : "");
		mapWebView.getEngine().loadContent(getMapsIframe(itemBean.getJobPhysicalLocationFullAddress()));
		chatBtn.setDisable(isNoUserLoggedIn);
		candidateBtn.setDisable(isNoUserLoggedIn);

		setListeners(itemBean);
	}

	private void setListeners(OfferBean itemBean) {
		chatBtn.setOnMouseClicked(new HomeViewController.HandleChatRequest(itemBean));
		candidateBtn.setOnMouseClicked(new HomeViewController.HandleCandidateRequest(itemBean));
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
