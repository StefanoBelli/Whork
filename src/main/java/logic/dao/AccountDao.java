package logic.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.Database;
import logic.bean.CandidatureBean;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CandidatureModel;
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
	
	public static List<CandidatureModel> getSeekerCandidature (UserModel userModel) throws DataAccessException, DataLogicException {
		List<CandidatureModel> listCandidatureModel = new ArrayList<CandidatureModel>();
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
	
	public static void editSocialAccountDao(JobSeekerUserModel user) throws DataAccessException {		
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
	
	public static void editJobSeekerInfoAccountDao(JobSeekerUserModel userModel, String email) throws DataAccessException {		
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
	
	public static void editJobSeekerBiographyDao(JobSeekerUserModel userModel) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_BIOGRAPHY)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getBiography());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
				
	}
	
}


