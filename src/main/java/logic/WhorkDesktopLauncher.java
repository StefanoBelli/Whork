package logic;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.view.ExceptionView;
import logic.view.HomeView;
import logic.view.View;
import logic.view.ViewStack;
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
		ViewStack stack = new ViewStack(primaryStage);
		attemptLogin(stack);
		stack.push(mainView);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		Thread t = getThreadByName("Thread-PwdRecCleanup");

		if(t != null) {
			t.interrupt();
		}
	}

	private Thread getThreadByName(String threadName) {
		for (final Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName)) {
				return t;
			}
		}

		return null;
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

	private void attemptLogin(ViewStack stack) {
		File f = new File(AUTH_FILE_PATH);
		String jsonAuthCred;

		try {
			jsonAuthCred = Util.Files.readAll(f);
		} catch(FileNotFoundException e) {
			mainView = new HomeView(stack);
			return;
		} catch(Exception e) {
			mainView = new ExceptionView(e, stack);
			return;
		}

		Pair<String, String> cred = parseJson(jsonAuthCred);
		if(cred != null) {
			try {
				LoginHandler.login(
					BeanFactory.buildUserAuthBean(cred.getFirst(), cred.getSecond()));
			} catch (Exception e) {
				mainView = new ExceptionView(e, stack);
				return;
			}
		}

		mainView = new HomeView(stack);
	}
}

