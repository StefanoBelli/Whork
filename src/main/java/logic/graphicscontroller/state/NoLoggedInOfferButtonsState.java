package logic.graphicscontroller.state;


import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;
import logic.controller.CandidatureController;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.util.GraphicsUtil;

public final class NoLoggedInOfferButtonsState implements OfferButtonsState {

	
	@Override
	public void login(Context context,UserBean user, OfferBean offer, Button chatBtn) {
		if(user==null || offer==null) {
			context.logout();
			return;
		}
		if(user.isEmployee()) {
			context.setState(new NoLoggedInOfferButtonsState());
			context.logout();
		} else
			try {
				if(CandidatureController.getCandidature(offer.getId(), user.getCf()) != null) {
					context.setState(new LoggedInAlreadyCandidatedOfferButtonsState());
					context.candidate();
				}else {
					context.setState(new LoggedInOfferButtonsState());					
				}
			} catch (DataAccessException | DataLogicException e) {
				GraphicsUtil.showExceptionStage(e);
			}
		
		
	}

	@Override
	public void logout(Context context) {
		
	}

	@Override
	public void candidate(Context context, Button candidateBtn) {
		
	}

	


}
