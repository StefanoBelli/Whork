package logic.dao;

import logic.exception.DataAccessException;
import logic.Database;
import logic.model.TypeOfContractModel;
import logic.bean.TypeOfContractBean;
import logic.pool.TypeOfContractPool;
import logic.factory.ModelFactory;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public final class TypeOfContractDao {
	private TypeOfContractDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String STMT_MAIN_POPULATE_POOL = 
		"{ call GetTypesOfContract() }";

	public static void populatePool() throws DataAccessException {
		try (CallableStatement stmt = CONN.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try (ResultSet rs = stmt.getResultSet()) {
				List<TypeOfContractBean> tcl = new ArrayList<>();

				while (rs.next()) {
					TypeOfContractBean m = new TypeOfContractBean();
					m.setContract(rs.getString(1));
					tcl.add(m);
				}

				TypeOfContractPool.setTypesOfContract(tcl);
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static TypeOfContractModel getTypeOfContract(String name) {
		for(final TypeOfContractBean m : TypeOfContractPool.getTypesOfContract()) {
			if(m.getContract().equals(name))
				return ModelFactory.buildTypeOfContractModel(m);
		}

		return null;
	}
}
