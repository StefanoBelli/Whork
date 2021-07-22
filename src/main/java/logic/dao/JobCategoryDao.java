package logic.dao;

import logic.exception.DataAccessException;
import logic.Database;
import logic.model.JobCategoryModel;
import logic.bean.JobCategoryBean;
import logic.pool.JobCategoryPool;
import logic.factory.ModelFactory;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public final class JobCategoryDao {
	private JobCategoryDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String STMT_MAIN_POPULATE_POOL = 
		"{ call GetJobCategories() }";

	public static void populatePool() throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<JobCategoryBean> esl = new ArrayList<>();

				while (rs.next()) {
					JobCategoryBean m = new JobCategoryBean();
					m.setCategory(rs.getString(1));
					esl.add(m);
				}

				JobCategoryPool.setJobCategories(esl);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static JobCategoryModel getJobCategory(String name) {
		for(final JobCategoryBean m : JobCategoryPool.getJobCategories()) {
			if(m.getCategory().equals(name))
				return ModelFactory.buildJobCategoryModel(m);
		}

		return null;
	}
}
