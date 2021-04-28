package logic.pool;

import java.util.List;
import logic.model.ComuneModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;

public final class ComuniPool {
	private ComuniPool() {}
	
	private static List<ComuneModel> comuni;
	private static List<ProvinciaModel> province;
	private static List<RegioneModel> regioni;

	public static void setComuni(List<ComuneModel> comuni) {
		ComuniPool.comuni = comuni;
	}

	public static void setProvince(List<ProvinciaModel> province) {
		ComuniPool.province = province;
	}

	public static void setRegioni(List<RegioneModel> regioni) {
		ComuniPool.regioni = regioni;
	}

	public static List<ComuneModel> getComuni() {
		return comuni;
	}

	public static List<ProvinciaModel> getProvince() {
		return province;
	}

	public static List<RegioneModel> getRegioni() {
		return regioni;
	}
}
