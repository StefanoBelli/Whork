package logic.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	private static final String MAIN_STMT_GET_ALL_OFFERS = 
			"{ call GetOffers() }";
	private static final String DATA_LOGIC_ERROR_SAMEID_MOREOFFERS = 
			"Multiple offers detected with same Id";
	
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
				om.setDescription(rs.getString(2));
				om.setCompanyHeadQuarterAddress(rs.getString(3));
				om.setEmployeeMail(rs.getString(4));
				om.setCompanyVat(rs.getString(5));
				om.setSalary(rs.getString(6));
				om.setPhoto(rs.getString(7));
				om.setOtherTime(rs.getBoolean(8));
				om.setWorkShit(rs.getString(9));
				om.setJobPosition(rs.getString(10));
				om.setQualification(rs.getString(11));
				om.setTypeOfContract(rs.getString(12));
				om.setYearSalary(rs.getBoolean(13));
				om.setOfferName(rs.getString(14));
				om.setNumberOfCandidatures(rs.getInt(15));
				om.setData(rs.getDate(16));
				om.setClickNumber(rs.getInt(17));
				om.setNote(rs.getString(18));
				om.setNumberSaved(rs.getString(19));
				om.setChecked(rs.getBoolean(20));
				
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
	
	public static List<OfferModel> getOffers()
			throws DataAccessException{
		List<OfferModel> offers= new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_ALL_OFFERS)) {
			stmt.execute();
			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return offers;
				}
				do {			

		
					OfferModel om= new OfferModel();
					om.setId(rs.getInt(1));
					om.setDescription(rs.getString(2));
					om.setCompanyHeadQuarterAddress(rs.getString(3));
					om.setEmployeeMail(rs.getString(4));
					om.setCompanyVat(rs.getString(5));
					om.setSalary(rs.getString(6));
					om.setPhoto(rs.getString(7));
					om.setOtherTime(rs.getBoolean(8));
					om.setWorkShit(rs.getString(9));
					om.setJobPosition(rs.getString(10));
					om.setQualification(rs.getString(11));
					om.setTypeOfContract(rs.getString(12));
					om.setYearSalary(rs.getBoolean(13));
					om.setOfferName(rs.getString(14));
					om.setNumberOfCandidatures(rs.getInt(15));
					om.setData(rs.getDate(16));
					om.setClickNumber(rs.getInt(17));
					om.setNote(rs.getString(18));
					om.setNumberSaved(rs.getString(19));
					om.setChecked(rs.getBoolean(20));
					
					
					offers.add(om);
				}while(rs.next());
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
		
		
		return offers;
		
	}

}
