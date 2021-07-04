package logic.controller;

import logic.controller.service.TokenizedServiceController;
import logic.util.Util;

public final class EditorController extends TokenizedServiceController {
	private EditorController() {
		super(Util.InstanceConfig.getInt(Util.InstanceConfig.KEY_SVC_EDITOR_PORT));
	}

	private static EditorController instance = null;

	public static EditorController getInstance() {
		if(instance == null) {
			instance = new EditorController();
		}

		return instance;
	}
}
