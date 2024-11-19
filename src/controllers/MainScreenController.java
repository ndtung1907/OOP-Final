package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import Connection.JDBC;
import DAO.UserDAO;

public class MainScreenController {

    @FXML
    private ComboBox<String> positionComboBox;  // ComboBox cho lựa chọn chức vụ
    
    @FXML
    private TextField usernameField;  // TextField cho tài khoản
    
    @FXML
    private PasswordField passwordField;  // PasswordField cho mật khẩu
    
    @FXML
    private Button loginButton;  // Button đăng nhập
    
    @FXML
    private Label loginMessage;
    
    @FXML
    private Hyperlink forgotPasswordLink;
    
    @FXML
    private Button changePasswordButton;
    
    @FXML
    private TextField maNhanVienField;
    @FXML
    private Label soNgayCongLabel, soNgayNghiPhepLabel, luongCoBanLabel, ungLuongLabel, phuCapLabel, thuongLabel;
    @FXML
    private VBox salaryInfoContainer;
    @FXML
    private Label salarySectionLabel;



    public void initialize() {
    	// Thêm các mục vào ComboBox
        positionComboBox.setItems(FXCollections.observableArrayList("Nhân Viên", "Quản Lý"));

        // Đặt sự kiện cho nút đăng nhập
        loginButton.setOnAction(e -> handleLogin());
    }
    
