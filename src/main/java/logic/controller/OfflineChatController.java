package logic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import logic.bean.ChatLogEntryBean;
import logic.bean.UserBean;
import logic.dao.ChatLogDao;
import logic.dao.UserDao;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.exception.InternalException;
import logic.factory.BeanFactory;
import logic.factory.ModelFactory;
import logic.model.ChatLogEntryModel;
import logic.util.Util;

/**
 * @author Stefano Belli
 */
public final class OfflineChatController {
	private OfflineChatController() {}

	private static final String UNABLE_RETR_CHATLOGS =
		"Unable to retrieve chat logs";

	private static final String UNABLE_PUSH_CHATLOG_ENTRY =
		"Unable to push chat log entry";

	private static final String UNABLE_GET_EMAIL =
		"Unable to get email for employee";

	public static List<ChatLogEntryBean> retrieveLastMessages(String meEmail, String remoteEmail) 
			throws InternalException {
		final long now = new Date().getTime();
		List<ChatLogEntryModel> clem;

		try {
			clem = ChatLogDao.getLog(meEmail, remoteEmail, now, now - 43200000);
		} catch (DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(UNABLE_RETR_CHATLOGS);
		}

		List<ChatLogEntryBean> cleb = new ArrayList<>();
		
		for(final ChatLogEntryModel entry : clem) {
			cleb.add(BeanFactory.buildChatEntryLogBean(entry));
		}

		Collections.reverse(cleb);
		return cleb;
	}

	public static ChatLogEntryBean pushMessage(String meEmail, String remoteEmail, String text) 
			throws InternalException {
		ChatLogEntryModel model = new ChatLogEntryModel();
		model.setText(text);
		model.setReceiverEmail(remoteEmail);
		model.setSenderEmail(meEmail);
		model.setDeliveryRequestTime(new Date().getTime());

		try {
			ChatLogDao.addLogEntry(model);
		} catch(DataAccessException | DataLogicException e) {
			Util.exceptionLog(e);
			throw new InternalException(UNABLE_PUSH_CHATLOG_ENTRY);
		}

		return BeanFactory.buildChatEntryLogBean(model);
	}

	public static String getEmployeeEmail(UserBean userBean) 
			throws InternalException{
		try {
			return UserDao.getEmployeeEmailByCf(ModelFactory.buildUserModel(userBean));
		} catch (DataLogicException | DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(UNABLE_GET_EMAIL);
		}
	}

	public static String getJobSeekerEmail(UserBean userBean) 
			throws InternalException {
		try {
			return UserDao.getJobSeekerEmailByCf(ModelFactory.buildUserModel(userBean));
		} catch (DataLogicException | DataAccessException e) {
			Util.exceptionLog(e);
			throw new InternalException(UNABLE_GET_EMAIL);
		}
	}
}
