package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

/**
 * @author Michele Tosi
 */
public final class Context {
	/* package-private */ OfferButtonsState state;
	private Button candidateBtn;
	
	public Context(UserBean user, OfferBean offer, Button candidateBtn, Button chatBtn) {
		this.candidateBtn=candidateBtn;
		this.state=new NoLoggedInOfferButtonsState();
		this.state.login(this, user, offer, candidateBtn, chatBtn);
	}

	public void candidate() {
		state.candidate(this, candidateBtn);
	}
}
