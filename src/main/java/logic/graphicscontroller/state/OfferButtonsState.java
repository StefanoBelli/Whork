package logic.graphicscontroller.state;

import javafx.scene.control.Button;
import logic.bean.OfferBean;
import logic.bean.UserBean;

/**
 * @author Michele Tosi
 */
/* package-private */ interface OfferButtonsState {
	void login(Context context,UserBean user, OfferBean offer, Button candidateBtn, Button chatBtn);
	void candidate(Context context, Button candidateBtn);
}
