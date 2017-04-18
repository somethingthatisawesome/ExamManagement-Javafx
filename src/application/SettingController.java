package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.security.auth.callback.Callback;

import bll.BLO;
import ell.Gobal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
/*
 * Form cài đặt
 * Người dùng tự chọn màu sắc mong muốn để định dạng văn bản
 */
public class SettingController implements Initializable {
	BLO blo= new BLO();
	String[] colors = {"Black","Dark Red","Red","Orange","Yellow","Light Green","Green","Light Blue","Blue","Dark Blue","Purple"};
	String[] highlightColors = {"Black","Dark Red","Red","Yellow","Dark Yellow","Dark Blue","Bright Green","Turquoise","Blue","Teal","Grey-50%","Pink","Green","Grey-25%","Blue","Violet"};
	@FXML
	private ComboBox DAcolorComboBox;
	@FXML
	private ComboBox DQcolorComboBox;
	@FXML
	private ComboBox NCAcolorComboBox;
	@FXML
	private ComboBox correctAcomboBox;
	@FXML
	private ComboBox staticQcomboBox;
	@FXML
	private Button applyButton;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		correctAcomboBox.getItems().add("Auto");
		staticQcomboBox.getItems().add("Auto");
		addHighLightItemtoComboBox(DAcolorComboBox);
		addHighLightItemtoComboBox(DQcolorComboBox);
		addHighLightItemtoComboBox(NCAcolorComboBox);
		addItemtoComboBox(correctAcomboBox);
		addItemtoComboBox(staticQcomboBox);
		this.getValue();
		System.out.println("fawef");
	};
	/*
	 * nạp thông tin màu từ Gobal
	 */
	private void getValue()
	{
		DAcolorComboBox.getSelectionModel().select(Gobal._DA_error);
		DQcolorComboBox.getSelectionModel().select(Gobal._DQ_error);
		NCAcolorComboBox.getSelectionModel().select(Gobal._NCA_error);
		correctAcomboBox.getSelectionModel().select(Gobal._isCorrectAns);
		staticQcomboBox.getSelectionModel().select(Gobal._staticQuestion);
	}
	/*
	 * Thêm màu vào danh sách
	 */
	private void addItemtoComboBox(ComboBox comboBox)
	{
		comboBox.getItems().addAll(colors);
		comboBox.setCellFactory(new ColorCellFactory());	
	}
	private void addHighLightItemtoComboBox(ComboBox comboBox)
	{
		comboBox.getItems().addAll(highlightColors);
		comboBox.setCellFactory(new ColorCellFactory());	
	}
	public void applyButtonClicked()
	{
		List<String> format = new ArrayList<String>();
		Gobal._staticQuestion = staticQcomboBox.getSelectionModel().getSelectedItem().toString();
		Gobal._isCorrectAns = correctAcomboBox.getSelectionModel().getSelectedItem().toString();
		Gobal._DA_error = DAcolorComboBox.getSelectionModel().getSelectedItem().toString();
		Gobal._DQ_error = DQcolorComboBox.getSelectionModel().getSelectedItem().toString();
		Gobal._NCA_error = NCAcolorComboBox.getSelectionModel().getSelectedItem().toString();
		format.add(String.valueOf(Gobal._staticQuestion));
		format.add(String.valueOf(Gobal._isCorrectAns));
		format.add(String.valueOf(Gobal._DA_error));
		format.add(String.valueOf(Gobal._DQ_error));
		format.add(String.valueOf(Gobal._NCA_error));
		blo.saveFormat(format);
		Stage stage = (Stage) applyButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	public void defaultValueClicked()
	{
		DAcolorComboBox.getSelectionModel().select("Red");
		DQcolorComboBox.getSelectionModel().select("Green");
		NCAcolorComboBox.getSelectionModel().select("Blue");
		correctAcomboBox.getSelectionModel().select("Auto");
		staticQcomboBox.getSelectionModel().select("Auto");
	}
}
