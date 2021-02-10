package logic.dao;

import logic.Database;
import logic.model.EmploymentStatusModel;
import logic.EmploymentsStatusPool;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class EmploymentStatusDao {
	private static final String STMT_MAIN_POPULATE_POOL = "{ call GetEmploymentStatuses() }";

	public static void populatePool() throws DataAccessException {
		Connection conn = Database.getInstance().getConnection();

		try (CallableStatement stmt = conn.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<EmploymentStatusModel> esl = new ArrayList<>();

				while (rs.next()) {
					EmploymentStatusModel m = new EmploymentStatusModel();
					m.setStatus(rs.getString(1));
					esl.add(m);
				}

				EmploymentsStatusPool.setEmploymentsStatus(esl);
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
