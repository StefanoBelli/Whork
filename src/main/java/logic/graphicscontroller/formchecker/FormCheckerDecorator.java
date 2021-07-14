package logic.graphicscontroller.formchecker;

public abstract class FormCheckerDecorator extends FormChecker {
	protected final FormChecker formChecker;

	protected FormCheckerDecorator(FormChecker formChecker) {
		this.formChecker = formChecker;
	}
}
