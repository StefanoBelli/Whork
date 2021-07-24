package logic.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CandidatureModel;
import logic.model.JobSeekerUserModel;

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
	
	public static ArrayList<CandidatureModel> getSeekerCandidature (String cf) throws DataAccessException, DataLogicException {
		ArrayList<CandidatureModel> listCandidatureModel = new ArrayList<CandidatureModel>();
		try (CallableStatement stmt = CONN.prepareCall(GET_SEEKER_CANDIDATURE)) {
			stmt.setString(1, cf);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {				
				while(rs.next()) {
					
					CandidatureModel element = new CandidatureModel();
					element.setOffer(OfferDao.getOfferById(rs.getInt(1)));
					element.setJobSeeker(UserDao.getUserByCf(rs.getString(2)));
					element.setCandidatureDate(rs.getDate(3));
					/*element.setTypeOfContract(rs.getString(3));
					element.setJobOccupation(rs.getString(4));
					element.setEmail(rs.getString(5));*/
					listCandidatureModel.add(element);			
					
				}				
			}

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return listCandidatureModel;		
	}
	
	public static void editSocialAccountDao(String cf, String website, String twitter, String facebook, String instagram) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_SOCIAL_ACCOUNT)) {
			stmt.setString(1, cf);
			stmt.setString(2, website);
			stmt.setString(3, twitter);
			stmt.setString(4, facebook);
			stmt.setString(5, instagram);
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return;		
	}
	
	public static void editJobSeekerInfoAccountDao(JobSeekerUserModel userModel, String email) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_INFO_ACCOUNT)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getName());
			stmt.setString(3, userModel.getSurname());
			stmt.setString(4, email);
			stmt.setString(5, userModel.getPhoneNumber());
			stmt.setString(5, userModel.getHomeAddress());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return;		
	}
	
	public static void editJobSeekerBiographyDao(JobSeekerUserModel userModel) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_BIOGRAPHY)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getBiography());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return;		
	}	
	
}


