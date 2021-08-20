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
import logic.model.EmployeeUserModel;
import logic.model.OfferModel;

/**
 * @author Michele Tosi
 */
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
	private static final String MAIN_STMT_POST_OFFER = 
			"{ call PostOffer(?,?, ?,?,?,?,?,?,?,?,?,?,?,?) }";
	private static final String TOTAL_NUMBER_OFFERS = 
			"{ call TotalNumberOffers(?) }";
	private static final String TOTAL_NUMBER_OF_CLICK = 
			"{ call TotalNumberOfClick(?) }";
	private static final String DATA_LOGIC_ERROR_SAMEID_MOREOFFERS = 
			"Multiple offers detected with same Id";
	private static final String DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED =
			"More than two result set, this is unexpected";
	
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
				om.setWorkShift(rs.getString(8));
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
					om.setWorkShift(rs.getString(8));
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
				} while(rs.next());
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}		
		
		return offers;
	}
	

	public static void postOffer(OfferModel offerModel) throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_POST_OFFER)){
			stmt.setString(1, offerModel.getOfferName());
			stmt.setString(2, offerModel.getDescription());
			stmt.setString(3, offerModel.getJobPhysicalLocationFullAddress());
			stmt.setString(4, offerModel.getCompany().getVat());
			stmt.setInt(5, offerModel.getSalaryEUR());
			stmt.setString(6, offerModel.getPhoto());
			stmt.setString(7, offerModel.getWorkShift());
			stmt.setString(8, offerModel.getJobPosition().getPosition());
			stmt.setString(9, offerModel.getQualification().getQualify());
			stmt.setString(10, offerModel.getTypeOfContract().getContract());
			stmt.setTimestamp(11, new Timestamp(new Date().getTime()));
			stmt.setString(12, offerModel.getNote());
			stmt.setString(13, offerModel.getJobCategory().getCategory());
			stmt.setString(14, offerModel.getEmployee().getCf());
			stmt.execute();
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	private static String strOrNull(String s) {
		if(s == null) {
			return null;
		}

		return s.isBlank() ? null : s;
	}
	
	public static int totalNumberOffers(CompanyModel company) throws DataAccessException, DataLogicException {		
		int n = 0;
		try (CallableStatement stmt = CONN.prepareCall(TOTAL_NUMBER_OFFERS)) {
			stmt.setString(1, company.getVat());
			stmt.execute();
			
			try (ResultSet rs = stmt.getResultSet()) {				
				if(!rs.next()) 
					throw new DataLogicException(DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED);				
				
				n = rs.getInt(1);
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return n;
	}
	
	public static int totalNumberOfClick(CompanyModel company) throws DataAccessException, DataLogicException {		
		int n = 0;
		try (CallableStatement stmt = CONN.prepareCall(TOTAL_NUMBER_OF_CLICK)) {
			stmt.setString(1, company.getVat());
			stmt.execute();
			
			try (ResultSet rs = stmt.getResultSet()) {				
				if(!rs.next()) 
					throw new DataLogicException(DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED);				
				
				n = rs.getInt(1);
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		return n;
	}
}
