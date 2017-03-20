package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			/*
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();*/
			Main.class.getResource("/asset");
	           Parent root = FXMLLoader.load(getClass()
	                   .getResource("MainLayout.fxml"));
	           //primaryStage.initStyle(StageStyle.UNDECORATED);
	           primaryStage.setScene(new Scene(root));
	           primaryStage.setTitle("Phần mềm soạn đề thi trắc nghiệm");
	           primaryStage.setResizable(false);
	           primaryStage.show();
	           
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}
