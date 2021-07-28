package logic.graphicscontroller.state;



import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

public final class LoggedInAlreadyCandidatedOfferButtonsState implements OfferButtonsState {


	@Override
	public void candidate(Context context, Button candidateBtn) {
		candidateBtn.setDisable(true);
	}

	@Override
	public void login(Context context, UserBean user, OfferBean offer, 
			Button candidateBtn, Button chatBtn) {
		
	}

	

	

}
