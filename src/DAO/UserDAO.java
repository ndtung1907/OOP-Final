package DAO;

// UserDAO.java
import java.sql.*;

import Connection.JDBC;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        try {
            this.connection = JDBC.getConnection(); // Lấy kết nối từ DatabaseConnection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFirstLoginStatus(String username) {
        String sql = "UPDATE users SET first_login = FALSE WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPassword(String username) {
        String password = "";
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                password = resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    public boolean login(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true nếu có kết quả, tức là đăng nhập thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updatePassword(String username, String newPassword) {
        String query = "UPDATE users SET password = ?, firstLogin = FALSE WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức xác định userType dựa vào username
    public String getUserType(String username) {
        if (username.startsWith("NV")) {
            return "Nhân Viên";
        } else if (username.startsWith("QL")) {
            return "Quản Lý";
        }
        return null; // Trả về null nếu không xác định được loại người dùng
    }
}
