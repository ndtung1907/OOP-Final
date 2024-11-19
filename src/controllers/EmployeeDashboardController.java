package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Connection.JDBC;

public class EmployeeDashboardController {

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label maNhanVienLabel, hoTenLabel, ngaySinhLabel, soCMNDLabel,
                  queQuanLabel, soDienThoaiLabel, gioiTinhLabel, diaChiLabel, maPhongBanLabel;

    @FXML
    private Button logoutButton;  // Nút đăng xuất
    
    @FXML
    private Button viewSalaryButton;

    // Hàm để hiển thị thông tin nhân viên
    public void showEmployeeInfo(String username) {
        String query = "SELECT * FROM NhanVien NV JOIN users U ON NV.MaNhanVien = U.username WHERE U.username = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Gán dữ liệu từ database lên giao diện
                maNhanVienLabel.setText(rs.getString("MaNhanVien"));
                hoTenLabel.setText(rs.getString("HoTen"));
                ngaySinhLabel.setText(rs.getDate("NgaySinh").toString());
                soCMNDLabel.setText(rs.getString("SoCMND"));
                queQuanLabel.setText(rs.getString("QueQuan"));
                soDienThoaiLabel.setText(rs.getString("SoDienThoai"));
                gioiTinhLabel.setText(rs.getString("GioiTinh"));
                diaChiLabel.setText(rs.getString("DiaChi"));
                maPhongBanLabel.setText(rs.getString("MaPhongBan"));
                
                // Nếu có ảnh đại diện, bạn có thể sử dụng đường dẫn từ database
                // profileImageView.setImage(new Image(rs.getString("AnhDaiDien")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void viewSalary() {
        String maNhanVien = maNhanVienLabel.getText(); // Lấy mã nhân viên từ label

        if (maNhanVien != null && !maNhanVien.isEmpty()) {
            // Gọi hàm để lấy thông tin lương từ cơ sở dữ liệu
            String salaryInfo = getSalaryInfo(maNhanVien); // Bạn cần cài đặt hàm này
            showAlert("Thông Tin Lương", salaryInfo); // Hiển thị thông tin lương
        } else {
            showAlert("Lỗi", "Mã nhân viên không hợp lệ!");
        }
    }

    private String getSalaryInfo(String maNhanVien) {
        StringBuilder salaryInfo = new StringBuilder();


        String sql = "SELECT c.NgayCong, c.SoNgayNghiPhep, l.LuongCoBan, l.UngLuong, l.PhuCap, l.Thuong " +
                     "FROM Cong c " +
                     "JOIN Luong l ON c.maChamCong = l.maChamCong " +
                     "WHERE c.maNhanVien = ?";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, maNhanVien); // Set mã nhân viên vào truy vấn

            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                int ngayCong = resultSet.getInt("ngayCong");
                int soNgayNghiPhep = resultSet.getInt("soNgayNghiPhep");
                double luongCoBan = resultSet.getDouble("LuongCoBan");
                double ungLuong = resultSet.getDouble("ungLuong");
                double phuCap = resultSet.getDouble("phuCap");
                double thuong = resultSet.getDouble("thuong");
                double tongLuong = (luongCoBan * ngayCong) - ungLuong + phuCap + thuong;

                salaryInfo.append("Thông tin lương cho nhân viên ").append(maNhanVien).append(":\n")
                		   .append("Lương cơ bản: ").append(luongCoBan).append("/ngày").append("\n")
                		   .append("Ngày công: ").append(ngayCong).append("\n")
                           .append("Ngày nghỉ phép: ").append(soNgayNghiPhep).append("\n")
                           .append("Ứng lương: ").append(ungLuong).append(" VNĐ\n")
                           .append("Phụ cấp: ").append(phuCap).append(" VNĐ\n")
                           .append("Thưởng: ").append(thuong).append(" VNĐ\n")
                           .append("Tổng lương: ").append(tongLuong).append(" VNĐ\n");
            } else {
                salaryInfo.append("Không tìm thấy thông tin lương cho nhân viên ").append(maNhanVien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            salaryInfo.append("Lỗi khi truy vấn thông tin lương: ").append(e.getMessage());
        }

        return salaryInfo.toString();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hàm xử lý sự kiện đăng xuất
    @FXML
    public void logout() {
        try {
            // Tải lại trang đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main-screen.fxml"));
            Parent root = loader.load();

            // Lấy cửa sổ hiện tại và chuyển đổi giao diện
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng Nhập");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
