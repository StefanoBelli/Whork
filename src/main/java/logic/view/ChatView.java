package logic.view;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.graphicscontroller.ChatViewController;
import logic.graphicscontroller.GraphicsController;

public final class ChatView implements ControllableView {
	private static final String WIN_TITLE = "Chat - Whork";

	private static final String BTN_SEND_MSG = "Send";

	private TextArea chatTextArea;
	private TextArea sndTextArea;
	private Button sndButton;
	private Label chattingWithLabel;

	private Scene scene;

	private GraphicsController controller;

	public ChatView(ViewStack viewStack) {
		controller = new ChatViewController(this, viewStack);
		init();
		setNodesProps();
		populateScene();
	}

	private void populateScene() {
		VBox rootVbox = new VBox();
		rootVbox.getChildren().add(chattingWithLabel);
		rootVbox.getChildren().add(chatTextArea);

		HBox sndHbox = new HBox();
		sndHbox.getChildren().add(sndTextArea);
		sndHbox.getChildren().add(sndButton);

		rootVbox.getChildren().add(sndHbox);

		scene = new Scene(rootVbox, DefaultWindowSize.WIDTH, DefaultWindowSize.HEIGHT);
	}

	private void setNodesProps() {
		chatTextArea.setDisable(true);
		sndTextArea.setDisable(true);
		sndButton.setDisable(true);
	}

	private void init() {
		sndButton = new Button(BTN_SEND_MSG);
		chatTextArea = new TextArea();
		sndTextArea = new TextArea();
		chattingWithLabel = new Label();
		controller.setup();
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void setWindowProperties(Stage stage) {
		stage.setTitle(WIN_TITLE);
		stage.setResizable(false);
	}

	@Override
	public void visible() {
		//no need to update anything
	}

	@Override
	public Node[] getNodes() {
		return new Node[] {
			chattingWithLabel,
			chatTextArea,
			sndTextArea,
			sndButton
		};
	}
	
}
