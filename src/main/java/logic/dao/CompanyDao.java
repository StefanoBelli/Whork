package logic.dao;

import logic.exception.DataAccessException;
import logic.exception.DataLogicException;
import logic.model.CompanyModel;
import logic.Database;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;

public final class CompanyDao {
	private CompanyDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String MAIN_STMT_GET_COMPANY_BYVAT = 
		"{ call GetCompanyByVAT(?) }";
	private static final String MAIN_STMT_REGISTER_COMPANY = 
		"{ call RegisterCompany(?,?,?,?) }";
	private static final String DATA_LOGIC_ERROR_SAMEVAT_MORECOMPANIES =
		"Multiple companies detected with same VAT code";

	public static CompanyModel getCompanyByVat(String vat) 
			throws DataAccessException, DataLogicException {
		try (CallableStatement stmt = CONN.prepareCall(MAIN_STMT_GET_COMPANY_BYVAT)) {
			stmt.setString(1, vat);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}

				CompanyModel cm = new CompanyModel();
				cm.setSocialReason(rs.getString(1));
				cm.setCf(rs.getString(2));
				cm.setLogo(rs.getString(3));
				cm.setVat(vat);

				if(rs.next()) {
					throw new DataLogicException(DATA_LOGIC_ERROR_SAMEVAT_MORECOMPANIES);
				}

				return cm;
			}

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static void registerCompany(CompanyModel companyModel) 
			throws DataAccessException {
		try(CallableStatement stmt = CONN.prepareCall(MAIN_STMT_REGISTER_COMPANY)) {
			stmt.setString(1, companyModel.getVat());
			stmt.setString(2, companyModel.getSocialReason());
			stmt.setString(3, companyModel.getCf());
			stmt.setString(4, companyModel.getLogo());
			stmt.execute();
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
