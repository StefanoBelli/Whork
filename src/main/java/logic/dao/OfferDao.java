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

}
