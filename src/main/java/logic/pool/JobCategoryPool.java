package logic.pool;

import java.util.List;

import logic.bean.JobCategoryBean;

public final class JobCategoryPool {
	private JobCategoryPool() {}
	
	private static List<JobCategoryBean> jobCategories;

	public static void setJobCategories(List<JobCategoryBean> jobCategories) {
		JobCategoryPool.jobCategories = jobCategories;
	}

	public static List<JobCategoryBean> getJobCategories() {
		return jobCategories;
	}
}
