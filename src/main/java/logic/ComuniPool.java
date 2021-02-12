package logic;

import java.util.Set;
import logic.model.ComuneModel;
import logic.model.ProvinciaModel;
import logic.model.RegioneModel;

public final class ComuniPool {
	private ComuniPool() {}
	
	private static Set<ComuneModel> comuni;
	private static Set<ProvinciaModel> province;
	private static Set<RegioneModel> regioni;

	public static void setComuni(Set<ComuneModel> comuni) {
		ComuniPool.comuni = comuni;
	}

	public static void setProvince(Set<ProvinciaModel> province) {
		ComuniPool.province = province;
	}

	public static void setRegioni(Set<RegioneModel> regioni) {
		ComuniPool.regioni = regioni;
	}

	public static Set<ComuneModel> getComuni() {
		return comuni;
	}

	public static Set<ProvinciaModel> getProvince() {
		return province;
	}

	public static Set<RegioneModel> getRegioni() {
		return regioni;
	}
}
