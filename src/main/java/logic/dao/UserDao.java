package logic.dao;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.Date;

import logic.Database;
import logic.model.CompanyModel;
import logic.model.ComuneModel;
import logic.model.EmployeeUserModel;
import logic.model.EmploymentStatusModel;
import logic.model.JobSeekerUserModel;
import logic.model.UserModel;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;

public final class UserDao {
	private UserDao() {}

	private static final Connection CONN =
		Database.getInstance().getConnection();
	
	private static final String STMT_GETUSER_BYCF = 
		"{ call GetUserDetails(?) }";
	private static final String STMT_GET_EMPLOYEE_EMAIL_BY_CF=
		"{ call GetEmployeeEmailByCf(?)} ";
	private static final String STMT_GET_JOBSEEKER_EMAIL_BY_CF=
		"{ call GetJobSeekerEmailByCf(?)} ";
	private static final String STMT_REGDET_EMPLOYEE = 
		"{ call RegisterEmployeeUserDetails(?,?,?,?,?,?,?,?,?) }";
	private static final String STMT_REGDET_JOBSEEKER = 
		"{ call RegisterJobSeekerUserDetails(?,?,?,?,?,?,?,?,?,?,?,?) }";
	private static final String STMT_GET_EMPLOYMENT_STATUS_BY_COMPANY_VAT = 
		"{ call GetEmploymentStatusByCompanyVAT(?) }";
	private static final String STMT_GET_EMPLOYEE_USER_DETAILS_BY_COMPANY_VAT = 
			"{ call GetEmployeeUserDetailsByCompanyVAT(?) }";
	private static final String DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED =
		"More than two result set, this is unexpected";
	private static final String DATA_LOGIC_ERROR_SAMECF_MOREMAILS = 
		"Multiple mails detected with same Cf";
	private static final String DATA_LOGIC_ERR_NOCF = 
		"No CF";

	private static UserModel getJobSeeker(ResultSet rs) 
			throws SQLException {
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
		m.setPhoto(rs.getString(11));

		return m;
	}

	private static UserModel getEmployee(ResultSet rs) 
			throws SQLException, DataLogicException, DataAccessException {
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

		return m;
	}

	private static void stmtSetSpecModelData(
			CallableStatement stmt, UserModel userModel, boolean isEmployee) 
				throws SQLException {
		if (isEmployee) {
			EmployeeUserModel employeeUserModel = (EmployeeUserModel) userModel;

			stmt.setString(6, employeeUserModel.getCompany().getVat());
			stmt.setBoolean(7, employeeUserModel.isRecruiter());
			stmt.setBoolean(8, employeeUserModel.isAdmin());
			stmt.setString(9, employeeUserModel.getNote());
		} else {
			JobSeekerUserModel jobSeekerUserModel = (JobSeekerUserModel) userModel;
			ComuneModel comuneModel = jobSeekerUserModel.getComune();

			stmt.setString(6, jobSeekerUserModel.getHomeAddress());
			stmt.setDate(7, new Date(jobSeekerUserModel.getBirthday().getTime()));
			stmt.setString(8, jobSeekerUserModel.getBiography());
			stmt.setString(9, comuneModel.getNome());
			stmt.setString(10, comuneModel.getCap());
			stmt.setString(11, jobSeekerUserModel.getEmploymentStatus().getStatus());
			stmt.setString(12, jobSeekerUserModel.getCv());
		}
	}

	public static UserModel getUserByCf(String cf) 
			throws DataAccessException, DataLogicException {
		try(CallableStatement stmt = CONN.prepareCall(STMT_GETUSER_BYCF)) {
			stmt.setString(1, cf);
			stmt.execute();

			int i = 1;

			do {
				if(i > 2) {
					throw new DataLogicException(DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED);
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

	public static void registerUserDetails(UserModel userModel) 
			throws DataAccessException {
		boolean isEmployee = userModel.isEmployee();
		String callStmt = isEmployee ? STMT_REGDET_EMPLOYEE : STMT_REGDET_JOBSEEKER;

		try(CallableStatement stmt = CONN.prepareCall(callStmt)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getName());
			stmt.setString(3, userModel.getSurname());
			stmt.setString(4, userModel.getPhoneNumber());
			stmt.setString(5, userModel.getPhoto());

			stmtSetSpecModelData(stmt, userModel, isEmployee);
			
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public static String getEmployeeEmailByCf(UserModel userModel) throws DataLogicException, DataAccessException {
		String email = null;
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_EMPLOYEE_EMAIL_BY_CF)) {
			stmt.setString(1, userModel.getCf());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERROR_SAMECF_MOREMAILS);
				}

				email = rs.getString(1);
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		return email;
		
	}
	
	public static String getJobSeekerEmailByCf(UserModel userModel) throws DataLogicException, DataAccessException {
		String email = null;
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_JOBSEEKER_EMAIL_BY_CF)) {
			stmt.setString(1, userModel.getCf());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERROR_SAMECF_MOREMAILS);
				}

				email = rs.getString(1);
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		return email;
		
	}
	
	public static List<EmploymentStatusModel> getEmploymentStatusByCompanyVat(CompanyModel company) 
			throws DataAccessException, DataLogicException {
		List<EmploymentStatusModel> listEmploymentStatus = new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_EMPLOYMENT_STATUS_BY_COMPANY_VAT)) {
			stmt.setString(1, company.getVat());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while(rs.next()) {
		
					JobSeekerUserModel cm = (JobSeekerUserModel) UserDao.getUserByCf(rs.getString(1));
					if(cm == null) throw new DataLogicException(DATA_LOGIC_ERR_NOCF);
						
					listEmploymentStatus.add(cm.getEmploymentStatus());
				
				}
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		return listEmploymentStatus;
	}
	
	public static Map<String, EmployeeUserModel> getEmployeeByCompanyVAT(CompanyModel company) throws DataAccessException {
		Map<String, EmployeeUserModel> map = new HashMap<>();
		try(CallableStatement stmt = CONN.prepareCall(STMT_GET_EMPLOYEE_USER_DETAILS_BY_COMPANY_VAT)) {
			stmt.setString(1, company.getVat());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while(rs.next()) {
					EmployeeUserModel model = new EmployeeUserModel();
				
					model.setName(rs.getString(1));
					model.setSurname(rs.getString(2));
					model.setPhoneNumber(rs.getString(3));
					model.setCompany(company);
					model.setRecruiter(true);
					model.setAdmin(false);
					model.setNote(rs.getString(4));
					model.setPhoto(rs.getString(5));
					model.setCf(rs.getString(6));
					map.put(rs.getString(7), model);
				}
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return map;
	}
}
