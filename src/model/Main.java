package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải tệp FXML cho giao diện người dùng
            Parent root = FXMLLoader.load(getClass().getResource("/views/main-screen.fxml"));
            primaryStage.setTitle("Quản Lý Nhân Sự");
            primaryStage.setScene(new Scene(root, 600, 400)); // Đặt kích thước cửa sổ
            primaryStage.show(); // Hiển thị cửa sổ
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args); // Khởi động ứng dụng
    }
}