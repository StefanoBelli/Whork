package logic.pool;

import java.util.List;

import logic.bean.TypeOfContractBean;

public final class TypeOfContractPool {
	private TypeOfContractPool() {}
	
	private static List<TypeOfContractBean> typesOfContract;

	public static void setTypesOfContract(List<TypeOfContractBean> typesOfContract) {
		TypeOfContractPool.typesOfContract = typesOfContract;
	}

	public static List<TypeOfContractBean> getTypesOfContract() {
		return typesOfContract;
	}
}
