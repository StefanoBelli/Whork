package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;
import logic.controller.CandidatureController;
import logic.exception.InternalException;
import logic.util.GraphicsUtil;

/* package-private */ final class NoLoggedInOfferButtonsState implements OfferButtonsState {	
	@Override
	public void login(Context context,UserBean user, OfferBean offer, Button candidateBtn, Button chatBtn) {
		if(user==null || offer==null) {
			disableBtns(candidateBtn, chatBtn);
			return;
		}
		
		if(user.isEmployee()) {
			context.state = new NoLoggedInOfferButtonsState();
			disableBtns(candidateBtn, chatBtn);
		} else {
			try {
				if(CandidatureController.getCandidature(offer.getId(), user.getCf()) != null) {
					context.state = new LoggedInAlreadyCandidatedOfferButtonsState();
					candidateBtn.setDisable(true);
				} else {
					context.state = new LoggedInOfferButtonsState();		
				}
			} catch (InternalException e) {
				GraphicsUtil.showExceptionStage(e);
			}
		}
	}

	@Override
	public void candidate(Context context, Button candidateBtn) {
		//no action needed	
	}
	
	private void disableBtns(Button candidateBtn, Button chatBtn) {
		candidateBtn.setDisable(true);
		chatBtn.setDisable(true);
	}
}
