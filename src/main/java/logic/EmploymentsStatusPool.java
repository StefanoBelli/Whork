package logic;

import java.util.List;
import logic.model.EmploymentStatusModel;

public final class EmploymentsStatusPool {
	private static List<EmploymentStatusModel> employmentsStatus;

	public static void setEmploymentsStatus(List<EmploymentStatusModel> employmentsStatus) {
		EmploymentsStatusPool.employmentsStatus = employmentsStatus;
	}

	public static List<EmploymentStatusModel> getEmploymentsStatus() {
		return employmentsStatus;
	}
}
