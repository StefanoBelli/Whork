package logic.graphicscontroller.commons;

import logic.exception.InternalException;
import logic.factory.DialogFactory;

public final class Commons {
	private Commons() {}

	public static final class ShowAndWaitDialog {
		private ShowAndWaitDialog() {}

		private static final String ERROR = "Error";

		public static void ioException() {
			DialogFactory.error(ERROR, "Unable to copy one or more file", "Check logs to get more infos").showAndWait();
		}

		public static void internalException(InternalException e) {
			DialogFactory.error("Internal exception", "Something bad just happened, we don't know much about it",
					e.getMessage()).showAndWait();
		}

		public static void dataAccessException() {
			DialogFactory.error(ERROR, "Unable to read data", "Check logs to get more infos").showAndWait();
		}

		public static void formDoesNotPassChecks(String errorString) {
			DialogFactory.error("Form does not pass checks", "Errors are following, fix them all", errorString)
					.showAndWait();
		}
	}
}
