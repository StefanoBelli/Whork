package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

public final class LoggedInOfferButtonsState implements OfferButtonsState {

	@Override
	public void candidate(Context context, Button candidateBtn) {
		context.setState(new LoggedInAlreadyCandidatedOfferButtonsState());
		candidateBtn.setDisable(true);
	}

	@Override
	public void login(Context context,UserBean user, OfferBean offer,Button candidateBtn, Button chatBtn) {
		
	}

	



}
