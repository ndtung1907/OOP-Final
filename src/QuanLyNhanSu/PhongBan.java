package QuanLyNhanSu;

public class PhongBan {
    private String maPhongBan;
    private String tenPhongBan;
    private String soDienThoai;

    public PhongBan(String maPhongBan, String tenPhongBan, String soDienThoai) {
        this.maPhongBan = maPhongBan;
        this.tenPhongBan = tenPhongBan;
        this.soDienThoai = soDienThoai;
    }

    // Getters and Setters
    public String getMaPhongBan() { return maPhongBan; }
    public void setMaPhongBan(String maPhongBan) { this.maPhongBan = maPhongBan; }

    public String getTenPhongBan() { return tenPhongBan; }
    public void setTenPhongBan(String tenPhongBan) { this.tenPhongBan = tenPhongBan; }
    
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    @Override
    public String toString() {
        return "PhongBan [MaPhongBan=" + maPhongBan + ", TenPhongBan=" + tenPhongBan + ", SoDienThoai=" + soDienThoai + "]";
    }
}
