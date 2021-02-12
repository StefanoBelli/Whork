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
	
	private static final String MAIN_STMT_GET_COMPANY_BYVAT = "{ call GetCompanyByVAT(?) }";
	private static final String DATA_LOGIC_ERROR_SAMEVAT_MORECOMPANIES =
		"Multiple companies detected with same VAT code";

	public static CompanyModel getCompanyByVat(String vat) 
			throws DataAccessException, DataLogicException {
		
		Connection conn = Database.getInstance().getConnection();
		
		try (CallableStatement stmt = conn.prepareCall(MAIN_STMT_GET_COMPANY_BYVAT)) {
			stmt.setString(1, vat);
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				if(!rs.next()) {
					return null;
				}

				CompanyModel cm = new CompanyModel();
				cm.setSocialReason(stmt.getString(1));
				cm.setCf(stmt.getString(2));
				cm.setLogo(stmt.getString(3));
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
}
