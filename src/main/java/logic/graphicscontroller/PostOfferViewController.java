package logic.graphicscontroller;

import java.io.File;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import logic.bean.JobCategoryBean;
import logic.bean.JobPositionBean;
import logic.bean.OfferBean;
import logic.bean.QualificationBean;
import logic.bean.TypeOfContractBean;
import logic.bean.UserBean;
import logic.controller.OfferController;
import logic.exception.InternalException;
import logic.factory.BeanFactory;
import logic.factory.DialogFactory;
import logic.graphicscontroller.commons.Commons;
import logic.graphicscontroller.formchecker.FormChecker;
import logic.graphicscontroller.formchecker.OfferFormChecker;
import logic.pool.JobCategoryPool;
import logic.pool.JobPositionPool;
import logic.pool.QualificationPool;
import logic.pool.TypeOfContractPool;
import logic.util.GraphicsUtil;
import logic.util.Util;
import logic.view.ControllableView;
import logic.view.ViewStack;

/**
 * @author Michele Tosi
 */
public final class PostOfferViewController extends GraphicsController {
	public static final String SELECT_FILE_MESSAGE = "(Select a file)";
	private final UserBean sessionUser = LoginHandler.getSessionUser();

	private Button postOfferBtn;
	private TextArea offerDescriptionTxt;
	private TextField offerSalaryTxt;
	private Button offerPhotoBtn;
	private TextField offerNameTxt;
	private ChoiceBox<String> jobCategoryCB;
	private ChoiceBox<String> jobPositionCB;
	private ChoiceBox<String> qualificationCB;
	private ChoiceBox<String> typeOfContractCB;
	private TextField offerNoteTxt;
	private TextField offerAddressTxt;
	private TextField workShiftTxt;
	private Label photoDetailLbl;
	
	private File offerPhoto;
	
	public PostOfferViewController(ControllableView view, ViewStack viewStack) {
		super(view, viewStack);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setup() {
		Node[] n = view.getNodes();
		postOfferBtn=(Button) n[0];
		offerDescriptionTxt=(TextArea) n[1];
		offerSalaryTxt=(TextField) n[2];
		offerPhotoBtn=(Button) n[3];
		offerNameTxt=(TextField) n[4];
		jobCategoryCB=(ChoiceBox<String>) n[5];
		jobPositionCB=(ChoiceBox<String>) n[6];
		qualificationCB=(ChoiceBox<String>) n[7];
		typeOfContractCB=(ChoiceBox<String>) n[8];
		offerNoteTxt=(TextField) n[9];
		offerAddressTxt=(TextField) n[10];
		workShiftTxt=(TextField) n[11];
		photoDetailLbl=(Label)n[12];
		
		GraphicsUtil.loadDataInChoiceBox(qualificationCB, QualificationPool.getQualifications(), QualificationBean.class);
		GraphicsUtil.loadDataInChoiceBox(jobCategoryCB, JobCategoryPool.getJobCategories(), JobCategoryBean.class);
		GraphicsUtil.loadDataInChoiceBox(typeOfContractCB, TypeOfContractPool.getTypesOfContract(), TypeOfContractBean.class);
		GraphicsUtil.loadDataInChoiceBox(jobPositionCB, JobPositionPool.getJobPositions(), JobPositionBean.class);
				
		setListener();
	}

	private void setListener() {
		offerPhotoBtn.setOnMouseClicked(new HandleOfferPhotoButtonClicked());
		postOfferBtn.setOnMouseClicked(new HandlePostOfferButtonClicked());	
	}

	@Override
	public void update() {
		//no need to update anything
	}
	
	private final class HandleOfferPhotoButtonClicked implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent event) {
			offerPhoto = GraphicsUtil.showFileChooser((Stage)view.getScene().getWindow(), 
				"Choose your profile photo", new ExtensionFilter("Image Files", "*.png", "*.jpg"));

			if(offerPhoto != null) {
				photoDetailLbl.setText(new StringBuilder("Selected: ").append(offerPhoto.getName()).toString());
			} else {
				photoDetailLbl.setText(SELECT_FILE_MESSAGE);
			}
		}
	}
	
	private final class HandlePostOfferButtonClicked implements EventHandler<MouseEvent> {
		private String category;
		private String description;
		private String salary;
		private String name;
		private String position;
		private String qualification;
		private String typeOfContract;
		private String note;
		private String address;
		private String workShift;
		
		
		@Override
		public void handle(MouseEvent event) {
			name = offerNameTxt.getText();
			description = offerDescriptionTxt.getText();
			salary = offerSalaryTxt.getText();
			category = jobCategoryCB.getValue();
			position = jobPositionCB.getValue();
			qualification = qualificationCB.getValue();
			typeOfContract = typeOfContractCB.getValue();
			note = offerNoteTxt.getText();
			address = offerAddressTxt.getText();
			workShift = workShiftTxt.getText();

			if(checksArePassing()) {
				try {
					OfferController.postOffer(createBeans());
				} catch (IOException e) {
					Util.exceptionLog(e);
					Commons.ShowAndWaitDialog.ioException();
					return;
				} catch (InternalException e) {
					Util.exceptionLog(e);
					Commons.ShowAndWaitDialog.internalException(e);
					return;
				}

				showSuccessDialogAndCloseStage(name);
			}
		}
	
		private void showSuccessDialogAndCloseStage(String offName) {
			ShowAndWaitDialog.success(sessionUser.getName(), offName);
			((Stage) view.getScene().getWindow()).close();
		}

		private OfferBean createBeans() throws IOException {
			OfferBean offerBean = new OfferBean();
			offerBean.setOfferName(name);
			offerBean.setCompany(sessionUser.getCompany());
			offerBean.setEmployee(sessionUser);
			offerBean.setDescription(description);
			offerBean.setJobCategory(BeanFactory.buildJobCategoryBean(category));
			offerBean.setJobPhysicalLocationFullAddress(address);
			offerBean.setJobPosition(BeanFactory.buildJobPositionBean(position));
			offerBean.setNote(note);
			offerBean.setPhoto(Util.Files.saveUserFile(sessionUser.getCf(), offerPhoto));
			offerBean.setQualification(BeanFactory.buildQualificationBean(qualification));
			offerBean.setSalaryEUR(Integer.parseInt(salary));
			offerBean.setTypeOfContract(BeanFactory.buildTypeOfContractBean(typeOfContract));
			offerBean.setWorkShift(workShift);
			return offerBean;
		}
		
		private boolean checksArePassing() {
			FormChecker checker = new OfferFormChecker();
			String errorString = checker.doChecks(new Object[] {
					name, workShift, category,
					position,qualification, typeOfContract,description
			});

			if(!errorString.equals("")) {
				Commons.ShowAndWaitDialog.formDoesNotPassChecks(errorString);
				return false;
			}

			return true;
		}
	}

	public static final class ShowAndWaitDialog {
		private ShowAndWaitDialog() {}

		public static void success(String name, String offerName) {
			final StringBuilder msgBuilder = new StringBuilder("You successfully post offer \"").append(offerName)
					.append("\" for Whork.\nThe Whork team.");

			DialogFactory.info("Success", new StringBuilder("Yay! ").append(name).append(" you did it!").toString(),
					msgBuilder.toString()).showAndWait();
		}
	}
}
