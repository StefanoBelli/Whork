package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import logic.graphicscontroller.ExceptionViewController;
import logic.graphicscontroller.GraphicsController;


/**
 * @author Stefano Belli
 * 
 * IMPORTANTE: Questa classe non ha la responsabilità di chiudere altre eventuali
 * Window (Stage in JavaFX). L'applicazione termina effettivamente solo se tutte le
 * Window sono chiuse, pertanto è responsabilità del metodo chiamante chiuderle.'
 */
public final class ExceptionView implements ControllableView {
	private static final String UNHANDLED_EXCEPTION_MSG = 
		"Unhandled exception: Whork desktop needs to terminate";
	private static final String EXIT_MSG = "Exit";
	private static final String WINDOW_TITLE = "Whork desktop unhandled exception log";
	private static final String CONFIG_FONT_FAMILY_STACKTRACE = "Monospace Regular";
	private static final String STACK_TRACE_MSG = "Stack trace below:";
	private static final int CONFIG_FONT_SIZE_STACKTRACE = 15;
	private static final int CONFIG_DESCFONT_SIZE = 15;
	private static final int CONFIG_EXCPNAMEFONT_SIZE = 14;
	private static final int CONFIG_EXCPMSGFONT_SIZE = 14;
	private static final int CONFIG_TAMINHEIGHT_SIZE = 290;

	private Scene scene;

	private Label desc;
	private Label exceptionName;
	private Label exceptionMessage;
	private Label stackTraceFollowsMessage;
	private TextArea exceptionStackTrace;
	private Button exitApp;

	private GraphicsController controller;

	public ExceptionView(Exception e, ViewStack viewStack) {
		controller = new ExceptionViewController(e, this, viewStack);
		init();
		populateScene();
	}

	private void init() {
		desc = new Label(UNHANDLED_EXCEPTION_MSG);
		desc.setFont(Font.font("", FontWeight.NORMAL, CONFIG_DESCFONT_SIZE));
		exceptionName = new Label();
		exceptionName.setFont(Font.font("", FontWeight.BOLD, CONFIG_EXCPNAMEFONT_SIZE));
		exceptionMessage = new Label();
		exceptionMessage.setFont(Font.font("", FontWeight.BOLD, CONFIG_EXCPMSGFONT_SIZE));
		stackTraceFollowsMessage = new Label(STACK_TRACE_MSG);
		exceptionStackTrace = new TextArea();
		exceptionStackTrace.setMinSize(0, CONFIG_TAMINHEIGHT_SIZE);
		exceptionStackTrace.setEditable(false);
		exceptionStackTrace.setFont(
			new Font(CONFIG_FONT_FAMILY_STACKTRACE, CONFIG_FONT_SIZE_STACKTRACE));
		exitApp = new Button(EXIT_MSG);
		controller.setup();
	}

	private void populateScene() {
		VBox vbox = new VBox();
		vbox.getChildren().add(desc);
		vbox.getChildren().add(exceptionName);
		vbox.getChildren().add(exceptionMessage);
		vbox.getChildren().add(stackTraceFollowsMessage);
		vbox.getChildren().add(exceptionStackTrace);
		vbox.getChildren().add(exitApp);
		scene = new Scene(vbox, DefaultWindowSize.WIDTH, DefaultWindowSize.HEIGHT);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WINDOW_TITLE);
		stage.setResizable(false);
	}

	@Override
	public Node[] getNodes() {
		return new Node[] { 
			exceptionName, 
			exceptionMessage, 
			exceptionStackTrace,
			exitApp
		};
	}

	@Override
	public void visible() {
		//no need to request update
	}
}
