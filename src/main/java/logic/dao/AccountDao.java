package logic.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.Database;
import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CandidatureModel;
import logic.model.CompanyModel;
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
	private static final String EDIT_JOB_SEEKER_PICTURE = 
			"{ call ChangePictureJobSeekerUser(?, ?) }";
	private static final String COUNT_OF_EMPLOYEES = 
			"{ call CountOfEmployee(?) }";
	private static final String STMT_GET_EMPLOYMENT_STATUS_BY_COMPANY_VAT = 
			"{ call GetEmploymentStatusByCompanyVAT(?) }";
	private static final String FISCAL_CODE_DECODE = 
			"{ call GetCountryByFiscalCode(?) }";
	private static final String STMT_GET_NUMBER_OFFERTS_OF_AN_EMPLOYEE = 
			"{ call GetNumberOffersOfAnEmployee(?) }";
	private static final String STMT_GET_CLICK_NUMBER_OF_AN_EMPLOYEE = 
			"{ call GetTotalClickNumberOfAnEmployee(?) }";
	private static final String DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED =
			"More than two result set, this is unexpected";
	
	public static List<CandidatureModel> getSeekerCandidature(UserModel userModel) throws DataAccessException, DataLogicException {
		List<CandidatureModel> listCandidatureModel = new ArrayList<>();
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
	
	public static void editSocialAccount(JobSeekerUserModel user) throws DataAccessException {		
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
	
	public static void editJobSeekerInfoAccount(JobSeekerUserModel userModel, String email) throws DataAccessException {		
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
	
	public static void editJobSeekerBiography(JobSeekerUserModel userModel) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_BIOGRAPHY)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, userModel.getBiography());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public static void editJobSeekerPicture(JobSeekerUserModel userModel, String photo) throws DataAccessException {		
		try (CallableStatement stmt = CONN.prepareCall(EDIT_JOB_SEEKER_PICTURE)) {
			stmt.setString(1, userModel.getCf());
			stmt.setString(2, photo);
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public static int countOfEmployees(CompanyModel company) throws DataAccessException, DataLogicException {		
		int n = 0;
		try (CallableStatement stmt = CONN.prepareCall(COUNT_OF_EMPLOYEES)) {
			stmt.setString(1, company.getVat());
			stmt.execute();
			
			try (ResultSet rs = stmt.getResultSet()) {				
				if(!rs.next()) 
					throw new DataLogicException(DATA_LOGIC_ERR_MORE_RS_THAN_EXPECTED);				
				
				n = rs.getInt(1) - 1; //admin is not counted
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}

		return n;
	}
	
	public static List<String> getCountryFiscalCodeDecode(CompanyModel company) throws DataAccessException {				
		List<String> listJobSeekerCF = new ArrayList<>();
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_EMPLOYMENT_STATUS_BY_COMPANY_VAT)) {
			stmt.setString(1, company.getVat());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				while(rs.next()) {
					String cf = rs.getString(1);
					if(!listJobSeekerCF.contains(cf))
						listJobSeekerCF.add(cf);
				}
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		
		List<String> listCode = new ArrayList<>();
		for(int i=0;  i<listJobSeekerCF.size(); i++) {
			String country = getCountryFiscalCodeDecodePrivateMethod(listJobSeekerCF.get(i).substring(11, 15));
			if(country != null) 
				listCode.add(country);
			else 
				listCode.add("Italy");
		}

		return listCode;
	}
	
	private static String getCountryFiscalCodeDecodePrivateMethod(String code) throws DataAccessException {	
		try (CallableStatement stmt = CONN.prepareCall(FISCAL_CODE_DECODE)) {
			stmt.setString(1, code);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(rs.next())
					return rs.getString(1);
				else
					return null;
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
	
	public static int getNumberOfferOfAnEmployee(UserModel user) throws DataAccessException {	
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_NUMBER_OFFERTS_OF_AN_EMPLOYEE)) {
			stmt.setString(1, user.getCf());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(rs.next())
					return rs.getInt(1);				
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		return 0;
	}
	
	public static int getNUmberClickOfAnEmployee(UserModel user) throws DataAccessException {	
		try (CallableStatement stmt = CONN.prepareCall(STMT_GET_CLICK_NUMBER_OF_AN_EMPLOYEE)) {
			stmt.setString(1, user.getCf());
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(rs.next())
					return rs.getInt(1);				
			}
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
		return 0;
	}
}


