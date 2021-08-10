package logic.graphicscontroller;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.controller.LoginController;
import logic.exception.InternalException;

/**
 * Ha la responsabilità di mantenere l'utente globale attivo
 */
public final class LoginHandler {
	private LoginHandler() {}

	private static UserBean sessionUser = null;

	public static boolean login(UserAuthBean userAuthBean) 
			throws InternalException {
		sessionUser = LoginController.basicLogin(userAuthBean);
		return sessionUser != null;
	}

	public static UserBean getSessionUser() {
		return sessionUser;
	}
	
	// Modifica dell'utente di sessione in caso di edit
	public static void setSessionUser(UserBean user) {
		sessionUser = user;
	}

	public static void logout() {
		sessionUser = null;
	}
}
