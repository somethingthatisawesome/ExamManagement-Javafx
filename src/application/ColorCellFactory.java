package application;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.util.Callback;

public class ColorCellFactory implements Callback<ListView<String>, ListCell<String>> {

	@Override
	public ListCell<String> call(ListView<String> arg0) {
		// TODO Auto-generated method stub
		return new ColorCell();
	}

}
