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
import logic.bean.JobCategoryBean;
import logic.bean.JobPositionBean;
import logic.bean.OfferBean;
import logic.bean.QualificationBean;
import logic.bean.TypeOfContractBean;
import logic.controller.CandidatureController;
import logic.controller.OfferController;
import logic.exception.InternalException;
import logic.factory.BeanFactory;

import logic.graphicscontroller.state.Context;
import logic.pool.JobCategoryPool;
import logic.pool.JobPositionPool;
import logic.pool.QualificationPool;
import logic.pool.TypeOfContractPool;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.AccountView;
import logic.view.ChatView;
import logic.view.ControllableView;
import logic.view.LoginView;
import logic.view.PostOfferView;
import logic.view.OfferItem;
import logic.view.ViewStack;

public final class HomeViewController extends GraphicsController {


	private static final String SELECT_AN_OPTION = "--select an option--";
	
	private Button accountBtn;
	private Button resetBtn;
	private TextField searchField;
	private Button searchBtn;
	private ListView<OfferBean> offersLst;
	private ChoiceBox<String> jobCategoryCB;
	private ChoiceBox<String> jobPositionCB;
	private ChoiceBox<String> qualificationCB;
	private ChoiceBox<String> typeOfContractCB;
	private Button postOfferBtn;
	
	private List<OfferBean> offers = new ArrayList<>();
	
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
		postOfferBtn=(Button) n[9];
		
		GraphicsUtil.loadDataInChoiceBox(jobCategoryCB, JobCategoryPool.getJobCategories(), JobCategoryBean.class);
		GraphicsUtil.loadDataInChoiceBox(jobPositionCB, JobPositionPool.getJobPositions(), JobPositionBean.class);
		GraphicsUtil.loadDataInChoiceBox(qualificationCB, QualificationPool.getQualifications(), QualificationBean.class);
		GraphicsUtil.loadDataInChoiceBox(typeOfContractCB, TypeOfContractPool.getTypesOfContract(), TypeOfContractBean.class);

		
		
		setListeners();
	}

	private void setListeners() {
		accountBtn.setOnMouseClicked(new HandleAccountButtonRequest());
		searchBtn.setOnMouseClicked(new HandleSearchButtonRequest());
		resetBtn.setOnMouseClicked(new HandleResetButtonRequest());
		postOfferBtn.setOnMouseClicked(new HandlePostOfferButtonRequest());
	}

	private void dynamicViewUpdate() {
		if(LoginHandler.getSessionUser() != null) {
			accountBtn.setText("My account");
		} else {
			accountBtn.setText("Login");
		}
		
		if(LoginHandler.getSessionUser()== null || !LoginHandler.getSessionUser().isEmployee()) {
			postOfferBtn.setVisible(false);
		}else if(LoginHandler.getSessionUser().isAdmin()) {
			postOfferBtn.setVisible(true);
			postOfferBtn.setDisable(true);
		}else {
			postOfferBtn.setVisible(true);
			postOfferBtn.setDisable(false);
		}

		loadOffersNoFilters();
	}

	private void loadOffersNoFilters() {
		searchField.setText("");
		jobCategoryCB.setValue(SELECT_AN_OPTION);
		jobPositionCB.setValue(SELECT_AN_OPTION);
		qualificationCB.setValue(SELECT_AN_OPTION);
		typeOfContractCB.setValue(SELECT_AN_OPTION);

		try {
			offers = OfferController.searchOffers(null, null, null, null, null);
		} catch (InternalException e) {
			Util.exceptionLog(e);
			GraphicsUtil.showExceptionStage(e);
		}

		fillListView(FXCollections.observableArrayList(offers));
	}
	
	private final class HandleResetButtonRequest implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			loadOffersNoFilters();
		}
	}
	
	private final class HandleSearchButtonRequest implements EventHandler<MouseEvent> {
		
		@Override
		public void handle(MouseEvent event) {
			try {
				String searchTerm = searchField.getText();
				if(!searchTerm.isBlank()) {
					offers = OfferController.searchOffers(
						searchTerm,
						strOrNull(jobCategoryCB.getValue()),
						strOrNull(jobPositionCB.getValue()),
						strOrNull(qualificationCB.getValue()),
						strOrNull(typeOfContractCB.getValue()));
					fillListView(FXCollections.observableArrayList(offers));
				}
			} catch (InternalException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
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
	
	
	private final class HandlePostOfferButtonRequest implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			GraphicsUtil.showAndWaitWindow(PostOfferView.class);
		}
	}

	public static final class HandleChatRequest implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			GraphicsUtil.showAndWaitWindow(ChatView.class);
		}
	}

	public static final class HandleCandidateRequest implements EventHandler<MouseEvent> {
		private OfferBean offer;
		private Context context;
		private Button candidateBtn;
		
		public HandleCandidateRequest(OfferBean offer, Button candidateBtn, Context context) {
			this.offer = offer;
			this.context=context;
			this.candidateBtn=candidateBtn;
			
		}

		@Override
		public void handle(MouseEvent event) {
			try {
				CandidatureController.insertCandidature(BeanFactory.buildCandidatureBean(offer, LoginHandler.getSessionUser()));
				context.candidate();
				candidateBtn.setDisable(true);
			} catch (InternalException e) {
				Util.exceptionLog(e);
				GraphicsUtil.showExceptionStage(e);
			}
		}
	}

	@Override
	public void update() {
		dynamicViewUpdate();
	}

	
	private static String strOrNull(String s) {
		return s.equals(SELECT_AN_OPTION) ? null : s;
	}
	
	@SuppressWarnings({"squid:S110", "squid:S1854"})
	private void fillListView(ObservableList<OfferBean> list) {
		offersLst.setItems(list);
		offersLst.setCellFactory((ListView<OfferBean> oUnused) -> new ListCell<OfferBean>() {
				@Override
				public void updateItem(OfferBean itemBean, boolean empty) {
					super.updateItem(itemBean, empty);
					if (itemBean != null) {
						OfferItem newItem = new OfferItem();
						newItem.setInfo(itemBean);
						setGraphic(newItem.getBox());
					}
				}
			}
		);
	}
}
