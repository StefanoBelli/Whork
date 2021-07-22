package logic.dao;

import logic.Database;
import logic.exception.DataAccessException;
import logic.model.CandidatureModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public final class AccountDao {
	private AccountDao() {}
	
	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String GET_SEEKER_CANDIDATURE = 
			"{ call GetCandidature(?) }";
	private static final String EDIT_SOCIAL_ACCOUNT = 
			"{ call EditSocialAccount(?, ?, ?, ?, ?) }";
	
	public static ArrayList<CandidatureModel> getSeekerCandidature (String cf) throws DataAccessException {
		ArrayList<CandidatureModel> listCandidatureModel = new ArrayList<CandidatureModel>();
		try (CallableStatement stmt = CONN.prepareCall(GET_SEEKER_CANDIDATURE)) {
			stmt.setString(1, cf);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(rs == null) {
					return null;
				}				
				
				while(rs.next()) {
				
					CandidatureModel element = new CandidatureModel();
					element.setSocialReason(rs.getString(1));
					element.setCandidatureDate(rs.getDate(2).toString());
					element.setTypeOfContract(rs.getString(3));
					element.setJobOccupation(rs.getString(4));
					element.setEmail(rs.getString(5));
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
}


