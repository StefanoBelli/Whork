package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

public final class LoggedInOfferButtonsState implements OfferButtonsState {
	
	@Override
	public void logout(Context context) {
		context.setState(new NoLoggedInOfferButtonsState());
		context.logout();
	}

	@Override
	public void candidate(Context context, Button candidateBtn) {
		
	}

	@Override
	public void login(Context context,UserBean user, OfferBean offer, Button chatBtn) {
		
	}

	



}
