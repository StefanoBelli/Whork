package logic.pool;

import java.util.List;
import logic.bean.EmploymentStatusBean;

public final class EmploymentsStatusPool {
	private EmploymentsStatusPool() {}
	
	private static List<EmploymentStatusBean> employmentsStatus;

	public static void setEmploymentsStatus(List<EmploymentStatusBean> employmentsStatus) {
		EmploymentsStatusPool.employmentsStatus = employmentsStatus;
	}

	public static List<EmploymentStatusBean> getEmploymentsStatus() {
		return employmentsStatus;
	}
}
