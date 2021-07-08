package logic.pool;

import java.util.List;
import logic.bean.ComuneBean;
import logic.bean.ProvinciaBean;
import logic.bean.RegioneBean;

public final class ComuniPool {
	private ComuniPool() {}
	
	private static List<ComuneBean> comuni;
	private static List<ProvinciaBean> province;
	private static List<RegioneBean> regioni;

	public static void setComuni(List<ComuneBean> comuni) {
		ComuniPool.comuni = comuni;
	}

	public static void setProvince(List<ProvinciaBean> province) {
		ComuniPool.province = province;
	}

	public static void setRegioni(List<RegioneBean> regioni) {
		ComuniPool.regioni = regioni;
	}

	public static List<ComuneBean> getComuni() {
		return comuni;
	}

	public static List<ProvinciaBean> getProvince() {
		return province;
	}

	public static List<RegioneBean> getRegioni() {
		return regioni;
	}
}
