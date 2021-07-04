package logic.controller.service;

import logic.controller.ChatController;
import logic.controller.EditorController;

public final class ServiceControllerHolder {
	private ServiceControllerHolder() {}
	
	private static TokenizedServiceController chatServiceController = null;
	private static TokenizedServiceController editorServiceController = null;

	static {
		chatServiceController = ChatController.getInstance();
		editorServiceController = EditorController.getInstance();
	}

	public static TokenizedServiceController getService(Service service) {
		if(service == Service.CHAT && chatServiceController != null) {
			return chatServiceController;
		} else if(service == Service.EDITOR && editorServiceController != null) {
			return editorServiceController;
		}

		return null; //should never be reached
	}
}