 // Xử lý khi người dùng nhấn vào nút "Quên mật khẩu"
    @FXML
    private void handleForgotPassword() {
        // Tạo hộp thoại để người dùng nhập email
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Quên mật khẩu");
        dialog.setHeaderText("Nhập email của bạn để khôi phục mật khẩu");

        // Tạo nút xác nhận và hủy
        ButtonType submitButtonType = new ButtonType("Gửi mã xác minh", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        // Tạo form nhập email
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        VBox content = new VBox();
        content.getChildren().add(new Label("Email:"));
        content.getChildren().add(emailField);
        dialog.getDialogPane().setContent(content);

        // Xử lý khi người dùng nhấn nút "Gửi mã xác minh"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return emailField.getText();
            }
            return null;
        });

        // Hiển thị hộp thoại và lấy kết quả
        dialog.showAndWait().ifPresent(email -> {
            if (checkEmailExists(email)) {
                // Logic gửi mã xác minh đến email
                sendVerificationCodeToEmail(email);  // Gọi hàm gửi mã xác minh

                // Thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Mã xác minh đã được gửi đến email của bạn.");
                alert.showAndWait();
            } else {
                // Thông báo nếu email không tồn tại
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Email không tồn tại trong hệ thống.");
                alert.showAndWait();
            }
        });
    }

    // Hàm kiểm tra email có tồn tại trong database hay không
    private boolean checkEmailExists(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra nếu email tồn tại
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm giả lập gửi mã xác minh đến email (có thể thêm logic gửi email thực sự ở đây)
    private void sendVerificationCodeToEmail(String email) {
        // Logic gửi email
        System.out.println("Gửi mã xác minh đến: " + email);
    }
    
    @FXML
    private void handleChangePassword() {
        // Mở một hộp thoại để nhập thông tin đổi mật khẩu
        Stage dialog = new Stage();
        dialog.setTitle("Đổi Mật Khẩu");

        VBox layout = new VBox(10);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Tài khoản");

        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Mật khẩu hiện tại");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Mật khẩu mới");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Xác minh mật khẩu mới");

        Button changeButton = new Button("Đổi mật khẩu");
        changeButton.setOnAction(e -> {
            String username = usernameField.getText();
            String currentPassword = currentPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // Kiểm tra nếu mật khẩu mới và xác nhận mật khẩu khớp nhau
            if (newPassword.equals(confirmPassword)) {
                // Gọi hàm changePassword để cập nhật mật khẩu
                if (changePassword(username, currentPassword, newPassword)) {
                    // Hiển thị thông báo đổi mật khẩu thành công
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Đổi mật khẩu");
                    alert.setHeaderText(null);
                    alert.setContentText("Đổi mật khẩu thành công!");

                    alert.showAndWait();  // Hiển thị hộp thoại và đợi người dùng đóng nó
                    // Quay trở lại trang đăng nhập
                    Stage stage = (Stage) changeButton.getScene().getWindow();  // Lấy stage hiện tại
                    stage.close();  // Đóng cửa sổ hiện tại
                } else {
                    // Nếu đổi mật khẩu không thành công, hiển thị cảnh báo
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Đổi mật khẩu không thành công. Tài khoản hoặc mật khẩu hiện tại không đúng.");
                    alert.showAndWait();
                }
            } else {
                // Nếu mật khẩu không khớp, hiển thị cảnh báo
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Mật khẩu mới và xác minh mật khẩu không khớp.");
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(usernameField, currentPasswordField, newPasswordField, confirmPasswordField, changeButton);

        Scene scene = new Scene(layout, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }
    
    
    private void goToMainScreen(Button logoutButton) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-screen.fxml"));
	        Parent root = loader.load();

	        // Hiển thị màn hình chính
	        Stage stage = (Stage) logoutButton.getScene().getWindow(); // Lấy cửa sổ hiện tại
	        stage.setScene(new Scene(root));
	        stage.setTitle("Đăng Nhập");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

    private void showLoginScreen() {
        try {
            // Tải màn hình đăng nhập từ file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-screen.fxml"));
            Parent loginScreen = loader.load();

            // Tạo Scene mới cho màn hình đăng nhập
            Stage stage = new Stage();
            stage.setScene(new Scene(loginScreen));
            stage.setTitle("Đăng Nhập");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean changePassword(String username, String currentPassword, String newPassword) {
        // Kiểm tra tài khoản và mật khẩu hiện tại
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, currentPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Cập nhật mật khẩu mới
                String updateQuery = "UPDATE Users SET password = ? WHERE username = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, newPassword);
                    updateStmt.setString(2, username);
                    updateStmt.executeUpdate();
                    return true;  // Đổi mật khẩu thành công
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Đổi mật khẩu không thành công
    }


    public void handleLogin() {
        String userType = positionComboBox.getValue();  // Lấy chức vụ đã chọn
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userType == null || username.isEmpty() || password.isEmpty()) {
            showAlert("Vui lòng nhập đầy đủ thông tin.", Alert.AlertType.ERROR);
            return;
        }

        // Kiểm tra đăng nhập
        if (isValidAccount(username, userType) && login(username, password)) {
            // Lấy cửa sổ hiện tại và đóng nó
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();  // Đóng cửa sổ đăng nhập

            if (isFirstLogin(username)) {
                showPasswordResetDialog(username, userType);  // Mở giao diện đổi mật khẩu
            } 
            else {
                if (userType.equals("Nhân Viên")) {
                    goToEmployeeScreen(username);  // Chuyển đến màn hình dành cho nhân viên
                } else {
                    goToAdminScreen(username);  // Chuyển đến màn hình dành cho quản lý
                }
            }
        } else {
            showAlert("Tài khoản hoặc mật khẩu không đúng.", Alert.AlertType.ERROR);
        }
    }
    
    public void goToAdminScreen(String username) {
        // Tạo giao diện chính với VBox và HBox để chia màn hình
        VBox mainLayout = new VBox(10);  // Main layout
        HBox contentLayout = new HBox(10);  // Chia đôi màn hình 70% - 30%

        // Left side - 70% dành cho hiển thị danh sách nhân viên
        VBox leftPane = new VBox(10);
        leftPane.setPrefWidth(0.8 * 1200);

        Label employeeListLabel = new Label("Danh sách Nhân Viên:");
        ListView<String> employeeListView = new ListView<>();  // Danh sách nhân viên
        populateEmployeeList(employeeListView);  // Gọi hàm để đổ danh sách nhân viên từ database

        leftPane.getChildren().addAll(employeeListLabel, employeeListView);

        // Right side - 30% dành cho các chức năng
        VBox rightPane = new VBox(10);
        rightPane.setPrefWidth(0.2 * 1200);  //

        // Nút thêm nhân viên
        Button addButton = new Button("Thêm Nhân Viên");
        addButton.setOnAction(e -> showAddEmployeeDialog(employeeListView)); // Truyền vào employeeListView

        // Nút xóa nhân viên
        Button deleteButton = new Button("Xóa Nhân Viên");
        deleteButton.setOnAction(e -> showDeleteEmployeeDialog(employeeListView)); // Thêm employeeListView nếu cần

        // Nút sửa thông tin nhân viên
        Button editButton = new Button("Sửa Thông Tin Nhân Viên");
        editButton.setOnAction(e -> showEditEmployeeDialog(employeeListView)); // Thêm employeeListView nếu cần

        Button editSalary = new Button("Sửa Thông Tin Lương Nhân Viên");
        editSalary.setOnAction(e -> showEditSalaryDialog()); // Thêm employeeListView nếu cần
        rightPane.getChildren().addAll(addButton, deleteButton, editButton, editSalary);

        // Thêm hai phần vào layout chính
        contentLayout.getChildren().addAll(leftPane, rightPane);
        mainLayout.getChildren().add(contentLayout);

        // Thêm nút đăng xuất ở góc dưới bên trái
        Button logoutButton = new Button("Đăng Xuất");
        logoutButton.setOnAction(e -> goToMainScreen(logoutButton)); // Chuyển về màn hình chính

        HBox logoutLayout = new HBox(10);
        logoutLayout.setAlignment(Pos.BOTTOM_LEFT); // Canh giữa dưới bên trái
        logoutLayout.getChildren().add(logoutButton);
        mainLayout.getChildren().add(logoutLayout); // Thêm vào layout chính

        // Hiển thị giao diện
        Scene scene = new Scene(mainLayout, 1100, 500);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Nhân Sự - Quản Lý Nhân Viên");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @FXML
    private void searchSalary() {
        String maNhanVien = maNhanVienField.getText();
        if (maNhanVien.isEmpty()) {
            showAlert("Vui lòng nhập mã nhân viên.", Alert.AlertType.ERROR);
            return;
        }

        String congQuery = "SELECT * FROM Cong WHERE MaNhanVien = ?";
        String luongQuery = "SELECT * FROM Luong WHERE MaChamCong = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement congStmt = conn.prepareStatement(congQuery)) {
            
            congStmt.setString(1, maNhanVien);
            ResultSet congRs = congStmt.executeQuery();

            if (congRs.next()) {
                // Lấy thông tin từ bảng Công
                String maChamCong = congRs.getString("MaChamCong");
                int soNgayCong = congRs.getInt("SoNgayCong");
                int soNgayNghiPhep = congRs.getInt("SoNgayNghiPhep");

                // Hiển thị số ngày công và ngày nghỉ phép
                soNgayCongLabel.setText(String.valueOf(soNgayCong));
                soNgayNghiPhepLabel.setText(String.valueOf(soNgayNghiPhep));

                // Truy vấn bảng Lương bằng mã chấm công
                try (PreparedStatement luongStmt = conn.prepareStatement(luongQuery)) {
                    luongStmt.setString(1, maChamCong);
                    ResultSet luongRs = luongStmt.executeQuery();

                    if (luongRs.next()) {
                        // Lấy thông tin từ bảng Lương
                        double luongCoBan = luongRs.getDouble("LuongCoBan");
                        double ungLuong = luongRs.getDouble("UngLuong");
                        double phuCap = luongRs.getDouble("PhuCap");
                        double thuong = luongRs.getDouble("Thuong");

                        // Hiển thị thông tin lương
                        luongCoBanLabel.setText(String.valueOf(luongCoBan));
                        ungLuongLabel.setText(String.valueOf(ungLuong));
                        phuCapLabel.setText(String.valueOf(phuCap));
                        thuongLabel.setText(String.valueOf(thuong));

                        // Hiển thị khu vực thông tin lương
                        salaryInfoContainer.setVisible(true);
                    } else {
                        showAlert("Không tìm thấy thông tin lương cho nhân viên.", Alert.AlertType.ERROR);
                    }
                }
            } else {
                showAlert("Không tìm thấy mã nhân viên trong bảng công.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi khi tìm kiếm thông tin lương.", Alert.AlertType.ERROR);
        }
    }

    private void showEditSalaryDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Sửa Thông Tin Lương Nhân Viên");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));

        // Ô nhập mã nhân viên
        Label maNhanVienLabel = new Label("Mã Nhân Viên:");
        TextField maNhanVienField = new TextField();
        maNhanVienField.setPromptText("Nhập mã nhân viên");

        // Nút tìm kiếm để lấy thông tin lương dựa trên mã nhân viên
        Button searchButton = new Button("Tìm Kiếm");
        searchButton.setOnAction(e -> {
            String maNhanVien = maNhanVienField.getText();
            if (!maNhanVien.isEmpty()) {
                // Gọi hàm hiển thị thông tin lương sau khi tìm kiếm mã nhân viên
                displaySalaryDetails(layout, maNhanVien, dialog);
            } else {
                showAlert("Vui lòng nhập mã nhân viên.", Alert.AlertType.ERROR);
            }
        });

        // Thêm phần nhập mã nhân viên và nút tìm kiếm vào layout
        layout.getChildren().addAll(maNhanVienLabel, maNhanVienField, searchButton);

        Scene scene = new Scene(layout, 400, 600);
        dialog.setScene(scene);
        dialog.show();
    }

    private void displaySalaryDetails(VBox layout, String maNhanVien, Stage dialog) {
        // Các label và textfield để hiển thị thông tin lương
        Label soNgayCongLabel = new Label("Số Ngày Công:");
        TextField soNgayCongField = new TextField();
        soNgayCongField.setEditable(false);  // Không cho phép chỉnh sửa ban đầu

        Label soNgayNghiPhepLabel = new Label("Số Ngày Nghỉ Phép:");
        TextField soNgayNghiPhepField = new TextField();
        soNgayNghiPhepField.setEditable(false);

        Label luongCoBanLabel = new Label("Lương Cơ Bản:");
        TextField luongCoBanField = new TextField();
        luongCoBanField.setEditable(false);

        Label ungLuongLabel = new Label("Ứng Lương:");
        TextField ungLuongField = new TextField();
        ungLuongField.setEditable(false);

        Label phuCapLabel = new Label("Phụ Cấp:");
        TextField phuCapField = new TextField();
        phuCapField.setEditable(false);

        Label thuongLabel = new Label("Thưởng:");
        TextField thuongField = new TextField();
        thuongField.setEditable(false);

        // Label cho tổng lương
        Label tongLuongLabel = new Label("Tổng Lương:");
        Label tongLuongValueLabel = new Label("0.00");  // Giá trị tổng lương

        // Nút sửa để kích hoạt chỉnh sửa
        Button editButton = new Button("Sửa");
        Button updateButton = new Button("Cập Nhật");
        updateButton.setDisable(true);  // Ban đầu ẩn nút Cập Nhật

        // Nút quay lại
        Button backButton = new Button("Quay Lại");
        backButton.setOnAction(e -> {
            dialog.close();  // Đóng màn hình sửa thông tin lương
        });

        // Kích hoạt chế độ chỉnh sửa khi nhấn nút Sửa
        editButton.setOnAction(e -> {
            soNgayCongField.setEditable(true);
            soNgayNghiPhepField.setEditable(true);
            luongCoBanField.setEditable(true);
            ungLuongField.setEditable(true);
            phuCapField.setEditable(true);
            thuongField.setEditable(true);

            updateButton.setDisable(false);  // Hiển thị nút Cập Nhật
        });

        // Cập nhật tổng lương khi các trường thông tin thay đổi
        soNgayCongField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalSalary(tongLuongValueLabel, soNgayCongField, luongCoBanField, ungLuongField, phuCapField, thuongField));
        luongCoBanField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalSalary(tongLuongValueLabel, soNgayCongField, luongCoBanField, ungLuongField, phuCapField, thuongField));
        ungLuongField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalSalary(tongLuongValueLabel, soNgayCongField, luongCoBanField, ungLuongField, phuCapField, thuongField));
        phuCapField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalSalary(tongLuongValueLabel, soNgayCongField, luongCoBanField, ungLuongField, phuCapField, thuongField));
        thuongField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalSalary(tongLuongValueLabel, soNgayCongField, luongCoBanField, ungLuongField, phuCapField, thuongField));

        // Xử lý cập nhật thông tin khi nhấn nút Cập Nhật
        updateButton.setOnAction(e -> {
            try {
                int soNgayCong = Integer.parseInt(soNgayCongField.getText());
                int soNgayNghiPhep = Integer.parseInt(soNgayNghiPhepField.getText());
                double luongCoBan = Double.parseDouble(luongCoBanField.getText());
                double ungLuong = Double.parseDouble(ungLuongField.getText());
                double phuCap = Double.parseDouble(phuCapField.getText());
                double thuong = Double.parseDouble(thuongField.getText());

                // Gọi hàm cập nhật vào database
                updateSalaryDetailsInDatabase(maNhanVien, soNgayCong, soNgayNghiPhep, luongCoBan, ungLuong, phuCap, thuong);

                showAlert("Cập nhật thành công", Alert.AlertType.INFORMATION);
            } catch (NumberFormatException ex) {
                showAlert("Vui lòng nhập đúng định dạng số.", Alert.AlertType.ERROR);
            }	
        });

        // Thêm các trường thông tin vào layout
        layout.getChildren().clear();  // Xóa các phần nhập mã nhân viên trước đó
        layout.getChildren().addAll(
                soNgayCongLabel, soNgayCongField,
                soNgayNghiPhepLabel, soNgayNghiPhepField,
                luongCoBanLabel, luongCoBanField,
                ungLuongLabel, ungLuongField,
                phuCapLabel, phuCapField,
                thuongLabel, thuongField,
                tongLuongLabel, tongLuongValueLabel,
                editButton, updateButton, backButton // Thêm nút Quay Lại vào layout
        );

        // Lấy thông tin lương từ cơ sở dữ liệu và hiển thị
        populateSalaryFields(maNhanVien, soNgayCongField, soNgayNghiPhepField, luongCoBanField, ungLuongField, phuCapField, thuongField);
    }

    private void updateTotalSalary(Label tongLuongValueLabel, TextField soNgayCongField, TextField luongCoBanField, TextField ungLuongField, TextField phuCapField, TextField thuongField) {
        try {
            int soNgayCong = Integer.parseInt(soNgayCongField.getText());
            double luongCoBan = Double.parseDouble(luongCoBanField.getText());
            double ungLuong = Double.parseDouble(ungLuongField.getText());
            double phuCap = Double.parseDouble(phuCapField.getText());
            double thuong = Double.parseDouble(thuongField.getText());
            double tongLuong = (luongCoBan * soNgayCong) - ungLuong + phuCap + thuong;
            tongLuongValueLabel.setText(String.format("%.2f", tongLuong));
        } catch (NumberFormatException ex) {
            tongLuongValueLabel.setText("N/A");
        }
    }

    private void populateSalaryFields(String maNhanVien, TextField ngayCongField, TextField soNgayNghiPhepField, TextField luongCoBanField, TextField ungLuongField, TextField phuCapField, TextField thuongField) {
        // Truy vấn dữ liệu từ cơ sở dữ liệu để lấy thông tin lương dựa trên mã nhân viên
        String query = "SELECT Cong.NgayCong, Cong.SoNgayNghiPhep, Luong.LuongCoBan, Luong.UngLuong, Luong.PhuCap, Luong.Thuong " +
                       "FROM Cong INNER JOIN Luong ON Cong.MaChamCong = Luong.MaChamCong WHERE Cong.MaNhanVien = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, maNhanVien);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ngayCongField.setText(String.valueOf(rs.getInt("NgayCong")));
                soNgayNghiPhepField.setText(String.valueOf(rs.getInt("SoNgayNghiPhep")));
                luongCoBanField.setText(String.valueOf(rs.getDouble("LuongCoBan")));
                ungLuongField.setText(String.valueOf(rs.getDouble("UngLuong")));
                phuCapField.setText(String.valueOf(rs.getDouble("PhuCap")));
                thuongField.setText(String.valueOf(rs.getDouble("Thuong")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi khi lấy thông tin lương.", Alert.AlertType.ERROR);
        }
    }

    private void updateSalaryDetailsInDatabase(String maNhanVien, int ngayCong, int soNgayNghiPhep, double luongCoBan, double ungLuong, double phuCap, double thuong) {
        String updateQuery = "UPDATE Luong INNER JOIN Cong ON Cong.MaChamCong = Luong.MaChamCong " +
                             "SET Cong.NgayCong = ?, Cong.SoNgayNghiPhep = ?, Luong.LuongCoBan = ?, Luong.UngLuong = ?, Luong.PhuCap = ?, Luong.Thuong = ? " +
                             "WHERE Cong.MaNhanVien = ?";

        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            
            stmt.setInt(1, ngayCong);
            stmt.setInt(2, soNgayNghiPhep);
            stmt.setDouble(3, luongCoBan);
            stmt.setDouble(4, ungLuong);
            stmt.setDouble(5, phuCap);
            stmt.setDouble(6, thuong);
            stmt.setString(7, maNhanVien);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi khi cập nhật thông tin lương.", Alert.AlertType.ERROR);
        }
    }


    
    


    
    private boolean isPhongBanValid(String maPhongBan) {
        String query = "SELECT COUNT(*) FROM PhongBan WHERE MaPhongBan = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, maPhongBan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu số lượng lớn hơn 0 thì mã phòng ban tồn tại
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi khi kiểm tra mã phòng ban", Alert.AlertType.ERROR);
        }
        return false; // Mặc định trả về false nếu có lỗi
    }


    private void showAddEmployeeDialog(ListView<String> employeeListView) {
        Stage dialog = new Stage();
        dialog.setTitle("Thêm Nhân Viên");

        VBox layout = new VBox(10);
        
        // Các trường nhập thông tin nhân viên
        TextField maNhanVienField = new TextField();
        maNhanVienField.setPromptText("Mã Nhân Viên");

        TextField hoTenField = new TextField();
        hoTenField.setPromptText("Họ Tên");

        TextField ngaySinhField = new TextField();
        ngaySinhField.setPromptText("Ngày Sinh (yyyy-mm-dd)");

        ComboBox<String> gioiTinhComboBox = new ComboBox<>();
        gioiTinhComboBox.getItems().addAll("Nam", "Nữ", "Khác");
        gioiTinhComboBox.setPromptText("Giới Tính");

        TextField soDienThoaiField = new TextField();
        soDienThoaiField.setPromptText("Số Điện Thoại");

        TextField soCMNDField = new TextField();
        soCMNDField.setPromptText("Số CMND");

        TextField queQuanField = new TextField();
        queQuanField.setPromptText("Quê Quán");

        TextField diaChiField = new TextField();
        diaChiField.setPromptText("Địa Chỉ");
        
        TextField maPhongBanField = new TextField();
        maPhongBanField.setPromptText("Mã Phòng Ban");

        // Nút Thêm Nhân Viên
        Button addButton = new Button("Thêm");
        addButton.setOnAction(e -> {
            // Lấy dữ liệu từ các trường nhập liệu
            String maNhanVien = maNhanVienField.getText();
            String hoTen = hoTenField.getText();
            String ngaySinh = ngaySinhField.getText();
            String gioiTinh = gioiTinhComboBox.getValue();
            String soDienThoai = soDienThoaiField.getText();
            String soCMND = soCMNDField.getText();
            String queQuan = queQuanField.getText();
            String diaChi = diaChiField.getText();
            String maPhongBan = maPhongBanField.getText();

         // Kiểm tra tính hợp lệ và thêm vào cơ sở dữ liệu
            if (!maNhanVien.isEmpty() && !hoTen.isEmpty() && !ngaySinh.isEmpty() && gioiTinh != null &&
                !soDienThoai.isEmpty() && !soCMND.isEmpty() && !queQuan.isEmpty() && !diaChi.isEmpty() &&
                !maPhongBan.isEmpty()) {
                
                // Kiểm tra mã nhân viên
                if (maNhanVien.length() >= 2 && maNhanVien.startsWith("NV")) {
                    // Kiểm tra mã phòng ban có hợp lệ không
                    if (isPhongBanValid(maPhongBan)) {
                        if (addEmployeeToDatabase(maNhanVien, hoTen, ngaySinh, gioiTinh, soDienThoai, soCMND, queQuan, diaChi, maPhongBan)) {
                            showAlert("Thêm nhân viên thành công!", Alert.AlertType.INFORMATION);
                            updateEmployeeList(employeeListView); // Cập nhật danh sách nhân viên
                            dialog.close();
                        } else {
                            showAlert("Đã xảy ra lỗi khi thêm nhân viên", Alert.AlertType.ERROR);
                        }
                    } else {
                        showAlert("Mã Phòng Ban không hợp lệ", Alert.AlertType.ERROR);
                    }
                } else {
                    showAlert("Mã Nhân Viên phải bắt đầu bằng 'NV' và có ít nhất 2 ký tự", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Vui lòng nhập đầy đủ thông tin", Alert.AlertType.ERROR);
            }

        });

        // Thêm các trường vào layout
        layout.getChildren().addAll(
            new Label("Mã Nhân Viên:"), maNhanVienField,
            new Label("Họ Tên:"), hoTenField,
            new Label("Ngày Sinh:"), ngaySinhField,
            new Label("Giới Tính:"), gioiTinhComboBox,
            new Label("Số Điện Thoại:"), soDienThoaiField,
            new Label("Số CMND:"), soCMNDField,
            new Label("Quê Quán:"), queQuanField,
            new Label("Địa Chỉ:"), diaChiField,
            new Label("Mã Phòng Ban:"), maPhongBanField,
            addButton
        );

        Scene scene = new Scene(layout, 400, 600);  // Cài đặt kích thước cửa sổ
        dialog.setScene(scene);
        dialog.show();
    }


    private void updateEmployeeList(ListView<String> employeeListView) {
        employeeListView.getItems().clear(); // Xóa danh sách hiện tại
        populateEmployeeList(employeeListView); // Gọi lại hàm để lấy danh sách từ cơ sở dữ liệu
    }
    
    private boolean addEmployeeToDatabase(String maNhanVien, String hoTen, String ngaySinh, String gioiTinh, 
            String soDienThoai, String soCMND, String queQuan, String diaChi, String maPhongBan) {
		String insertEmployeeQuery = "INSERT INTO NhanVien (MaNhanVien, HoTen, NgaySinh, GioiTinh, SoDienThoai, SoCMND, QueQuan, DiaChi, MaPhongBan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String insertUserQuery = "INSERT INTO users (username, password, userType, firstLogin, email) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = JDBC.getConnection();
			PreparedStatement employeeStmt = conn.prepareStatement(insertEmployeeQuery);
			PreparedStatement userStmt = conn.prepareStatement(insertUserQuery)) {
			
				// Thêm thông tin nhân viên
				employeeStmt.setString(1, maNhanVien);
				employeeStmt.setString(2, hoTen);
				employeeStmt.setString(3, ngaySinh);
				employeeStmt.setString(4, gioiTinh);
				employeeStmt.setString(5, soDienThoai);
				employeeStmt.setString(6, soCMND);
				employeeStmt.setString(7, queQuan);
				employeeStmt.setString(8, diaChi);
				employeeStmt.setString(9, maPhongBan);
				
				// Thực hiện thêm nhân viên
				int employeeRowsAffected = employeeStmt.executeUpdate();
				
				if (employeeRowsAffected > 0) {
				// Nếu thêm nhân viên thành công, thêm tài khoản cho nhân viên
				String username = maNhanVien; // Username là mã nhân viên
				String password = "123456a@"; // Mật khẩu mặc định
				String userType = maNhanVien.substring(0, 2); // Hai ký tự đầu của mã nhân viên
				boolean firstLogin = true; // Đặt true cho lần đăng nhập đầu tiên
				String email = username + "@pk.edu.vn"; // Cú pháp email
				
				// Thêm tài khoản người dùng
				userStmt.setString(1, username);
				userStmt.setString(2, password);
				userStmt.setString(3, userType);
				userStmt.setBoolean(4, firstLogin);
				userStmt.setString(5, email);
				
				int userRowsAffected = userStmt.executeUpdate(); // Thêm tài khoản
				
				return userRowsAffected > 0; // Trả về true nếu thêm tài khoản thành công
			}
			
		} catch (SQLException e) {
		e.printStackTrace();
	}

	return false; // Trả về false nếu xảy ra lỗi
	}

    
    private void showDeleteEmployeeDialog(ListView<String> employeeListView) {
        Stage dialog = new Stage();
        dialog.setTitle("Xóa Nhân Viên");

        VBox layout = new VBox(10);
        TextField maNhanVienField = new TextField();
        maNhanVienField.setPromptText("Nhập Mã Nhân Viên");

        Button deleteButton = new Button("Xóa");
        deleteButton.setOnAction(e -> {
            String maNhanVien = maNhanVienField.getText();
            if (!maNhanVien.isEmpty()) {
                boolean isDeleted = deleteEmployee(maNhanVien);  // Xóa nhân viên theo Mã Nhân Viên
                if (isDeleted) {
                    populateEmployeeList(employeeListView);  // Cập nhật danh sách nhân viên
                    showAlert("Xóa nhân viên thành công!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Không tìm thấy Mã Nhân Viên!", Alert.AlertType.ERROR);
                }
                dialog.close();  // Đóng màn hình xóa
            } else {
                showAlert("Mã Nhân Viên không được để trống", Alert.AlertType.ERROR);
            }
        });

        layout.getChildren().addAll(maNhanVienField, deleteButton);
        
        Scene scene = new Scene(layout, 300, 150);
        dialog.setScene(scene);
        dialog.show();
    }


    private boolean deleteEmployee(String maNhanVien) {
        // Bắt đầu giao dịch
        try (Connection conn = JDBC.getConnection()) {
            conn.setAutoCommit(false); // Tắt chế độ tự động cam kết

            // Xóa tài khoản của nhân viên
            String deleteUserQuery = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery)) {
                deleteUserStmt.setString(1, maNhanVien);
                deleteUserStmt.executeUpdate();
            }

            // Xóa nhân viên
            String deleteEmployeeQuery = "DELETE FROM NhanVien WHERE MaNhanVien = ?";
            try (PreparedStatement deleteEmployeeStmt = conn.prepareStatement(deleteEmployeeQuery)) {
                deleteEmployeeStmt.setString(1, maNhanVien);
                int rowsAffected = deleteEmployeeStmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit(); // Cam kết giao dịch nếu xóa thành công
                    return true;  // Trả về true nếu xóa thành công
                } else {
                    conn.rollback(); // Quay lại nếu không xóa được nhân viên
                    return false; // Trả về false nếu không tìm thấy nhân viên
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Trả về false nếu xảy ra lỗi
        }
    }

    private void showEditEmployeeDialog(ListView<String> employeeListView) {
        Stage dialog = new Stage();
        dialog.setTitle("Sửa Thông Tin Nhân Viên");

        VBox layout = new VBox(10);
        
        // Nhập Mã Nhân Viên
        TextField maNhanVienField = new TextField();
        maNhanVienField.setPromptText("Nhập Mã Nhân Viên");
        
        // ComboBox chọn thuộc tính muốn sửa
        ComboBox<String> attributeComboBox = new ComboBox<>();
        attributeComboBox.setItems(FXCollections.observableArrayList(
            "MaNhanVien", "HoTen", "NgaySinh", "SoCMND", "QueQuan", "SoDienThoai", "DiaChi", "MaPhongBan"
        ));
        attributeComboBox.setPromptText("Chọn thuộc tính"); // Thiết lập văn bản placeholder

        
        // Nhập giá trị mới cho thuộc tính
        TextField newValueField = new TextField();
        newValueField.setPromptText("Giá trị mới");

        // Nút cập nhật thông tin
        Button editButton = new Button("Cập nhật");
        editButton.setOnAction(e -> {
            String maNhanVien = maNhanVienField.getText();
            String attribute = attributeComboBox.getValue();
            String newValue = newValueField.getText();
            
            if (!maNhanVien.isEmpty() && attribute != null && !newValue.isEmpty()) {
                // Cập nhật thông tin nhân viên trong cơ sở dữ liệu
                if (updateEmployeeInfo(maNhanVien, attribute, newValue)) {
                    showAlert("Cập nhật thông tin thành công!", Alert.AlertType.INFORMATION);
                    updateEmployeeList(employeeListView);  // Cập nhật danh sách nhân viên
                    dialog.close();
                } else {
                    showAlert("Đã xảy ra lỗi khi cập nhật thông tin nhân viên", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Vui lòng nhập đầy đủ thông tin", Alert.AlertType.ERROR);
            }
        });

        layout.getChildren().addAll(maNhanVienField, attributeComboBox, newValueField, editButton);
        
        Scene scene = new Scene(layout, 400, 300);
        dialog.setScene(scene);
        dialog.show();
    }


    private boolean updateEmployeeInfo(String maNhanVien, String attribute, String newValue) {
        String query = "UPDATE NhanVien SET " + attribute + " = ? WHERE MaNhanVien = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newValue);
            stmt.setString(2, maNhanVien);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Trả về false nếu có lỗi
        }
    }


    private void populateEmployeeList(ListView<String> employeeListView) {
        // Kết nối với Database và lấy danh sách nhân viên
        String query = "SELECT * FROM NhanVien";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            // Xóa tất cả các mục cũ trong ListView trước khi thêm mới
            employeeListView.getItems().clear();
            
            while (rs.next()) {
                String maNhanVien = rs.getString("MaNhanVien");
                String hoTen = rs.getString("HoTen");
                String ngaySinh = rs.getString("NgaySinh");
                String gioitinh = rs.getString("GioiTinh");
                String soCMND = rs.getString("SoCMND");
                String queQuan = rs.getString("QueQuan");
                String soDienThoai = rs.getString("SoDienThoai");
                String diaChi = rs.getString("DiaChi");
                String maPhongBan = rs.getString("MaPhongBan");
                
                // Định dạng chuỗi để hiển thị toàn bộ thông tin
                String employeeInfo = String.format("Mã NV: %s, Họ Tên: %s, Ngày Sinh: %s, Giới tính: %s, Số CMND: %s, Quê Quán: %s, Số Điện Thoại: %s, Địa Chỉ: %s, Mã Phòng Ban: %s", 
                    maNhanVien, hoTen, ngaySinh, gioitinh, soCMND, queQuan, soDienThoai, diaChi, maPhongBan);
                    
                employeeListView.getItems().add(employeeInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Đã xảy ra lỗi khi tải danh sách nhân viên.", Alert.AlertType.ERROR);
        }
    }

    private void goToEmployeeScreen(String username) {
        try {
            // Tải FXML cho màn hình Nhân Viên
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/showEmployeeDashboard.fxml")); // Đường dẫn tới file FXML
            Parent root = loader.load();

            // Lấy controller và truyền username
            EmployeeDashboardController controller = loader.getController();
            controller.showEmployeeInfo(username);
            // Hiển thị cửa sổ mới
            Stage stage = (Stage) loginButton.getScene().getWindow(); // Lấy cửa sổ hiện tại
            stage.setScene(new Scene(root)); // Thiết lập cảnh mới
            stage.setTitle("Màn hình Nhân Viên");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Có lỗi xảy ra khi mở màn hình nhân viên.", Alert.AlertType.ERROR);
        }
    }

    private boolean isValidAccount(String username, String userType) {
        String prefix = "";
        if (userType.equals("Nhân Viên")) {
            prefix = "NV";
        } else if (userType.equals("Quản Lý")) {
            prefix = "QL";
        }
        return username.matches(prefix + "\\d{4}");  // Kiểm tra tài khoản có hợp lệ không
    }

    private boolean login(String username, String password) {
        UserDAO userDAO = new UserDAO();
        return userDAO.login(username, password);  // Gọi UserDAO để kiểm tra thông tin đăng nhập
    }

    private boolean isFirstLogin(String username) {
        UserDAO userDAO = new UserDAO();
        String defaultPassword = "123456a@";
        String currentPassword = userDAO.getPassword(username);
        return currentPassword.equals(defaultPassword);  // Kiểm tra nếu mật khẩu là mật khẩu mặc định
    }

    private void showPasswordResetDialog(String username, String userType) {
        // Hiển thị giao diện đổi mật khẩu
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Đổi Mật Khẩu - " + userType);
        alert.setHeaderText("Nhập mật khẩu mới");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Mật khẩu mới");

        VBox dialogPaneContent = new VBox(10);
        dialogPaneContent.getChildren().addAll(newPasswordField);
        alert.getDialogPane().setContent(dialogPaneContent);

        ButtonType resetButtonType = new ButtonType("Đổi mật khẩu");
        ButtonType cancelButtonType = new ButtonType("Hủy");
        alert.getDialogPane().getButtonTypes().addAll(resetButtonType, cancelButtonType);

        // Hiển thị hộp thoại và đợi người dùng phản hồi
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == resetButtonType) {
            String newPassword = newPasswordField.getText();
            if (!newPassword.isEmpty()) {
                updateUserPassword(username, newPassword);  // Cập nhật mật khẩu trong database
                showAlert("Mật khẩu đã được thay đổi thành công!", Alert.AlertType.INFORMATION);
                alert.close();  // Đóng cửa sổ đổi mật khẩu
            } else {
                showAlert("Mật khẩu không được để trống.", Alert.AlertType.ERROR);
            }
        }
        showLoginScreen();
    }

    private void updateUserPassword(String username, String newPassword) {
        UserDAO userDAO = new UserDAO();
        userDAO.updatePassword(username, newPassword);  // Gọi phương thức để cập nhật mật khẩu trong database
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

}