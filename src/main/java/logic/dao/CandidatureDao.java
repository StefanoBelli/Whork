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
import logic.model.CandidatureModel;
import logic.model.CompanyModel;
import logic.model.JobSeekerUserModel;

/**
 * @author Michele Tosi
 */
public final class CandidatureDao {
	
	private CandidatureDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();

	private static final String MAIN_STMT_GET_CANDIDATURE = 
			"{ call GetCandidature(?,?) }";
	private static final String MAIN_STMT_INSERT_CANDIDATURE = 
			"{ call InsertCandidature(?,?,?) }";
	private static final String DELETE_CANDIDATURE = 
			"{ call DeleteCandidature(?,?) }";
	private static final String GET_CANDIDATURE_BY_COMPANY_VAT = 
			"{ call GetCandidatureByCompanyVAT(?) }";
	
	public static void insertCandidature(CandidatureModel candidatureModel) 
			throws DataAccessException {
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_INSERT_CANDIDATURE)) {
			stmt.setInt(1, candidatureModel.getOffer().getId());
			stmt.setString(2, candidatureModel.getJobSeeker().getCf());
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
				cm.setJobSeeker((JobSeekerUserModel) UserDao.getUserByCf(rs.getString(2)));
				cm.setCandidatureDate(rs.getDate(3));
				
				return cm;
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}

	/**
	 * @author Magliari Elio
	 */
	public static void deleteCandidature (JobSeekerUserModel userModel, CandidatureModel candidature) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(DELETE_CANDIDATURE)) {
			stmt.setString(1, userModel.getCf());
			stmt.setInt(2, candidature.getOffer().getId());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * @author Magliari Elio
	 */
	public static List<CandidatureModel> getCandidatureByCompanyVat(CompanyModel company) 
			throws DataAccessException, DataLogicException {
		List<CandidatureModel> listCandidatureModel = new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(GET_CANDIDATURE_BY_COMPANY_VAT)) {
			stmt.setString(1, company.getVat());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while(rs.next()) {
					CandidatureModel cm = new CandidatureModel();
					cm.setOffer(OfferDao.getOfferById(rs.getInt(1)));
					cm.setJobSeeker((JobSeekerUserModel) UserDao.getUserByCf(rs.getString(2)));
					cm.setCandidatureDate(rs.getDate(3));
					listCandidatureModel.add(cm);
				}
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return listCandidatureModel;
	}
}
