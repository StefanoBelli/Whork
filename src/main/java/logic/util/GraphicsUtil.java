package logic.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logic.bean.JobCategoryBean;
import logic.bean.JobPositionBean;
import logic.bean.QualificationBean;
import logic.bean.TypeOfContractBean;
import logic.graphicscontroller.LoginHandler;
import logic.view.ExceptionView;
import logic.view.View;
import logic.view.ViewStack;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Stefano Belli
 */
public final class GraphicsUtil {
	private GraphicsUtil() {}

	public static void showExceptionStage(Exception e) {
		Stage newStage = new Stage();
		ViewStack stack = new ViewStack(newStage);
		stack.push(new ExceptionView(e, stack));
		newStage.setOnCloseRequest(ev -> Platform.exit());
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.show();
	}

	public static void closeStageByMouseEvent(MouseEvent event) {
		((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
	}

	public static Font getBoldFont() {
		Font dflFont = Font.getDefault();
		return Font.font(dflFont.getFamily(), FontWeight.BOLD, dflFont.getSize());
	}

	public static File showFileChooser(Stage owner, String title, ExtensionFilter ...filters) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		fileChooser.getExtensionFilters().addAll(filters);

		return fileChooser.showOpenDialog(owner);
	}

	public static HBox getHBox(int spacing, Node... nodes) {
		HBox hbox = new HBox(spacing);
		for(final Node node : nodes) {
			hbox.getChildren().add(node);
		}

		return hbox;
	}

	public static void showAndWaitWindow(Class<?> cls, String ... kvProps) {
		if(kvProps.length % 2 != 0) {
			throw new IllegalArgumentException("kvProps.length must be even");
		}

		View instance;

		Stage stage = new Stage();
		ViewStack stack = new ViewStack(stage);
		
		try {
			instance = (View) cls.getConstructor(ViewStack.class).newInstance(stack);
			for(int i = 0; i < kvProps.length; i += 2) {
				cls.getMethod(kvProps[i], String.class).invoke(instance, kvProps[i + 1]);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
			return; //unreachable
		}

		stack.push(instance);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

	public static final class OnlyNumbersChangeListener implements ChangeListener<String> {
		private final TextField textField;
		
		public OnlyNumbersChangeListener(TextField textField) {
			this.textField = textField;
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if (!newValue.matches("\\d*")) {
				textField.setText(newValue.replaceAll("[^\\d]", ""));
			}
		}
	}

	public static final class LimitLengthChangeListener implements ChangeListener<String> {
		private final TextField textField;
		private final int maxLength;
		
		public LimitLengthChangeListener(TextField textField, int maxLength) {
			if(maxLength < 1) {
				throw new IllegalArgumentException("maxLength must be greater than or equal to 1");
			}

			this.textField = textField;
			this.maxLength = maxLength;
		}

		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			if(newValue.length() > maxLength) {
				textField.setText(oldValue);
			}
		}
	}

	public static final class HandleGoBackRequest implements EventHandler<MouseEvent> {
		private final ViewStack viewStack;

		public HandleGoBackRequest(ViewStack viewStack) {
			this.viewStack = viewStack;
		}

		@Override
		public void handle(MouseEvent event) {
			viewStack.pop();
		}
	}
	
	/**
	 * @author Michele Tosi
	 */
	public static void loadDataInChoiceBox(ChoiceBox<String> choiceBox,List<?> data, Object object) {
		List<String> items= new ArrayList<>();
		items.add("--select an option--");
		data.forEach(e->
		{if((object).equals(JobCategoryBean.class)) {
			items.add(((JobCategoryBean)e).getCategory());
		}else if((object).equals(JobPositionBean.class)) {
			items.add(((JobPositionBean)e).getPosition());
		}else if((object).equals(QualificationBean.class)) {
			items.add(((QualificationBean)e).getQualify());
		}else {
			items.add(((TypeOfContractBean)e).getContract());
		}		
		});
		choiceBox.setItems(FXCollections.observableArrayList(items));
		choiceBox.getSelectionModel().select(0);
	}

	public static String getMapsIframe(String query) {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append("<html><body><iframe title='whereareyou' width='200' height='200'")
					.append(" style='border:0' loading='lazy' allowfullscreen src='https://www.google.com")
					.append("/maps/embed/v1/place?key=AIzaSyAp5hG3kGqNGj6Auxh4IhC0Y60hzgUyzKo&q=")
					.append(URLEncoder.encode(query, "UTF-8")).append("'></iframe></body></html>");
		} catch (UnsupportedEncodingException e) {
			Util.exceptionLog(e);
			return e.getMessage();
		}

		return builder.toString();
	}
	
	public static final class HandleLogoutRequest implements EventHandler<MouseEvent> {
		private ViewStack viewStack;
		
		public HandleLogoutRequest(ViewStack viewStack) {
			this.viewStack = viewStack;
		}

		@Override
		public void handle(MouseEvent event) {
			LoginHandler.logout();
			
			try {
				Util.Files.overWriteJsonAuth(null, null);
			} catch(IOException e) {
				Util.exceptionLog(e);
				GraphicsUtil.closeStageByMouseEvent(event);
				GraphicsUtil.showExceptionStage(e);
			}

			viewStack.pop();
		}
	}

	
	/**
	 * @author Magliari Elio
	 */
	public static <K, V> K getKey(Map<K, V> map, V value){
		   for (Map.Entry<K, V> entry: map.entrySet())
		   {
		       if (value.equals(entry.getValue())) {
		           return entry.getKey();
		       }
		   }
		   return null;
		}
}