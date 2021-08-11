package logic.graphicscontroller;

import java.io.IOException;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.bean.UserBean;
import logic.controller.AccountController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.PostOfferView;
import logic.view.ViewStack;

public class AccountCompanyViewController extends GraphicsController {
	
	private static final double MAX_WIDTH = 200;
	//private static final String WHORK = "whork";

	private final static String JAN = "Jan";
	private final static String FEB = "Feb";
	private final static String MAR = "Mar";
	private final static String APR = "Apr";
	private final static String MAY = "May";
	private final static String JUN = "Jun";
	private final static String JUL = "Jul";
	private final static String AUG = "Aug";
	private final static String SEP = "Sep";
	private final static String OCT = "Oct";
	private final static String NOV = "Nov";
	private final static String DEC = "Dec";

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

	UserBean user;

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
		
		user = LoginHandler.getSessionUser();

		setPostOfferButton();
		setHeader();
		setNumber();
		setNumberCandidateChart();
		
		setListeners();
	}
	
	private void setListeners() {
		homeBtn.setOnMouseClicked(new HandleHomeRequest());
		postOfferBtn.setOnMouseClicked(new HandlePostOfferRequest());
		logOutBtn.setOnMouseClicked(new HandleLogoutRequest());
	}

	private void setPostOfferButton() {
		if(user.isRecruiter()) postOfferBtn.setVisible(true);
		else postOfferBtn.setVisible(false);
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
		series = new XYChart.Series<Number, String>();
		
		candidateBarChart.setTitle("Number of Candidate");
		yAxis.setCategories(FXCollections.<String>observableArrayList(
		        Arrays.asList(JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC)));
		
		//series.setName("2003");
		series.getData().add(new XYChart.Data<Number, String>(25601.34, JAN));
		series.getData().add(new XYChart.Data<Number, String>(20148.82, FEB));
		series.getData().add(new XYChart.Data<Number, String>(10000, MAR));
		series.getData().add(new XYChart.Data<Number, String>(35407.15, APR));
		series.getData().add(new XYChart.Data<Number, String>(12000, MAY));
		
		candidateBarChart.getData().add(series);
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

	private final class HandleLogoutRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			LoginHandler.logout();
			
			try {
				Util.Files.overWriteJsonAuth(null, null);
			} catch(IOException e) {
				Util.exceptionLog(e);
				GraphicsUtil.closeStageByMouseEvent(event);
				GraphicsUtil.showExceptionStage(e);
			}

			viewStack.pop();
		}
	}
}

