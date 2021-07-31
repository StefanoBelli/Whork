package logic.dao;

import logic.exception.DataAccessException;
import logic.Database;
import logic.model.QualificationModel;
import logic.bean.QualificationBean;
import logic.pool.QualificationPool;
import logic.factory.ModelFactory;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public final class QualificationDao {
	private QualificationDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String STMT_MAIN_POPULATE_POOL = 
		"{ call GetQualifications() }";

	public static void populatePool() throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<QualificationBean> ql = new ArrayList<>();

				while (rs.next()) {
					QualificationBean m = new QualificationBean();
					m.setQualify(rs.getString(1));
					ql.add(m);
				}

				QualificationPool.setQualifications(ql);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static QualificationModel getQualification(String name) {
		for(final QualificationBean m : QualificationPool.getQualifications()) {
			if(m.getQualify().equals(name))
				return ModelFactory.buildQualificationModel(m);
		}

		return null;
	}
}
