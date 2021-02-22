package logic.view;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Questa struttura dati permette ad un singolo stage di assumere
 * più schermate (di fatto tornare indietro di molteplici schermate)
 * l'ultima nello stack è la schermata visibile.
 * 
 * Stage stage = new Stage();
 * ViewStack vstack = new ViewStack(stage);
 * View view = new ConcreteView(vstack);
 * vstack.push(view);
 * stage.show();
 */
public final class ViewStack {
	private Stage stage;
	private Deque<View> views;
	private boolean validState;

	public ViewStack(Stage stage) {
		this.stage = stage;
		this.views = new ArrayDeque<>();
		this.validState = true;

		this.stage.setOnCloseRequest(new StageCloseHandler());
	}

	private void makeItVisible(View view) {
		view.setWindowProperties(this.stage);
		this.stage.setScene(view.getScene());
		view.visible();
	}

	public void push(View view) {
		if(validState) {
			this.views.push(view);
			makeItVisible(view);
		}
	}

	public void pop() {
		if(validState) {
			this.views.pop();
			makeItVisible(this.views.getLast());
		}
	}

	private final class StageCloseHandler implements EventHandler<WindowEvent> {

		/**
		 * Invalida in modo permanente lo stato dell'oggetto (instanceof ViewStack)! 
		 * Permette alla JVM di liberare memoria: 
		 * 	(1) elimina tutti gli elementi dallo stack 
		 *	(2) elimina le referenze agli oggetti in heap
		 */
		@Override
		public void handle(WindowEvent event) {
			if (validState) {
				views.clear();
				views = null;
				stage = null;
				validState = false;
			}
		}

	}
}
