package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import ell.Gobal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import ell.Paragraph;
public class PragraphController implements Initializable {
	@FXML
	private RadioButton questionRadioButton;
	@FXML
	private RadioButton answerRadioButton;
	@FXML 
	private ListView paragraphListView;
	@FXML
	private TextArea questionTextArea;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		for(Paragraph para:Gobal.paragraph)
		{
			paragraphListView.getItems().add(para.value.getText());
		
		}
	}
	public void paragraphOnClick()
	{
		
		int index = paragraphListView.getSelectionModel().getSelectedIndex();
		
		Paragraph pr = Gobal.paragraph.get(index);
		System.out.println(pr.isQuestion);
		if(pr.isQuestion)
		{
			questionRadioButton.setSelected(true);
			answerRadioButton.setSelected(false);
		}
		else
		{
			questionRadioButton.setSelected(false);
			answerRadioButton.setSelected(true);
		}
		
		
		if(pr.belongtoQuestion!=-1)
		{
			questionTextArea.setText(Gobal.paragraph.get(pr.belongtoQuestion).value.getText());
		}
		else
		{
			questionTextArea.clear();
		}
	}
	
}
