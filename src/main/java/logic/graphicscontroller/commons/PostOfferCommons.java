package logic.graphicscontroller.commons;

import logic.factory.DialogFactory;

public final class PostOfferCommons {
	private PostOfferCommons() {}

	public static final class ShowAndWaitDialog {
		private ShowAndWaitDialog() {}
	
		private static final String ERROR = "Error";

		public static void ioException() {
			DialogFactory.error(
				ERROR, 
				"Unable to copy one or more file", 
				"Check logs to get more infos").showAndWait();
		}
		
		public static void dataAccessException() {
			DialogFactory.error(
				ERROR, 
				"Unable to read data", 
				"Check logs to get more infos").showAndWait();
		}
	
		public static void success(String name, String offerName) {
			final StringBuilder msgBuilder = 
				new StringBuilder("You successfully post an offer")
				.append(offerName)
				.append(" for Whork.\nThe Whork team.");

			DialogFactory.info(
				"Success",
				new StringBuilder("Yay! ").append(name).append(" you did it!").toString(),
				msgBuilder.toString()
			).showAndWait();
		}
		
		public static void formDoesNotPassChecks(String errorString) {
			DialogFactory.error(
				"Form does not pass checks", 
				"Errors are following, fix them all", 
				errorString).showAndWait();
		}
	}
}
