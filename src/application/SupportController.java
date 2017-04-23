package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SupportController implements Initializable {
	private final String _Tempalepath = "./src/asset/headers/Template.docx";
	@FXML
	private Button openTemplateButton;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
	public void openTemplate()
	{
		File f = new File(_Tempalepath);
		try {
			String cmd = "cmd /C start winword.exe /t\"" + f.getAbsolutePath() + "\"";
	        Process child = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void showAboutUsForm()
	{
		try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AboutUsLayout.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setTitle("Về chúng tôi");
            stage.setScene(new Scene(root));  
            stage.showAndWait();
          }
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
	}
}
