package logic.controller.service;

import logic.controller.ChatController;
import logic.exception.ServiceControllerManagerStateException;

public final class ServiceControllerManager {
	private static ServiceController chatServiceController = null;

	static {
		chatServiceController = ChatController.getInstance();
	}

	public ServiceController getService(Service service) 
			throws ServiceControllerManagerStateException {
		if(service == Service.CHAT && chatServiceController != null) {
			return chatServiceController;
		}

		throw new ServiceControllerManagerStateException();
	}
}
