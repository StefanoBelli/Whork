package logic.dao;

import logic.model.ComuneModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.Database;
import logic.ComuniPool;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.SortedSet;
import java.util.TreeSet;

public class ComuniDao {
	private static final String STMT_MAIN_POPULATE_POOL = "{ call GetComuni() }";
	
	public static void populatePool() 
			throws DataAccessException {
		Connection conn = Database.getInstance().getConnection();

		try(CallableStatement stmt = conn.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try(ResultSet rs = stmt.getResultSet()) {
				SortedSet<ComuneModel> c = new TreeSet<>();
				SortedSet<ProvinciaModel> p = new TreeSet<>();
				SortedSet<RegioneModel> r = new TreeSet<>();
				
				while(rs.next()) {
					String cap = rs.getString(1);
					String nome = rs.getString(2);
					String prov = rs.getString(3);
					String reg = rs.getString(4);

					RegioneModel rm = new RegioneModel();
					rm.setNome(reg);
					
					ProvinciaModel pm = new ProvinciaModel();
					pm.setSigla(prov);
					pm.setRegione(rm);

					ComuneModel cm = new ComuneModel();
					cm.setCap(cap);
					cm.setNome(nome);
					cm.setProvincia(pm);

					c.add(cm);
					p.add(pm);
					r.add(rm);
				}

				ComuniPool.setComuni(c);
				ComuniPool.setProvince(p);
				ComuniPool.setRegioni(r);
				
			} catch(SQLException e) {
				throw new DataAccessException(e);
			}

		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
