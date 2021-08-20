package logic.dao;

import logic.exception.DataAccessException;
import logic.Database;
import logic.model.JobPositionModel;
import logic.bean.JobPositionBean;
import logic.pool.JobPositionPool;
import logic.factory.ModelFactory;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Michele Tosi
 */
public final class JobPositionDao {
	private JobPositionDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String STMT_MAIN_POPULATE_POOL = 
		"{ call GetJobPositions() }";

	public static void populatePool() throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<JobPositionBean> jpl = new ArrayList<>();

				while (rs.next()) {
					JobPositionBean m = new JobPositionBean();
					m.setPosition(rs.getString(1));
					jpl.add(m);
				}

				JobPositionPool.setJobPositions(jpl);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static JobPositionModel getJobPosition(String name) {
		for(final JobPositionBean m : JobPositionPool.getJobPositions()) {
			if(m.getPosition().equals(name))
				return ModelFactory.buildJobPositionModel(m);
		}

		return null;
	}
}
