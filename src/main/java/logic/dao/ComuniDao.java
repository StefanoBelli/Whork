package logic.dao;

import logic.exception.DataAccessException;
import logic.util.tuple.Threeple;
import logic.model.ComuneModel;
import logic.bean.ComuneBean;
import logic.bean.ProvinciaBean;
import logic.bean.RegioneBean;
import logic.Database;
import logic.pool.ComuniPool;
import logic.factory.ModelFactory;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class ComuniDao {
	private ComuniDao() {}

	private static final Connection CONN = 
		Database.getInstance().getConnection();
	
	private static final String STMT_MAIN_POPULATE_POOL = 
		"{ call GetComuni() }";
	
	private static Threeple<RegioneBean, ProvinciaBean, ComuneBean> 
		getModels(String c, String n, String p, String r) {
		
		RegioneBean rm = new RegioneBean();
		rm.setNome(r);

		ProvinciaBean pm = new ProvinciaBean();
		pm.setSigla(p);
		pm.setRegione(rm);

		ComuneBean cm = new ComuneBean();
		cm.setCap(c);
		cm.setNome(n);
		cm.setProvincia(pm);

		return new Threeple<>(rm, pm, cm);
	}

	private static void realPopulatePool(List<ComuneBean> sscm, 
		List<ProvinciaBean> sspm, List<RegioneBean> ssrm) {
		
		ComuniPool.setComuni(sscm);
		ComuniPool.setProvince(sspm);
		ComuniPool.setRegioni(ssrm);
	}

	private static boolean provinceContains(List<ProvinciaBean> p, String sigla) {
		for(final ProvinciaBean provincia : p) {
			if(provincia.getSigla().equals(sigla)) {
				return true;
			}
		}

		return false;
	}

	private static boolean regioniContains(List<RegioneBean> r, String nome) {
		for(final RegioneBean regione : r) {
			if(regione.getNome().equals(nome)) {
				return true;
			}
		}

		return false;
	}

	public static void populatePool() 
			throws DataAccessException {
		try(CallableStatement stmt = CONN.prepareCall(STMT_MAIN_POPULATE_POOL)) {
			stmt.execute();

			try(ResultSet rs = stmt.getResultSet()) {
				List<ComuneBean> c = new ArrayList<>();
				List<ProvinciaBean> p = new ArrayList<>();
				List<RegioneBean> r = new ArrayList<>();
				
				while(rs.next()) {
					Threeple<RegioneBean, ProvinciaBean, ComuneBean> models = getModels(
							rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));

					c.add(models.getThird());
					ProvinciaBean pms = models.getSecond();
					RegioneBean rms = models.getFirst();

					if(!provinceContains(p, pms.getSigla())) {
						p.add(pms);
					}

					if(!regioniContains(r, rms.getNome())) {
						r.add(rms);
					}
				}

				realPopulatePool(c, p, r);
			}
			
		} catch(SQLException e) {
			throw new DataAccessException(e);
		}
	}

	public static ComuneModel getComune(String name, String cap) {
		for(final ComuneBean m : ComuniPool.getComuni()) {
			if(m.getNome().equals(name) && m.getCap().equals(cap))
				return ModelFactory.buildComuneModel(m);
		}

		return null;
	}
}
