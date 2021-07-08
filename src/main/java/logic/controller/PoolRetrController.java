package logic.controller;

import java.util.ArrayList;
import java.util.List;

import logic.bean.ComuneBean;
import logic.bean.EmploymentStatusBean;
import logic.factory.BeanFactory;
import logic.model.ComuneModel;
import logic.model.EmploymentStatusModel;
import logic.pool.ComuniPool;
import logic.pool.EmploymentsStatusPool;

public final class PoolRetrController {
	private PoolRetrController() {}

	public static List<EmploymentStatusBean> getEmploymentStatuses() {
		List<EmploymentStatusBean> empStatus = new ArrayList<>();

		for(final EmploymentStatusModel model : EmploymentsStatusPool.getEmploymentsStatus()) {
			empStatus.add(BeanFactory.buildEmploymentStatusBean(model));	
		}

		return empStatus;
	}

	public static List<ComuneBean> getComuni() {
		List<ComuneBean> comuni = new ArrayList<>();

		for (final ComuneModel model : ComuniPool.getComuni()) {
			comuni.add(BeanFactory.buildComuneBean(model));
		}

		return comuni;
	}
}
