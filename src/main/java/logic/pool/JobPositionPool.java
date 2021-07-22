package logic.pool;

import java.util.List;

import logic.bean.JobPositionBean;

public final class JobPositionPool {
	private JobPositionPool() {}
	
	private static List<JobPositionBean> positions;

	public static void setJobPositions(List<JobPositionBean> positions) {
		JobPositionPool.positions = positions;
	}

	public static List<JobPositionBean> getJobPositions() {
		return positions;
	}
}
