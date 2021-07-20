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
import logic.model.OfferModel;

public final class OfferDao {

	private OfferDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String MAIN_STMT_GET_OFFER_BYID = 
			"{ call GetOfferByID(?) }";
	private static final String MAIN_STMT_GET_FILTERED_OFFERS = 
			"{ call FilterOffers(?,?,?,?,?) }";
	private static final String MAIN_STMT_GET_EMPLOYEE_MAIL = 
			"{ call GetEmployeeEmail(?) }";
	private static final String MAIN_STMT_INSERT_CANDIDATURE = 
			"{ call InsertCandidature(?,?,?) }";
	private static final String MAIN_STMT_UPDATE_CLICK_STATS = 
			"{ call UpdateNumClick(?) }";
	private static final String DATA_LOGIC_ERROR_SAMEID_MOREOFFERS = 
			"Multiple offers detected with same Id";
	
	
	public static void updateClickStats(Integer id) 
			throws DataAccessException, DataLogicException{
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_UPDATE_CLICK_STATS)) {
			stmt.setInt(1, id);
			stmt.execute();

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}
	

	public static void insertCandidature(Integer id, String cf) 
			throws DataAccessException, DataLogicException{
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_INSERT_CANDIDATURE)) {
			stmt.setInt(1, id);
			stmt.setString(2, cf);
			stmt.setTimestamp(3, new Timestamp(new Date().getTime()));
			stmt.execute();

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}

	
	
	public static String getEmployeeEmail(Integer id) 
			throws DataAccessException, DataLogicException{
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_EMPLOYEE_MAIL)) {
			stmt.setInt(1, id);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}


				if(rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERROR_SAMEID_MOREOFFERS);
				}
				return rs.getString(1);
				
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}

	
	
	
	public static OfferModel getOfferById(Integer id) 
			throws DataAccessException, DataLogicException{
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_OFFER_BYID)) {
			stmt.setInt(1, id);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}

		
				OfferModel om= new OfferModel();
				om.setOfferName(rs.getString(2));
				om.setDescription(rs.getString(3));
				om.setJobPhysicalLocationFullAddress(rs.getString(4));
				om.setCompanyVat(rs.getString(5));
				om.setSalaryEUR(rs.getInt(6));
				om.setPhoto(rs.getString(7));
				om.setWorkShit(rs.getString(8));
				om.setJobPosition(rs.getString(9));
				om.setQualification(rs.getString(10));
				om.setTypeOfContract(rs.getString(11));
				om.setPublishDate(rs.getDate(12));
				om.setClickStats(rs.getInt(13));
				om.setNote(rs.getString(14));
				om.setVerifiedByWhork(rs.getBoolean(15));
				om.setJobCategory(rs.getString(16));
				om.setEmployeeCF(rs.getString(17));
				
				om.setId(id);

				if(rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERROR_SAMEID_MOREOFFERS);
				}
				
				return om;
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}

	
	public static List<OfferModel> getOffers(String searchVal, String jobCategory, String jobPosition, String qualification, String typeOfContract)
			throws DataAccessException{
		List<OfferModel> offers= new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_FILTERED_OFFERS)) {
			stmt.setString(1, strOrNull(searchVal));
			stmt.setString(2, strOrNull(jobCategory));
			stmt.setString(3, strOrNull(jobPosition));
			stmt.setString(4, strOrNull(qualification));
			stmt.setString(5, strOrNull(typeOfContract));
			stmt.execute();
			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return offers;
				}
				do {			

					
					OfferModel om= new OfferModel();
					om.setId(rs.getInt(1));
					om.setOfferName(rs.getString(2));
					om.setDescription(rs.getString(3));
					om.setJobPhysicalLocationFullAddress(rs.getString(4));
					om.setCompanyVat(rs.getString(5));
					om.setSalaryEUR(rs.getInt(6));
					om.setPhoto(rs.getString(7));
					om.setWorkShit(rs.getString(8));
					om.setJobPosition(rs.getString(9));
					om.setQualification(rs.getString(10));
					om.setTypeOfContract(rs.getString(11));
					om.setPublishDate(rs.getDate(12));
					om.setClickStats(rs.getInt(13));
					om.setNote(rs.getString(14));
					om.setVerifiedByWhork(rs.getBoolean(15));
					om.setJobCategory(rs.getString(16));
					om.setEmployeeCF(rs.getString(17));
					
					
					offers.add(om);
				}while(rs.next());
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
		
		
		return offers;
		
	}
	

	private static String strOrNull(String s) {
		if(s==null)
			return null;
		return s.isBlank()? null : s;
	}

}
