package logic.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CandidatureModel;
import logic.model.CompanyModel;
import logic.model.JobSeekerUserModel;
import logic.model.UserModel;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public final class AccountDao {
	private AccountDao() {}
	
	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String GET_SEEKER_CANDIDATURE = 
			"{ call GetCandidatureAccount(?) }";
	private static final String EDIT_SOCIAL_ACCOUNT = 
			"{ call EditSocialAccount(?, ?, ?, ?, ?) }";
	private static final String EDIT_JOB_SEEKER_INFO_ACCOUNT = 
			"{ call EditJobSeekerInfoAccount(?, ?, ?, ?, ?, ?) }";
	private static final String EDIT_JOB_SEEKER_BIOGRAPHY = 
			"{ call EditJobSeekerBiography(?, ?) }";
	private static final String EDIT_JOB_SEEKER_PICTURE = 
			"{ call ChangePictureJobSeekerUser(?, ?) }";
	private static final String COUNT_OF_EMPLOYEES = 
			"{ call CountOfEmployee(?) }";
	private static final String DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED =
			"More than two result set, this is unexpected";
	
	public static List<CandidatureModel> getSeekerCandidature(UserModel userModel) throws DataAccessException, DataLogicException {
		List<CandidatureModel> listCandidatureModel = new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(GET_SEEKER_CANDIDATURE)) {
			stmt.setString(1, userModel.getCf());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {				
				while(rs.next()) {
					
					CandidatureModel element = new CandidatureModel();
					element.setOffer(OfferDao.getOfferById(rs.getInt(1)));					
					element.setJobSeeker((JobSeekerUserModel) UserDao.getUserByCf(rs.getString(2)));
					element.setCandidatureDate(rs.getDate(3));					
					listCandidatureModel.add(element);			
					
				}				
			}

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return listCandidatureModel;		
	}
	
	public static void editSocialAccount(JobSeekerUserModel user) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_SOCIAL_ACCOUNT)) {
			stmt.setString(1, user.getCf());
			stmt.setString(2, user.getWebsite());
			stmt.setString(3, user.getTwitter());
			stmt.setString(4, user.getFacebook());
			stmt.setString(5, user.getInstagram());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
	}
	
	public static void editJobSeekerInfoAccount(JobSeekerUserModel userModel, String email) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_INFO_ACCOUNT)) {			
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getName());
			stmt.setString(3, userModel.getSurname());
			stmt.setString(4, email);
			stmt.setString(5, userModel.getPhoneNumber());
			stmt.setString(6, userModel.getHomeAddress());			
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
				
	}
	
	public static void editJobSeekerBiography(JobSeekerUserModel userModel) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_BIOGRAPHY)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getBiography());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
				
	}
	
	public static void editJobSeekerPicture(JobSeekerUserModel userModel, String photo) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_PICTURE)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, photo);
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
				
	}
	
	public static int countOfEmployees(CompanyModel company) throws DataAccessException, DataLogicException {		
		int n = 0;
		try (CallableStatement stmt = CONN.prepareCall(COUNT_OF_EMPLOYEES)) {
			stmt.setString(1, company.getVat());
			stmt.execute();
			
			try (ResultSet rs = stmt.getResultSet()) {				
				if(!rs.next()) 
					throw new DataLogicException(DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED);				
				
				n = rs.getInt(1) - 1; //admin is not counted
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		return n;
	}
	
}


