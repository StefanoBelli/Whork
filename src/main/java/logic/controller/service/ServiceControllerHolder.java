package logic.controller.service;

import logic.controller.ChatController;
import logic.exception.ServiceControllerHolderStateException;

public final class ServiceControllerHolder {
	private ServiceControllerHolder() {}
	
	private static ServiceController chatServiceController = null;

	static {
		chatServiceController = ChatController.getInstance();
	}

	public static ServiceController getService(Service service) 
			throws ServiceControllerHolderStateException {
		if(service == Service.CHAT && chatServiceController != null) {
			return chatServiceController;
		}

		throw new ServiceControllerHolderStateException();
	}
}
