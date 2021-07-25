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
import logic.model.CandidatureModel;
import logic.model.EmployeeUserModel;
import logic.model.OfferModel;

public final class OfferDao {

	private OfferDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String MAIN_STMT_GET_OFFER_BYID = 
			"{ call GetOfferByID(?) }";
	private static final String MAIN_STMT_GET_FILTERED_OFFERS = 
			"{ call FilterOffers(?,?,?,?,?) }";
	private static final String MAIN_STMT_UPDATE_CLICK_STATS = 
			"{ call UpdateNumClick(?) }";
	private static final String DATA_LOGIC_ERROR_SAMEID_MOREOFFERS = 
			"Multiple offers detected with same Id";
	
	public static void updateClickStats(CandidatureModel candidatureModel) 
			throws DataAccessException {
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_UPDATE_CLICK_STATS)) {
			stmt.setInt(1, candidatureModel.getOffer().getId());
			stmt.execute();

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
	}
	


	public static OfferModel getOfferById(int id) 
			throws DataAccessException, DataLogicException {
		
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_OFFER_BYID)) {
			stmt.setInt(1, id);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}
		
				OfferModel om = new OfferModel();
				om.setOfferName(rs.getString(2));
				om.setEmployee((EmployeeUserModel) UserDao.getUserByCf(rs.getString(17)));
				om.setJobCategory(JobCategoryDao.getJobCategory(rs.getString(16)));
				om.setClickStats(rs.getInt(13));
				om.setSalaryEUR(rs.getInt(6));
				om.setPublishDate(rs.getDate(12));
				om.setWorkShit(rs.getString(8));
				om.setTypeOfContract(TypeOfContractDao.getTypeOfContract(rs.getString(11)));
				om.setQualification(QualificationDao.getQualification(rs.getString(10)));
				om.setJobPosition(JobPositionDao.getJobPosition(rs.getString(9)));
				om.setPhoto(rs.getString(7));
				om.setNote(rs.getString(14));
				om.setCompany(CompanyDao.getCompanyByVat(rs.getString(5)));
				om.setVerifiedByWhork(rs.getBoolean(15));
				om.setJobPhysicalLocationFullAddress(rs.getString(4));
				om.setDescription(rs.getString(3));
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

	public static List<OfferModel> getOffers(String searchVal, String category,
			String position, String qualification, String typeOfContract)
			throws DataAccessException, DataLogicException {

		List<OfferModel> offers = new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_FILTERED_OFFERS)) {
			stmt.setString(1, strOrNull(searchVal));
			stmt.setString(2, strOrNull(category));
			stmt.setString(3, strOrNull(position));
			stmt.setString(4, strOrNull(qualification));
			stmt.setString(5, strOrNull(typeOfContract));
			stmt.execute();
			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return offers;
				}
				
				do {			
					OfferModel om = new OfferModel();

					om.setId(rs.getInt(1));
					om.setOfferName(rs.getString(2));
					om.setDescription(rs.getString(3));
					om.setJobPhysicalLocationFullAddress(rs.getString(4));
					om.setCompany(CompanyDao.getCompanyByVat(rs.getString(5)));
					om.setSalaryEUR(rs.getInt(6));
					om.setPhoto(rs.getString(7));
					om.setWorkShit(rs.getString(8));
					om.setQualification(QualificationDao.getQualification(rs.getString(10)));
					om.setTypeOfContract(TypeOfContractDao.getTypeOfContract(rs.getString(11)));
					om.setJobPosition(JobPositionDao.getJobPosition(rs.getString(9)));
					om.setPublishDate(rs.getDate(12));
					om.setClickStats(rs.getInt(13));
					om.setNote(rs.getString(14));
					om.setVerifiedByWhork(rs.getBoolean(15));
					om.setEmployee((EmployeeUserModel) UserDao.getUserByCf(rs.getString(17)));
					om.setJobCategory(JobCategoryDao.getJobCategory(rs.getString(16)));
					
					offers.add(om);
				}while(rs.next());
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
		
		return offers;
	}
	
	private static String strOrNull(String s) {
		if(s == null) {
			return null;
		}

		return s.isBlank() ? null : s;
	}
}
