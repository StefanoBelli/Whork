package logic.controller.service;

import logic.controller.ChatController;

public final class ServiceControllerHolder {
	private ServiceControllerHolder() {}
	
	private static ServiceController chatServiceController = null;

	static {
		chatServiceController = ChatController.getInstance();
	}

	public static ServiceController getService(Service service) {
		if(service == Service.CHAT && chatServiceController != null) {
			return chatServiceController;
		}

		return null; //should never be reached
	}
}
