package logic.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.CallableStatement;
import java.sql.Timestamp;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.PasswordRestoreModel;
import logic.model.UserAuthModel;
import logic.model.UserModel;
import logic.util.Pair;

public final class UserAuthDao {
	private UserAuthDao() {}

	private static final Connection CONN =
		Database.getInstance().getConnection();

	private static final String STMT_GETUSERCF_AND_PWD_BYEMAIL = 
		"{ call GetUserCfAndPwdByEmail(?) }";
	private static final String STMT_CONFIRM_REG = 
		"{ call ConfirmRegistration(?,?) }";
	private static final String STMT_REGAUTH_JOBSEEKER = 
		"{ call RegisterJobSeekerUserAuth(?,?,?,?) }";
	private static final String STMT_REGAUTH_EMPLOYEE = 
		"{ call RegisterEmployeeUserAuth(?,?,?,?) }";
	private static final String STMT_GETPWDRES_PENDING =
		"{ call GetPendingPasswordRestoreRequest() }";
	private static final String STMT_NEWPWDRES_PENDING =
		"{ call NewPendingPasswordRestoreRequest(?,?,?) }";
	private static final String STMT_DELPWDRES_PENDING =
		"{ call RemovePendingPasswordRestoreRequest(?) }";
	private static final String STMT_CHANGEUSERAUTH_PASSWORD =
		"{ call ChangeUserAuthPassword(?,?) }";
	private static final String STMT_GETSINGLE_PWDRES_PENDING =
		"{ call GetSinglePendingPasswordRestoreRequest(?) }";
	private static final String STMT_GETPWDRES_PENDING_BYEMAIL =
		"{ call GetPendingPasswordRestoreRequestByEmail(?) }";
	private static final String STMT_UPDATE_PWDRES_PENDING = 
		"{ call UpdatePasswordRestorePendingRequestByEmail(?,?,?) }";
	private static final String DATA_LOGIC_ERR_TWOCF_ONECREDPAIR = 
		"Can't have both CFs with same pair email and password";
	private static final String DATA_LOGIC_ERR_ZEROCF_ONECREDPAIR = 
		"Can't have both CFs *NULL* with same pair email and password";
	private static final String DATA_LOGIC_ERR_MULTIPLE_ROWS = 
		"Multiple rows in result set, where we expect only one";

	private static String getTargetCf(String cf, String cf2) 
			throws DataLogicException {
		String target = cf;

		if (cf != null) { /* cf != null && cf2 = ??? */
			if (cf2 != null) { /* cf != null && cf2 != null */
				throw new DataLogicException(DATA_LOGIC_ERR_TWOCF_ONECREDPAIR);
			}
		} else { /* cf = null && cf2 = ??? */
			if (cf2 == null) { /* cf = null && cf2 = null */
				throw new DataLogicException(DATA_LOGIC_ERR_ZEROCF_ONECREDPAIR);
			} else { /* cf = null && cf2 != null */
				target = cf2;
			}
		}

		return target;
	}

	public static Pair<String, ByteArrayInputStream> getUserCfAndBcryPwdByEmail(String email)
			throws DataAccessException, DataLogicException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GETUSERCF_AND_PWD_BYEMAIL)) {
			stmt.setString(1, email);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if (!rs.next()) {
					return null;
				}

				String target = getTargetCf(rs.getString(1), rs.getString(2));

				Pair<String, ByteArrayInputStream> pair = 
					new Pair<>(target, (ByteArrayInputStream) rs.getBinaryStream(3));

				if (rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERR_MULTIPLE_ROWS);
				}

				return pair;
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void confirmRegistration(String email, String regToken) 
			throws DataAccessException, DataLogicException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_CONFIRM_REG)) {
			stmt.setString(1, email);
			stmt.setString(2, regToken);
			stmt.execute();
		} catch (SQLException e) {
			if (e.getSQLState().equals("45001")) {
				throw new DataLogicException(e.getMessage());
			}

			throw new DataAccessException(e);
		}
	}

	public static void registerUserAuth(
			UserModel userModel, UserAuthModel userAuthModel, String regToken) 
				throws DataAccessException {
		String callStmt = userModel.isEmployee() ? 
			STMT_REGAUTH_EMPLOYEE : STMT_REGAUTH_JOBSEEKER;

		try (CallableStatement stmt = CONN.prepareCall(callStmt)) {
			stmt.setString(1, userAuthModel.getEmail());
			stmt.setBinaryStream(2, userAuthModel.getBcryptedPassword());
			stmt.setString(3, userModel.getCf());
			stmt.setString(4, regToken);
			stmt.execute();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static List<PasswordRestoreModel> getPasswordRestorePendingRequest() 
			throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GETPWDRES_PENDING)) {
			stmt.execute();

			try(ResultSet rs = stmt.getResultSet()) {
				List<PasswordRestoreModel> pwdPend = new ArrayList<>();
				if(rs.next()) {
					do {
						PasswordRestoreModel model = new PasswordRestoreModel();
						model.setToken(rs.getString(1));
						model.setDate(rs.getTimestamp(2));
						model.setEmail(rs.getString(3));

						pwdPend.add(model);
					} while(rs.next());
				}

				return pwdPend;
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void newPasswordRestorePendingRequest(PasswordRestoreModel passwordRestoreModel) 
			throws DataAccessException {
		try(CallableStatement stmt = CONN.prepareCall(STMT_NEWPWDRES_PENDING)) {
			stmt.setString(1, passwordRestoreModel.getToken());
			stmt.setTimestamp(2, 
				new Timestamp(
					passwordRestoreModel.getDate().getTime()));
			stmt.setString(3, passwordRestoreModel.getEmail());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void delPasswordRestorePendingRequest(String token) 
			throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_DELPWDRES_PENDING)) {
			stmt.setString(1, token);
			stmt.execute();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static PasswordRestoreModel getSinglePasswordRestorePendingRequest(String token) 
			throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GETSINGLE_PWDRES_PENDING)) {
			stmt.setString(1, token);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				PasswordRestoreModel model = null;
				if (rs.next()) {
					model = new PasswordRestoreModel();
					model.setToken(token);
					model.setDate(rs.getTimestamp(1));
					model.setEmail(rs.getString(2));
				}

				return model;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static PasswordRestoreModel getPasswordRestorePendingRequestByEmail(String email) 
			throws DataAccessException, DataLogicException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_GETPWDRES_PENDING_BYEMAIL)) {
			stmt.setString(1, email);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				PasswordRestoreModel model = null;
				if (rs.next()) {
					model = new PasswordRestoreModel();
					model.setToken(rs.getString(1));
					model.setDate(rs.getTimestamp(2));
					model.setEmail(email);
				}

				if(rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERR_MULTIPLE_ROWS);
				}

				return model;
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void updatePasswordRestorePendingRequest(PasswordRestoreModel passwordRestoreModel)
			throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_UPDATE_PWDRES_PENDING)) {
			stmt.setString(1, passwordRestoreModel.getEmail());
			stmt.setString(2, passwordRestoreModel.getToken());
			stmt.setTimestamp(3,
				new Timestamp(
					passwordRestoreModel.getDate().getTime()));
			stmt.execute();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void changeUserAuthPassword(UserAuthModel userAuthModel) 
			throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_CHANGEUSERAUTH_PASSWORD)) {
			stmt.setString(1, userAuthModel.getEmail());
			stmt.setBinaryStream(2, userAuthModel.getBcryptedPassword());
			stmt.execute();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
