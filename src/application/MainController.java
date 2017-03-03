package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import bll.BLO;
import ell.Gobal;;
public class MainController implements Initializable {
	String filePath = "";
	BLO blo = new BLO();
	@FXML
	private TextField numberTextField;
	@FXML
	private Button helpButton;
	@FXML
	private ImageView openFileButton;
	@FXML
	private RadioButton letterRadioButton;
	@FXML
	private RadioButton numberRadioButton;
	@FXML
	private Pane statusPane;
	@FXML
	private Label statusInfo;
	@FXML
	private ImageView statusInfoIcon;
	@FXML 
	private Button chooseFileButton;
	@FXML
	private TextField pathTextField;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		statusPane.setVisible(false);
		letterRadioButton.setSelected(true);
		helpButton.setTooltip(new Tooltip("Trợ giúp"));
		
	}
	public void chooseFile()
	{
		 FileChooser chooser = new FileChooser();
		   chooser.setTitle("Open File");
		   chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document (*.docx)", "*.docx"));
		   String path = chooser.showOpenDialog(new Stage()).getAbsolutePath();
		   //System.out.println(path);
		   
		   pathTextField.setText(path);
	}
	public void beginButtonClick()
	{
		
	}
	public void checkButtonClick()
	{
		//check file
		String filepath  = pathTextField.getText();
		File f = new File(filepath);
		if(f.exists() && !f.isDirectory()) { 
		    System.out.println("file exist");
		}
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("");
			alert.setHeaderText("Đường dẫn không đúng!");
			alert.setContentText("Đường dẫn được cung cấp không khả dụng, xin vùi lòng nhập đường dẫn hợp lệ.");
			alert.showAndWait();
			return;
		}
		String extension="";
		int index = filepath.lastIndexOf('.');
		if (index > 0) {
		    extension = filepath.substring(index+1);
		}
		System.out.println(extension);
		
		if(!extension.equals("docx"))
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("");
			alert.setHeaderText("Tập tin không hợp lệ!");
			alert.setContentText("Phần mềm chỉ hỗ trợ các tập tin văn bản có đuôi mở rộng là \".docx\".");
			alert.showAndWait();
			return;
		}
		this.filePath = filepath;
		int fileStatus = blo.readParagraph(filePath);
		if(fileStatus ==0)
		{
			showSuccessStatus();
		};
		if(fileStatus==1)
		{
			showWarningStatus();
		};
		if(fileStatus==2)
		{
			showErrorStatus();
		};
	}
	public void exportRandomizeExam()
	{
		File file = new File(filePath);
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Document (*.docx)", "*.docx"));
		//chooser.setInitialDirectory(file);
		String path = chooser.showSaveDialog(new Stage()).getAbsolutePath();
		path=path.substring(0,path.lastIndexOf("."));
		blo.exportExams(Gobal.paragraph,path,Integer.parseInt(numberTextField.getText()),letterRadioButton.isSelected());
		//blo.exportRandomizeExam(Gobal.paragraph,path);
	}
	private void showSuccessStatus()
	{
		statusPane.setVisible(true);
	}
	private void showErrorStatus()
	{
		statusPane.setStyle("-fx-background-color:#EC7063;");
		statusInfo.setText("Error: Đề thi không hợp lệ");
		statusPane.setVisible(true);
	}
	private void showWarningStatus()
	{
		statusPane.setStyle("-fx-background-color:#F4D313;");
		statusInfo.setText("Warning: Cấu trúc đề thi có thể bị sai sót");
		statusPane.setVisible(true);
	}
	
	public void showCheckFileForm()
	{
		  try{
	            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CheckFileLayout.fxml"));
	            Parent root = (Parent) fxmlLoader.load();
	            Stage stage = new Stage();
	            stage.initModality(Modality.APPLICATION_MODAL);
	            //stage.initStyle(StageStyle.UNDECORATED);
	            stage.setResizable(false);
	            stage.setTitle("Kiểm tra");
	            stage.setScene(new Scene(root));  
	            stage.show();
	          }
		  catch(Exception e)
		  {
			  
		  }
	}
	
}
