package logic.dao;

import logic.exception.DataAccessException;
import logic.Database;
import logic.model.EmploymentStatusModel;
import logic.bean.EmploymentStatusBean;
import logic.pool.EmploymentsStatusPool;
import logic.factory.ModelFactory;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public final class EmploymentStatusDao {
	private EmploymentStatusDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String STMT_MAIN_POPULATE_POOL = 
		"{ call GetEmploymentStatuses() }";

	public static void populatePool() throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<EmploymentStatusBean> esl = new ArrayList<>();

				while (rs.next()) {
					EmploymentStatusBean m = new EmploymentStatusBean();
					m.setStatus(rs.getString(1));
					esl.add(m);
				}

				EmploymentsStatusPool.setEmploymentsStatus(esl);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static EmploymentStatusModel getEmploymentStatus(String name) {
		for(final EmploymentStatusBean m : EmploymentsStatusPool.getEmploymentsStatus()) {
			if(m.getStatus().equals(name))
				return ModelFactory.buildEmploymentStatusModel(m);
		}

		return null;
	}
}
