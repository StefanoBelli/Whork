package logic.graphicscontroller;

import logic.view.ControllableView;
import logic.view.ViewStack;

public abstract class GraphicsController {
	protected ControllableView view;
	protected ViewStack viewStack;

	protected GraphicsController(ControllableView view, ViewStack viewStack) {
		this.view = view;
		this.viewStack = viewStack;
	}

	public abstract void setup();
	public abstract void update();
}
