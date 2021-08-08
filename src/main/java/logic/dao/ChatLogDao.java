package logic.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.ChatLogEntryModel;

/**
 * @author Stefano Belli
 */
public final class ChatLogDao {
	private ChatLogDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();

	private static final String STMT_ADD_CHAT_LOG_ENTRY = 
		"{ call AddChatLogEntry(?,?,?,?) }";
	private static final String STMT_GET_CHAT_LOG = 
		"{ call GetChatLog(?,?,?,?) }";
	private static final String NON_EXISTANT_USER_ERROR = 
		"Non-existant user";
	private static final String STMT_GET_CHATTING_PEERS =
		"{ call GetChattingPeers(?) }";
	private static final String STMT_GET_LAST_MSG_WITH_PEER =
		"{ call GetLastMessageWithPeer(?,?) }";
	private static final String ERR_MORE_LASTMSGS_THAN_ONE = 
		"More than one last messages retrieved";

	public static void addLogEntry(ChatLogEntryModel entry) 
			throws DataAccessException, DataLogicException {
		try(CallableStatement stmt = CONN.prepareCall(STMT_ADD_CHAT_LOG_ENTRY)) {
			stmt.setString(1, entry.getSenderEmail());
			stmt.setString(2, entry.getReceiverEmail());
			stmt.setString(3, entry.getText());
			stmt.setTimestamp(4, new Timestamp(entry.getDeliveryRequestTime()));
			stmt.execute();
		} catch(SQLException e) {
			if(e.getErrorCode() == 1452) {
				throw new DataLogicException(NON_EXISTANT_USER_ERROR);
			}

			throw new DataAccessException(e);
		}
	}

	public static List<ChatLogEntryModel> getLog(
			String senderEmail, String receiverEmail, 
			long deliverRequestFromTime, long deliverRequestToTime) 
				throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_CHAT_LOG)) {
			stmt.setString(1, senderEmail);
			stmt.setString(2, receiverEmail);
			stmt.setTimestamp(3, new Timestamp(deliverRequestFromTime));
			stmt.setTimestamp(4, new Timestamp(deliverRequestToTime));
			stmt.execute();

			try(ResultSet rs = stmt.getResultSet()) {
				List<ChatLogEntryModel> cle = new ArrayList<>();

				while(rs.next()) {
					ChatLogEntryModel model = new ChatLogEntryModel();
					model.setLogEntryId(rs.getLong(1));
					model.setSenderEmail(rs.getString(2));
					model.setReceiverEmail(rs.getString(3));
					model.setText(rs.getString(4));
					model.setDeliveryRequestTime(rs.getTimestamp(5).getTime());

					cle.add(model);
				}

				return cle;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static List<String> getChattingPeers(String email) 
			throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_CHATTING_PEERS)) {
			stmt.setString(1, email);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<String> remotes = new ArrayList<>();

				while (rs.next()) {
					remotes.add(rs.getString(1));
				}

				return remotes;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static ChatLogEntryModel getLastMessageWithPeer(String firstEmail, String secondEmail) 
			throws DataAccessException, DataLogicException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_LAST_MSG_WITH_PEER)) {
			stmt.setString(1, firstEmail);
			stmt.setString(2, secondEmail);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				ChatLogEntryModel entry = new ChatLogEntryModel();

				if(rs.next()) {
					entry.setLogEntryId(rs.getLong(1));
					entry.setSenderEmail(rs.getString(2));
					entry.setReceiverEmail(rs.getString(3));
					entry.setText(rs.getString(4));
					entry.setDeliveryRequestTime(rs.getTimestamp(5).getTime());
				}

				if(rs.next()) {
					throw new DataLogicException(ERR_MORE_LASTMSGS_THAN_ONE);
				}

				return entry;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
