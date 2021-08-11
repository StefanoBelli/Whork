package logic.graphicscontroller;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import logic.bean.UserBean;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.PostOfferView;
import logic.view.ViewStack;

public class AccountCompanyViewController extends GraphicsController {
	
	private static final double MAX_WIDTH = 200;
	private static final String WHORK = "whork";

	private Button homeBtn;
	private Button postOfferBtn;
	private Button logOutBtn;
	private Text nameAdminText;
	private Text nameCompanyText;
	private ImageView imgAdminView;

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
		
		user = LoginHandler.getSessionUser();

		setPostOfferButton();
		setHeader();
		
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



