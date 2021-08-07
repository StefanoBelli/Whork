package logic.graphicscontroller.commons;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import logic.factory.DialogFactory;

public final class RegisterCommons {
	private RegisterCommons() {}

	public static final class ShowAndWaitDialog {
		private ShowAndWaitDialog() {}
		
		private static final String ERROR = "Error";

		public static void alreadyExistantUser() {
			DialogFactory.error(
				ERROR, 
				"Already existant user", 
				"Another user with same email and/or fiscal code already exists").showAndWait();
		}

		public static void success(String name, String email, String company) {
			StringBuilder msgBuilder = new StringBuilder("You successfully signed up for Whork. ")
					.append("Now it is time to confirm your request to join us by checking for a mail ")
					.append("we sent you at the address you just gave us: ").append(email)
					.append(", be sure to check spam also.\nThe Whork team.");

			if (company != null) {
				msgBuilder.append("\n\nAbout your company\nJust to be sure, we registered your company: ")
					.append(company);
			}

			DialogFactory.info(
				"Success",
				new StringBuilder("Yay! ").append(name).append(" you did it!").toString(),
				msgBuilder.toString()
			).showAndWait();
		}

		public static void success(String name, String email) {
			success(name, email, null);
		}
	}

	public static final class HandlePrivacyPolicyCheckBoxClicked implements EventHandler<MouseEvent> {
		private final Button confirmButton;
		private final CheckBox privacyPolicyCheckBox;

		public HandlePrivacyPolicyCheckBoxClicked(Button confirmButton, CheckBox privacyPolicyCheckBox) {
			this.confirmButton = confirmButton;
			this.privacyPolicyCheckBox = privacyPolicyCheckBox;
		}
		
		@Override
		public void handle(MouseEvent event) {
			confirmButton.setDisable(!privacyPolicyCheckBox.isSelected());
		}
	}
}
