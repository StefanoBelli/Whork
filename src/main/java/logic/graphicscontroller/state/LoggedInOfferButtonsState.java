package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

/**
 * @author Michele Tosi
 */
/* package-private */ final class LoggedInOfferButtonsState implements OfferButtonsState {

	@Override
	public void candidate(Context context, Button candidateBtn) {
		context.state = new LoggedInAlreadyCandidatedOfferButtonsState();
		candidateBtn.setDisable(true);
	}

	@Override
	public void login(Context context,UserBean user, OfferBean offer,Button candidateBtn, Button chatBtn) {
		//no action needed
	}
}
