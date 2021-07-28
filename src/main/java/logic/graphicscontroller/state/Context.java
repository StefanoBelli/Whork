package logic.graphicscontroller.state;


import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

public final class Context {
	private OfferButtonsState state;
	private UserBean user;
	private OfferBean offer;
	private Button candidateBtn;
	private Button chatBtn;
	
	public Context(UserBean user, OfferBean offer, Button candidateBtn, Button chatBtn) {
		this.user=user;
		this.offer=offer;
		this.candidateBtn=candidateBtn;
		this.chatBtn=chatBtn;
		this.state=new NoLoggedInOfferButtonsState();
		login();
	}
	
	public void setState(OfferButtonsState state) {
		this.state=state;
	}

	public void candidate() {
		state.candidate(this, candidateBtn);
	}
	
	public void login() {
		state.login(this, user, offer, candidateBtn, chatBtn);
	}
	
	

}
