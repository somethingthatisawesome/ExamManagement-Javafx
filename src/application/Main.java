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
			Main.class.getResource("/asset");
	           Parent root = FXMLLoader.load(getClass()
	                   .getResource("MainLayout.fxml"));
	           primaryStage.setScene(new Scene(root));
	           primaryStage.setTitle("Ứng dụng hỗ trợ giáo viên soạn đề thi và chấm bài");
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
