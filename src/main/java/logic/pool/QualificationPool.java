package logic.pool;

import java.util.List;

import logic.bean.QualificationBean;

public final class QualificationPool {
	private QualificationPool() {}
	
	private static List<QualificationBean> qualifications;

	public static void setQualifications(List<QualificationBean> qualifications) {
		QualificationPool.qualifications = qualifications;
	}

	public static List<QualificationBean> getQualifications() {
		return qualifications;
	}
}
