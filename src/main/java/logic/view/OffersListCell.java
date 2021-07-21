package logic.view;

import javafx.scene.control.ListCell;
import logic.bean.OfferBean;

public class OffersListCell extends ListCell<OfferBean> {

	@Override
	public void updateItem(OfferBean itemBean, boolean empty) {
		super.updateItem(itemBean, empty);
		if(itemBean!=null) {
			OfferItem newItem = new OfferItem();
			newItem.setInfo(itemBean);
			setGraphic(newItem.getBox());
		}
		
	}
}
