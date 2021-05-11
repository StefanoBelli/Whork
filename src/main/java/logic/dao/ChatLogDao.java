package logic.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.ChatLogEntryModel;
import logic.model.EmployeeUserModel;
import logic.model.JobSeekerUserModel;

public final class ChatLogDao {
	private ChatLogDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();

	private static final String MAIN_STMT_NEW_CHAT_LOG_ENTRY = 
		"{ call NewChatLogEntry(?,?,?,?,?,?,?) }";
	private static final String MAIN_STMT_FLAG_MESSAGE_AS_DELIVERED = 
		"{ call FlagMessageAsDelivered(?,?,?) }";
	private static final String MAIN_STMT_GET_CHAT_LOG = 
		"{ call GetChatLog(?,?) }";

	public static void newChatLogEntry(EmployeeUserModel employee, JobSeekerUserModel jobSeeker, ChatLogEntryModel chatLogEntry) 
			throws DataAccessException {
		try(CallableStatement stmt = CONN.prepareCall(MAIN_STMT_NEW_CHAT_LOG_ENTRY)) {
			stmt.setString(1, employee.getCf());
			stmt.setString(2, jobSeeker.getCf());
			stmt.setInt(3, chatLogEntry.getScopeId());
			stmt.setString(4, chatLogEntry.getMessage());
			stmt.setTimestamp(5, new Timestamp(chatLogEntry.getDateSent().getTime()));
			Date dateDelivered = chatLogEntry.getDateDelivered();
			if(dateDelivered == null) {
				stmt.setTimestamp(6, null);
			} else {
				stmt.setTimestamp(6, new Timestamp(dateDelivered.getTime()));
			}
			stmt.setString(7, chatLogEntry.getSender().getCf());
			stmt.setString(8, chatLogEntry.getReceiver().getCf());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void flagMessageAsDelivered(EmployeeUserModel employee, JobSeekerUserModel jobSeeker, ChatLogEntryModel chatLogEntry) 
			throws DataAccessException {
		try(CallableStatement stmt = CONN.prepareCall(MAIN_STMT_FLAG_MESSAGE_AS_DELIVERED)) {
			stmt.setString(1, employee.getCf());
			stmt.setString(2, jobSeeker.getCf());
			stmt.setInt(3, chatLogEntry.getScopeId());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static List<ChatLogEntryModel> getChatLog(EmployeeUserModel employee, JobSeekerUserModel jobSeeker) 
			throws DataAccessException, DataLogicException {
		List<ChatLogEntryModel> entries = new ArrayList<>();

		try(CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_CHAT_LOG)) {
			stmt.setString(1, employee.getCf());
			stmt.setString(2, jobSeeker.getCf());
			stmt.execute();
			
			try(ResultSet rs = stmt.getResultSet()) {
				do {
					ChatLogEntryModel entry = new ChatLogEntryModel();
					entry.setScopeId(rs.getInt(1));
					entry.setMessage(rs.getString(2));
					entry.setDateSent(rs.getTimestamp(3));
					entry.setDateDelivered(rs.getTimestamp(4));
					entry.setSender(UserDao.getUserByCf(rs.getString(5)));
					entry.setReceiver(UserDao.getUserByCf(rs.getString(6)));

					entries.add(entry);
				} while(rs.next());
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}

		return entries;
	}
}
