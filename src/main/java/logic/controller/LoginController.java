package logic.controller;

import logic.bean.UserAuthBean;
import logic.bean.UserBean;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.exception.SendMailException;
import logic.exception.SyntaxException;
import logic.factory.BeanFactory;
import logic.dao.UserAuthDao;
import logic.dao.UserDao;
import logic.util.Pair;
import logic.util.Util;
import logic.model.PasswordRestoreModel;
import logic.model.UserAuthModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public final class LoginController {
	private LoginController() {}

	private static ReentrantLock mutexNewReq = new ReentrantLock();
	private static ReentrantLock mutexOldReq = new ReentrantLock();
	private static Thread pwdRecCleanupThread = new Thread(
			new PasswordRecoveryPendingCleanup(), "Thread-PwdRecCleanup");

	static {
		pwdRecCleanupThread.start();
	}

	/**
	 * This method will interact with UserDao to fetch data from
	 * DB and attempt a login for user (standard email and password).
	 * 
	 * Password are stored using BCrypt salted hash.
	 * 
	 * Once email / password are compared and matched, this controller
	 * method will load user personal data interacting, again, with UserDao.
	 * (see return).
	 * 
	 * @param userAuthBean : object containing email / password
	 * @return UserBean object representing user data or null if no matching
	 */
	public static UserBean basicLogin(UserAuthBean userAuthBean) 
			throws InternalException {
		try {
			Pair<String, ByteArrayInputStream> pair = UserAuthDao.getUserCfAndBcryPwdByEmail(
													userAuthBean.getEmail());
			if(pair == null) {
				return null; // email was not found
			}

			byte[] bcryptedPwd;
			try(ByteArrayInputStream bcryptedPwdIstream = pair.getSecond()) {
				bcryptedPwd = bcryptedPwdIstream.readAllBytes();
			}

			if(Util.Bcrypt.equals(userAuthBean.getPassword(), bcryptedPwd)) {
				return BeanFactory.buildUserBean(
							UserDao.getUserByCf(pair.getFirst()));
			}

			return null; // passwords are not matching
		} catch(IOException e) {
			Util.exceptionLog(e);
			throw new InternalException("General I/O error");
		} catch(DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data access error");
		} catch(DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data logic error");
		} catch(SyntaxException e) {
			Util.exceptionLog(e);
			throw new InternalException("Data syntax error");
		}
	}

	/**
	 * 
	 * @param email
	 */
	public static void recoverPassword(String email) {
		PasswordRestoreModel passwordRestoreModel = null;
		boolean success = true;

		try {
			mutexNewReq.lock();

			passwordRestoreModel = 
				UserAuthDao.getPasswordRestorePendingRequestByEmail(email);

			addOrUpdatePwdReq(passwordRestoreModel, email);
		} catch(DataLogicException | DataAccessException e) {
			Util.exceptionLog(e);
			success = false;
		} finally {
			mutexNewReq.unlock();
		}

		if(success) {
			Thread t = new Thread(new SendPwdReqLinkViaEmail(passwordRestoreModel));
			t.setDaemon(true);
			t.start();
		}
	}

	/**
	 * @param token
	 * @param password
	 * @return if password is changed or not
	 */
	public static boolean changePassword(String token, String password) {
		PasswordRestoreModel passwordRestoreModel;

		try {
			mutexOldReq.lock();
			
			passwordRestoreModel
				= getPasswordRestoreModelFromToken(token);
		} finally {
			mutexOldReq.unlock();
		}

		if(passwordRestoreModel != null) {
			return effectivelyChangePassword(passwordRestoreModel, password);
		}

		return false;
	}

	private static void addOrUpdatePwdReq(PasswordRestoreModel passwordRestoreModel, String email) 
			throws DataAccessException {
		if(passwordRestoreModel == null) { /* new request */
			passwordRestoreModel = new PasswordRestoreModel();
			passwordRestoreModel.setEmail(email);
			passwordRestoreModel.setDate(new Date());
			passwordRestoreModel.setToken(generatePwdReqToken());
			UserAuthDao.newPasswordRestorePendingRequest(passwordRestoreModel);
		} else { /* update old request */
			passwordRestoreModel.setDate(new Date());
			passwordRestoreModel.setToken(generatePwdReqToken());
			UserAuthDao.updatePasswordRestorePendingRequest(passwordRestoreModel);
		}
	}

	private static PasswordRestoreModel getPasswordRestoreModelFromToken(String token) {
		PasswordRestoreModel passwordRestoreModel;

		try {
			passwordRestoreModel = UserAuthDao.getSinglePasswordRestorePendingRequest(token);
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			return null;
		}

		if (passwordRestoreModel == null) {
			return null;
		}

		try {
			UserAuthDao.delPasswordRestorePendingRequest(token);
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
		}

		if (isNotValidRestoreRequest(passwordRestoreModel)) {
			return null;
		}

		return passwordRestoreModel;
	}

	private static boolean effectivelyChangePassword(
			PasswordRestoreModel passwordRestoreModel, String password) {

		try (ByteArrayInputStream bcryptedPassword = 
				new ByteArrayInputStream(Util.Bcrypt.hash(password))) {
			UserAuthModel userAuthModel = new UserAuthModel();
			userAuthModel.setEmail(passwordRestoreModel.getEmail());
			userAuthModel.setBcryptedPassword(bcryptedPassword);
			UserAuthDao.changeUserAuthPassword(userAuthModel);
		} catch (IOException | DataAccessException e) {
			Util.exceptionLog(e);
			return false;
		}

		return true;
	}

	private static boolean isNotValidRestoreRequest(PasswordRestoreModel passwordRestoreModel) {
		final int INVALID_DIFF_MS = 86400000; // 24 hrs

		long nowTime = new Date().getTime();
		long reqTime = passwordRestoreModel.getDate().getTime();

		return nowTime - reqTime > INVALID_DIFF_MS;
	}

	private static String generatePwdReqToken() {
		StringBuilder builder = new StringBuilder();
		builder.append(Long.toString(new Date().getTime()));
		builder.append("-");
		builder.append(UUID.randomUUID().toString());

		return builder.toString();
	}

	private static final class PasswordRecoveryPendingCleanup implements Runnable {
		private static final int CLEANUP_EACHMS = 43200000; // 12 hrs

		private void deleteNoMoreValidPendingRequests() {
			List<PasswordRestoreModel> requests;
			try {
				requests = 
					UserAuthDao.getPasswordRestorePendingRequest();
			} catch(DataAccessException e) {
				Util.exceptionLog(e);
				return;
			}

			List<PasswordRestoreModel> noMoreValidRequests = new ArrayList<>();
			for(final PasswordRestoreModel req : requests) {
				if(isNotValidRestoreRequest(req)) {
					noMoreValidRequests.add(req);
				}
			}

			for(final PasswordRestoreModel invalidReq : noMoreValidRequests) {
				try {
					UserAuthDao.delPasswordRestorePendingRequest(invalidReq.getToken());
				} catch(DataAccessException e) {
					Util.exceptionLog(e);
				}
			}
		}

		@Override
		public void run() {
			while(true) {
				try {
					mutexNewReq.lock();
					mutexOldReq.lock();
					deleteNoMoreValidPendingRequests();
				} finally {
					mutexNewReq.unlock();
					mutexOldReq.unlock();
				}

				try {
					Thread.sleep(CLEANUP_EACHMS);
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}

	private static final class SendPwdReqLinkViaEmail implements Runnable {
		private PasswordRestoreModel passwordRestoreModel;

		private SendPwdReqLinkViaEmail(PasswordRestoreModel passwordRestoreModel) {
			this.passwordRestoreModel = passwordRestoreModel;
		}

		@Override
		public void run() {
			StringBuilder builder = new StringBuilder();
			builder.append("Whork recieved a request to restore your password:");
			builder.append(" if you requested so, click on the link below:\n\n");
			builder.append("/changepwd.jsp?token=");
			builder.append(passwordRestoreModel.getToken());
			try {
				Util.Mailer.sendMail(
					passwordRestoreModel.getEmail(), 
					"Whork password reset", 
					builder.toString());
			} catch (SendMailException e) {
				Util.exceptionLog(e);
			}
		}
	}
}
