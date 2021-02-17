package logic;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.view.ExceptionView;
import logic.view.HomeView;
import logic.view.LoginView;
import logic.view.View;
import logic.factory.BeanFactory;
import logic.graphicscontroller.LoginHandler;
import logic.util.Pair;
import logic.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public final class WhorkDesktopLauncher extends Application {

	private View mainView = null;
	public static final String AUTH_FILE_PATH = 
		System.getProperty("user.home") + "/.whork_desktop_auth.json";
	
	public static void launchApp(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		attemptLogin();
		mainView.setWindowProperties(primaryStage);
		primaryStage.setScene(mainView.getScene());
		primaryStage.show();
	}

	private Pair<String, String> parseJson(String json) {
		String email;
		String password;

		try {
			JSONObject rootObject = new JSONObject(json);
			email = rootObject.getString("email");
			password = rootObject.getString("password");
		} catch (JSONException e) {
			return null;
		}

		return new Pair<>(email, password);
	}

	private void attemptLogin() {
		File f = new File(AUTH_FILE_PATH);
		String jsonAuthCred;

		try {
			jsonAuthCred = Util.readFileEntirely(f);
		} catch(FileNotFoundException e) {
			mainView = new LoginView();
			return;
		} catch(Exception e) {
			mainView = new ExceptionView(e);
			return;
		}

		Pair<String, String> cred = parseJson(jsonAuthCred);
		if(cred != null) {
			boolean outcome;

			try {
				outcome = LoginHandler.login(
					BeanFactory.buildUserAuthBean(cred.getFirst(), cred.getSecond()));
			} catch (Exception e) {
				mainView = new ExceptionView(e);
				return;
			}

			mainView = outcome ? new HomeView() : new LoginView();
		} else {
			mainView = new LoginView();
		}
	}
}

