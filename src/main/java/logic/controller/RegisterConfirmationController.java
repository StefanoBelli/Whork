package logic.controller;

import logic.util.Util;
import logic.dao.UserAuthDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;

public final class RegisterConfirmationController {
	protected RegisterConfirmationController() {}

	public static void confirm(String email, String regToken) 
			throws InternalException {
		try {
			UserAuthDao.confirmRegistration(email, regToken);
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data access error");
		} catch (DataLogicException e) {
			throw new InternalException(e.getMessage());
		}
	}
}
