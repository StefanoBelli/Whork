package logic.dao;

import java.sql.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import logic.Database;
import logic.model.EmployeeUserModel;
import logic.model.JobSeekerUserModel;
import logic.model.UserModel;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.util.Pair;

public final class UserDao {
	private UserDao() {}
	
	private static final String STMT_GETUSERCF_AND_PWD_BYEMAIL = "{ call GetUserCfAndPwdByEmail(?) }";
	private static final String DATA_LOGIC_ERR_TWOCF_ONECREDPAIR = 
		"Can't have both CFs with same pair email and password";
	private static final String DATA_LOGIC_ERR_ZEROCF_ONECREDPAIR = 
		"Can't have both CFs *NULL* with same pair email and password";
	private static final String DATA_LOGIC_ERR_MULTIPLE_ROWS =
		"Multiple rows in result set, where we expect only one";
	private static final String STMT_GETUSER_BYCF = "{ call GetUserDetails(?) }";
	private static final String DATA_LOGIC_ERR_MULTIPLE_USERS =
		"Multiple users for same CF";
	private static final String DATA_LOGIC_ERR_MORE_RS_THAN_EXEPECTED =
		"More than two result set, this is unexpected";
	private static final String STMT_CONFIRM_REG = "{ call ConfirmRegistration(?) }";

	private static String getTargetCf(String cf, String cf2) 
			throws DataLogicException {
		String target = cf;

		if (target != null) {
			if (cf2 != null) {
				throw new DataLogicException(DATA_LOGIC_ERR_TWOCF_ONECREDPAIR);
			} else {
				target = cf2;
			}
		} else {
			if (cf2 == null) {
				throw new DataLogicException(DATA_LOGIC_ERR_ZEROCF_ONECREDPAIR);
			}
		}

		return target;
	}

	private static UserModel getJobSeeker(ResultSet rs) 
			throws SQLException, DataLogicException {

		if(rs.next()) {
			throw new DataLogicException(DATA_LOGIC_ERR_MULTIPLE_USERS);
		}

		JobSeekerUserModel m = new JobSeekerUserModel();
		m.setName(rs.getString(1));
		m.setSurname(rs.getString(2));
		m.setPhoneNumber(rs.getString(3));
		m.setBirthday(rs.getDate(4));
		m.setCv(rs.getString(5));
		m.setHomeAddress(rs.getString(6));
		m.setBiography(rs.getString(7));
		m.setComune(
			ComuniDao.getComune(rs.getString(8), 
								rs.getString(9)));
		m.setEmploymentStatus(
			EmploymentStatusDao.getEmploymentStatus(rs.getString(10)));
		m.setEmployee(false);

		return m;
	}

	private static UserModel getEmployee(ResultSet rs) 
			throws SQLException, DataLogicException, DataAccessException {

		if (rs.next()) {
			throw new DataLogicException(DATA_LOGIC_ERR_MULTIPLE_USERS);
		}

		EmployeeUserModel m = new EmployeeUserModel();
		m.setName(rs.getString(1));
		m.setSurname(rs.getString(2));
		m.setPhoneNumber(rs.getString(3));
		m.setCompany(
			CompanyDao.getCompanyByVat(rs.getString(4)));
		m.setRecruiter(rs.getBoolean(5));
		m.setAdmin(rs.getBoolean(6));
		m.setNote(rs.getString(7));
		m.setPhoto(rs.getString(8));
		m.setEmployee(true);

		return m;
	}

	public static Pair<String, byte[]> getUserCfAndBcrypwdByEmail(String email) 
			throws DataAccessException, DataLogicException, IOException {
		Connection conn = Database.getInstance().getConnection();

		try(CallableStatement stmt = conn.prepareCall(STMT_GETUSERCF_AND_PWD_BYEMAIL)) {
			stmt.setString(1, email);
			stmt.execute();

			try(ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}

				String target = getTargetCf(rs.getString(1), rs.getString(2));

				Pair<String, byte[]> pair = null;

				try (InputStream stream = rs.getBinaryStream(3)) {
					pair = new Pair<>(target, stream.readAllBytes());
				}

				if(rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERR_MULTIPLE_ROWS);
				}

				return pair;
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static UserModel getUserByCf(String cf) 
			throws DataAccessException, DataLogicException {
		Connection conn = Database.getInstance().getConnection();

		try(CallableStatement stmt = conn.prepareCall(STMT_GETUSER_BYCF)) {
			stmt.setString(1, cf);
			stmt.execute();

			int i = 1;

			do {
				if(i > 2) {
					throw new DataLogicException(DATA_LOGIC_ERR_MORE_RS_THAN_EXEPECTED);
				}
				
				try(ResultSet rs = stmt.getResultSet()) {
					if(rs.next()) {
						UserModel model = i == 2 ? getEmployee(rs) : getJobSeeker(rs);
						model.setCf(cf);
						
						return model;
					}
				}

				++i;
			} while(stmt.getMoreResults());
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}

		return null;
	}

	public static void confirmRegistration(UserModel userModel) 
			throws DataAccessException {
		
		Connection conn = Database.getInstance().getConnection();

		try(CallableStatement stmt = conn.prepareCall(STMT_CONFIRM_REG)) {
			stmt.setString(1, userModel.getCf());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/*
	public static void registerUser(UserModel userModel) 
			throws DataAccessException {

		Connection conn = Database.getInstance().getConnection();
		//TODO
	}

	public static void registerUserAuth(UserModel userModel, UserAuthModel userAuthModel) {
		//TODO
	}
	*/
}
