package logic.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CandidatureModel;

public final class CandidatureDao {
	
	private CandidatureDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();

	private static final String MAIN_STMT_GET_CANDIDATURE = 
			"{ call GetCandidature(?,?) }";
	private static final String MAIN_STMT_INSERT_CANDIDATURE = 
			"{ call InsertCandidature(?,?,?) }";
	private static final String DATA_LOGIC_ERROR_SAMEID_MORECANDIDATURE = 
			"Multiple candidature detected with same Id";
	
	public static void insertCandidature(int id, String cf) 
			throws DataAccessException {
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_INSERT_CANDIDATURE)) {
			stmt.setInt(1, id);
			stmt.setString(2, cf);
			stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			stmt.execute();

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}
	
	public static CandidatureModel getCandidature(int id, String cf) 
			throws DataAccessException, DataLogicException {
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_CANDIDATURE)) {
			stmt.setInt(1, id);
			stmt.setString(2,cf);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}
		
				CandidatureModel cm = new CandidatureModel();
				cm.setOffer(OfferDao.getOfferById(rs.getInt(1)));
				cm.setJobSeeker(UserDao.getUserByCf(rs.getString(2)));
				cm.setCandidatureDate(rs.getDate(3));
				
				if(rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERROR_SAMEID_MORECANDIDATURE);
				}
				
				return cm;
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}

	
}
