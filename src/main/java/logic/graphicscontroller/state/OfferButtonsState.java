package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

public interface OfferButtonsState {
	
	public void login(Context context,UserBean user, OfferBean offer, Button chatBtn);
	public void logout(Context context);
	public void candidate(Context context, Button candidateBtn);
	
}
