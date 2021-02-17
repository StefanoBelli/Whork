package logic.graphicscontroller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logic.view.ControllableView;

public final class ExceptionViewController implements GraphicsController {
	private Exception e;
	private ControllableView view;

	public ExceptionViewController(Exception e, ControllableView view) {
		this.e = e;
		this.view = view;
	}

	@Override
	public void setup() {
		Node[] n = view.getNodes();
		Button btnExit = (Button) n[3];

		String simpleName = e.getClass().getSimpleName();
		((Label)n[0]).setText("Class name: " + simpleName);

		String msg = e.getMessage();
		((Label)n[1]).setText(formatMessage(msg, simpleName));

		((TextArea)n[2]).setText(getStackTraceAsString(e));

		btnExit.setOnMouseClicked(event -> {
			Stage window = (Stage) btnExit.getScene().getWindow();
			window.close();
		});
	}

	private String formatMessage(String msg, String simpleName) {
		if(msg == null || msg.isEmpty()) {
			return simpleName + " left no message :/";
		}
		
		return simpleName + " says: " + msg;
	}

	private String getStackTraceAsString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
