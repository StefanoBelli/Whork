package logic.graphicscontroller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import logic.bean.OfferBean;
import logic.controller.OfferController;
import logic.exception.DataAccessException;
import logic.pool.JobCategoryPool;
import logic.pool.JobPositionPool;
import logic.pool.QualificationPool;
import logic.pool.TypeOfContractPool;
import logic.util.GraphicsUtil;
import logic.view.AccountView;
import logic.view.ControllableView;
import logic.view.LoginView;
import logic.view.OffersListCell;
import logic.view.ViewStack;

public final class HomeViewController extends GraphicsController {
	private Button accountBtn;
	private Button resetBtn;
	private TextField searchField;
	private Button searchBtn;
	private ListView<OfferBean> offersLst;
	private ChoiceBox<String> jobCategoryCB;
	private ChoiceBox<String> jobPositionCB;
	private ChoiceBox<String> qualificationCB;
	private ChoiceBox<String> typeOfContractCB;
	
	private List<OfferBean> offers=new ArrayList<>();
	

	public HomeViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setup() {
		Node[] n = view.getNodes();
		accountBtn = (Button) n[0];
		searchField=(TextField) n[1];
		searchBtn = (Button) n[2];
		offersLst= (ListView<OfferBean>) n[3];
		jobCategoryCB=(ChoiceBox<String>) n[4];
		jobPositionCB=(ChoiceBox<String>) n[5];
		qualificationCB=(ChoiceBox<String>) n[6];
		typeOfContractCB=(ChoiceBox<String>) n[7];
		resetBtn = (Button) n[8];
		
		
		
		loadJobCategories();
		loadJobPositions();
		loadQualifications();
		loadTypesOfContract();
		fillListView(FXCollections.observableArrayList(offers));
		setListeners();
	}

	private void setListeners() {
		accountBtn.setOnMouseClicked(new HandleAccountButtonRequest());
		searchBtn.setOnMouseClicked(new HandleSearchButtonRequest());
		resetBtn.setOnMouseClicked(new HandleResetButtonRequest());
		
	}

	private void dynamicViewUpdate() {
		if(LoginHandler.getSessionUser() != null) {
			accountBtn.setText("My account");
		} else {
			accountBtn.setText("Login");
		}
	}
	
private final class HandleResetButtonRequest implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			try {
				searchField.setText("");
				jobCategoryCB.setValue("--select an option--");
				jobPositionCB.setValue("--select an option--");
				qualificationCB.setValue("--select an option--");
				typeOfContractCB.setValue("--select an option--");
				offers = OfferController.getOffers(null, null, null, null, null);
				fillListView(FXCollections.observableArrayList(offers));
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private final class HandleSearchButtonRequest implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			try {
				offers = OfferController.getOffers(searchField.getText(),strOrNull(jobCategoryCB.getValue()),
						strOrNull(jobPositionCB.getValue()),strOrNull(qualificationCB.getValue()),
						strOrNull(typeOfContractCB.getValue()));
				fillListView(FXCollections.observableArrayList(offers));
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private final class HandleAccountButtonRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			if(LoginHandler.getSessionUser() == null) {
				GraphicsUtil.showAndWaitWindow(LoginView.class);
				dynamicViewUpdate();
			} else {
				viewStack.push(new AccountView(viewStack));
			}
		}
	}

	@Override
	public void update() {
		dynamicViewUpdate();
	}
	
	private void loadJobCategories() {
		List<String> category = new ArrayList<>();
		category.add("--select an option--");
		JobCategoryPool.getJobCategories().forEach(e -> category.add(e.getCategory()));
		jobCategoryCB.setItems(FXCollections.observableArrayList(category));
		jobCategoryCB.getSelectionModel().select(0);
	}

	private void loadJobPositions() {
		List<String> position = new ArrayList<>();
		position.add("--select an option--");
		JobPositionPool.getJobPositions().forEach(e -> position.add(e.getPosition()));
		jobPositionCB.setItems(FXCollections.observableArrayList(position));
		jobPositionCB.getSelectionModel().select(0);
	}
	
	
	private void loadQualifications() {
		List<String> qualify = new ArrayList<>();
		qualify.add("--select an option--");
		QualificationPool.getQualifications().forEach(e -> qualify.add(e.getQualify()));
		qualificationCB.setItems(FXCollections.observableArrayList(qualify));
		qualificationCB.getSelectionModel().select(0);
	}
	
	
	private void loadTypesOfContract() {
		List<String> contract = new ArrayList<>();
		contract.add("--select an option--");
		TypeOfContractPool.getTypesOfContract().forEach(e -> contract.add(e.getContract()));
		typeOfContractCB.setItems(FXCollections.observableArrayList(contract));
		typeOfContractCB.getSelectionModel().select(0);
	}
	
	public static String strOrNull(String s) {
		return (s=="--select an option")? null:s;
	}
	
	
	private void fillListView(ObservableList<OfferBean> list) {
		offersLst.setItems(list);
		offersLst.setCellFactory(new Callback<ListView<OfferBean>, ListCell<OfferBean>>() {
            @Override
            public ListCell<OfferBean> call(ListView<OfferBean> offers) {
                return new OffersListCell();
            }
        });
	}
	
}
