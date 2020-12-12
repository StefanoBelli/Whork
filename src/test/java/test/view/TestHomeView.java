package test.view;

import static org.junit.Assert.*;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import logic.view.HomeView;

public class TestHomeView extends ApplicationTest {

	private HomeView view;
	
	@Override
	public void start(Stage stage) throws Exception {
		view = new HomeView();
		
		stage.setTitle("whork-test");
		stage.setScene(view.getScene());
		stage.show();
	}
	
	@Test
	public void testLabelHasWhorkDescription() {
		HBox hbox = (HBox) view.getScene().getRoot();
		Label label = (Label) from(hbox).lookup("#mainLabel").query();
		
		assertEquals("Whork desktop app", label.getText());
	}

}
