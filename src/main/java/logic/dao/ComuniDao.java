package logic.dao;

import logic.exception.DataAccessException;
import logic.util.Threeple;
import logic.model.ComuneModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;
import logic.Database;
import logic.ComuniPool;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class ComuniDao {
	private ComuniDao() {}
	
	private static final String STMT_MAIN_POPULATE_POOL = "{ call GetComuni() }";
	
	private static Threeple<RegioneModel, ProvinciaModel, ComuneModel> 
		getModels(String c, String n, String p, String r) {
		
		RegioneModel rm = new RegioneModel();
		rm.setNome(r);

		ProvinciaModel pm = new ProvinciaModel();
		pm.setSigla(p);
		pm.setRegione(rm);

		ComuneModel cm = new ComuneModel();
		cm.setCap(c);
		cm.setNome(n);
		cm.setProvincia(pm);

		return new Threeple<>(rm, pm, cm);
	}

	private static void realPopulatePool(List<ComuneModel> sscm, 
		List<ProvinciaModel> sspm, List<RegioneModel> ssrm) {
		
		ComuniPool.setComuni(new ArrayList<>(new HashSet<>(sscm)));
		ComuniPool.setProvince(new ArrayList<>(new HashSet<>(sspm)));
		ComuniPool.setRegioni(new ArrayList<>(new HashSet<>(ssrm)));
	}

	public static void populatePool() 
			throws DataAccessException {
		Connection conn = Database.getInstance().getConnection();

		try(CallableStatement stmt = conn.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try(ResultSet rs = stmt.getResultSet()) {
				List<ComuneModel> c = new ArrayList<>();
				List<ProvinciaModel> p = new ArrayList<>();
				List<RegioneModel> r = new ArrayList<>();
				
				while(rs.next()) {
					Threeple<RegioneModel, ProvinciaModel, ComuneModel> models = getModels(
							rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));

					c.add(models.getThird());
					p.add(models.getSecond());
					r.add(models.getFirst());
				}

				realPopulatePool(c, p, r);
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static ComuneModel getComune(String name, String cap) {
		for(final ComuneModel m : ComuniPool.getComuni()) {
			if(m.getNome().equals(name) && m.getCap().equals(cap))
				return m;
		}

		return null;
	}
}
