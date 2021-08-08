package logic.graphicscontroller;

import logic.view.ControllableView;
import logic.view.ViewStack;

/**
 * @author Stefano Belli
 */
public abstract class GraphicsController {
	protected final ControllableView view;
	protected final ViewStack viewStack;

	protected GraphicsController(ControllableView view, ViewStack viewStack) {
		this.view = view;
		this.viewStack = viewStack;
	}

	public abstract void setup();
	public abstract void update();
}
